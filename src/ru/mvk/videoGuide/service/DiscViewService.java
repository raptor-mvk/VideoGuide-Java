/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.service;

import org.jetbrains.annotations.NotNull;
import ru.mvk.iluvatar.descriptor.ListViewInfo;
import ru.mvk.iluvatar.descriptor.ListViewInfoImpl;
import ru.mvk.iluvatar.descriptor.ViewInfo;
import ru.mvk.iluvatar.descriptor.ViewInfoImpl;
import ru.mvk.iluvatar.descriptor.column.DurationColumnInfo;
import ru.mvk.iluvatar.descriptor.column.FileSizeColumnInfo;
import ru.mvk.iluvatar.descriptor.column.NumColumnInfo;
import ru.mvk.iluvatar.descriptor.column.StringColumnInfo;
import ru.mvk.iluvatar.descriptor.field.NaturalFieldInfo;
import ru.mvk.iluvatar.descriptor.field.TextFieldInfo;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.service.ViewServiceDescriptor;
import ru.mvk.iluvatar.service.ViewServiceImpl;
import ru.mvk.iluvatar.view.Layout;
import ru.mvk.videoGuide.dao.DiscDao;
import ru.mvk.videoGuide.dao.DiscTotalDao;
import ru.mvk.videoGuide.model.Disc;

public class DiscViewService extends ViewServiceImpl<Disc> {
  @NotNull
  DiscTotalDao discTotalDao;

  public DiscViewService(@NotNull HibernateAdapter hibernateAdapter,
                         @NotNull Layout layout) {
    super(new ViewServiceDescriptor<>(new DiscDao(hibernateAdapter),
                                         prepareDiscViewInfo(),
                                         prepareDiscListViewInfo()), layout, "Диски");
    discTotalDao = new DiscTotalDao(hibernateAdapter);
    setTotalSupplier(() -> discTotalDao.list().get(0).getDisc());
  }

  @NotNull
  private static ViewInfo<Disc> prepareDiscViewInfo() {
    @NotNull ViewInfo<Disc> viewInfo = new ViewInfoImpl<>(Disc.class);
    viewInfo.addFieldInfo("name", new TextFieldInfo("Диск", 1));
    viewInfo.addFieldInfo("size", new NaturalFieldInfo<>(Long.class, "Размер", 13));
    return viewInfo;
  }

  @NotNull
  private static ListViewInfo<Disc> prepareDiscListViewInfo() {
    @NotNull ListViewInfo<Disc> listViewInfo = new ListViewInfoImpl<>(Disc.class);
    listViewInfo.showTotalRow();
    listViewInfo.disableRemove();
    listViewInfo.addColumnInfo("name", new NumColumnInfo("Диск", 8));
    listViewInfo.addColumnInfo("size", new FileSizeColumnInfo("Всего", 10));
    listViewInfo.addColumnInfo("filmsCount", new NumColumnInfo("Фильмов", 8));
    listViewInfo.addColumnInfo("filmsFilesCount", new NumColumnInfo("Файлов", 8));
    listViewInfo.addColumnInfo("filmsLength", new DurationColumnInfo("Длит.", 10));
    listViewInfo.addColumnInfo("filmsSize", new FileSizeColumnInfo("Занято", 10));
    listViewInfo.addColumnInfo("freeSize", new FileSizeColumnInfo("Свободно", 10));
    listViewInfo.addColumnInfo("usedRatio", new NumColumnInfo("Занято, %", 8));
    return listViewInfo;
  }

}
