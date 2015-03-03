/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.exception;

import org.jetbrains.annotations.NotNull;

public class VideoGuideRuntimeException extends RuntimeException {
  public VideoGuideRuntimeException(@NotNull String message) {
    super(message);
  }
}
