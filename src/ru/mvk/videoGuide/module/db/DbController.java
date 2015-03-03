/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.videoGuide.module.db;

public interface DbController {
  boolean isDbSuitable();

  int getDbVersion();

  boolean updateDb();

  boolean createDb();
}
