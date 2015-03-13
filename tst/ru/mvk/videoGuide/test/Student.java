/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.videoGuide.test;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Student {
  private int id;
  @NotNull
  private String name;
  private double gpa;
  private short penalty;
  private boolean graduated;
  private long fileSize;
  private int lecturesTime;

  public Student() {
    name = "";
  }

  public boolean isGraduated() {
    return graduated;
  }

  public void setGraduated(boolean graduated) {
    this.graduated = graduated;
  }

  public int getId() {
    return id;
  }

  @NotNull
  public String getName() {
    return name;
  }

  public double getGpa() {
    return gpa;
  }

  public void setGpa(double gpa) {
    this.gpa = gpa;
  }

  public short getPenalty() {
    return penalty;
  }

  public void setPenalty(short penalty) {
    this.penalty = penalty;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(@NotNull String name) {
    this.name = name;
  }

  public long getFileSize() {
    return fileSize;
  }

  public void setFileSize(long fileSize) {
    this.fileSize = fileSize;
  }

  public int getLecturesTime() {
    return lecturesTime;
  }

  public void setLecturesTime(int lecturesTime) {
    this.lecturesTime = lecturesTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, gpa, penalty, graduated, fileSize, lecturesTime);
  }

  private boolean equals(@NotNull Student object) {
    return Objects.equals(id, object.id) && Objects.equals(name, object.name) &&
        Objects.equals(gpa, object.gpa) && Objects.equals(penalty, object.penalty) &&
        Objects.equals(graduated, object.graduated) &&
        Objects.equals(fileSize, object.fileSize) &&
        Objects.equals(lecturesTime, object.lecturesTime);
  }

  @Override
  public boolean equals(@Nullable Object object) {
    return (this == object) || ((object instanceof Student) && equals((Student) object));
  }
}
