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
import ru.mvk.iluvatar.descriptor.column.StringColumnInfo;
import ru.mvk.iluvatar.descriptor.field.NaturalFieldInfo;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.service.ViewServiceDescriptor;
import ru.mvk.iluvatar.service.ViewServiceImpl;
import ru.mvk.iluvatar.view.Layout;
import ru.mvk.videoGuide.dao.DiscDao;
import ru.mvk.videoGuide.dao.FilmDao;
import ru.mvk.videoGuide.model.Disc;

public class DiscViewService extends ViewServiceImpl<Disc> {
  public DiscViewService(@NotNull HibernateAdapter hibernateAdapter,
                         @NotNull Layout layout) {
    super(new ViewServiceDescriptor<>(new DiscDao(hibernateAdapter),
        prepareDiscViewInfo(), prepareDiscListViewInfo()), layout, "Диски");
    setDefaultOrder("number", true);
  }

  @NotNull
  private static ViewInfo<Disc> prepareDiscViewInfo() {
    @NotNull ViewInfo<Disc> viewInfo = new ViewInfoImpl<>(Disc.class);
    viewInfo.addFieldInfo("number", new NaturalFieldInfo<>(Byte.class, "Диск", 2));
    viewInfo.addFieldInfo("sizeGb", new NaturalFieldInfo<>(Short.class, "Размер, Гб", 4));
    return viewInfo;
  }

  @NotNull
  private static ListViewInfo<Disc> prepareDiscListViewInfo() {
    @NotNull ListViewInfo<Disc> listViewInfo = new ListViewInfoImpl<>(Disc.class);
    listViewInfo.addColumnInfo("number", new StringColumnInfo("Диск", 8));
    listViewInfo.addColumnInfo("size", new FileSizeColumnInfo("Всего", 10));
    listViewInfo.addColumnInfo("filmsCount", new StringColumnInfo("Фильмов", 8));
    listViewInfo.addColumnInfo("filmsFilesCount", new StringColumnInfo("Файлов", 8));
    listViewInfo.addColumnInfo("filmsLength", new DurationColumnInfo("Длит.", 10));
    listViewInfo.addColumnInfo("filmsSize", new FileSizeColumnInfo("Размер", 10));
    listViewInfo.addColumnInfo("freeSize", new FileSizeColumnInfo("Свободно", 10));
    return listViewInfo;
  }

}
