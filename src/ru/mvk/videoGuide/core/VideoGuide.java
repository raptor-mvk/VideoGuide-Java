/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.core;

import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.dao.Dao;
import ru.mvk.videoGuide.dao.DiscDao;
import ru.mvk.videoGuide.dao.FilmDao;
import ru.mvk.videoGuide.descriptor.ListViewInfo;
import ru.mvk.videoGuide.descriptor.ListViewInfoImpl;
import ru.mvk.videoGuide.descriptor.ViewInfo;
import ru.mvk.videoGuide.descriptor.ViewInfoImpl;
import ru.mvk.videoGuide.descriptor.column.DurationColumnInfo;
import ru.mvk.videoGuide.descriptor.column.FileSizeColumnInfo;
import ru.mvk.videoGuide.descriptor.column.LowerStringColumnInfo;
import ru.mvk.videoGuide.descriptor.column.StringColumnInfo;
import ru.mvk.videoGuide.descriptor.field.NaturalFieldInfo;
import ru.mvk.videoGuide.descriptor.field.TextFieldInfo;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;
import ru.mvk.videoGuide.javafx.layout.JFXLayout;
import ru.mvk.videoGuide.javafx.layout.JFXTabViewWindowLayout;
import ru.mvk.videoGuide.model.Disc;
import ru.mvk.videoGuide.model.Film;
import ru.mvk.videoGuide.module.db.DbController;
import ru.mvk.videoGuide.module.db.HibernateAdapter;
import ru.mvk.videoGuide.module.db.HibernateAdapterImpl;
import ru.mvk.videoGuide.module.db.VideoGuideDbController;
import ru.mvk.videoGuide.service.DiscViewService;
import ru.mvk.videoGuide.service.FilmViewService;
import ru.mvk.videoGuide.service.ViewService;
import ru.mvk.videoGuide.service.ViewServiceDescriptor;

import java.util.Properties;

public class VideoGuide extends Application {
  @NotNull
  private static final String DB_FILENAME = "VideoGuide.sqlite";
  @NotNull
  private final JFXLayout layout;
  @NotNull
  private final ViewInfo<Film> filmViewInfo;
  @NotNull
  private final ListViewInfo<Film> filmListViewInfo;
  @NotNull
  private final ViewService<Film> filmViewService;
  @NotNull
  private final Dao<Film, Integer> filmDao;
  @NotNull
  private final ViewInfo<Disc> discViewInfo;
  @NotNull
  private final ListViewInfo<Disc> discListViewInfo;
  @NotNull
  private final ViewService<Disc> discViewService;
  @NotNull
  private final Dao<Disc, Integer> discDao;
  @NotNull
  private final HibernateAdapter hibernateAdapter;
  @NotNull
  private final DbController videoGuideDbController;

