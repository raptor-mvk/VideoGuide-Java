/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.descriptor.column;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

public class BooleanColumnInfoUnitTests {
  @Test
  public void constructor_ShouldSetName() {
    @NotNull String name = "enabled";
    @NotNull ColumnInfo booleanColumnInfo = new BooleanColumnInfo(name, 5);
    @NotNull String columnName = booleanColumnInfo.getName();
    Assert.assertEquals("constructor should set correct value of 'name'", name,
        columnName);
  }

  @Test
  public void constructor_ShouldSetWidth() {
    int width = 7;
    @NotNull ColumnInfo booleanColumnInfo = new BooleanColumnInfo("sized", width);
    int columnWidth = booleanColumnInfo.getWidth();
    Assert.assertEquals("constructor should set correct value of 'width'", width,
        columnWidth);
  }

  @Test(expected = VideoGuideRuntimeException.class)
  public void constructor_ZeroWidth_ShouldThrowVideoGuideRuntimeException() {
    new BooleanColumnInfo("zero", 0);
  }

  @Test(expected = VideoGuideRuntimeException.class)
  public void constructor_NegativeWidth_ShouldThrowVideoGuideRuntimeException() {
    new BooleanColumnInfo("negative", -43);
  }

  @Test
  public void viewFormatter_ShouldReturnPlusForTrueValue() {
    @NotNull ColumnInfo booleanColumnInfo = new BooleanColumnInfo("isGood", 1);
    @NotNull ViewFormatter viewFormatter = booleanColumnInfo.getViewFormatter();
    @NotNull String result = viewFormatter.apply(true);
    Assert.assertEquals("viewFormatter should return '+' for true value", "+", result);
  }

  @Test
  public void viewFormatter_ShouldReturnMinusForFalseValue() {
    @NotNull ColumnInfo booleanColumnInfo = new BooleanColumnInfo("isEmpty", 7);
    @NotNull ViewFormatter viewFormatter = booleanColumnInfo.getViewFormatter();
    @NotNull String result = viewFormatter.apply(false);
    Assert.assertEquals("viewFormatter should return '-' for false value", "-", result);
  }

  @Test
  public void viewFormatter_ShouldReturnEmptyStringForNonBooleanValue() {
    @NotNull ColumnInfo booleanColumnInfo = new BooleanColumnInfo("isNumber", 5);
    @NotNull ViewFormatter viewFormatter = booleanColumnInfo.getViewFormatter();
    @NotNull Object object = new Object();
    @NotNull String result = viewFormatter.apply(object);
    Assert.assertEquals("viewFormatter should return '' for non-boolean value", "",
        result);
  }
}
