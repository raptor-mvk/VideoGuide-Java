/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.descriptor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.descriptor.field.NamedFieldInfo;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ViewInfoImpl<EntityType> implements ViewInfo<EntityType> {
  @NotNull
  private final Map<String, NamedFieldInfo> fields;
  @NotNull
  private final Class<EntityType> entityType;

  public ViewInfoImpl(@NotNull Class<EntityType> entityType) {
    this.entityType = entityType;
    fields = new LinkedHashMap<>();
  }

  @NotNull
  @Override
  public Class<EntityType> getEntityType() {
    return entityType;
  }

  @Override
  public int getFieldsCount() {
    return fields.size();
  }

  @NotNull
  @Override
  public Iterator<Entry<String, NamedFieldInfo>> getIterator() {
    return fields.entrySet().iterator();
  }

  @NotNull
  @Override
  public NamedFieldInfo getFieldInfo(@NotNull String fieldKey) {
    @Nullable NamedFieldInfo result = fields.get(fieldKey);
    if (result == null) {
      throw new VideoGuideRuntimeException("SimpleViewInfo: no field with key '" +
          fieldKey + "'");
    }
    return result;
  }

  @Override
  public void addFieldInfo(@NotNull String fieldKey, @NotNull NamedFieldInfo fieldInfo) {
    fields.put(fieldKey, fieldInfo);
  }
}
