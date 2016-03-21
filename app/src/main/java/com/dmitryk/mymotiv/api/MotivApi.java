package com.dmitryk.mymotiv.api;

import com.dmitryk.mymotiv.model.Results;
import com.dmitryk.mymotiv.model.StepRecord;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

interface MotivApi {

  @GET("/shared/static/{filename}")
  Call<Results<StepRecord>> stepRecords(@Path("filename") String filename);
}
