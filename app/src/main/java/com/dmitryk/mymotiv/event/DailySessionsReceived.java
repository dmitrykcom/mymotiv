package com.dmitryk.mymotiv.event;

import com.dmitryk.mymotiv.model.DailySession;
import com.dmitryk.mymotiv.model.StepRecord;

import java.util.List;

public class DailySessionsReceived {

  private List<DailySession> dailySessions;

  public DailySessionsReceived(List<DailySession> dailySessions) {
    this.dailySessions = dailySessions;
  }

  public List<DailySession> getDailySessions() {
    return dailySessions;
  }
}
