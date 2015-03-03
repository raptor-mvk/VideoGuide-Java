/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.descriptor.field;

import org.jetbrains.annotations.NotNull;

public class RealFieldInfo<Type> extends NumberFieldInfoImpl<Type> {
  public RealFieldInfo(@NotNull Class<Type> type, @NotNull String name, int width) {
    super(type, name, width);
  }

  @Override
  boolean isTypeCorrect(@NotNull Class<Type> type) {
    return type.equals(Float.class) || type.equals(Double.class);
  }
}
