/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.videoGuide.model;

import org.hibernate.annotations.Formula;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

public final class Disc {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "rowid", nullable = false, unique = true)
  private int id;

  @Column(name = "number", nullable = false)
  private byte number;

  @Column(name = "size", nullable = false)
  private short size;

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

  public short getSize() {
    return size;
  }

  public void setSize(short size) {
    this.size = size;
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
