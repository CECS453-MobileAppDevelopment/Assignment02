// Author: Juan Pasillas

package com.example.assignment02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "com.example.assignment01.EXTRA_USERNAME";

    ArrayList<User> userList;
    private TextInputLayout loginUsername;
    private TextInputLayout loginPassword;
    private Button mbtnSignUp;
    private Button mbtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUsers();
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        mbtnSignUp = findViewById(R.id.btn_signup);
        mbtnLogin = findViewById(R.id.btn_login);

        mbtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpPage.class));
            }
        });

        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateLogin()) {
                    Intent intent = new Intent(MainActivity.this, LoggedInPage.class);
                    //username sent to logged in activity
                    intent.putExtra(EXTRA_USERNAME, loginUsername.getEditText().getText().toString().trim().toLowerCase());
                    startActivity(intent);
                } else {
                    loginPassword.getEditText().getText().clear();
                    Toast.makeText( getApplicationContext(), "Invalid Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /* Loads current list of current users
     *  Uses a JSON file to pull users locally */
    private void loadUsers() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson(); //parser for json file
        String json = sharedPreferences.getString("user list", null);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        userList = gson.fromJson(json, type);
        if (userList == null) {
            userList = new ArrayList<>();
        } //create arraylist if not already existing
    }

    /* Validates login credentials with current users */
    private boolean validateLogin() {
        for(User user: userList) {
            Log.d("UserPrint", user.toString());
            if (loginUsername.getEditText().getText().toString().trim().equalsIgnoreCase(user.getUsername())
                    && loginPassword.getEditText().getText().toString().equals(user.getPassword())) {
                return true; //valid login
            }
        }
        return false; //invalid login
    }
}
