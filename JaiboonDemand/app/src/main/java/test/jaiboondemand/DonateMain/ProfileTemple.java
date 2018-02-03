package test.jaiboondemand.DonateMain;

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

import test.jaiboondemand.R;

public class ProfileTemple extends AppCompatActivity {
    private EditText name_temple, name_Leader, address_temple, post_temple, country_temple,phone_temple,editFacebook;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String Exists;
    private Button btn_submit,btn_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_temple);
        name_temple = (EditText) findViewById(R.id.input_Name_Temple);
        name_Leader = (EditText) findViewById(R.id.input_Name_Leader);
        address_temple = (EditText) findViewById(R.id.input_Address);
        post_temple = (EditText) findViewById(R.id.input_postcode);
        country_temple = (EditText) findViewById(R.id.input_Country);
        phone_temple = (EditText) findViewById(R.id.input_phone);
        btn_submit = (Button)findViewById(R.id.submit_profile);
        btn_update = (Button)findViewById(R.id.submit_update);
        editFacebook = (EditText) findViewById(R.id.Edit_Facebook_link);


        Exists = getIntent().getExtras().getString("Current");

        databaseReference = database.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        final String user_id = mAuth.getCurrentUser().getUid();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              if(Exists.equals("Yes")){
                  btn_submit.setVisibility(View.INVISIBLE);
                  btn_update.setVisibility(View.VISIBLE);
                  databaseReference.child(user_id).addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                          String post_name = (String)dataSnapshot.child("Name_User").getValue();
                          String Name_Leader = (String)dataSnapshot.child("Name_Owner").getValue();
                          String address = (String)dataSnapshot.child("Address").getValue();
                          String post = (String)dataSnapshot.child("Post").getValue();
                          String country = (String)dataSnapshot.child("Country").getValue();
                          String Phone = (String)dataSnapshot.child("Phone").getValue();
                          String face = (String) dataSnapshot.child("facebooklink").getValue();
                          name_temple.setText(post_name);
                          name_Leader.setText(Name_Leader);
                          address_temple.setText(address);
                          post_temple.setText(post);
                          country_temple.setText(country);
                          phone_temple.setText(Phone);
                          editFacebook.setText(face);
                      }

                      @Override
                      public void onCancelled(DatabaseError databaseError) {

                      }
                  });
              }
              else{
                  btn_submit.setVisibility(View.VISIBLE);
                  btn_update.setVisibility(View.INVISIBLE);
              }
            }
        };
    }

    public void ButtonClickeCreated(View view) {
        final String user_id = mAuth.getCurrentUser().getUid();
        final String Name_Temple = name_temple.getText().toString().trim();
        final String Name_Leader = name_Leader.getText().toString().trim();
        final String Address_Temple = address_temple.getText().toString().trim();
        final String Post_Temple = post_temple.getText().toString().trim();
        final String Country_Temple = country_temple.getText().toString().trim();
        final String Phone_Temple = phone_temple.getText().toString().trim();
        final String facebook_page = editFacebook.getText().toString().trim();
        if (!TextUtils.isEmpty(Name_Temple) && !TextUtils.isEmpty(Name_Leader) && !TextUtils.isEmpty(Address_Temple) &&
                !TextUtils.isEmpty(Post_Temple) && !TextUtils.isEmpty(Country_Temple) && !TextUtils.isEmpty(Phone_Temple)) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    databaseReference.child(user_id).child("Name").setValue(Name_Temple);
                    databaseReference.child(user_id).child("Name_User").setValue(Name_Temple);
                    databaseReference.child(user_id).child("Name_Owner").setValue(Name_Leader);
                    databaseReference.child(user_id).child("Address").setValue(Address_Temple);
                    databaseReference.child(user_id).child("Post").setValue(Post_Temple);
                    databaseReference.child(user_id).child("Country").setValue(Country_Temple);
                    databaseReference.child(user_id).child("Phone").setValue(Phone_Temple);
                    databaseReference.child(user_id).child("Selected").setValue("Temple");
                    if(TextUtils.isEmpty(facebook_page)) {
                        databaseReference.child(user_id).child("facebooklink").setValue("-");
                    }
                    else if(!TextUtils.isEmpty(facebook_page)){
                        databaseReference.child(user_id).child("facebooklink").setValue(facebook_page);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(ProfileTemple.this,Main2Activity.class);
        finish();
        startActivity(intent);
    }

    public void ButtonClickeUpdated(View view) {
        final String user_id = mAuth.getCurrentUser().getUid();
        final String Name_Temple = name_temple.getText().toString().trim();
        final String Name_Leader = name_Leader.getText().toString().trim();
        final String Address_Temple = address_temple.getText().toString().trim();
        final String Post_Temple = post_temple.getText().toString().trim();
        final String Country_Temple = country_temple.getText().toString().trim();
        final String Phone_Temple = phone_temple.getText().toString().trim();
        final String facebook_page = editFacebook.getText().toString().trim();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child(user_id).child("Name_User").setValue(Name_Temple);
                dataSnapshot.getRef().child(user_id).child("Name_Owner").setValue(Name_Leader);
                dataSnapshot.getRef().child(user_id).child("Address").setValue(Address_Temple);
                dataSnapshot.getRef().child(user_id).child("Post").setValue(Post_Temple);
                dataSnapshot.getRef().child(user_id).child("Country").setValue(Country_Temple);
                dataSnapshot.getRef().child(user_id).child("Phone").setValue(Phone_Temple);
                dataSnapshot.getRef().child(user_id).child("Selected").setValue("Temple");
                if(TextUtils.isEmpty(facebook_page)) {
                    dataSnapshot.getRef().child(user_id).child("facebooklink").setValue("-");
                }
                else if(!TextUtils.isEmpty(facebook_page)){
                    dataSnapshot.getRef().child(user_id).child("facebooklink").setValue(facebook_page);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        onRestart();
    }
}
