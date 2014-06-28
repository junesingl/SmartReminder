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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.smartreminder.app.R;
import com.smartreminder.app.module.EventFeed;
import com.smartreminder.app.module.EventProvider;

public class EditViewController extends Activity {

    private EventFeed event;
    private ContentResolver eventResolver;
    private Cursor cursor;

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Switch alertSwitch;
    private EditText dateEditText;
    private EditText timeEditText;

    private MenuItem saveAction;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // view
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_edit_view_controller);

        titleEditText = (EditText) findViewById(R.id.titleInputView);
        descriptionEditText = (EditText) findViewById(R.id.descriptionInputView);
        alertSwitch = (Switch) findViewById(R.id.alertSwitch);
        dateEditText = (EditText) findViewById(R.id.dateSelect);
        timeEditText = (EditText) findViewById(R.id.timeSelect);

        saveAction = (MenuItem) findViewById(R.id.action_save);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        Button.OnClickListener buttonListener = new Button.OnClickListener() {
            @Override

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.deleteButton:
                        Intent intent1 = new Intent(EditViewController.this, TimeLineViewController.class);
                        startActivity(intent1);
                        finish();
                        return;
                    case R.id.dateSelect:
                        Intent intent2 = new Intent(EditViewController.this, SetDateTimeViewController.class);
                        startActivity(intent2);
                        return;
                    case R.id.timeSelect:
                        Intent intent3 = new Intent(EditViewController.this, SetDateTimeViewController.class);
                        startActivity(intent3);
                        return;
                }
            }
        };

        dateEditText.setOnClickListener(buttonListener);
        timeEditText.setOnClickListener(buttonListener);
        deleteButton.setOnClickListener(buttonListener);

        // data
        if(event != null) {
            eventResolver = this.getContentResolver();
            Uri uri = Uri.parse(EventProvider.CONTENT_URI_STRING + "/" + event.getCreateTime());
            cursor = eventResolver.query(uri, new String[]{EventProvider.KEY_ID, EventProvider.KEY_TITLE, EventProvider.KEY_DESCRIPTION, EventProvider.KEY_ALERTTIME, EventProvider.KEY_ALERTON}, null, null, EventProvider.KEY_ALERTTIME);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_view_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            event.setTitle(titleEditText.getText().toString());
            event.setDescription(descriptionEditText.getText().toString());
            event.setAlertOn(alertSwitch.isChecked());

            if(event.getTitle().equals("")) {
                Toast.makeText(this.getBaseContext(), "Event should have a title", Toast.LENGTH_SHORT).show();
            } else {
                if (event.getAlertOn()) {
                    if ((event.getAlertTime() - 1000 * 60) > System.currentTimeMillis()) {
                        //


                        Intent intent = new Intent(EditViewController.this, TimeLineViewController.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this.getBaseContext(), "Alert time should be any moment in the future", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //
                    Intent intent = new Intent(EditViewController.this, TimeLineViewController.class);
                    startActivity(intent);
                    finish();
                }
            }

            return true;
        } else if (id == android.R.id.home){
            //
            Intent intent = new Intent(EditViewController.this, EventViewController.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //
        Intent intent = new Intent(EditViewController.this, TimeLineViewController.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}
