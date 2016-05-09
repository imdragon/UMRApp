package org.imdragon.umrapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class ProfileSetup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Firebase mRef;
    Firebase usersRef;
    String pName, pAbout, pLocation;
    private String userID;
    private String locationString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        mRef.setAndroidContext(this);
        mRef = new Firebase("https://umr-theapp.firebaseio.com");
        userID = getIntent().getExtras().get("uid").toString();
        usersRef = mRef.child("users/"+userID);
        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.locations, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p/>
     * Impelmenters can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        locationString = parent.getItemAtPosition(position).toString();

        //use the string above for location info
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getProfileInfo() {
        pName = ((EditText) findViewById(R.id.profileName)).getEditableText().toString();
        pAbout = ((EditText) findViewById(R.id.profileAbout)).getEditableText().toString();


    }

    public void saveProfile(View v) {
        getProfileInfo();
        Map<String, String> profileInfo = new HashMap<String, String>();
        profileInfo.put("name", pName);
        profileInfo.put("about", pAbout);
        profileInfo.put("location", locationString);

        usersRef.setValue(profileInfo);
        Intent intent = new Intent(this, MyProfile.class);
        intent.putExtra("uid", userID);
startActivity(intent);    }
}
