/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.module.db;

import org.jetbrains.annotations.NotNull;

public class VideoGuideDbController extends SQLiteAbstractDbController {
  public VideoGuideDbController(@NotNull HibernateAdapter hibernateAdapter) {
    super(hibernateAdapter);
  }

  @Override
  protected boolean updateDbSchema(int fromDbVersion) {
    return true;
  }

  @Override
  protected boolean createDbSchema() {
    execute("create table film (name text, length int, size int, disc int, " +
        "filesCount int);");
    return true;
  }

  @Override
  protected int getAppId() {
    return 0x53EA3849;
  }

  @Override
  protected int getAppDbVersion() {
    return 1;
  }
}
