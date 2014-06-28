package com.smartreminder.app.controller;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.smartreminder.app.R;
import com.smartreminder.app.module.EventFeed;
import com.smartreminder.app.module.EventProvider;
import com.smartreminder.app.view.EventViewHolder;

import java.util.ArrayList;
import java.util.List;


public class TimeLineViewController extends Activity {

    private ContentResolver eventResolver;
    private List<EventFeed> event;
    private List<EventViewHolder> eventHolder;

    private ListView eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // view creating
        setContentView(R.layout.activity_time_line_view_controller);
        eventList = (ListView) findViewById(R.id.listView);

        // data preparing
        eventResolver = this.getContentResolver();
        Uri uri = Uri.parse(EventProvider.CONTENT_URI_STRING + "/" + "*");
        Cursor cursor = eventResolver.query(uri, new String[] {EventProvider.KEY_ID, EventProvider.KEY_TITLE, EventProvider.KEY_DESCRIPTION, EventProvider.KEY_ALERTTIME, EventProvider.KEY_ALERTON}, null, null, EventProvider.KEY_ALERTTIME);

        event = new ArrayList <EventFeed>();
        eventHolder = new ArrayList<EventViewHolder>();

        if (cursor!=null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                EventFeed eventFeed = new EventFeed();

                eventFeed.setCreateTime(cursor.getLong(cursor.getColumnIndex(EventProvider.KEY_ID)));
                eventFeed.setTitle(cursor.getString(cursor.getColumnIndex(EventProvider.KEY_TITLE)));
                eventFeed.setDescription(cursor.getString(cursor.getColumnIndex(EventProvider.KEY_DESCRIPTION)));
                eventFeed.setAlertTime(cursor.getLong(cursor.getColumnIndex(EventProvider.KEY_ALERTTIME)));
                eventFeed.setAlertOn(cursor.getString(cursor.getColumnIndex(EventProvider.KEY_ALERTON)) == "true" ? true : false);
                eventFeed.setCategory();
                cursor.moveToNext();

                event.add(eventFeed);

                EventViewHolder eventViewHolder = new EventViewHolder();

                eventViewHolder.titleTag.setText(eventFeed.getTitle());
                if (eventFeed.getAlertOn()) {
                    eventViewHolder.alertTag.setVisibility(View.VISIBLE);
                } else
                    eventViewHolder.alertTag.setVisibility(View.GONE);

                int q = (int) (eventFeed.getAlertTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
                eventViewHolder.daysLeftTag.setText(q + "d");
                switch (eventFeed.getCategory()) {
                    case EventFeed.RED_TAG:
                        eventViewHolder.daysLeftTag.setBackgroundColor(0xFF0000);
                        break;
                    case EventFeed.YELLOW_TAG:
                        eventViewHolder.daysLeftTag.setBackgroundColor(0xFFFF00);
                        break;
                    case EventFeed.BLUE_TAG:
                        eventViewHolder.daysLeftTag.setBackgroundColor(0x0000FF);
                        break;
                    case EventFeed.GRAY_TAG:
                        eventViewHolder.daysLeftTag.setBackgroundColor(0x808080);
                        break;
                }

                eventHolder.add(eventViewHolder);
            }

        } else {
            Toast.makeText(this.getBaseContext(), "No Event Created", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_line_view_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(TimeLineViewController.this, EditViewController.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_account) {
            Intent intent = new Intent(TimeLineViewController.this, AccountViewController.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(TimeLineViewController.this, SettingsViewController.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
