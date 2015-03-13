/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.service;

import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.dao.Dao;
import ru.mvk.videoGuide.descriptor.ListViewInfo;
import ru.mvk.videoGuide.descriptor.ViewInfo;

public class ViewServiceDescriptor<EntityType> {
  @NotNull
  private final Dao<EntityType, ?> dao;
  @NotNull
  private final ViewInfo<EntityType> viewInfo;
  @NotNull
  private final ListViewInfo<EntityType> listViewInfo;

  public ViewServiceDescriptor(@NotNull Dao<EntityType, ?> dao,
                               @NotNull ViewInfo<EntityType> viewInfo,
                               @NotNull ListViewInfo<EntityType> listViewInfo) {
    this.dao = dao;
    this.viewInfo = viewInfo;
    this.listViewInfo = listViewInfo;
  }

  @NotNull
  public Dao<EntityType, ?> getDao() {
    return dao;
  }

  @NotNull
  public ViewInfo<EntityType> getViewInfo() {
    return viewInfo;
  }

  @NotNull
  public ListViewInfo<EntityType> getListViewInfo() {
    return listViewInfo;
  }

  @NotNull
  public Class<EntityType> getEntityType() {
    return dao.getEntityType();
  }
}
