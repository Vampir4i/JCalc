package com.example.jcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HistoryActivity extends AppCompatActivity {

    ListView lv;
    DB db;
    Cursor dbCursor;
    SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lv = findViewById(R.id.list_history);
        db = new DB(this);
        db.open();

        dbCursor = db.getAllData();
        String[] from = new String[] {DB.COLUMN_EXP, DB.COLUMN_RES};
        int[] to = new int[] {R.id.history_expression, R.id.history_result};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.history_item,
                dbCursor, from, to, 0);

        lv.setAdapter(cursorAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentVibrate =new Intent(getApplicationContext(),VibrateService.class);
                startService(intentVibrate);
                db.delRec(id);
                dbCursor = db.getAllData();
                cursorAdapter.swapCursor(dbCursor);
                cursorAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}