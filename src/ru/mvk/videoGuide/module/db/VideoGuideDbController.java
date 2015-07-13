/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.module.db;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.module.db.SQLiteAbstractDbController;

import java.util.ArrayList;
import java.util.List;

public class VideoGuideDbController extends SQLiteAbstractDbController {
  public VideoGuideDbController(@NotNull HibernateAdapter hibernateAdapter) {
    super(hibernateAdapter);
  }

  @Override
  protected boolean updateDbSchema(int fromDbVersion) {
    if (fromDbVersion < 4) {
      updateFromVersionUnder4(fromDbVersion);
    }
    if (fromDbVersion < 5) {
      execute("update disc set size=size*1073741824");
    }
    if (fromDbVersion < 6) {
      executeArray(new String[]{"create table totals (name text)",
                                   "insert into totals (name) values ('Всего')"});
    }
    if (fromDbVersion < 7) {
      executeArray(new String[]{"create table tmp (name text, size int)",
                                   "insert into tmp (rowid, name, size) " +
                                       "select number, number, size from disc",
                                   "drop table disc",
                                   "alter table tmp rename to disc"});
    }
    return true;
  }

  @Override
  protected boolean createDbSchema() {
    execute("create table film (name text, lowerName text, length int, size int, " +
                "disc int, filesCount int);");
    execute("create table disc (name text, size int);");
    execute("create table totals (name text);");
    execute("insert into totals (name) values ('Всего')");
    return true;
  }

  @Override
  protected int getAppId() {
    return 0x53EA3849;
  }

  @Override
  protected int getAppDbVersion() {
    return 7;
  }

  private void updateFromVersionUnder4(int fromDbVersion) {
    if (fromDbVersion < 2) {
      updateFromVersion1();
    }
    if (fromDbVersion < 3) {
      execute("create table disc (number int, size int);");
    }
    if (fromDbVersion < 4) {
      updateFromVersion3();
    }
  }

  private void updateFromVersion1() {
    @NotNull List<String> sqlList = new ArrayList<>();
    sqlList.add("alter table film add column lowerName text");
    List<?> rows = executeQuery("select rowid, name from film;");
    for (Object row : rows) {
      @NotNull String id = ((Object[]) row)[0].toString();
      @NotNull String name = ((Object[]) row)[1].toString().toLowerCase();
      @NotNull String escapedName = name.replace("'", "''");
      @NotNull String sqlQuery = "update film set lowerName='" + escapedName +
                                     "' where rowid=" + id + ';';
      sqlList.add(sqlQuery);
    }
    executeList(sqlList);
  }

  private void updateFromVersion3() {
    @NotNull List<?> rows = executeQuery("select rowid, lowerName from film;");
    @NotNull List<String> sqlList = new ArrayList<>();
    for (Object row : rows) {
      @NotNull String id = ((Object[]) row)[0].toString();
      @NotNull String lowerName = ((Object[]) row)[1].toString().toLowerCase();
      @NotNull String fixedLowerName = lowerName.replace('ё', 'е');
      @NotNull String sqlQuery = "update film set lowerName='" + fixedLowerName +
                                     "' where rowid=" + id + ';';
      sqlList.add(sqlQuery);
    }
    executeList(sqlList);
  }
}
