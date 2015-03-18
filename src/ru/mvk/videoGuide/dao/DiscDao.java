/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.dao;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.model.Disc;
import ru.mvk.videoGuide.model.Film;
import ru.mvk.videoGuide.module.db.HibernateAdapter;

public class DiscDao extends DaoImpl<Disc, Integer> {
  public DiscDao(@NotNull HibernateAdapter hibernateAdapter) {
    super(Disc.class, Integer.class, hibernateAdapter);
  }
}
