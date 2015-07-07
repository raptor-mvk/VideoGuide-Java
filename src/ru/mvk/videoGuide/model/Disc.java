/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.model;

import org.hibernate.annotations.Formula;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "disc")
public final class Disc {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "rowid", nullable = false, unique = true)
  private int id;

  @Column(name = "number", nullable = false)
  private byte number;

  @Column(name = "size", nullable = false)
  private long size;

  @Formula("(select count(*) from Film where film.disc=number)")
  private int filmsCount;

  @Formula("(select case when sum(film.filesCount) is null then 0 else " +
               "sum(film.filesCount) end from Film where film.disc=number)")
  private int filmsFilesCount;

  @Formula("(select case when sum(film.length) is null then 0 else sum(film.length) " +
               "end from Film where film.disc=number)")
  private int filmsLength;

  @Formula("(select case when sum(film.size) is null then 0 else sum(film.size) end " +
               "from Film where film.disc=number)")
  private long filmsSize;

  @Transient
  private long freeSize;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public byte getNumber() {
    return number;
  }

  public void setNumber(byte number) {
    this.number = number;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public long getFreeSize() {
    return size - filmsSize;
  }

  public long getSize() {
    return size;
  }

  public int getFilmsCount() {
    return filmsCount;
  }

  public int getFilmsFilesCount() {
    return filmsFilesCount;
  }

  public int getFilmsLength() {
    return filmsLength;
  }

  public long getFilmsSize() {
    return filmsSize;
  }

  public void setFilmsCount(int filmsCount) {
    this.filmsCount = filmsCount;
  }

  public void setFilmsFilesCount(int filmsFilesCount) {
    this.filmsFilesCount = filmsFilesCount;
  }

  public void setFilmsLength(int filmsLength) {
    this.filmsLength = filmsLength;
  }

  public void setFilmsSize(long filmsSize) {
    this.filmsSize = filmsSize;
  }

  public boolean equals(@NotNull Disc disc) {
    return (id == disc.id) && (number == disc.number) && (size == disc.size);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    return this == o || o != null && getClass() == o.getClass() &&
                            this.equals((Disc) o);
  }

  @Override
  public int hashCode() {
    int result = 31 * id + (int) number;
    return 31 * result + (int) size;
  }
}
