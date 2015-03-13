/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.javafx.field;

import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.descriptor.field.SizedFieldInfo;
import ru.mvk.videoGuide.descriptor.field.TextFieldInfo;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

import java.util.function.Consumer;

public class LimitedTextField extends SizedTextField implements Field<String> {
  @NotNull
  private Consumer<String> fieldUpdater;

  public LimitedTextField(@NotNull SizedFieldInfo fieldInfo) {
    super(fieldInfo.getWidth());
    fieldUpdater = (value) -> {
    };
    setChangeListener();
  }

  @Override
  public void setFieldUpdater(@NotNull Consumer<String> fieldUpdater) {
    this.fieldUpdater = fieldUpdater;
  }

  @Override
  public void setFieldValue(@NotNull Object value) {
    if (value instanceof String) {
      setText((String) value);
    } else {
      throw new VideoGuideRuntimeException("LimitedTextField: incorrect value type");
    }
  }

  private void setChangeListener() {
    @Nullable StringProperty fieldTextProperty = textProperty();
    if (fieldTextProperty == null) {
      throw new VideoGuideRuntimeException("LimitedTextField: textProperty is null");
    }
    fieldTextProperty.addListener((observableValue, oldValue, newValue) -> {
      if (newValue.length() > getMaxLength()) {
        restoreOldValue(oldValue, newValue);
      } else {
        fieldUpdater.accept(newValue);
      }
    });
  }
}
