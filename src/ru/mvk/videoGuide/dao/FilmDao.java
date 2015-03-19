/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.dao;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.dao.DaoImpl;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.videoGuide.model.Film;

public class FilmDao extends DaoImpl<Film, Integer> {
  public FilmDao(@NotNull HibernateAdapter hibernateAdapter) {
    super(Film.class, Integer.class, hibernateAdapter);
  }
}
