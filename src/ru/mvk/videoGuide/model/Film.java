/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.videoGuide.model;

import org.hibernate.annotations.Formula;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.utils.IluvatarUtils;

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
  @Column(name = "name", nullable = false, length = 85)
  private String name;

  @NotNull
  @Column(name = "lowerName", nullable = false, length = 85)
  private String lowerName;

  @Column(name = "length", nullable = false)
  private int length;

  @Column(name = "size", nullable = false)
  private long size;

  @Column(name = "disc", nullable = false)
  private int disc;

  @Nullable
  @Formula("(select disc.name from disc where disc.rowid=disc)")
  private String discName;

  @Column(name = "filesCount", nullable = false)
  private short filesCount;

  @Formula("case when filesCount > 0 then length / filesCount else 0 end")
  private int averageLength;

  @Formula("case when filesCount > 0 then size / filesCount else 0 end")
  private long averageSize;

  public Film() {
    name = "";
    lowerName = "";
    discName = "";
    filesCount = 1;
  }

  @NotNull
  public Integer getDisc() {
    return disc;
  }

  public void setDisc(@NotNull Integer disc) {
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
  public String getDiscName() {
    return discName == null ? "" : discName;
  }

  public void setDiscName(@NotNull String discName) {
    this.discName = discName;
  }

  @NotNull
  public String getName() {
    return name;
  }

  public void setName(@NotNull String name) {
    this.name = name;
    this.lowerName = IluvatarUtils.normalizeString(name);
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

  public boolean equals(@NotNull Film film) {
    return (disc == film.disc) && (filesCount == film.filesCount) &&
               (id == film.id) && (length == film.length) &&
               (size == film.size) && name.equals(film.name);

  }

  @Override
  public boolean equals(@Nullable Object o) {
    return this == o || o != null && getClass() == o.getClass() &&
                            this.equals((Film) o);
  }

  @Override
  public int hashCode() {
    int result = 31 * id + name.hashCode();
    result = 31 * result + length;
    result = 31 * result + (int) (size ^ (size >>> 32));
    result = 31 * result + (int) disc;
    return 31 * result + (int) filesCount;
  }

  @Override
  public String toString() {
    return name;
  }
}
