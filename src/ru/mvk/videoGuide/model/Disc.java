/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.model;

import org.hibernate.annotations.Formula;
import org.jetbrains.annotations.NotNull;

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
  private short sizeGb;

  @Formula("size*1073741824")
  private long size;

  @Formula("(select count(*) from Film where film.disc=number)")
  private int filmsCount;

  @Formula("(select sum(film.filesCount) from Film where film.disc=number)")
  private int filmsFilesCount;

  @Formula("(select sum(film.length) from Film where film.disc=number)")
  private int filmsLength;

  @Formula("(select sum(film.size) from Film where film.disc=number)")
  private long filmsSize;

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

  public short getSizeGb() {
    return sizeGb;
  }

  public void setSizeGb(short sizeGb) {
    this.sizeGb = sizeGb;
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

  @Override
  public boolean equals(Object o) {
    boolean result;
    if (this == o) {
      result = true;
    } else if (o == null || getClass() != o.getClass()) {
      result = false;
    } else {
      Disc disc = (Disc) o;
      result = (id == disc.id) && (number == disc.number) && (size == disc.size);
    }
    return result;
  }

  @Override
  public int hashCode() {
    int result = 31 * id + (int) number;
    return 31 * result + (int) size;
  }
}
