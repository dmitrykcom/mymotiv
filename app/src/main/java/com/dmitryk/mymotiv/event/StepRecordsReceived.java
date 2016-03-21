package com.dmitryk.mymotiv.event;

import com.dmitryk.mymotiv.model.StepRecord;

import java.util.List;

public class StepRecordsReceived {

  private List<StepRecord> stepRecords;

  public StepRecordsReceived(List<StepRecord> stepRecords) {
    this.stepRecords = stepRecords;
  }

  public List<StepRecord> getStepRecords() {
    return stepRecords;
  }
}
