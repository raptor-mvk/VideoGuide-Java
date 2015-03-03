/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.videoGuide.model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "film")
public class Film implements Serializable {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "rowid", nullable = false, unique = true)
  private int id;

  @NotNull
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "length", nullable = false)
  private int length;

  @Column(name = "size", nullable = false)
  private long size;

  @Column(name = "disc", nullable = false)
  private byte disc;

  @Column(name = "filesCount", nullable = false)
  private short filesCount;

  @Transient
  private int averageLength;

  @Transient
  private long averageSize;

  public Film() {
    name = "";
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
    return length / filesCount;
  }

  public long getAverageSize() {
    return size / filesCount;
  }
}
