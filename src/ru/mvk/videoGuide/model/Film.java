/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.videoGuide.model;

import org.hibernate.annotations.Formula;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "film")
public final class Film implements Serializable {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "rowid", nullable = false, unique = true)
  private int id;

  @NotNull
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @NotNull
  @Column(name = "lowerName", nullable = false, length = 100)
  private String lowerName;

  @Column(name = "length", nullable = false)
  private int length;

  @Column(name = "size", nullable = false)
  private long size;

  @Column(name = "disc", nullable = false)
  private byte disc;

  @Column(name = "filesCount", nullable = false)
  private short filesCount;

  @Formula("length / filesCount")
  private int averageLength;

  @Formula("size / filesCount")
  private long averageSize;

  public Film() {
    name = "";
    lowerName = "";
    filesCount = 1;
  }

  public byte getDisc() {
    return disc;
  }

  public void setDisc(byte disc) {
    this.disc = disc;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  @NotNull
  public String getName() {
    return name;
  }

  public void setName(@NotNull String name) {
    this.name = name;
    this.lowerName = name.toLowerCase();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public short getFilesCount() {
    return filesCount;
  }

  public void setFilesCount(short filesCount) {
    this.filesCount = filesCount;
  }

  public int getAverageLength() {
    return averageLength;
  }

  public long getAverageSize() {
    return averageSize;
  }

  @NotNull
  public String getLowerName() {
    return lowerName + name;
  }

  @Override
  public boolean equals(Object o) {
    boolean result;
    if (this == o) {
      result = true;
    } else if (o == null || getClass() != o.getClass()) {
      result = false;
    } else {
      Film film = (Film) o;
      result = (disc == film.disc) && (filesCount == film.filesCount) &&
          (id == film.id) && (length == film.length) && (size == film.size) &&
          name.equals(film.name);
    }
    return result;
  }

  @Override
  public int hashCode() {
    int result = 31 * id + name.hashCode();
    result = 31 * result + length;
    result = 31 * result + (int) (size ^ (size >>> 32));
    result = 31 * result + (int) disc;
    return 31 * result + (int) filesCount;
  }
}
