/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.dao;

import ru.mvk.iluvatar.dao.DaoImpl;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.videoGuide.model.DiscTotal;

public class DiscTotalDao extends DaoImpl<DiscTotal, Integer> {
  public DiscTotalDao(HibernateAdapter hibernateAdapter) {
    super(DiscTotal.class, Integer.class, hibernateAdapter);
  }
}
