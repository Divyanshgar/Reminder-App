package com.example.remainderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText mReminderEditText;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReminderEditText = findViewById(R.id.editText);
        mDatePicker = findViewById(R.id.datePicker);
        mTimePicker = findViewById(R.id.timePicker);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = mReminderEditText.getText().toString();
                if (reminderText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a reminder", Toast.LENGTH_SHORT).show();
                    return;
                }

                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int dayOfMonth = mDatePicker.getDayOfMonth();
                int hourOfDay = mTimePicker.getHour();
                int minute = mTimePicker.getMinute();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth, hourOfDay, minute, 0);

                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                    Toast.makeText(MainActivity.this, "Please select a future date and time", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ReminderBroadcastReceiver.class);
                intent.putExtra("reminderText", reminderText);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                Toast.makeText(MainActivity.this, "Reminder set for " + DateFormat.getDateTimeInstance().format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
