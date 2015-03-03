/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.test;

import org.jetbrains.annotations.Nullable;

public class FieldValueTester<Type> {
  @Nullable
  private Type value;

  @Nullable
  public Type getValue() {
    return value;
  }

  public void setValue(@Nullable Type value) {
    this.value = value;
  }
}
