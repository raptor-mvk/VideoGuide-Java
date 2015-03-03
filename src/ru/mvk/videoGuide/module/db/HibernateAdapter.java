/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.module.db;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.videoGuide.exception.VideoGuideRuntimeException;

import java.util.function.Function;

public class HibernateAdapter {
  @NotNull
  private final SessionFactory sessionFactory;

  public HibernateAdapter(@NotNull SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @NotNull
  public Session getCurrentSession() {
    @Nullable Session result = sessionFactory.getCurrentSession();
    if (result == null) {
      throw new VideoGuideRuntimeException("HibernateAdapter: currentSession is null");
    }
    return result;
  }

  @NotNull
  public Transaction beginTransaction(@NotNull Session session) {
    @Nullable Transaction transaction = session.beginTransaction();
    if (transaction == null) {
      throw new VideoGuideRuntimeException("HibernateAdapter: transaction is null");
    }
    return transaction;
  }

  @NotNull
  public Query prepareSqlQuery(@NotNull String sql, @NotNull Session session) {
    @Nullable Query query = session.createSQLQuery(sql);
    if (query == null) {
      throw new VideoGuideRuntimeException("HibernateAdapter: query is null");
    }
    return query;
  }

  @Nullable
  public <Type> Type executeInTransaction(@NotNull Function<Session, Type> function) {
    @Nullable Type result = null;
    @NotNull Session session = getCurrentSession();
    @NotNull Transaction transaction = beginTransaction(session);
    try {
      result = function.apply(session);
      transaction.commit();
    } catch (Throwable e) {
      transaction.rollback();
    }
    return result;
  }
}
