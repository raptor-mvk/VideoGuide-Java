/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.javafx.layout;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class JFXViewWindowLayout extends JFXLayout {
  @NotNull
  private final ScrollPane listViewRoot = new ScrollPane();
  @NotNull
  private final ScrollPane viewRoot = new ScrollPane();
  @NotNull
  private final Stage viewWindowStage = new Stage(StageStyle.UTILITY);

  public JFXViewWindowLayout(int viewWindowWidth, int viewWindowHeight) {
    @NotNull Scene viewScene = new Scene(viewRoot, viewWindowWidth, viewWindowHeight);
    viewWindowStage.setScene(viewScene);
    viewWindowStage.setAlwaysOnTop(true);
    viewWindowStage.setResizable(false);
    viewWindowStage.initModality(Modality.WINDOW_MODAL);
  }

  @Override
  public int registerViewService(@NotNull String serviceKey) {
    return 0;
  }

  @NotNull
  @Override
  public Consumer<Object> getViewUpdater(int serviceId) {
    return (content) -> {
      if (content instanceof Node) {
        viewRoot.setContent((Node) content);
        viewWindowStage.show();
      }
    };
  }

  @NotNull
  @Override
  public Consumer<Object> getListViewUpdater(int serviceId) {
    return (content) -> {
      if (content instanceof Node) {
        listViewRoot.setContent((Node) content);
        viewWindowStage.hide();
      }
    };
  }

  @Override
  public void show(int width, int height) {
    @NotNull Stage stage = getStage();
    @NotNull Scene listViewScene = new Scene(listViewRoot, width, height);
    stage.setScene(listViewScene);
    stage.setResizable(false);
    stage.show();
  }

  @Override
  public void setStage(@Nullable Stage stage) {
    super.setStage(stage);
    viewWindowStage.initOwner(stage);
  }
}
