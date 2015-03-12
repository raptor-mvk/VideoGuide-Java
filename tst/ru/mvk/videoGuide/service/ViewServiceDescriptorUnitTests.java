/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.service;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.videoGuide.dao.Dao;
import ru.mvk.videoGuide.dao.DaoImpl;
import ru.mvk.videoGuide.descriptor.ListViewInfo;
import ru.mvk.videoGuide.descriptor.ListViewInfoImpl;
import ru.mvk.videoGuide.descriptor.ViewInfo;
import ru.mvk.videoGuide.descriptor.ViewInfoImpl;
import ru.mvk.videoGuide.module.db.HibernateAdapter;
import ru.mvk.videoGuide.test.Student;
import ru.mvk.videoGuide.utils.PowerMockUtils;

public class ViewServiceDescriptorUnitTests {
  @Test
  public void constructor_ShouldSetDao() {
    @NotNull HibernateAdapter hibernateAdapter =
        PowerMockUtils.mock(HibernateAdapter.class);
    @NotNull Dao<Student, Long> expectedDao =
        new DaoImpl<>(Student.class, Long.class, hibernateAdapter);
    @NotNull ViewInfo<Student> viewInfo = new ViewInfoImpl<>(Student.class);
    @NotNull ListViewInfo<Student> listViewInfo = new ListViewInfoImpl<>(Student.class);
    @NotNull ViewServiceDescriptor<Student> viewServiceDescriptor =
        new ViewServiceDescriptor<>(expectedDao, viewInfo, listViewInfo);
    @NotNull Dao<?, ?> dao = viewServiceDescriptor.getDao();
    Assert.assertEquals("constructor should set correct value of 'dao'",
        expectedDao, dao);
  }

  @Test
  public void constructor_ShouldSetViewInfo() {
    @NotNull HibernateAdapter hibernateAdapter =
        PowerMockUtils.mock(HibernateAdapter.class);
    @NotNull Dao<Student, Long> dao =
        new DaoImpl<>(Student.class, Long.class, hibernateAdapter);
    @NotNull ViewInfo<Student> expectedViewInfo = new ViewInfoImpl<>(Student.class);
    @NotNull ListViewInfo<Student> listViewInfo = new ListViewInfoImpl<>(Student.class);
    @NotNull ViewServiceDescriptor<Student> viewServiceDescriptor =
        new ViewServiceDescriptor<>(dao, expectedViewInfo, listViewInfo);
    @NotNull ViewInfo<?> viewInfo = viewServiceDescriptor.getViewInfo();
    Assert.assertEquals("constructor should set correct value of 'viewInfo'",
        expectedViewInfo, viewInfo);
  }

  @Test
  public void constructor_ShouldSetListViewInfo() {
    @NotNull HibernateAdapter hibernateAdapter =
        PowerMockUtils.mock(HibernateAdapter.class);
    @NotNull Dao<Student, Long> dao =
        new DaoImpl<>(Student.class, Long.class, hibernateAdapter);
    @NotNull ViewInfo<Student> viewInfo = new ViewInfoImpl<>(Student.class);
    @NotNull ListViewInfo<Student> expectedListViewInfo =
        new ListViewInfoImpl<>(Student.class);
    @NotNull ViewServiceDescriptor<Student> viewServiceDescriptor =
        new ViewServiceDescriptor<>(dao, viewInfo, expectedListViewInfo);
    @NotNull ListViewInfo<?> listViewInfo = viewServiceDescriptor.getListViewInfo();
    Assert.assertEquals("constructor should set correct value of 'listViewInfo'",
        expectedListViewInfo, listViewInfo);
  }

  @Test
  public void getEntityType_ShouldReturnCorrectEntityType() {
    @NotNull HibernateAdapter hibernateAdapter =
        PowerMockUtils.mock(HibernateAdapter.class);
    @NotNull Dao<Student, Long> dao =
        new DaoImpl<>(Student.class, Long.class, hibernateAdapter);
    @NotNull ViewInfo<Student> viewInfo = new ViewInfoImpl<>(Student.class);
    @NotNull ListViewInfo<Student> listViewInfo = new ListViewInfoImpl<>(Student.class);
    @NotNull ViewServiceDescriptor<Student> viewServiceDescriptor =
        new ViewServiceDescriptor<>(dao, viewInfo, listViewInfo);
    @NotNull Class<?> entityType = viewServiceDescriptor.getEntityType();
    Assert.assertEquals("getEntityType() should return correct value of 'entityType'",
        Student.class, entityType);
  }
}
