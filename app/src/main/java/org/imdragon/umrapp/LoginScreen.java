package org.imdragon.umrapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class LoginScreen extends AppCompatActivity {
    Firebase mRef;
    public static final String PREFS_NAME = "MyPrefsFile";

    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        mRef.setAndroidContext(this);
        mRef = new Firebase("https://umr-theapp.firebaseio.com");
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        editor = settings.edit();


    }

    public void login(View v) {
        EditText mUsername = (EditText) findViewById(R.id.usernameInput);
        EditText mPassword = (EditText) findViewById(R.id.passwordInput);

        mRef.authWithPassword(mUsername.getText().toString(), mPassword.getText().toString(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Toast.makeText(LoginScreen.this, "You successfully logged in!", Toast.LENGTH_SHORT).show();
                editor.putString(authData.getUid(), "myUID");
                editor.commit();
                Intent intent = new Intent(LoginScreen.this, MyProfile.class);
                intent.putExtra("uid", authData.getUid());
                //
                editor.putString(authData.getUid(), "myUID");
                editor.commit();

                //
                startActivity(intent);;
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Toast.makeText(LoginScreen.this, "Try again?", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void register(View v) {
        EditText mUsername = (EditText) findViewById(R.id.usernameInput);
        EditText mPassword = (EditText) findViewById(R.id.passwordInput);

        mRef.createUser(mUsername.getText().toString(), mPassword.getText().toString(),
                new Firebase.ValueResultHandler<Map<String, Object>>() {

                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Toast.makeText(LoginScreen.this, "You're in! ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginScreen.this, ProfileSetup.class);
                        intent.putExtra("uid", result.get("uid").toString());
                        //
                        editor.putString(result.get("uid").toString(), "myUID");
                        editor.commit();

                        //
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(LoginScreen.this, "Try again!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
