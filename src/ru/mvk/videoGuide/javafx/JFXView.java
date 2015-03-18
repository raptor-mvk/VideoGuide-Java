/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.videoGuide.javafx;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.descriptor.ViewInfo;
import ru.mvk.videoGuide.descriptor.field.NamedFieldInfo;
import ru.mvk.videoGuide.descriptor.field.SizedFieldInfo;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;
import ru.mvk.videoGuide.javafx.field.Field;
import ru.mvk.videoGuide.view.View;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class JFXView<EntityType> implements View<EntityType> {
  @NotNull
  private final GridPane gridPane;
  @NotNull
  private final Map<String, Node> fields = new HashMap<>();
  private boolean newEntity;
  @NotNull
  private final Button saveButton;
  @NotNull
  private final Button cancelButton;
  @NotNull
  private final ViewInfo<EntityType> viewInfo;
  @NotNull
  private final String entityClassName;
  @NotNull
  private Consumer<Boolean> saveButtonHandler = (isNeEntity) -> {
  };
  @NotNull
  private Runnable cancelButtonHandler = () -> {
  };

  public JFXView(@NotNull ViewInfo<EntityType> viewInfo) {
    @NotNull Class<EntityType> entityType = viewInfo.getEntityType();
    entityClassName = entityType.getSimpleName();
    this.viewInfo = viewInfo;
    saveButton = prepareSaveButton();
    cancelButton = prepareCancelButton();
    gridPane = prepareGridPane();
    @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
    prepareView(iterator);
    setKeyListeners();
  }

  @Override
  @NotNull
  public final String getFieldId(@NotNull String key) {
    return entityClassName + '-' + key + "-field";
  }

  @Override
  @NotNull
  public final String getLabelId(@NotNull String key) {
    return entityClassName + '-' + key + "-label";
  }

  @Override
  @NotNull
  public final String getSaveButtonId() {
    return entityClassName + "-save-button";
  }

  @Override
  @NotNull
  public final String getCancelButtonId() {
    return entityClassName + "-cancel-button";
  }

  @Nullable
  @Override
  public GridPane getView(@NotNull EntityType entity, boolean isNewEntity) {
    newEntity = isNewEntity;
    prepareFieldValues(entity);
    return gridPane;
  }

  @Override
  public void setSaveButtonHandler(@NotNull Consumer<Boolean> handler) {
    saveButtonHandler = handler;
    saveButton.setOnAction(event -> handler.accept(newEntity));
  }

  @Override
  public void setCancelButtonHandler(@NotNull Runnable handler) {
    cancelButtonHandler = handler;
    cancelButton.setOnAction(event -> handler.run());
  }

  private void setKeyListeners() {
    gridPane.setOnKeyPressed((event) -> {
      @NotNull KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.ENTER || keyCode == KeyCode.ESCAPE) {
        event.consume();
      }
    });
    gridPane.setOnKeyReleased((event) -> {
      @NotNull KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.ENTER || keyCode == KeyCode.ESCAPE) {
        event.consume();
        if (keyCode == KeyCode.ENTER) {
          saveButtonHandler.accept(newEntity);
        } else if (keyCode == KeyCode.ESCAPE) {
          cancelButtonHandler.run();
        }
      }
    });
  }

  private void prepareView(@NotNull Iterator<Entry<String, NamedFieldInfo>> iterator) {
    for (int i = 0; iterator.hasNext(); i++) {
      @Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
      if (entry != null) {
        @Nullable NamedFieldInfo fieldInfo = entry.getValue();
        @Nullable String fieldKey = entry.getKey();
        if ((fieldKey != null) && (fieldInfo != null)) {
          prepareLabel(i, fieldKey);
          prepareField(i, fieldKey, fieldInfo);
        }
      }
    }
    int buttonsRow = viewInfo.getFieldsCount();
    gridPane.add(saveButton, 0, buttonsRow);
    gridPane.add(cancelButton, 1, buttonsRow);
  }

  private void prepareLabel(int index, @NotNull String fieldKey) {
    @NotNull String labelId = getLabelId(fieldKey);
    @NotNull NamedFieldInfo fieldInfo = viewInfo.getFieldInfo(fieldKey);
    @NotNull String fieldLabel = fieldInfo.getName();
    @NotNull Label label = new Label(fieldLabel);
    label.setId(labelId);
    gridPane.add(label, 0, index);
  }

  private void prepareField(int index, @NotNull String fieldKey,
                            @NotNull NamedFieldInfo fieldInfo) {
    @NotNull Node field = getField(fieldInfo);
    @NotNull String fieldId = getFieldId(fieldKey);
    field.setId(fieldId);
    fields.put(fieldKey, field);
    gridPane.add(field, 1, index);

  }

  @NotNull
  private GridPane prepareGridPane() {
    @NotNull GridPane result = new GridPane();
    // 5.0 is empirically selected gap and padding
    result.setHgap(5.0);
    result.setVgap(5.0);
    result.setPadding(new Insets(5.0));
    return result;
  }

  @NotNull
  private Button prepareSaveButton() {
    @NotNull String saveButtonId = getSaveButtonId();
    @NotNull Button result = new Button("Сохранить");
    result.setId(saveButtonId);
    return result;
  }

  @NotNull
  private Button prepareCancelButton() {
    @NotNull String cancelButtonId = getCancelButtonId();
    @NotNull Button result = new Button("Отменить");
    result.setId(cancelButtonId);
    return result;
  }

  private void prepareFieldValues(@NotNull EntityType entity) {
    @NotNull Iterator<Entry<String, NamedFieldInfo>> iterator = viewInfo.getIterator();
    while (iterator.hasNext()) {
      @Nullable Entry<String, NamedFieldInfo> entry = iterator.next();
      if (entry != null) {
        @Nullable String fieldKey = entry.getKey();
        if (fieldKey != null) {
          setFieldValue(fieldKey, entity);
        }
      }
    }
  }

  private void setFieldValue(@NotNull String fieldKey,
                             @NotNull EntityType object) {
    try {
      @NotNull Node field = getFieldNode(fieldKey);
      @NotNull PropertyDescriptor propertyDescriptor =
          getPropertyDescriptor(fieldKey, object);
      @NotNull Object value = getFieldValue(propertyDescriptor, object);
      if (field instanceof Field) {
        ((Field) field).setFieldValue(value);
      } else {
        throw new VideoGuideRuntimeException("JFXView: Incorrect field type");
      }
      setFieldUpdater(field, propertyDescriptor, object);
    } catch (IllegalAccessException | InvocationTargetException |
        NoSuchMethodException e) {
      throw new VideoGuideRuntimeException("JFXView: Could not access field '" +
          fieldKey + '\'');
    }
  }

  @NotNull
  private Node getFieldNode(@NotNull String fieldKey) {
    @Nullable Node result = fields.get(fieldKey);
    if (result == null) {
      throw new VideoGuideRuntimeException("JFXView: field '" + fieldKey + "' was not " +
          "found");
    }
    return result;
  }

  @NotNull
  public Node getField(@NotNull NamedFieldInfo fieldInfo) {
    @Nullable Object fieldInstance = null;
    @Nullable String fieldClassName = fieldInfo.getJFXFieldClassName();
    @Nullable Class<?> fieldClass = null;
    try {
      fieldClass = Class.forName(fieldClassName);
    } catch (ClassNotFoundException e) {
      throw new VideoGuideRuntimeException("FieldUtils: Could not load class " +
          fieldClassName);
    }
    if (fieldClass != null) {
      if (fieldInfo instanceof SizedFieldInfo) {
        fieldInstance = instantiateSizedField(fieldClass, (SizedFieldInfo) fieldInfo);
      } else {
        fieldInstance = instantiateNamedField(fieldClass);
      }
    }
    if (!(fieldInstance instanceof Node)) {
      throw new VideoGuideRuntimeException("JFXView: instantiated field is not " +
          "JavaFX Node");
    }
    return (Node) fieldInstance;
  }

  @NotNull
  private Object instantiateSizedField(@NotNull Class<?> fieldClass,
                                       @NotNull SizedFieldInfo fieldInfo) {
    @NotNull Object result;
    @Nullable Class<?> fieldInfoClass = fieldInfo.getClass();
    if (fieldInfoClass == null) {
      throw new VideoGuideRuntimeException("JFXView: fieldInfo class is null");
    }
    @Nullable Constructor fieldConstructor =
        ConstructorUtils.getMatchingAccessibleConstructor(fieldClass, fieldInfoClass);
    if (fieldConstructor == null) {
      throw new VideoGuideRuntimeException("Could not get constructor for " + fieldClass);
    }
    try {
      result = fieldConstructor.newInstance(fieldInfo);
    } catch (InstantiationException | IllegalAccessException |
        InvocationTargetException e) {
      throw new VideoGuideRuntimeException("JFXView: Could not instantiate field for " +
          fieldClass);
    }
    return result;
  }

  @NotNull
  private Object instantiateNamedField(@NotNull Class<?> fieldClass) {
    @NotNull Object result;
    @Nullable Constructor fieldConstructor =
        ConstructorUtils.getMatchingAccessibleConstructor(fieldClass);
    if (fieldConstructor == null) {
      throw new VideoGuideRuntimeException("Could not get constructor for " + fieldClass);
    }
    try {
      result = fieldConstructor.newInstance();
    } catch (InstantiationException | IllegalAccessException |
        InvocationTargetException e) {
      throw new VideoGuideRuntimeException("JFXView: Could not instantiate field for " +
          fieldClass);
    }
    return result;
  }

  @NotNull
  private PropertyDescriptor getPropertyDescriptor(@NotNull String fieldKey,
                                                   @NotNull EntityType object)
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    @Nullable PropertyDescriptor propertyDescriptor =
        PropertyUtils.getPropertyDescriptor(object, fieldKey);
    if (propertyDescriptor == null) {
      throw new VideoGuideRuntimeException("JFXView: could not access field '" +
          fieldKey + "'");
    }
    return propertyDescriptor;
  }

  @NotNull
  private Object getFieldValue(@NotNull PropertyDescriptor propertyDescriptor,
                               @NotNull EntityType object) throws IllegalAccessException,
      InvocationTargetException {
    @Nullable Method readMethod = PropertyUtils.getReadMethod(propertyDescriptor);
    if (readMethod == null) {
      throw new VideoGuideRuntimeException("JFXView: could not access field");
    }
    @Nullable Object result = readMethod.invoke(object);
    if (result == null) {
      throw new VideoGuideRuntimeException("JFXView: field has null value");
    }
    return result;
  }

  private void setFieldUpdater(@NotNull Node field,
                               @NotNull PropertyDescriptor propertyDescriptor,
                               @NotNull EntityType object) {
    @Nullable Method writeMethod = PropertyUtils.getWriteMethod(propertyDescriptor);
    if (field instanceof Field<?>) {
      ((Field<?>) field).setFieldUpdater((fieldValue) -> {
        try {
          if (writeMethod == null) {
            throw new IllegalAccessException();
          }
          writeMethod.invoke(object, fieldValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
          throw new VideoGuideRuntimeException("JFXView: Could not access field");
        }
      });
    }
  }
}
