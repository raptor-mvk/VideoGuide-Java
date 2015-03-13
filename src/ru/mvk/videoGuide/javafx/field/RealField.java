/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.javafx.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.descriptor.field.NaturalFieldInfo;
import ru.mvk.videoGuide.descriptor.field.NumberFieldInfo;
import ru.mvk.videoGuide.descriptor.field.RealFieldInfo;

public class RealField<Type> extends NaturalField<Type> {
  public RealField(@NotNull NumberFieldInfo<Type> fieldInfo) {
    super(fieldInfo);
  }
}
