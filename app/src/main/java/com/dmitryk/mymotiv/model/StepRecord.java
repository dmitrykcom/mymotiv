package com.dmitryk.mymotiv.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class StepRecord {

  @DatabaseField(canBeNull = false, dataType = DataType.LONG_OBJ, generatedId = true)
  private Long id;

  @SerializedName("utcStart")
  @DatabaseField(dataType = DataType.DATE, uniqueCombo=true)
  private Date started;

  @SerializedName("utcEnd")
  @DatabaseField(dataType = DataType.DATE, uniqueCombo=true)
  private Date ended;

  @DatabaseField(dataType = DataType.INTEGER)
  private int steps;

  @DatabaseField(dataType = DataType.INTEGER, useGetSet = true)
  private int activeSeconds;


  /**
   * @return Record start timestamp in seconds
   */
  public Date getStarted() {
    return started;
  }

  public void setStarted(Date started) {
    this.started = started;
  }

  /**
   * @return Record end timestamp in seconds
   */
  public Date getEnded() {
    return ended;
  }

  public void setEnded(Date ended) {
    this.ended = ended;
  }

  public int getSteps() {
    return steps;
  }

  public void setSteps(int steps) {
    this.steps = steps;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getActiveSeconds() {
    return this.activeSeconds;
  }

  public void setActiveSeconds(int activeSeconds) {
    this.activeSeconds = activeSeconds;
  }
}
