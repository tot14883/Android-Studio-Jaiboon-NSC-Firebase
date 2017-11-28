package test.jaiboondemand;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileTemple extends AppCompatActivity {
    private EditText name_temple, name_Leader, address_temple, post_temple, country_temple, phone_temple;
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

        Exists = getIntent().getExtras().getString("Current");

        databaseReference = database.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              if(Exists.equals("Yes")){
                  btn_submit.setVisibility(View.INVISIBLE);
                  btn_update.setVisibility(View.VISIBLE);
                  databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                          String post_name = (String)dataSnapshot.child("Name_temple").getValue();
                          String Name_Leader = (String)dataSnapshot.child("Name_Leader").getValue();
                          String address = (String)dataSnapshot.child("Address_temple").getValue();
                          String post = (String)dataSnapshot.child("Post_temple").getValue();
                          String country = (String)dataSnapshot.child("Country_temple").getValue();
                          String Phone = (String)dataSnapshot.child("Phone_temple").getValue();
                          name_temple.setText(post_name);
                          name_Leader.setText(Name_Leader);
                          address_temple.setText(address);
                          post_temple.setText(post);
                          country_temple.setText(country);
                          phone_temple.setText(Phone);
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
        if (!TextUtils.isEmpty(Name_Temple) && !TextUtils.isEmpty(Name_Leader) && !TextUtils.isEmpty(Address_Temple) &&
                !TextUtils.isEmpty(Post_Temple) && !TextUtils.isEmpty(Country_Temple) && !TextUtils.isEmpty(Phone_Temple)) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    databaseReference.child(user_id).child("Name_temple").setValue(Name_Temple);
                    databaseReference.child(user_id).child("Name_Leader").setValue(Name_Leader);
                    databaseReference.child(user_id).child("Address_temple").setValue(Address_Temple);
                    databaseReference.child(user_id).child("Post_temple").setValue(Post_Temple);
                    databaseReference.child(user_id).child("Country_temple").setValue(Country_Temple);
                    databaseReference.child(user_id).child("Phone_temple").setValue(Phone_Temple);
                    databaseReference.child(user_id).child("Selected").setValue("Temple");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        Intent intent = new Intent(ProfileTemple.this,Main2Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void ButtonClickeUpdated(View view) {
    }
}
