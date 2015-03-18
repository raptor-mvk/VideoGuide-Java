/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.service;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.model.Disc;
import ru.mvk.videoGuide.view.Layout;

public class DiscViewService extends ViewServiceImpl<Disc> {
  public DiscViewService(@NotNull ViewServiceDescriptor<Disc> viewServiceDescriptor,
                         @NotNull Layout layout) {
    super(viewServiceDescriptor, layout, "Диски");
  }
}
