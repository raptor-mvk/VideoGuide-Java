/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ListView<EntityType> {
  @Nullable
  Object getListView();

  @NotNull
  String getTableId();

  @NotNull
  String getAddButtonId();

  @NotNull
  String getEditButtonId();

  @NotNull
  String getRemoveButtonId();

  void refreshTable(int selectedIndex);

  void setAddButtonHandler(@NotNull Runnable handler);

  void setEditButtonHandler(@NotNull Runnable handler);

  void setRemoveButtonHandler(@NotNull Runnable handler);

  void setSelectedEntitySetter(@NotNull Consumer<EntityType> setter);

  void setSelectedIndexSetter(@NotNull Consumer<Integer> setter);

  void setListSupplier(@NotNull Supplier<List<EntityType>> listSupplier);

  void selectRow(int index);

  void clearSelection();
}
