package com.dmitryk.mymotiv.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.dmitryk.mymotiv.MyMotivApplication;
import com.dmitryk.mymotiv.R;
import com.dmitryk.mymotiv.adapter.DailySessionAdapter;
import com.dmitryk.mymotiv.api.MotivClient;
import com.dmitryk.mymotiv.database.HelperFactory;
import com.dmitryk.mymotiv.event.DailySessionsReceived;
import com.dmitryk.mymotiv.event.StepRecordsReceived;
import com.dmitryk.mymotiv.model.DailySession;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

  private ListView listView;
  private DailySessionAdapter adapter;
  private List<DailySession> dailySessions = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MyMotivApplication.bus().register(this);
    listView = (ListView) findViewById(R.id.recordsView);
    adapter = new DailySessionAdapter(this, R.layout.daily_session_item, dailySessions);
    listView.setAdapter(adapter);

    // Request step records from server. Expect to receive StepRecordsReceived event
    MotivClient.stepRecordsAsync();
  }


  /**
   * Fired from the message bus into UI thread with StepRecordsReceived event
   */
  @Subscribe
  public void onRecordsReceived(StepRecordsReceived event) {
    try {
      // Request calculated list of DailySession from server. Expect to receive DailySessionReceived event
      HelperFactory.getHelper().getStepRecordsDAO().getDailySessionsAsync();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Fired from the message bus into UI thread with DailySessionReceived event
   */
  @Subscribe
  public void onDailySesmbsionsReceived(DailySessionsReceived event) {
    dailySessions.clear();
    dailySessions.addAll(event.getDailySessions());
    adapter.notifyDataSetChanged();
  }

  @Override
  protected void onDestroy() {
    MyMotivApplication.bus().unregister(this);
    super.onDestroy();
  }
}
