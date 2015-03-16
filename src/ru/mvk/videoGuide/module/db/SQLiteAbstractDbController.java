/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.videoGuide.module.db;

import org.hibernate.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.core.VideoGuide;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

import java.sql.ResultSet;
import java.util.List;

public abstract class SQLiteAbstractDbController implements DbController {
  @NotNull
  private final HibernateAdapter hibernateAdapter;

  protected SQLiteAbstractDbController(@NotNull HibernateAdapter hibernateAdapter) {
    this.hibernateAdapter = hibernateAdapter;
  }

  @Override
  public final boolean isDbSuitable() {
    @Nullable Object appId = getValue("pragma application_id;");
    return (appId instanceof Integer) && (int) appId == getAppId();
  }

  @Override
  public final int getDbVersion() {
    @Nullable Object dbVersion = getValue("pragma user_version;");
    if (!(dbVersion instanceof Integer)) {
      throw new VideoGuideRuntimeException("SQLiteAbstractDbController: user_version " +
          "has wrong type");
    }
    return (int) dbVersion;
  }

  @Override
  public boolean updateDb() {
    long appDbVersion = getAppDbVersion();
    @Nullable Boolean result = hibernateAdapter.executeInTransaction((session) -> {
      @NotNull Query query = hibernateAdapter.prepareSqlQuery("pragma user_version=" +
          appDbVersion + ';', session);
      return (query.executeUpdate() == 0);
    });
    if (result == null) {
      result = false;
    }
    int dbVersion = getDbVersion();
    return result && updateDbSchema(dbVersion);
  }

  @Override
  public boolean createDb() {
    long appId = getAppId();
    long appDbVersion = getAppDbVersion();
    @Nullable Boolean result = hibernateAdapter.executeInTransaction((session) -> {
      @NotNull Query idQuery = hibernateAdapter.prepareSqlQuery("pragma application_id=" +
          appId + ';', session);
      @NotNull Query dbVersionQuery = hibernateAdapter.prepareSqlQuery("pragma " +
          "user_version=" + appDbVersion + ';', session);
      return (idQuery.executeUpdate() == 0) && (dbVersionQuery.executeUpdate() == 0);
    });
    if (result == null) {
      result = false;
    }
    return result && createDbSchema();
  }

  protected void execute(@NotNull String sql) {
    hibernateAdapter.executeInTransaction((session) -> {
      @NotNull Query query = hibernateAdapter.prepareSqlQuery(sql, session);
      return query.executeUpdate();
    });
  }

  @NotNull
  protected List<?> executeQuery(@NotNull String sql) {
    @Nullable List<?> result = hibernateAdapter.executeInTransaction((session) -> {
      @NotNull Query query = hibernateAdapter.prepareSqlQuery(sql, session);
      return query.list();
    });
    if (result == null) {
      throw new VideoGuideRuntimeException("Query result is null");
    }
    return result;
  }

  @Nullable
  private Object getValue(@NotNull String sql) {
    @Nullable Object value = hibernateAdapter.executeInTransaction((session) -> {
      @NotNull Query query = hibernateAdapter.prepareSqlQuery(sql, session);
      @Nullable List<?> queryResult = query.list();
      if (queryResult == null) {
        throw new VideoGuideRuntimeException("SQLiteAbstractDbController: list() " +
            "returned null");
      }
      return queryResult.get(0);
    });
    return value;
  }

  protected abstract boolean updateDbSchema(int fromDbVersion);

  protected abstract boolean createDbSchema();

  protected abstract int getAppId();

  protected abstract int getAppDbVersion();
}
