/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.javafx.field;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.descriptor.field.IntegerFieldInfo;
import ru.mvk.videoGuide.descriptor.field.NumberFieldInfo;

public class IntegerField<Type> extends NaturalField<Type> {
  public IntegerField(@NotNull NumberFieldInfo<Type> fieldInfo) {
    super(fieldInfo);
  }
}