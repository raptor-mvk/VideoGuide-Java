/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.videoGuide.view;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface View<EntityType> {
  @Nullable
  Object getView(@NotNull EntityType entity, boolean newEntity);

  @NotNull
  String getFieldId(@NotNull String key);

  @NotNull
  String getLabelId(@NotNull String key);

  @NotNull
  String getSaveButtonId();

  @NotNull
  String getCancelButtonId();

  void setSaveButtonHandler(Consumer<Boolean> handler);

  void setCancelButtonHandler(Runnable handler);
}
