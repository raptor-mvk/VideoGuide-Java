/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.descriptor.column;

import org.jetbrains.annotations.NotNull;

public class BooleanColumnInfo extends StringColumnInfo {
  public BooleanColumnInfo(@NotNull String name, int width) {
    super(name, width);
  }

  @NotNull
  @Override
  public ViewFormatter getViewFormatter() {
    return (value) -> {
      @NotNull String result = "";
      if (value instanceof Boolean) {
        result = (Boolean) value ? "+" : "-";
      }
      return result;
    };
  }
}
