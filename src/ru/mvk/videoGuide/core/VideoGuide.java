/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.core;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;
import ru.mvk.videoGuide.dao.Dao;
import ru.mvk.videoGuide.dao.FilmDao;
import ru.mvk.videoGuide.descriptor.ListViewInfo;
import ru.mvk.videoGuide.descriptor.ListViewInfoImpl;
import ru.mvk.videoGuide.descriptor.ViewInfo;
import ru.mvk.videoGuide.descriptor.ViewInfoImpl;
import ru.mvk.videoGuide.descriptor.column.DurationColumnInfo;
import ru.mvk.videoGuide.descriptor.column.FileSizeColumnInfo;
import ru.mvk.videoGuide.descriptor.column.StringColumnInfo;
import ru.mvk.videoGuide.descriptor.field.NaturalFieldInfo;
import ru.mvk.videoGuide.descriptor.field.TextFieldInfo;
import ru.mvk.videoGuide.javafx.JFXListView;
import ru.mvk.videoGuide.javafx.JFXView;
import ru.mvk.videoGuide.model.Film;
import ru.mvk.videoGuide.module.db.DbController;
import ru.mvk.videoGuide.module.db.HibernateAdapter;
import ru.mvk.videoGuide.module.db.VideoGuideDbController;
import ru.mvk.videoGuide.service.FilmViewService;
import ru.mvk.videoGuide.service.ViewService;
import ru.mvk.videoGuide.view.ListView;
import ru.mvk.videoGuide.view.View;

import java.util.Properties;

public class VideoGuide extends Application {
	@NotNull
	private static final String DB_FILENAME = "VideoGuide.sqlite";
	@NotNull
	private final ScrollPane root;
	@NotNull
	private final ViewInfo<Film> filmViewInfo;
	@NotNull
	private final ListViewInfo<Film> filmListViewInfo;
	@NotNull
	private final View<Film> filmView;
	@NotNull
	private final ListView<Film> filmListView;
	@NotNull
	private final ViewService<Film> filmViewService;
	@NotNull
	private final Dao<Film, Integer> filmDao;
	@NotNull
	private final HibernateAdapter hibernateAdapter;
	@NotNull
	private final DbController videoGuideDbController;

	public VideoGuide() {
		root = new ScrollPane();
		filmViewInfo = prepareFilmViewInfo();
		filmListViewInfo = prepareFilmListViewInfo();
		filmView = new JFXView<>(filmViewInfo);
		filmListView = new JFXListView<>(filmListViewInfo);
		hibernateAdapter = prepareHibernateAdapter();
		filmDao = new FilmDao(hibernateAdapter);
		videoGuideDbController = new VideoGuideDbController(hibernateAdapter);
		if (!videoGuideDbController.isDbSuitable()) {
			videoGuideDbController.createDb();
		} else {
			videoGuideDbController.updateDb();
		}
		filmViewService = prepareFilmViewService();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		filmViewService.showListView();
		primaryStage.setScene(new Scene(root, 1000, 440));
		primaryStage.setResizable(false);
		primaryStage.show();
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
				new NaturalFieldInfo<>(Integer.class, "Длительность", 3));
		viewInfo.addFieldInfo("size", new NaturalFieldInfo<>(Long.class, "Размер", 12));
		viewInfo.addFieldInfo("disc", new NaturalFieldInfo<>(Byte.class, "Диск", 2));
		viewInfo.addFieldInfo("filesCount", new NaturalFieldInfo<>(Short.class, "Файлов", 3));
		return viewInfo;
	}

	@NotNull
	private ListViewInfo<Film> prepareFilmListViewInfo() {
		@NotNull ListViewInfo<Film> listViewInfo = new ListViewInfoImpl<>(Film.class);
		listViewInfo.addColumnInfo("name", new StringColumnInfo("Название", 60));
		listViewInfo.addColumnInfo("length", new DurationColumnInfo("Длит.", 10));
		listViewInfo.addColumnInfo("size", new FileSizeColumnInfo("Размер", 10));
		listViewInfo.addColumnInfo("disc", new StringColumnInfo("Диск", 8));
		listViewInfo.addColumnInfo("filesCount", new StringColumnInfo("Файлов", 8));
		listViewInfo.addColumnInfo("averageLength", new DurationColumnInfo("Ср. длит.", 10));
		listViewInfo.addColumnInfo("averageSize", new FileSizeColumnInfo("Ср. размер", 12));
		return listViewInfo;
	}

	@NotNull
	private ViewService<Film> prepareFilmViewService() {
		@NotNull ViewService<Film> viewService =
				new FilmViewService(filmDao, filmView, filmListView);
		viewService.setContentSetter((content) -> {
			if (content instanceof Node) {
				root.setContent((Node) content);
			}
		});
		return viewService;
	}

	@NotNull
	HibernateAdapter prepareHibernateAdapter() {
		@NotNull SessionFactory sessionFactory = prepareSessionFactory();
		return new HibernateAdapter(sessionFactory);
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
		return configuration;
	}
}