  public VideoGuide() {
    layout = new JFXTabViewWindowLayout(800, 400);
    filmViewInfo = prepareFilmViewInfo();
    filmListViewInfo = prepareFilmListViewInfo();
    discViewInfo = prepareDiscViewInfo();
    discListViewInfo = prepareDiscListViewInfo();
    hibernateAdapter = prepareHibernateAdapter();
    filmDao = new FilmDao(hibernateAdapter);
    discDao = new DiscDao(hibernateAdapter);
    videoGuideDbController = new VideoGuideDbController(hibernateAdapter);
    if (!videoGuideDbController.isDbSuitable()) {
      videoGuideDbController.createDb();
    } else {
      if (!videoGuideDbController.updateDb()) {
        throw new VideoGuideRuntimeException("VideoGuide: Could not update database");
      }
    }
    filmViewService = prepareFilmViewService();
    discViewService = prepareDiscViewService();
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(@NotNull Stage primaryStage) throws Exception {
    filmViewService.showListView();
    layout.setStage(primaryStage);
    layout.show(1000, 460);
  }

  @Override
  public void stop() throws Exception {
    System.exit(0);
  }

  @NotNull
  private ViewInfo<Film> prepareFilmViewInfo() {
    @NotNull ViewInfo<Film> viewInfo = new ViewInfoImpl<>(Film.class);
    viewInfo.addFieldInfo("name", new TextFieldInfo("Название", 60));
    viewInfo.addFieldInfo("length",
        new NaturalFieldInfo<>(Integer.class, "Длительность", 6));
    viewInfo.addFieldInfo("size", new NaturalFieldInfo<>(Long.class, "Размер", 12));
    viewInfo.addFieldInfo("disc", new NaturalFieldInfo<>(Byte.class, "Диск", 2));
    viewInfo.addFieldInfo("filesCount", new NaturalFieldInfo<>(Short.class, "Файлов", 3));
    return viewInfo;
  }

  @NotNull
  private ListViewInfo<Film> prepareFilmListViewInfo() {
    @NotNull ListViewInfo<Film> listViewInfo = new ListViewInfoImpl<>(Film.class);
    listViewInfo.addColumnInfo("lowerName", new LowerStringColumnInfo("Название", 60));
    listViewInfo.addColumnInfo("length", new DurationColumnInfo("Длит.", 10));
    listViewInfo.addColumnInfo("size", new FileSizeColumnInfo("Размер", 10));
    listViewInfo.addColumnInfo("disc", new StringColumnInfo("Диск", 8));
    listViewInfo.addColumnInfo("filesCount", new StringColumnInfo("Файлов", 8));
    listViewInfo.addColumnInfo("averageLength", new DurationColumnInfo("Ср. длит.", 10));
    listViewInfo.addColumnInfo("averageSize", new FileSizeColumnInfo("Ср. размер", 12));
    return listViewInfo;
  }

  @NotNull
  private ViewInfo<Disc> prepareDiscViewInfo() {
    @NotNull ViewInfo<Disc> viewInfo = new ViewInfoImpl<>(Disc.class);
    viewInfo.addFieldInfo("number", new NaturalFieldInfo<>(Byte.class, "Диск", 2));
    viewInfo.addFieldInfo("sizeGb", new NaturalFieldInfo<>(Short.class, "Размер, Гб", 4));
    return viewInfo;
  }

  @NotNull
  private ListViewInfo<Disc> prepareDiscListViewInfo() {
    @NotNull ListViewInfo<Disc> listViewInfo = new ListViewInfoImpl<>(Disc.class);
    listViewInfo.addColumnInfo("number", new StringColumnInfo("Диск", 8));
    listViewInfo.addColumnInfo("size", new FileSizeColumnInfo("Размер", 10));
    listViewInfo.addColumnInfo("filmsCount", new StringColumnInfo("Фильмов", 10));
    listViewInfo.addColumnInfo("filmsFilesCount", new StringColumnInfo("Файлов", 10));
    listViewInfo.addColumnInfo("filmsLength", new DurationColumnInfo("Длительность", 10));
    listViewInfo.addColumnInfo("filmsSize", new FileSizeColumnInfo("Размер", 10));
    return listViewInfo;
  }

  @NotNull
  private ViewService<Film> prepareFilmViewService() {
    @NotNull ViewServiceDescriptor<Film> viewServiceDescriptor =
        new ViewServiceDescriptor<>(filmDao, filmViewInfo, filmListViewInfo);
    @NotNull ViewService<Film> viewService =
        new FilmViewService(viewServiceDescriptor, layout);
    viewService.setDefaultOrder("lowerName", true);
    return viewService;
  }

  @NotNull
  private ViewService<Disc> prepareDiscViewService() {
    @NotNull ViewServiceDescriptor<Disc> viewServiceDescriptor =
        new ViewServiceDescriptor<>(discDao, discViewInfo, discListViewInfo);
    @NotNull ViewService<Disc> viewService =
        new DiscViewService(viewServiceDescriptor, layout);
    viewService.setDefaultOrder("number", true);
    return viewService;
  }

  HibernateAdapter prepareHibernateAdapter() {
    @NotNull SessionFactory sessionFactory = prepareSessionFactory();
    return new HibernateAdapterImpl(sessionFactory);
  }

  @NotNull
  private SessionFactory prepareSessionFactory() {
    @NotNull Configuration configuration = prepareConfiguration();
    @NotNull Properties properties = configuration.getProperties();
    @NotNull ServiceRegistry serviceRegistry =
        new StandardServiceRegistryBuilder().applySettings(properties).build();
    return configuration.buildSessionFactory(serviceRegistry);
  }

  @NotNull
  private Configuration prepareConfiguration() {
    @NotNull Configuration configuration = new Configuration();
    @NotNull Properties properties = new Properties();
    properties.put("hibernate.connection.driver_class", "org.sqlite.JDBC");
    properties.put("hibernate.connection.url", "jdbc:sqlite:" + DB_FILENAME);
    properties.put("hibernate.current_session_context_class", "thread");
    properties.put("hibernate.dialect", "ru.mvk.service.hibernate.SQLiteDialect");
    configuration.setProperties(properties);
    configuration.addAnnotatedClass(Film.class);
    configuration.addAnnotatedClass(Disc.class);
    return configuration;
  }
}
