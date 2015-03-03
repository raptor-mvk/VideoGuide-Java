/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.dao;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.model.Film;
import ru.mvk.videoGuide.module.db.HibernateAdapter;

public class FilmDao extends DaoImpl<Film, Integer> {
  public FilmDao(@NotNull HibernateAdapter hibernateAdapter) {
    super(Film.class, Integer.class, hibernateAdapter);
  }
}
