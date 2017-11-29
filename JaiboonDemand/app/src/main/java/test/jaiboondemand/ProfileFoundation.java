package test.jaiboondemand;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ProfileFoundation extends AppCompatActivity {
    private EditText name_foun,name_owner,address_foun,post_foun,country_foun,phone_foun;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private String Exists;
    private Button button_submit,butto_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_foundation);
        name_foun = (EditText) findViewById(R.id.input_Name_foun);
        name_owner = (EditText) findViewById(R.id.input_Name_ower);
        address_foun = (EditText) findViewById(R.id.input_Address_foun);
        post_foun = (EditText) findViewById(R.id.input_postcode_foun);
        country_foun = (EditText) findViewById(R.id.input_Country_foun);
        phone_foun =(EditText) findViewById(R.id.input_phone_foun);
        button_submit = (Button) findViewById(R.id.submit_profile_foun);
        butto_update = (Button)findViewById(R.id.submit_update_foun);

        Exists = getIntent().getExtras().getString("Current");


        databaseReference = database.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
             if(Exists.equals("Yes")){
                 button_submit.setVisibility(View.INVISIBLE);
                 butto_update.setVisibility(View.VISIBLE);
                 databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         String post_name = (String)dataSnapshot.child("Name_foun").getValue();
                         String post_owner = (String)dataSnapshot.child("Name_Owner").getValue();
                         String address = (String)dataSnapshot.child("Address_foun").getValue();
                         String post = (String)dataSnapshot.child("Post_foun").getValue();
                         String country = (String)dataSnapshot.child("Country_foun").getValue();
                         String Phone = (String)dataSnapshot.child("Phone_foun").getValue();
                         name_foun.setText(post_name);
                         name_owner.setText(post_owner);
                         address_foun.setText(address);
                         post_foun.setText(post);
                         country_foun.setText(country);
                         phone_foun.setText(Phone);
                     }
                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
             }
             else{

                 button_submit.setVisibility(View.VISIBLE);
                 butto_update.setVisibility(View.INVISIBLE);
             }
            }
        };

    }

    public void ButtonClickeCreated(View view) {
        final String user_id = mAuth.getCurrentUser().getUid();
        final String Name_foun = name_foun.getText().toString().trim();
        final String Name_Owner = name_owner.getText().toString().trim();
        final String Address_foun = address_foun.getText().toString().trim();
        final String Post_foun = post_foun.getText().toString().trim();
        final String Country_foun = country_foun.getText().toString().trim();
        final String Phone_foun = phone_foun.getText().toString().trim();
        if(!TextUtils.isEmpty(Name_foun) && !TextUtils.isEmpty(Name_Owner) && !TextUtils.isEmpty(Address_foun) &&
            !TextUtils.isEmpty(Post_foun) && !TextUtils.isEmpty(Country_foun) && !TextUtils.isEmpty(Phone_foun)) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    databaseReference.child(user_id).child("Name_foun").setValue(Name_foun);
                    databaseReference.child(user_id).child("Name_Owner").setValue(Name_Owner);
                    databaseReference.child(user_id).child("Address_foun").setValue(Address_foun);
                    databaseReference.child(user_id).child("Post_foun").setValue(Post_foun);
                    databaseReference.child(user_id).child("Country_foun").setValue(Country_foun);
                    databaseReference.child(user_id).child("Phone_foun").setValue(Phone_foun);
                    databaseReference.child(user_id).child("Selected").setValue("Foundation");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
            onRestart();
        }


    public void ButtonClickeUpdated(View view) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(ProfileFoundation.this,Main2Activity.class);
        finish();
        startActivity(intent);
    }

}
