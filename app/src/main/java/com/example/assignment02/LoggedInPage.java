package com.example.assignment02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoggedInPage extends AppCompatActivity {

    private TextView loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_page);

        loggedInUsername = findViewById(R.id.logged_in_username);

        Intent intent = getIntent();
        //username taken from log in page
        String username = intent.getStringExtra(MainActivity.EXTRA_USERNAME);
        loggedInUsername.setText(" " + username + "!");
    }
}
