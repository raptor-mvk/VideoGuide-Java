/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.service;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.dao.Dao;
import ru.mvk.videoGuide.model.Film;
import ru.mvk.videoGuide.view.ListView;
import ru.mvk.videoGuide.view.View;

public class FilmViewService extends SimpleViewService<Film> {
  public FilmViewService(@NotNull Dao<Film, ?> dao, @NotNull View<Film> view,
                         @NotNull ListView<Film> listView) {
    super(dao, view, listView);
  }
}
