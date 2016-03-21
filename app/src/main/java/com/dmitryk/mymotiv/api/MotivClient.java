package com.dmitryk.mymotiv.api;

import com.dmitryk.mymotiv.MyMotivApplication;
import com.dmitryk.mymotiv.database.HelperFactory;
import com.dmitryk.mymotiv.event.StepRecordsReceived;
import com.dmitryk.mymotiv.model.Results;
import com.dmitryk.mymotiv.model.StepRecord;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MotivClient {

  private static final String API_BASE_URL = "https://mechio.box.com/";
  private static final String STEP_RECORDS_FILE_NAME = "cqfszus3zxxkf6mnbtlycr2btbysdjgz.json";

  private static MotivApi getApi() {
    GsonBuilder builder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    builder.registerTypeAdapter(java.util.Date.class, new JsonSerializer<Date>() {
      @Override
      public JsonElement serialize(java.util.Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTime());
      }
    });
    builder.registerTypeAdapter(java.util.Date.class, new JsonDeserializer<Date>() {
      @Override
      public java.util.Date deserialize(com.google.gson.JsonElement p1, java.lang.reflect.Type p2, com.google.gson.JsonDeserializationContext p3) {
        return new java.util.Date(p1.getAsLong() * 1000); // to to date from millis
      }
    });

    Gson gson = builder.create();





    OkHttpClient httpClient = new OkHttpClient();
    MotivApi api = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
            .create(MotivApi.class);
    return api;
  }

  /**
   * Request steps records
   */
  public static Call<Results<StepRecord>> stepRecords() {
    return MotivClient.getApi().stepRecords(STEP_RECORDS_FILE_NAME);
  }

  /**
   * Request steps records async in io scheduler and report results back to main thread.
   * Posts event into messagebus
   */
  public static void stepRecordsAsync() {

    Observable.create(new Observable.OnSubscribe<List<StepRecord>>() {
      @Override
      public void call(Subscriber<? super List<StepRecord>> subscriber) {
        try {
          Call<Results<StepRecord>> call = stepRecords();
          retrofit.Response<Results<StepRecord>> response = call.execute();
          List<StepRecord> records = response.body().getData();

          // save records into the database
          HelperFactory.getHelper().getStepRecordsDAO().saveAll(records);

          subscriber.onNext(records);
          subscriber.onCompleted();
        } catch (Exception e) {
          subscriber.onError(e);
        }
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<StepRecord>>() {
          @Override
          public void call(List<StepRecord> stepRecords) {
            // onNext post event
            MyMotivApplication.bus().post(new StepRecordsReceived(stepRecords));
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            // ignore error and send bus event with empty recordset
            MyMotivApplication.bus().post(new StepRecordsReceived(new ArrayList<StepRecord>()));
          }
        });
  }


}
