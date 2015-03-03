/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.javafx.field;

import javafx.scene.Node;
import org.apache.commons.beanutils.ConstructorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.descriptor.field.*;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldUtils {
  @NotNull
  private static final Pattern NATURAL_NUMBER_PATTERN = Pattern.compile("^\\d*$");
  @NotNull
  private static final Pattern NATURAL_ZERO_EQUAL_PATTERN = Pattern.compile("^$");
  @NotNull
  private static final Pattern INTEGER_NUMBER_PATTERN = Pattern.compile("^-?\\d*$");
  @NotNull
  private static final Pattern INTEGER_ZERO_EQUAL_PATTERN = Pattern.compile("^-?$");
  @NotNull
  private static final Pattern REAL_NUMBER_PATTERN =
      Pattern.compile("^-?\\d*(\\.\\d*)?$");
  @NotNull
  private static final Pattern REAL_ZERO_EQUAL_PATTERN = Pattern.compile("-?\\.?");
  @NotNull
  private static final Matcher NATURAL_NUMBER_MATCHER =
      NATURAL_NUMBER_PATTERN.matcher("");
  @NotNull
  private static final Matcher NATURAL_ZERO_EQUAL_MATCHER =
      NATURAL_ZERO_EQUAL_PATTERN.matcher("");
  @NotNull
  private static final Matcher INTEGER_NUMBER_MATCHER =
      INTEGER_NUMBER_PATTERN.matcher("");
  @NotNull
  private static final Matcher INTEGER_ZERO_EQUAL_MATCHER =
      INTEGER_ZERO_EQUAL_PATTERN.matcher("");
  @NotNull
  private static final Matcher REAL_NUMBER_MATCHER = REAL_NUMBER_PATTERN.matcher("");
  @NotNull
  private static final Matcher REAL_ZERO_EQUAL_MATCHER =
      REAL_ZERO_EQUAL_PATTERN.matcher("");
  @NotNull
  private static final Map<Class<?>, Function<String, ?>> TYPE_CASTERS_MAP =
      initTypeCastersMap();
  @NotNull
  private static final Map<Class<?>, Matcher> NUMBER_MATCHER_MAP = initNumberMatcherMap();
  @NotNull
  private static final Map<Class<?>, Matcher> ZERO_EQUAL_MATCHER_MAP = initZeroEqualMap();
  @NotNull
  private static final Map<Class<? extends NamedFieldInfo>, Class<? extends Node>>
      FIELD_INFO_TO_FIELD_MAP = initFieldInfoToFieldMap();

  @NotNull
  private static Map<Class<?>, Function<String, ?>> initTypeCastersMap() {
    @NotNull Map<Class<?>, Function<String, ?>> result = new HashMap<>();
    result.put(Byte.class, Byte::parseByte);
    result.put(Short.class, Short::parseShort);
    result.put(Integer.class, Integer::parseInt);
    result.put(Long.class, Long::parseLong);
    result.put(Float.class, Float::parseFloat);
    result.put(Double.class, Double::parseDouble);
    return result;
  }

  @NotNull
  private static Map<Class<?>, Matcher> initNumberMatcherMap() {
    @NotNull Map<Class<?>, Matcher> result = new HashMap<>();
    result.put(NaturalField.class, NATURAL_NUMBER_MATCHER);
    result.put(IntegerField.class, INTEGER_NUMBER_MATCHER);
    result.put(RealField.class, REAL_NUMBER_MATCHER);
    return result;
  }

  @NotNull
  private static Map<Class<?>, Matcher> initZeroEqualMap() {
    @NotNull Map<Class<?>, Matcher> result = new HashMap<>();
    result.put(NaturalField.class, NATURAL_ZERO_EQUAL_MATCHER);
    result.put(IntegerField.class, INTEGER_ZERO_EQUAL_MATCHER);
    result.put(RealField.class, REAL_ZERO_EQUAL_MATCHER);
    return result;
  }

  @NotNull
  private static Map<Class<? extends NamedFieldInfo>,
      Class<? extends Node>> initFieldInfoToFieldMap() {
    @NotNull Map<Class<? extends NamedFieldInfo>, Class<? extends Node>> result =
        new HashMap<>();
    result.put(CheckBoxInfo.class, CheckBoxField.class);
    result.put(TextFieldInfo.class, LimitedTextField.class);
    result.put(NaturalFieldInfo.class, NaturalField.class);
    result.put(IntegerFieldInfo.class, IntegerField.class);
    result.put(RealFieldInfo.class, RealField.class);
    result.put(DurationFieldInfo.class, DurationField.class);
    return result;
  }

  @NotNull
  static Matcher getNumberMatcher(@NotNull Class<?> fieldType) {
    @Nullable Matcher result = NUMBER_MATCHER_MAP.get(fieldType);
    if (result == null) {
      throw new VideoGuideRuntimeException("FieldUtils: Unsupported field type '" +
          fieldType + "'");
    }
    return result;
  }

  @NotNull
  static Matcher getZeroEqualMatcher(@NotNull Class<?> fieldType) {
    @Nullable Matcher result = ZERO_EQUAL_MATCHER_MAP.get(fieldType);
    if (result == null) {
      throw new VideoGuideRuntimeException("FieldUtils: Unsupported field type '" +
          fieldType + "'");
    }
    return result;
  }

  @NotNull
  static Function<String, ?> getTypeCaster(Class<?> type) {
    @Nullable Function<String, ?> result = TYPE_CASTERS_MAP.get(type);
    if (result == null) {
      throw new VideoGuideRuntimeException("FieldUtils: Unsupported class '" + type +
          "'");
    }
    return result;
  }

  @NotNull
  public static Node getField(@NotNull NamedFieldInfo fieldInfo) {
    @Nullable Node result = null;
    try {
      @Nullable Class<? extends NamedFieldInfo> fieldInfoClass = fieldInfo.getClass();
      @Nullable Class<? extends Node> fieldClass =
          FIELD_INFO_TO_FIELD_MAP.get(fieldInfoClass);
      if (fieldClass != null) {
        result = (fieldInfo instanceof SizedFieldInfo) ?
            ConstructorUtils.invokeConstructor(fieldClass, new Object[]{fieldInfo}) :
            ConstructorUtils.invokeConstructor(fieldClass, new Object[]{});
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
        InstantiationException e) {
      throw new VideoGuideRuntimeException("FieldUtils: Could not run constructor for " +
          "field");
    }
    if (result == null) {
      throw new VideoGuideRuntimeException("FieldUtils: Unsupported field type");
    }
    return result;
  }
}
