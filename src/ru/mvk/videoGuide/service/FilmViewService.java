/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.service;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.service.ViewServiceDescriptor;
import ru.mvk.iluvatar.service.ViewServiceImpl;
import ru.mvk.iluvatar.view.Layout;
import ru.mvk.videoGuide.model.Film;

public class FilmViewService extends ViewServiceImpl<Film> {
  public FilmViewService(@NotNull ViewServiceDescriptor<Film> viewServiceDescriptor,
                         @NotNull Layout layout) {
    super(viewServiceDescriptor, layout, "Фильмы");
  }
}
