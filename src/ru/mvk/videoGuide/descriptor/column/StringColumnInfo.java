/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.descriptor.column;

import org.jetbrains.annotations.NotNull;

public class StringColumnInfo implements ColumnInfo {
  @NotNull
  private final String name;
  private final int width;

  public StringColumnInfo(@NotNull String name, int width) {
    this.name = name;
    this.width = width;
  }

  @Override
  public final int getWidth() {
    return width;
  }

  @NotNull
  @Override
  public final String getName() {
    return name;
  }

  @NotNull
  @Override
  public ViewFormatter getViewFormatter() {
    return (value) -> {
      @NotNull String result = "";
      if (value != null) {
        result = value.toString();
      }
      return result;
    };
  }
}
