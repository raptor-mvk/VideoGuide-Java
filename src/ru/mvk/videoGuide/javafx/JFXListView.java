/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.javafx;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.descriptor.ListViewInfo;
import ru.mvk.videoGuide.descriptor.column.ColumnInfo;
import ru.mvk.videoGuide.descriptor.column.ViewFormatter;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;
import ru.mvk.videoGuide.view.ListView;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class JFXListView<EntityType> implements ListView<EntityType> {
  @NotNull
  private final GridPane gridPane;
  @NotNull
  private final TableView<EntityType> tableView;
  @NotNull
  private final Button addButton;
  @NotNull
  private final Button editButton;
  @NotNull
  private final Button removeButton;
  @NotNull
  private final ListViewInfo<EntityType> listViewInfo;
  @NotNull
  private final String entityClassName;
  @NotNull
  private Consumer<EntityType> selectedEntitySetter = (entity) -> {
  };
  @NotNull
  private Consumer<Integer> selectedIndexSetter = (index) -> {
  };
  @NotNull
  private Runnable addButtonHandler = () -> {
  };
  @NotNull
  private Runnable editButtonHandler = () -> {
  };
  @NotNull
  private Runnable removeButtonHandler = () -> {
  };

  public JFXListView(@NotNull ListViewInfo<EntityType> listViewInfo) {
    @NotNull Class<EntityType> entityType = listViewInfo.getEntityType();
    entityClassName = entityType.getSimpleName();
    this.listViewInfo = listViewInfo;
    addButton = prepareAddButton();
    editButton = prepareEditButton();
    removeButton = prepareRemoveButton();
    tableView = new TableView<>();
    gridPane = prepareGridPane();
    prepareTableView();
    setMouseListener();
    setKeyPressedListener();
    setKeyReleasedListener();
  }

  @Override
  @NotNull
  public final String getTableId() {
    return entityClassName + "-list";
  }

  @Override
  @NotNull
  public final String getAddButtonId() {
    return entityClassName + "-add-button";
  }

  @Override
  @NotNull
  public final String getEditButtonId() {
    return entityClassName + "-edit-button";
  }

  @Override
  @NotNull
  public final String getRemoveButtonId() {
    return entityClassName + "-remove-button";
  }

  @Nullable
  @Override
  public GridPane getListView(@NotNull List<EntityType> objectList) {
    @NotNull ObservableList<EntityType> objectObservableList =
        FXCollections.observableList(objectList);
    tableView.setItems(objectObservableList);
    clearSelection();
    return gridPane;
  }

  @Override
  public void refreshTable(int selectedIndex) {
    @Nullable ObservableList<TableColumn<EntityType, ?>> columnList =
        tableView.getColumns();
    if (columnList == null) {
      throw new VideoGuideRuntimeException("JFXListView: column list is null");
    }
    @Nullable TableColumn<EntityType, ?> column = columnList.get(0);
    if (column != null) {
      column.setVisible(false);
      column.setVisible(true);
    }
    selectRow(selectedIndex);
  }

  @Override
  public void setAddButtonHandler(@NotNull Runnable handler) {
    addButtonHandler = handler;
    addButton.setOnAction(event -> handler.run());
  }

  @Override
  public void setEditButtonHandler(@NotNull Runnable handler) {
    editButtonHandler = handler;
    editButton.setOnAction(event -> handler.run());
  }

  @Override
  public void setRemoveButtonHandler(@NotNull Runnable handler) {
    removeButtonHandler = handler;
    removeButton.setOnAction(event -> handler.run());
  }

  @Override
  public void setSelectedEntitySetter(@NotNull Consumer<EntityType> entitySetter) {
    selectedEntitySetter = entitySetter;
  }

  @Override
  public void setSelectedIndexSetter(@NotNull Consumer<Integer> indexSetter) {
    selectedIndexSetter = indexSetter;
  }

  @Override
  public void selectRow(int rowIndex) {
    @NotNull TableViewSelectionModel<EntityType> tableViewSelectionModel =
        getTableViewSelectionModel();
    if ((rowIndex >= 0) && (rowIndex < getTableViewItemsCount())) {
      tableViewSelectionModel.select(rowIndex);
      editButton.setDisable(false);
      removeButton.setDisable(false);
    } else {
      tableViewSelectionModel.clearSelection();
      editButton.setDisable(true);
      removeButton.setDisable(true);
    }
  }

  @Override
  public void clearSelection() {
    selectRow(-1);
  }

  private void setMouseListener() {
    tableView.setOnMouseClicked((event) -> {
      if(event.getClickCount() == 2) {
        runEditButtonHandler();
      }
    });
  }

  private void setKeyPressedListener() {
    gridPane.setOnKeyPressed((event) -> {
      @NotNull KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.ENTER || keyCode == KeyCode.DELETE ||
          keyCode == KeyCode.INSERT) {
        event.consume();
      }
    });
  }

  private void setKeyReleasedListener() {
    gridPane.setOnKeyReleased((event) -> {
      @NotNull KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.ENTER || keyCode == KeyCode.DELETE ||
          keyCode == KeyCode.INSERT) {
        event.consume();
        if(keyCode == KeyCode.ENTER) {
          runEditButtonHandler();
        } else if (keyCode == KeyCode.DELETE) {
          runRemoveButtonHandler();
        } else if (keyCode == KeyCode.INSERT) {
          addButtonHandler.run();
        }
      }
    });
  }

  private void runEditButtonHandler() {
    if (!editButton.isDisabled()) {
      editButtonHandler.run();
    }
  }

  private void runRemoveButtonHandler() {
    if (!removeButton.isDisabled()) {
      removeButtonHandler.run();
    }
  }

  @NotNull
  private Button prepareAddButton() {
    @NotNull String addButtonId = getAddButtonId();
    @NotNull Button result = new Button("Добавить");
    result.setId(addButtonId);
    return result;
  }

  @NotNull
  private Button prepareEditButton() {
    @NotNull String editButtonId = getEditButtonId();
    @NotNull Button result = new Button("Изменить");
    result.setId(editButtonId);
    return result;
  }

  @NotNull
  private Button prepareRemoveButton() {
    @NotNull String removeButtonId = getRemoveButtonId();
    @NotNull Button result = new Button("Удалить");
    result.setId(removeButtonId);
    return result;
  }

  @NotNull
  private GridPane prepareGridPane() {
    @NotNull GridPane result = new GridPane();
    // 5.0 is empirically selected gap and padding
    result.setHgap(5.0);
    result.setVgap(5.0);
    result.setPadding(new Insets(5.0));
    result.add(tableView, 0, 0, 3, 1);
    result.add(addButton, 0, 1);
    result.add(editButton, 1, 1);
    result.add(removeButton, 2, 1);
    return result;
  }

  private void prepareTableView() {
    @NotNull String tableId = getTableId();
    tableView.setId(tableId);
    prepareColumns();
    setSelectedItemListener();
    setSelectedIndexListener();
  }

  private void setSelectedItemListener() {
    @Nullable TableViewSelectionModel<EntityType> tableViewSelectionModel =
        getTableViewSelectionModel();
    @Nullable ReadOnlyObjectProperty<EntityType> selectedItemProperty =
        tableViewSelectionModel.selectedItemProperty();
    if (selectedItemProperty == null) {
      throw new VideoGuideRuntimeException("JFXListView: selectedItemProperty is null");
    }
    ChangeListener<EntityType> selectedItemListener = prepareSelectedItemListener();
    selectedItemProperty.addListener(selectedItemListener);
  }

  @NotNull
  private ChangeListener<EntityType> prepareSelectedItemListener() {
    return (observableValue, oldValue, newValue) -> {
      selectedEntitySetter.accept(newValue);
      if (newValue != null) {
        editButton.setDisable(false);
        removeButton.setDisable(false);
      } else {
        editButton.setDisable(true);
        removeButton.setDisable(true);
      }
    };
  }

  private void setSelectedIndexListener() {
    @Nullable TableViewSelectionModel<EntityType> tableViewSelectionModel =
        getTableViewSelectionModel();
    @Nullable ReadOnlyIntegerProperty selectedIndexProperty =
        tableViewSelectionModel.selectedIndexProperty();
    if (selectedIndexProperty == null) {
      throw new VideoGuideRuntimeException("JFXListView: selectedIndexProperty is null");
    }
    selectedIndexProperty.addListener(
        (observableValue, oldValue, newValue) -> {
          selectedIndexSetter.accept((Integer) newValue);
        });
  }

  @NotNull
  private TableViewSelectionModel<EntityType> getTableViewSelectionModel() {
    @Nullable TableViewSelectionModel<EntityType> result = tableView.getSelectionModel();
    if (result == null) {
      throw new VideoGuideRuntimeException("JFXListView: selectionModel is null");
    }
    return result;
  }

  private int getTableViewItemsCount() {
    @Nullable ObservableList<EntityType> itemList = tableView.getItems();
    if (itemList == null) {
      throw new VideoGuideRuntimeException("JFXListView: itemList is null");
    }
    return itemList.size();
  }

  private void prepareColumns() {
    @Nullable ObservableList<TableColumn<EntityType, ?>> columnList = prepareColumnList();
    @NotNull Iterator<Entry<String, ColumnInfo>> iterator = listViewInfo.getIterator();
    while (iterator.hasNext()) {
      @Nullable Entry<String, ColumnInfo> entry = iterator.next();
      if (entry != null) {
        @Nullable String fieldName = entry.getKey();
        @Nullable ColumnInfo columnInfo = entry.getValue();
        if ((fieldName != null) && (columnInfo != null)) {
          @NotNull TableColumn<EntityType, Object> tableColumn =
              prepareTableColumn(fieldName, columnInfo);
          columnList.add(tableColumn);
        }
      }
    }
  }

  @NotNull
  private ObservableList<TableColumn<EntityType, ?>> prepareColumnList() {
    @Nullable ObservableList<TableColumn<EntityType, ?>> result =
        tableView.getColumns();
    if (result == null) {
      throw new VideoGuideRuntimeException("JFXListView: columnList is null");
    }
    return result;
  }

  @NotNull
  private TableColumn<EntityType, Object> prepareTableColumn(@NotNull String columnKey,
                                                             @NotNull
                                                             ColumnInfo columnInfo) {
    @NotNull String columnName = columnInfo.getName();
    @NotNull TableColumn<EntityType, Object> tableColumn = new TableColumn<>(columnName);
    tableColumn.setCellValueFactory(new PropertyValueFactory<>(columnKey));
    setTableColumnParams(tableColumn, columnInfo);
    return tableColumn;
  }

  private void setTableColumnParams(@NotNull TableColumn<EntityType, Object> tableColumn,
                                    @NotNull ColumnInfo columnInfo) {
    // 0.7 is a ratio of conversion from font size to average letter width
    @NotNull Font defaultFont = Font.getDefault();
    double width = columnInfo.getWidth() * defaultFont.getSize() * 0.7;
    tableColumn.setMinWidth(width);
    tableColumn.setPrefWidth(width);
    tableColumn.setMaxWidth(width);
    setCellFactory(tableColumn, columnInfo);
  }

  private void setCellFactory(@NotNull TableColumn<EntityType, Object> tableColumn,
                              @NotNull ColumnInfo columnInfo) {
    tableColumn.setCellFactory(param -> new TableCell<EntityType, Object>() {
      @Override
      public void updateItem(@Nullable Object item, boolean empty) {
        @NotNull ViewFormatter viewFormatter = columnInfo.getViewFormatter();
        @NotNull String value = viewFormatter.apply(item);
        setText(value);
      }
    });
  }
}
