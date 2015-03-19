/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.dao;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.dao.DaoImpl;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.videoGuide.model.Disc;

public class DiscDao extends DaoImpl<Disc, Integer> {
  public DiscDao(@NotNull HibernateAdapter hibernateAdapter) {
    super(Disc.class, Integer.class, hibernateAdapter);
  }
}
