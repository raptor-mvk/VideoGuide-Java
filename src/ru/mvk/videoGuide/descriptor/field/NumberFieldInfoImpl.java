/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.descriptor.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

abstract class NumberFieldInfoImpl<Type> extends SizedFieldInfoImpl
    implements NumberFieldInfo<Type> {
  @NotNull
  private final Class<Type> type;

  NumberFieldInfoImpl(@NotNull Class<Type> type, @NotNull String name, int width) {
    super(name, width);
    if (!isTypeCorrect(type)) {
      throw new VideoGuideRuntimeException("NumberFieldInfoImpl: incorrect type in " +
          "constructor");
    }
    this.type = type;
  }

  @NotNull
  @Override
  public final Class<Type> getType() {
    return type;
  }

  abstract boolean isTypeCorrect(@NotNull Class<Type> type);
}
