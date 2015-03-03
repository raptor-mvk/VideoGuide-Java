/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class IntegerFieldInfo<Type> extends NaturalFieldInfo<Type> {
  public IntegerFieldInfo(@NotNull Class<Type> type, @NotNull String name, int width) {
    super(type, name, width);
  }
}