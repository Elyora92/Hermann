package com.example.hermann_simple;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    CheckBox Day0, Day1, Day2, Day3, Day4, Day5, Day6, Day7, Day8, Day9, Day10;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name_long);

        Day0 = (CheckBox) findViewById(R.id.checkbox_0);
        Day1 = (CheckBox) findViewById(R.id.checkbox_1);
        Day2 = (CheckBox) findViewById(R.id.checkbox_2);
        Day3 = (CheckBox) findViewById(R.id.checkbox_3);
        Day4 = (CheckBox) findViewById(R.id.checkbox_4);
        Day5 = (CheckBox) findViewById(R.id.checkbox_5);
        Day6 = (CheckBox) findViewById(R.id.checkbox_6);
        Day7 = (CheckBox) findViewById(R.id.checkbox_7);
        Day8 = (CheckBox) findViewById(R.id.checkbox_8);
        Day9 = (CheckBox) findViewById(R.id.checkbox_9);
        Day10 = (CheckBox) findViewById(R.id.checkbox_10);

        CheckBox[] days = {Day0, Day1, Day2, Day3, Day4, Day5, Day6, Day7, Day8, Day9, Day10};

        for (CheckBox day : days) {
            day.setOnCheckedChangeListener(new Checker());
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Starte einen neuen Hermann!", Snackbar.LENGTH_LONG)
                        .setAction("Start", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showRecipe();
                                setAllCheckedFalse();
                                enable(Day0);
                                restartTimer();
                            }
                        }).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        CheckBox[] days = {Day0, Day1, Day2, Day3, Day4, Day5, Day6, Day7, Day8, Day9, Day10};
        for (int i=0; i<days.length; i++) {
            String key = "check_day" + i;
            Log.d("CHECKBOXES", "saveName is "+key);
            save(key, days[i].isChecked());
            Log.d("CHECKBOXES", "saveName is "+key);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckBox[] days = {Day0, Day1, Day2, Day3, Day4, Day5, Day6, Day7, Day8, Day9, Day10};
        for (int i=0; i<days.length; i++) {
            String key = "check_day" + i;
            Log.d("CHECKBOXES", "loadName is "+key);
            days[i].setChecked(load(key));
        }
    }

    private void save(String saveName, final boolean isChecked) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(saveName, isChecked);
        editor.commit();
    }

    private boolean load(String loadName) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(loadName, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_recipe) {
            // This code will start the new activity when the settings button is clicked on the bar at the top.
            Intent intent = new Intent(this, Recipe.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void restartTimer() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void showRecipe() {
        Intent intent = new Intent(this, Recipe.class);
        startActivity(intent);
    }

    private void setAllCheckedFalse() {
        CheckBox[] days = {Day0, Day1, Day2, Day3, Day4, Day5, Day6, Day7, Day8, Day9, Day10};

        for (CheckBox day : days) {
            day.setChecked(false);
        }

        for (int i=1; i<days.length; i++) {
            days[i].setClickable(false);
            days[i].setEnabled(false);
            days[i].setTextColor(getResources().getColor(R.color.greyed));
        }
    }

    class Checker implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(Day0.isChecked())
            {
                enable(Day1);
            }
            if(Day1.isChecked())
            {
                enable(Day2);
            }
            if(Day2.isChecked())
            {
                enable(Day3);
            }
            if(Day3.isChecked())
            {
                enable(Day4);
            }
            if(Day4.isChecked())
            {
                enable(Day5);
            }
            if(Day5.isChecked())
            {
                enable(Day6);
            }
            if(Day6.isChecked())
            {
                enable(Day7);
            }
            if(Day7.isChecked())
            {
                enable(Day8);
            }
            if(Day8.isChecked())
            {
                enable(Day9);
            }
            if(Day9.isChecked())
            {
                enable(Day10);
            }
            if(Day10.isChecked())
            {
                Day0.setText("Kuchen backen!");
                setAllCheckedFalse();
                restartTimer();
                enable(Day0);
            }
        }
    }

    public void enable(CheckBox day) {
        day.setClickable(true);
        day.setEnabled(true);
        day.setTextColor(getResources().getColor(R.color.textcolor));
    }
}
