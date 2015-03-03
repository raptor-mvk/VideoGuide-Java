/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.descriptor.field;

import org.jetbrains.annotations.NotNull;

public interface NumberFieldInfo<Type> extends SizedFieldInfo {
  @NotNull
  Class<Type> getType();
}
