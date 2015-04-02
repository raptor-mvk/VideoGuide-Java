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
import ru.mvk.iluvatar.descriptor.column.LowerStringColumnInfo;
import ru.mvk.iluvatar.descriptor.column.StringColumnInfo;
import ru.mvk.iluvatar.descriptor.field.NaturalFieldInfo;
import ru.mvk.iluvatar.descriptor.field.TextFieldInfo;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.service.ViewServiceDescriptor;
import ru.mvk.iluvatar.service.ViewServiceImpl;
import ru.mvk.iluvatar.view.Layout;
import ru.mvk.videoGuide.dao.FilmDao;
import ru.mvk.videoGuide.model.Film;

public class FilmViewService extends ViewServiceImpl<Film> {
  public FilmViewService(@NotNull HibernateAdapter hibernateAdapter,
                         @NotNull Layout layout) {
    super(new ViewServiceDescriptor<>(new FilmDao(hibernateAdapter),
        prepareFilmViewInfo(), prepareFilmListViewInfo()), layout, "Фильмы");
    setDefaultOrder("lowerName", true);
  }

  @NotNull
  private static ViewInfo<Film> prepareFilmViewInfo() {
    @NotNull ViewInfo<Film> viewInfo = new ViewInfoImpl<>(Film.class);
    viewInfo.addFieldInfo("name", new TextFieldInfo("Название", 80));
    viewInfo.addFieldInfo("length",
        new NaturalFieldInfo<>(Integer.class, "Длительность", 6));
    viewInfo.addFieldInfo("size", new NaturalFieldInfo<>(Long.class, "Размер", 13));
    viewInfo.addFieldInfo("disc", new NaturalFieldInfo<>(Byte.class, "Диск", 2));
    viewInfo.addFieldInfo("filesCount", new NaturalFieldInfo<>(Short.class, "Файлов", 3));
    return viewInfo;
  }

  @NotNull
  private static ListViewInfo<Film> prepareFilmListViewInfo() {
    @NotNull ListViewInfo<Film> listViewInfo = new ListViewInfoImpl<>(Film.class);
    listViewInfo.addColumnInfo("lowerName", new LowerStringColumnInfo("Название", 60));
    listViewInfo.addColumnInfo("length", new DurationColumnInfo("Длит.", 9));
    listViewInfo.addColumnInfo("size", new FileSizeColumnInfo("Размер", 9));
    listViewInfo.addColumnInfo("disc", new StringColumnInfo("Диск", 7));
    listViewInfo.addColumnInfo("filesCount", new StringColumnInfo("Файлов", 8));
    listViewInfo.addColumnInfo("averageLength", new DurationColumnInfo("Ср. длит.", 9));
    listViewInfo.addColumnInfo("averageSize", new FileSizeColumnInfo("Ср. размер", 12));
    return listViewInfo;
  }
}
