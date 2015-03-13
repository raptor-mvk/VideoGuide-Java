/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.service;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.model.Film;
import ru.mvk.videoGuide.view.Layout;

public class FilmViewService extends ViewServiceImpl<Film> {
  public FilmViewService(@NotNull ViewServiceDescriptor<Film> viewServiceDescriptor,
                         @NotNull Layout layout) {
    super(viewServiceDescriptor, layout, "film");
  }
}
