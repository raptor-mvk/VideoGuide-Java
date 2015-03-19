/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.module.db;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.module.db.SQLiteAbstractDbController;

import java.util.List;

public class VideoGuideDbController extends SQLiteAbstractDbController {
  public VideoGuideDbController(@NotNull HibernateAdapter hibernateAdapter) {
    super(hibernateAdapter);
  }

  @Override
  protected boolean updateDbSchema(int fromDbVersion) {
    if (fromDbVersion < 2) {
      execute("alter table film add column lowerName text");
      List<?> rows = executeQuery("select rowid, name from film;");
      for (Object row : rows) {
        @NotNull String id = ((Object[]) row)[0].toString();
        @NotNull String name = ((Object[]) row)[1].toString().toLowerCase();
        @NotNull String escapedName = name.replaceAll("'", "''");
        @NotNull String sqlQuery = "update film set lowerName='" + escapedName +
            "' where rowid=" + id + ';';
        execute(sqlQuery);
      }
    }
    if (fromDbVersion < 3) {
      execute("create table disc (number int, size int);");
    }
    return true;
  }

  @Override
  protected boolean createDbSchema() {
    execute("create table film (name text, lowerName text, length int, size int, " +
        "disc int, filesCount int);");
    execute("create table disc (number int, size int);");
    return true;
  }

  @Override
  protected int getAppId() {
    return 0x53EA3849;
  }

  @Override
  protected int getAppDbVersion() {
    return 3;
  }
}
