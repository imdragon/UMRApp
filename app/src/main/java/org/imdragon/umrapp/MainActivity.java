package org.imdragon.umrapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {
    Firebase myRef;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        myRef = new Firebase("https://umr-theapp.firebaseio.com");


    }

    public void sayHello(View v) {

        myRef.child("RandomUser" + i).setValue("Hello this is a test");
        i++;
    }
}
