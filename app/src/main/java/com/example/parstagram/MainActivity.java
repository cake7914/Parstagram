package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null)
                        {
                            Log.e(TAG, "Issue with logout", e);
                            Toast.makeText(MainActivity.this, "Issue with logout!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //go to the login activity
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                            Toast.makeText(MainActivity.this, "Success in logging out!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}