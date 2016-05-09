package org.imdragon.umrapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MyProfile extends AppCompatActivity {
    Firebase mRef;
    Firebase eventsRef;
    Firebase mUsers;
    String uid;
    TextView nameLabel, aboutLabel;
    ArrayList<Event> myEvents = new ArrayList<>();


    public static final String PREFS_NAME = "MyPrefsFile";
    private StringBuilder SB = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        mRef.setAndroidContext(this);
        mRef = new Firebase("https://umr-theapp.firebaseio.com");
        mUsers = new Firebase("https://umr-theapp.firebaseio.com/users");
        eventsRef = new Firebase("https://umr-theapp.firebaseio.com/events");
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        uid = getIntent().getExtras().getString("uid", null);
        nameLabel = (TextView) findViewById(R.id.profileName);
        aboutLabel = (TextView) findViewById(R.id.profileAbout);
        retrieveProfile();
    }

    public void retrieveProfile() {
        Log.e("MyKey", uid);
        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    BlogPost post = postSnapshot.getValue(BlogPost.class);
//                    System.out.println(post.getAuthor() + " - " + post.getTitle());
                    String string = userSnapshot.toString();

//                    if (userSnapshot.getKey().contentEquals(uid)) {
                    String uidKey = userSnapshot.getKey();
                    if (uidKey.equals(uid)) {
                        Toast.makeText(MyProfile.this, userSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                        nameLabel.setText(userSnapshot.child("name").getValue().toString());
                        aboutLabel.setText(userSnapshot.child("about").getValue().toString());
                        getMyEvents(userSnapshot.child("location").getValue().toString());
                        break;
                    }
//                    Toast.makeText(MyProfile.this, string, Toast.LENGTH_SHORT).show();
//                    Log.e("output", string);
//                    Log.e("keys", userSnapshot.getKey());
                }
            }

            public void getMyEvents(final String location) {

                eventsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myEvents.clear();
                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            Log.e("ID", eventSnapshot.child("location").getValue().toString());
                            String locale = eventSnapshot.child("location").getValue().toString();
                            if (locale.equals(location)) {
                                Event newEvent = new Event();
                                Log.e("address", eventSnapshot.child("address").getValue().toString());
                                newEvent.setAddress(eventSnapshot.child("address").getValue().toString());
                                newEvent.setDescription(eventSnapshot.child("description").getValue().toString());
                                newEvent.setLocation(eventSnapshot.child("location").getValue().toString());
                                newEvent.setTitle(eventSnapshot.child("title").getValue().toString());
                                myEvents.add(newEvent);
                            }
                        }
                        SB = new StringBuilder();
                        for (int i = 0; i < myEvents.size(); i++) {
                            Event tempEvent = myEvents.get(i);
                            SB.append(tempEvent.getTitle() + "\n");
                            SB.append(tempEvent.getDescription() + "\n");
                            SB.append(tempEvent.getAddress());
                            SB.append("\n-----------------------\n");
                        }
                        TextView eventsOutput = (TextView) findViewById(R.id.eventsListText);

                        eventsOutput.setText(SB.toString());

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
