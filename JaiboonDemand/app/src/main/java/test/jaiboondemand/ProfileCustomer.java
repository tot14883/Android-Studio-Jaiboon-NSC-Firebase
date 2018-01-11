package test.jaiboondemand;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ProfileCustomer extends AppCompatActivity {
    private TextView cus_birthday;
    private EditText name_cus,address_cus,post_cus,country_cus,phone_cus;
    private DatePickerDialog.OnDateSetListener  mDateSetListener;
    private String date;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String Exists;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private Button button_submit,button_update;
    private String  post_name,birthday,address,post,country,Phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customer);
        cus_birthday =(TextView) findViewById(R.id.input_birthday_cus);
        name_cus = (EditText) findViewById(R.id.input_Name_Cus);
        address_cus = (EditText) findViewById(R.id.input_Address_cus);
        post_cus = (EditText) findViewById(R.id.input_postcode_cus);
        country_cus = (EditText) findViewById(R.id.input_Country_cus);
        phone_cus = (EditText) findViewById(R.id.input_phone_cus);
        button_submit = (Button) findViewById(R.id.submit_profile_Cus);
        button_update = (Button) findViewById(R.id.submit_update_Cus);


        Exists = getIntent().getExtras().getString("Current");

        mAuth = FirebaseAuth.getInstance();

        databaseReference = database.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);


         mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(Exists.equals("Yes")){
                    button_submit.setVisibility(View.INVISIBLE);
                    button_update.setVisibility(View.VISIBLE);
                    databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            post_name = (String)dataSnapshot.child("Name_Cus").getValue();
                            birthday = (String)dataSnapshot.child("Birthday").getValue();
                            address = (String)dataSnapshot.child("Address_Cus").getValue();
                            post = (String)dataSnapshot.child("Post_Cus").getValue();
                            country = (String)dataSnapshot.child("Country_Cus").getValue();
                            Phone = (String)dataSnapshot.child("Phone_Cus").getValue();
                            name_cus.setText(post_name);
                            cus_birthday.setText(birthday);
                            address_cus.setText(address);
                            post_cus.setText(post);
                            country_cus.setText(country);
                            phone_cus.setText(Phone);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(Exists.equals("No")){
                    button_submit.setVisibility(View.VISIBLE);
                    button_update.setVisibility(View.INVISIBLE);
                }
            }
        };

        cus_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ProfileCustomer.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        Year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
              month = month+1;

              date = day+"/"+month+"/"+year;
              cus_birthday.setText(date);
            }
        };
    }

    public void ButtonClickeCreated(View view) {
            final String user_id = mAuth.getCurrentUser().getUid();
            final String Name_Cus = name_cus.getText().toString().trim();
            final String Birthday = cus_birthday.getText().toString().trim();
            final String Address_Cus = address_cus.getText().toString().trim();
            final String Post_Cus = post_cus.getText().toString().trim();
            final String Country_Cus = country_cus.getText().toString().trim();
            final String Phone_Cus = phone_cus.getText().toString().trim();
            if (!TextUtils.isEmpty(Name_Cus) && !TextUtils.isEmpty(Birthday) && !TextUtils.isEmpty(Address_Cus) &&
                    !TextUtils.isEmpty(Post_Cus) && !TextUtils.isEmpty(Country_Cus) && !TextUtils.isEmpty(Phone_Cus)) {
               databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.child(user_id).child("Name_Cus").setValue(Name_Cus);
                        databaseReference.child(user_id).child("Birthday").setValue(Birthday);
                        databaseReference.child(user_id).child("Address_Cus").setValue(Address_Cus);
                        databaseReference.child(user_id).child("Post_Cus").setValue(Post_Cus);
                        databaseReference.child(user_id).child("Country_Cus").setValue(Country_Cus);
                        databaseReference.child(user_id).child("Phone_Cus").setValue(Phone_Cus);
                        databaseReference.child(user_id).child("Selected").setValue("Customer");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            Intent intent = new Intent(ProfileCustomer.this, Main2Activity.class);
            finish();
            startActivity(intent);
    }

    public void ButtonClickeUpdated(View view) {
        final String user_id = mAuth.getCurrentUser().getUid();
        final String Name_Cus = name_cus.getText().toString().trim();
        final String Birthday = cus_birthday.getText().toString().trim();
        final String Address_Cus = address_cus.getText().toString().trim();
        final String Post_Cus = post_cus.getText().toString().trim();
        final String Country_Cus = country_cus.getText().toString().trim();
        final String Phone_Cus = phone_cus.getText().toString().trim();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   dataSnapshot.getRef().child(user_id).child("Name_Cus").setValue(Name_Cus);
                    dataSnapshot.getRef().child(user_id).child("Birthday").setValue(Birthday);
                    dataSnapshot.getRef().child(user_id).child("Address_Cus").setValue(Address_Cus);
                    dataSnapshot.getRef().child(user_id).child("Post_Cus").setValue(Post_Cus);
                    dataSnapshot.getRef().child(user_id).child("Country_Cus").setValue(Country_Cus);
                    dataSnapshot.getRef().child(user_id).child("Phone_Cus").setValue(Phone_Cus);
                    dataSnapshot.getRef().child(user_id).child("Selected").setValue("Customer");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
        Intent intent = new Intent(ProfileCustomer.this, Main2Activity.class);
        finish();
        startActivity(intent);
    }
}
