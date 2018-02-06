package test.jaiboondemand.Pay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import test.jaiboondemand.Deposit.AddressPlaceDeposit;
import test.jaiboondemand.R;

public class PlaceSend extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private EditText edt_name,edt_address,edt_post,edt_phone;
    private Spinner spinner_province;
    private String pay_send = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_send);
        edt_name = (EditText) findViewById(R.id.name_send);
        edt_address = (EditText) findViewById(R.id.address_send);
        edt_post = (EditText) findViewById(R.id.post_send);
        edt_phone = (EditText) findViewById(R.id.phone_send);

        pay_send = getIntent().getExtras().getString("Typesend");

        spinner_province = (Spinner) findViewById(R.id.province_send);
        ArrayAdapter mAdapter = new ArrayAdapter<String>(PlaceSend.this,
                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.Province));
        spinner_province.setAdapter(mAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("SendDonate");
        mAuth = FirebaseAuth.getInstance();
        mAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistener);
    }

    public void btnFillinfo(View view) {
        final String namesend = edt_name.getText().toString().trim();
        final String addresssend = edt_address.getText().toString().trim();
        final String postsend = edt_post.getText().toString().trim();
        final String uid = mAuth.getCurrentUser().getUid();
        final String phone = edt_phone.getText().toString().trim();
        final String provincesend = spinner_province.getSelectedItem().toString();
        if(!TextUtils.isEmpty(namesend) && !TextUtils.isEmpty(addresssend) && !TextUtils.isEmpty(postsend)) {
            final DatabaseReference newPlace = mDatabase.push();
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPlace.child("NameSend").setValue(namesend);
                    newPlace.child("AddSend").setValue(addresssend);
                    newPlace.child("PostSend").setValue(postsend);
                    newPlace.child("uid").setValue(uid);
                    newPlace.child("ProSend").setValue(provincesend);
                    newPlace.child("PhoneSend").setValue(phone);
                    if(pay_send.equals("Paymoney")) {
                        Intent intent = new Intent(PlaceSend.this, PayMoney.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(pay_send.equals("Deposit")){
                        Intent intent = new Intent(PlaceSend.this, AddressPlaceDeposit.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
