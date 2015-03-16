/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.javafx.layout;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class JFXSimpleLayout extends JFXLayout {
  @NotNull
  private final ScrollPane root = new ScrollPane();

  @Override
  public int registerViewService(@NotNull String serviceKey) {
    return 0;
  }

  @NotNull
  @Override
  public Consumer<Object> getViewUpdater(int serviceId) {
    return (content) -> {
      if (content instanceof Node) {
        root.setContent((Node) content);
      }
    };
  }

  @NotNull
  @Override
  public Consumer<Object> getListViewUpdater(int serviceId) {
    return (content) -> {
      if (content instanceof Node) {
        root.setContent((Node) content);
      }
    };
  }

  @Override
  public void show() {
    @NotNull Stage stage = getStage();
    stage.setScene(new Scene(root, 1000, 440));
    stage.setResizable(false);
    stage.show();
  }
}
