/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public interface ListView<EntityType> {
  @Nullable
  Object getListView(@NotNull List<EntityType> objectList);

  @NotNull
  String getTableId();

  @NotNull
  String getAddButtonId();

  @NotNull
  String getEditButtonId();

  @NotNull
  String getRemoveButtonId();

  void refreshTable(int selectedIndex);

  void setAddButtonHandler(Runnable handler);

  void setEditButtonHandler(Runnable handler);

  void setRemoveButtonHandler(Runnable handler);

  void setSelectedEntitySetter(Consumer<EntityType> setter);

  void setSelectedIndexSetter(Consumer<Integer> setter);

  void selectRow(int index);

  void clearSelection();
}
