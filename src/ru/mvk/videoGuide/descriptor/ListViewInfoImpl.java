/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.videoGuide.descriptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.descriptor.column.ColumnInfo;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ListViewInfoImpl<EntityType> implements ListViewInfo<EntityType> {
  @NotNull
  private final Map<String, ColumnInfo> columns;
  @NotNull
  private final Class<EntityType> entityType;

  public ListViewInfoImpl(@NotNull Class<EntityType> entityType) {
    this.entityType = entityType;
    columns = new LinkedHashMap<>();
  }

  @NotNull
  @Override
  public Class<EntityType> getEntityType() {
    return entityType;
  }

  @Override
  public int getColumnsCount() {
    return columns.size();
  }

  @NotNull
  @Override
  public Iterator<Entry<String, ColumnInfo>> getIterator() {
    return columns.entrySet().iterator();
  }

  @NotNull
  @Override
  public ColumnInfo getColumnInfo(@NotNull String columnKey) {
    @Nullable ColumnInfo result = columns.get(columnKey);
    if (result == null) {
      throw new VideoGuideRuntimeException("SimpleListViewInfo: no column with key '" +
          columnKey + "'");
    }
    return result;
  }

  @Override
  public void addColumnInfo(@NotNull String columnKey, @NotNull ColumnInfo columnInfo) {
    columns.put(columnKey, columnInfo);
  }
}
