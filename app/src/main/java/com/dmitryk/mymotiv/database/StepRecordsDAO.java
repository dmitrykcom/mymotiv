package com.dmitryk.mymotiv.database;

import com.dmitryk.mymotiv.MyMotivApplication;
import com.dmitryk.mymotiv.event.DailySessionsReceived;
import com.dmitryk.mymotiv.model.DailySession;
import com.dmitryk.mymotiv.model.StepRecord;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class StepRecordsDAO extends BaseDaoImpl<StepRecord, Integer> {
  protected StepRecordsDAO(Class<StepRecord> dataClass) throws SQLException {
    super(dataClass);
  }

  protected StepRecordsDAO(ConnectionSource connectionSource, Class<StepRecord> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  protected StepRecordsDAO(ConnectionSource connectionSource, DatabaseTableConfig<StepRecord> tableConfig) throws SQLException {
    super(connectionSource, tableConfig);
  }

  public void saveAll(List<StepRecord> records) {

    for(StepRecord record : records) {
      try {
        record.setActiveSeconds((int)(record.getEnded().getTime() - record.getStarted().getTime()) / 1000);
        createIfNotExists(record);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

//

  public List<DailySession> getDailySessions() throws SQLException {
    final DateFormat df = new SimpleDateFormat("yyyy - m - d");
    List<DailySession> results = new ArrayList<>();

      results = queryRaw("select strftime('%Y - %m - %d', ended) as valYear, SUM(steps), SUM(activeseconds) FROM steprecord GROUP BY valYear", new RawRowMapper<DailySession>() {
        @Override
        public DailySession mapRow(String[] columnNames, String[] resultColumns) {
          DailySession session = new DailySession();
          try {
            session.setDate(df.parse(resultColumns[0]));
            session.setTotalSteps(Integer.parseInt(resultColumns[1]));
            session.setActiveSeconds(Integer.parseInt(resultColumns[2]));
          } catch (ParseException e) {
            e.printStackTrace();
          }
          return session;
        }
      }).getResults();
    return results;
  }

  public void getDailySessionsAsync() throws SQLException {
    Observable.create(new Observable.OnSubscribe<List<DailySession>>() {
      @Override
      public void call(Subscriber<? super List<DailySession>> subscriber) {
        try {
          List<DailySession> records = getDailySessions();
          subscriber.onNext(records);
          subscriber.onCompleted();
        } catch (Exception e) {
          subscriber.onError(e);
        }
      }
    }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<DailySession>>() {
              @Override
              public void call(List<DailySession> records) {
                // onNext post event
                MyMotivApplication.bus().post(new DailySessionsReceived(records));
              }
            }, new Action1<Throwable>() {
              @Override
              public void call(Throwable throwable) {
                throwable.printStackTrace();
              }
            });
  }

}
