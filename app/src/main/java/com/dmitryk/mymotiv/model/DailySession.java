package com.dmitryk.mymotiv.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DailySession {

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, EEEE");

  private Date date;
  private int totalSteps;
  private long activeSeconds;


  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public int getTotalSteps() {
    return totalSteps;
  }

  public void setTotalSteps(int totalSteps) {
    this.totalSteps = totalSteps;
  }

  public int getAvgSteps() {
    return (int)(totalSteps / (activeSeconds / 60));
  }


  public long getActiveSeconds() {
    return activeSeconds;
  }

  public void setActiveSeconds(long activeSeconds) {
    this.activeSeconds = activeSeconds;
  }

  public String getDateString () {
    String dateString = dateFormat.format(date);
    return dateString;
  }
}
