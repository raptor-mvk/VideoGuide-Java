/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.service;

import javafx.application.Platform;
import org.apache.commons.beanutils.ConstructorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.dao.Dao;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;
import ru.mvk.videoGuide.view.ListView;
import ru.mvk.videoGuide.view.View;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public class ViewServiceImpl<EntityType> implements ViewService<EntityType> {
  @NotNull
  private final Class<EntityType> entityType;
  @NotNull
  private final Dao<EntityType, ?> dao;
  @NotNull
  private final View<EntityType> view;
  @NotNull
  private final ListView<EntityType> listView;
  @Nullable
  private EntityType entity;
  private int selectedIndex;
  @NotNull
  private Consumer<Object> listViewUpdater = (content) -> {
  };
  @NotNull
  private Consumer<Object> viewUpdater = (content) -> {
  };

  ViewServiceImpl(@NotNull Dao<EntityType, ?> dao, @NotNull View<EntityType> view,
                  @NotNull ListView<EntityType> listView) {
    entityType = dao.getEntityType();
    this.dao = dao;
    this.view = view;
    this.listView = listView;
    prepareView();
    prepareListView();
  }

  @Override
  public void showView(boolean isNewEntity) {
    @Nullable Object content = null;
    if (isNewEntity) {
      entity = getNewEntity();
    }
    if (entity != null) {
      content = view.getView(entity, isNewEntity);
    }
    viewUpdater.accept(content);
  }

  @NotNull
  @Override
  public EntityType getNewEntity() {
    try {
      @Nullable EntityType newEntity =
          ConstructorUtils.invokeConstructor(entityType, new Object[]{});
      if (newEntity == null) {
        throw new InstantiationException();
      }
      return newEntity;
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
        InstantiationException e) {
      @NotNull String className = entityType.getSimpleName();
      throw new VideoGuideRuntimeException("SimpleViewService: Could not create new " +
          "instance of '" + className + "' class");
    }
  }

  @Override
  public void showListView() {
    @Nullable Object content = listView.getListView();
    listViewUpdater.accept(content);
    listView.refreshTable();
  }

  @Override
  public void removeEntity() {
    if (entity != null) {
      dao.delete(entity);
      int lastSelectedIndex = selectedIndex;
      showListView();
      listView.selectRowByIndex(lastSelectedIndex);
      listView.scrollToIndex(lastSelectedIndex - 1);
    }
  }

  @NotNull
  @Override
  public Class<EntityType> getEntityType() {
    return entityType;
  }

  private void updateEntity(boolean isNewEntity) {
    if (entity != null) {
      if (isNewEntity) {
        dao.create(entity);
      } else {
        dao.update(entity);
      }
    }
    @Nullable EntityType lastSelectedEntity = entity;
    showListView();
    Platform.runLater(() -> {
      listView.selectRowByEntity(lastSelectedEntity);
      if (isNewEntity) {
        listView.scrollToEntity(lastSelectedEntity);
      }
    });
  }

  private void cancelUpdateEntity() {
    @Nullable EntityType lastSelectedEntity = entity;
    showListView();
    Platform.runLater(() -> {
      listView.selectRowByEntity(lastSelectedEntity);
      listView.scrollToEntity(lastSelectedEntity);
    });
  }

  @Override
  public void setListViewUpdater(@NotNull Consumer<Object> listViewUpdater) {
    this.listViewUpdater = listViewUpdater;
  }

  @Override
  public void setViewUpdater(@NotNull Consumer<Object> viewUpdater) {
    this.viewUpdater = viewUpdater;
  }

  @Override
  public void setDefaultOrder(@NotNull String fieldKey, boolean isAscending) {
    listView.setListSupplier(() -> dao.orderedList(fieldKey, isAscending));
  }

  private void setSelectedIndex(int index) {
    selectedIndex = index;
  }

  void setEntity(@Nullable EntityType entity) {
    this.entity = entity;
  }

  private void prepareView() {
    view.setSaveButtonHandler(this::updateEntity);
    view.setCancelButtonHandler(this::cancelUpdateEntity);
  }

  private void prepareListView() {
    listView.setSelectedEntitySetter(this::setEntity);
    listView.setSelectedIndexSetter(this::setSelectedIndex);
    listView.setRemoveButtonHandler(this::removeEntity);
    listView.setEditButtonHandler(() -> showView(false));
    listView.setAddButtonHandler(() -> showView(true));
    listView.setListSupplier(dao::list);
  }
}
