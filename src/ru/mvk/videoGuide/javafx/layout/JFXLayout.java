/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.javafx.layout;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.descriptor.ListViewInfo;
import ru.mvk.videoGuide.descriptor.ViewInfo;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;
import ru.mvk.videoGuide.javafx.JFXListView;
import ru.mvk.videoGuide.javafx.JFXView;
import ru.mvk.videoGuide.view.Layout;
import ru.mvk.videoGuide.view.ListView;
import ru.mvk.videoGuide.view.View;

public abstract class JFXLayout implements Layout {
  @Nullable
  private Stage stage;

  @NotNull
  @Override
  public <EntityType> ListView<EntityType> getListView(@NotNull ListViewInfo<EntityType>
                                                           listViewInfo) {
    return new JFXListView<>(listViewInfo);
  }

  @NotNull
  @Override
  public <EntityType> View<EntityType> getView(@NotNull ViewInfo<EntityType> viewInfo) {
    return new JFXView<>(viewInfo);
  }

  public void setStage(@Nullable Stage stage) {
    this.stage = stage;
  }

  @NotNull
  final Stage getStage() {
    if (stage == null) {
      throw new VideoGuideRuntimeException("Stage was not set");
    }
    return stage;
  }
}
