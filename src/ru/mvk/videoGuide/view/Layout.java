/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.view;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.descriptor.ListViewInfo;
import ru.mvk.videoGuide.descriptor.ViewInfo;

import java.util.function.Consumer;

public interface Layout {
  @NotNull
  <EntityType> ListView<EntityType> getListView(@NotNull
                                                ListViewInfo<EntityType> listViewInfo);

  @NotNull
  <EntityType> View<EntityType> getView(@NotNull ViewInfo<EntityType> viewInfo);

  int registerViewService(@NotNull String serviceKey,
                          @NotNull Runnable defaultViewSetter);

  @NotNull
  Consumer<Object> getViewUpdater(int serviceId);

  @NotNull
  Consumer<Object> getListViewUpdater(int serviceId);

  void show(int width, int height);
}
