/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.javafx.field;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

abstract class BasicSizedTextField extends TextField {
  private final int maxLength;

  BasicSizedTextField(int maxLength) {
    if (maxLength <= 0) {
      throw new VideoGuideRuntimeException("BasicSizedTextField: non-positive width");
    }
    this.maxLength = maxLength;
    // force length = 4 for too short fields
    if (maxLength > 4) {
      setAllWidths(maxLength);
    } else {
      setAllWidths(4);
    }
  }

  private void setAllWidths(int length) {
    // 0.7 is a ratio of conversion from font size to average letter width
    Font defaultFont = Font.getDefault();
    double width = length * defaultFont.getSize() * 0.7;
    setMinWidth(width);
    setMaxWidth(width);
    setMaxWidth(width);
  }

  final int getMaxLength() {
    return maxLength;
  }
}
