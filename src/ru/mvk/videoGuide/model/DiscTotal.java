/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.model;

import org.hibernate.annotations.Formula;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "totals")
public final class DiscTotal {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "rowid", nullable = false, unique = true)
  private int id;

  @Formula("(select sum(disc.size) from Disc)")
  private long size;

  @Formula("(select count(*) from Film)")
  private int filmsCount;

  @Formula("(select case when sum(film.filesCount) is null then 0 else " +
               "sum(film.filesCount) end from Film)")
  private int filmsFilesCount;

  @Formula("(select case when sum(film.length) is null then 0 else sum(film.length) " +
               "end from Film)")
  private int filmsLength;

  @Formula("(select case when sum(film.size) is null then 0 else sum(film.size) end " +
               "from Film)")
  private long filmsSize;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public int getFilmsCount() {
    return filmsCount;
  }

  public void setFilmsCount(int filmsCount) {
    this.filmsCount = filmsCount;
  }

  public int getFilmsFilesCount() {
    return filmsFilesCount;
  }

  public void setFilmsFilesCount(int filmsFilesCount) {
    this.filmsFilesCount = filmsFilesCount;
  }

  public int getFilmsLength() {
    return filmsLength;
  }

  public void setFilmsLength(int filmsLength) {
    this.filmsLength = filmsLength;
  }

  public long getFilmsSize() {
    return filmsSize;
  }

  public void setFilmsSize(long filmsSize) {
    this.filmsSize = filmsSize;
  }

  @NotNull
  public Disc getDisc() {
    @NotNull Disc totalDisc = new Disc();
    totalDisc.setId(0);
    totalDisc.setName("Всего");
    totalDisc.setSize(size);
    totalDisc.setFilmsCount(filmsCount);
    totalDisc.setFilmsFilesCount(filmsFilesCount);
    totalDisc.setFilmsLength(filmsLength);
    totalDisc.setFilmsSize(filmsSize);
    return totalDisc;
  }
}
