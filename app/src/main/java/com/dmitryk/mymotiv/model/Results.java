package com.dmitryk.mymotiv.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Wrapper for "results" field in Json response.
 * @param <K> - Model class
 */

public class Results<K> {
  @SerializedName("stepRecords")
  private List<K> data;

  public List<K> getData() {
    return data;
  }
}
