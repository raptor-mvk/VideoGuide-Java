/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.service;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface ViewService<EntityType> {
  void showView(boolean isNewEntity);

  EntityType getNewEntity();

  void showListView();

  void removeEntity();

  @NotNull
  Class<EntityType> getEntityType();

  void setListViewUpdater(@NotNull Consumer<Object> listViewUpdater);

  void setViewUpdater(@NotNull Consumer<Object> viewUpdater);

  void setDefaultOrder(@NotNull String fieldKey, boolean isAscending);
}

