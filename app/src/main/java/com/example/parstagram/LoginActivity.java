package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null)
        {
            goToMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "OnClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "OnClick signup button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signupUser(username, password);
            }
        });
    }

    private void signupUser(String username, String password) {
        Log.i(TAG, "Attempting to signup new user");
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    if(username.isEmpty())
                    {
                        Log.e(TAG, "Username field empty", e);
                        Toast.makeText(LoginActivity.this, "Please enter a username to signup", Toast.LENGTH_SHORT).show();
                    } else if(password.isEmpty()) {
                        Log.e(TAG, "Password field empty", e);
                        Toast.makeText(LoginActivity.this, "Please enter a password to signup", Toast.LENGTH_SHORT).show();
                    } else if(e.getCode() == 202) {
                        Log.e(TAG, "User already exists", e);
                        Toast.makeText(LoginActivity.this, "This user already exists; Please try a new username!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Issue with signup" + e.getCode(), e);
                        Toast.makeText(LoginActivity.this, "Issue with signup!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    goToMainActivity();
                    Toast.makeText(LoginActivity.this, "Signup Success!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);
        //navigate to the main activity if the user has signed in properly.
        // Logging in in the background executes on the background thread rather than main or UI thread. Better for performance/ user experience
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null)
                {
                    if(username.isEmpty())
                    {
                        Log.e(TAG, "Username field empty", e);
                        Toast.makeText(LoginActivity.this, "Please enter a username to login", Toast.LENGTH_SHORT).show();
                    } else if(password.isEmpty()) {
                        Log.e(TAG, "Password field empty", e);
                        Toast.makeText(LoginActivity.this, "Please enter a password to login", Toast.LENGTH_SHORT).show();
                    } else if(e.getCode() == 101) {
                        Log.e(TAG, "User does not exist", e);
                        Toast.makeText(LoginActivity.this, "Please enter a valid username and password to login.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Issue with login " + e.getCode(), e);
                        Toast.makeText(LoginActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    goToMainActivity();
                    Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}