/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.utils;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.module.db.HibernateAdapter;
import ru.mvk.videoGuide.module.db.SQLiteAbstractDbController;

public class SQLiteTestDbController extends SQLiteAbstractDbController {
  private int appId;
  private int appDbVersion;

  public SQLiteTestDbController(@NotNull HibernateAdapter hibernateAdapter, int appId,
                                int appDbVersion) {
    super(hibernateAdapter);
    this.appId = appId;
    this.appDbVersion = appDbVersion;
  }

  @Override
  protected boolean updateDbSchema(int fromDbVersion) {
    return true;
  }

  @Override
  protected boolean createDbSchema() {
    return true;
  }

  @Override
  protected int getAppId() {
    return appId;
  }

  @Override
  protected int getAppDbVersion() {
    return appDbVersion;
  }
}
