package com.dmitryk.mymotiv;

import android.app.Application;

import com.dmitryk.mymotiv.database.HelperFactory;
import com.squareup.otto.Bus;

public class MyMotivApplication extends Application {

  private static Bus bus;

  @Override
  public void onCreate() {
    HelperFactory.setHelper(getApplicationContext());
    super.onCreate();
  }

  @Override
  public void onTerminate() {
    HelperFactory.releaseHelper();
    super.onTerminate();
  }

  public static Bus bus() {
    if (bus == null) {
      bus = new Bus();
    }
    return bus;
  }

}
