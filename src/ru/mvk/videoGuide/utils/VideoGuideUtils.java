/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

import java.util.Collection;
import java.util.stream.Stream;

public class VideoGuideUtils {
  @NotNull
  public static <Type> Stream<Type> mapToTypedStream(@NotNull Collection<?> list,
                                                     @NotNull Class<Type> type) {
    @Nullable Stream<?> stream = list.stream();
    if (stream == null) {
      throw new VideoGuideRuntimeException("VideoGuideUtils: stream is null");
    }
    @Nullable Stream<?> filteredStream = ((Stream<?>) stream).filter(type::isInstance);
    if (filteredStream == null) {
      throw new VideoGuideRuntimeException("VideoGuideUtils: filtered stream is null");
    }
    @Nullable Stream<Type> result = filteredStream.map(type::cast);
    if (result == null) {
      throw new VideoGuideRuntimeException("VideoGuideUtils: mapped stream is null");
    }
    return result;
  }
}
