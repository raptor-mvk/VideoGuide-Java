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
import ru.mvk.iluvatar.dao.Dao;
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
import ru.mvk.iluvatar.exception.IluvatarRuntimeException;
import ru.mvk.iluvatar.javafx.layout.JFXLayout;
import ru.mvk.iluvatar.javafx.layout.JFXTabViewWindowLayout;
import ru.mvk.iluvatar.module.db.DbController;
import ru.mvk.iluvatar.module.db.HibernateAdapter;
import ru.mvk.iluvatar.module.db.HibernateAdapterImpl;
import ru.mvk.iluvatar.service.ViewService;
import ru.mvk.iluvatar.service.ViewServiceDescriptor;
import ru.mvk.videoGuide.dao.DiscDao;
import ru.mvk.videoGuide.dao.FilmDao;
import ru.mvk.videoGuide.model.Disc;
import ru.mvk.videoGuide.model.Film;
import ru.mvk.videoGuide.module.db.VideoGuideDbController;
import ru.mvk.videoGuide.service.DiscViewService;
import ru.mvk.videoGuide.service.FilmViewService;

import java.util.Properties;

public class VideoGuide extends Application {
  @NotNull
  private static final String DB_FILENAME = "VideoGuide.sqlite";
  @NotNull
  private final JFXLayout layout;
  @NotNull
  private final ViewService<Film> filmViewService;
  @NotNull
  private final ViewService<Disc> discViewService;
  @NotNull
  private final HibernateAdapter hibernateAdapter;
  @NotNull
  private final DbController videoGuideDbController;

  public VideoGuide() {
    layout = new JFXTabViewWindowLayout(800, 400);
    hibernateAdapter = prepareHibernateAdapter();
    videoGuideDbController = new VideoGuideDbController(hibernateAdapter);
    if (!videoGuideDbController.isDbSuitable()) {
      videoGuideDbController.createDb();
    } else {
      if (!videoGuideDbController.updateDb()) {
        throw new IluvatarRuntimeException("VideoGuide: Could not update database");
      }
    }
    filmViewService = new FilmViewService(hibernateAdapter, layout);
    discViewService = new DiscViewService(hibernateAdapter, layout);
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

  private HibernateAdapter prepareHibernateAdapter() {
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
