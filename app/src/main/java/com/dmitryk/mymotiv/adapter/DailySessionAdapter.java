package com.dmitryk.mymotiv.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmitryk.mymotiv.Config;
import com.dmitryk.mymotiv.R;
import com.dmitryk.mymotiv.model.DailySession;
import com.dmitryk.mymotiv.view.CircularProgress;

import java.text.DecimalFormat;
import java.util.List;

public class DailySessionAdapter extends ArrayAdapter<DailySession> {

  public DailySessionAdapter(Context context, int resource, List<DailySession> objects) {
    super(context, resource, objects);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;

    if (convertView == null) {
      LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
      convertView = inflater.inflate(R.layout.daily_session_item, parent, false);
    }

    viewHolder = new ViewHolder(convertView);

    DailySession session = getItem(position);
    DecimalFormat df = new DecimalFormat("#,###");

    String steps = df.format(session.getTotalSteps());

    String stepsFormat = getContext().getString(R.string.stepsFormat, steps);
    String avgStepsFormat = getContext().getString(R.string.avgStepsFormat, session.getAvgSteps());


    // change size of portion of text
    Spannable stepsSpan = new SpannableString(stepsFormat);
    Spannable avgStepsSpan = new SpannableString(avgStepsFormat);

    stepsSpan.setSpan(new RelativeSizeSpan(0.4f), stepsFormat.indexOf(" ") , stepsFormat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    avgStepsSpan.setSpan(new RelativeSizeSpan(0.4f), avgStepsFormat.indexOf(" ") , avgStepsFormat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    viewHolder.date.setText(session.getDateString());
    viewHolder.steps.setText(stepsSpan);
    viewHolder.progress.setProgress(session.getTotalSteps());
    viewHolder.averageSteps.setText(avgStepsSpan);


    return convertView;

  }

  public static class ViewHolder {

    public TextView date;
    public TextView steps;
    public CircularProgress progress;
    public TextView averageSteps;

    public ViewHolder(View v) {
      date = (TextView) v.findViewById(R.id.date);
      steps = (TextView) v.findViewById(R.id.steps);
      progress = (CircularProgress) v.findViewById(R.id.progress);
      progress.setMaxProgress(Config.DAILY_GOAL);
      averageSteps = (TextView) v.findViewById(R.id.averageSteps);
    }
  }
}
