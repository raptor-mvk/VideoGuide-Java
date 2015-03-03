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

  void setContentSetter(@NotNull Consumer<Object> contentSetter);
}

