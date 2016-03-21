package com.dmitryk.mymotiv.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dmitryk.mymotiv.model.StepRecord;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

  private static final String TAG = DatabaseHelper.class.getSimpleName();

  private static final String DATABASE_NAME = "motiv.db";

  private static final int DATABASE_VERSION = 1;

  private StepRecordsDAO stepRecordsDAO = null;

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    try {
      TableUtils.createTable(connectionSource, StepRecord.class);
    } catch (java.sql.SQLException e) {
      Log.e(TAG, "error creating DB " + DATABASE_NAME);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer) {
  }


  public StepRecordsDAO getStepRecordsDAO() throws SQLException {
    if (stepRecordsDAO == null) {
      stepRecordsDAO = new StepRecordsDAO(getConnectionSource(), StepRecord.class);
    }
    return stepRecordsDAO;
  }

  @Override
  public void close() {
    super.close();
    stepRecordsDAO = null;
  }
}