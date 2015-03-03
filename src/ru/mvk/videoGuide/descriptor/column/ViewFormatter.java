/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.descriptor.column;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface ViewFormatter extends Function<Object, String> {
  @NotNull
  @Override
  public String apply(@Nullable Object argument);
}
