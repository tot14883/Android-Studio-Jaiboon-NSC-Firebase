package test.jaiboondemand.Deposit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import test.jaiboondemand.R;

public class AddressAdmin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference mData;
    private DatabaseReference mUser;
    private TextView yourname,youraddress,yourphone;
    private TextView sendname,sendaddress,sendphone;
    private DatabaseReference mDataDeposit;
    private String typeSend,owner,phone,localname,localaddress,localpost,localcountry;
    private String post_name,address,post,country,phone_customer,Name_Leader,Type_foun,selected;
    private String post_Name,Send_address,Send_post,Send_country,Send_phone;
    private String post_key = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_admin);
        post_key = getIntent().getExtras().getString("post_key");
        mDataDeposit = FirebaseDatabase.getInstance().getReference().child("Deposit").child(post_key);//Dataตัวใหม่่
        mData = FirebaseDatabase.getInstance().getReference().child("SendDonate");
        mUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    MyProfile();
                }
            }
        };
        typeSend = getIntent().getExtras().getString("TypeSend");
        if(typeSend.equals("Quick")) {
            owner = getIntent().getExtras().getString("Name");
            phone = getIntent().getExtras().getString("Phone");
            localname = getIntent().getExtras().getString("Localname");
            localaddress = getIntent().getExtras().getString("LocalAddress");
            localpost = getIntent().getExtras().getString("LocalPost");
            localcountry = getIntent().getExtras().getString("LocalCountry");
        }


        yourname = (TextView)  findViewById(R.id.you_send_deposit);
        youraddress = (TextView) findViewById(R.id.you_address_deposit);
        yourphone = (TextView) findViewById(R.id.you_phone_deposit);
        sendname = (TextView) findViewById(R.id.name_send_deposit);
        sendaddress = (TextView) findViewById(R.id.address_send_deposit);
        sendphone = (TextView) findViewById(R.id.phone_send_deposit);
        if(typeSend.equals("Quick")) {
            sendname.setText(owner);
            sendaddress.setText(localname + "\n" + "ที่อยู่ " + localaddress + " รหัสไปรษญีย์ " + localpost + "\n" + "จังหวัด" + localcountry);
            sendphone.setText(phone);
        }
        else if(typeSend.equals("defualt")){
            final String[] key = new String[1];
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("uid").getValue().equals(mAuth.getCurrentUser().getUid()) && snapshot.child("select").getValue().equals(true)) {
                            key[0] = snapshot.getKey();
                            post_Name = (String) snapshot.child("NameSend").getValue();
                            Send_address = (String) snapshot.child("AddSend").getValue();
                            Send_post = (String) snapshot.child("PostSend").getValue();
                            Send_country = (String) snapshot.child("ProSend").getValue();
                            Send_phone = (String) snapshot.child("PhoneSend").getValue();
                            sendname.setText(post_Name);
                            sendaddress.setText(" ที่อยู่ " + Send_address + " รหัสไปรษญีย์ " + Send_post + "\n" + "จังหวัด" + Send_country);
                            sendphone.setText(Send_phone);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            setInfo();
        }

       // TextView textView = (TextView) findViewById(R.id.address_deposit);
        //textView.setText("295 หมู่ 7 บ.หนองเดิ่น ต.หนองกอมเกาะ อ.เมือง จ.หนองคาย"+"\n"+"หนองคาย/Nong Khai - เมืองหนองคาย/Mueang Nong Khai - 43000"+"\n"+"Tel.0828662279");


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }
    public void Next_submit(View view) {
        if(typeSend.equals("Quick")) {
            Intent intent = new Intent(AddressAdmin.this, ChoosePayment.class);
            intent.putExtra("Name",owner);
            intent.putExtra("Phone",phone);
            intent.putExtra("Localname",localname);
            intent.putExtra("LocalAddress",localaddress);
            intent.putExtra("LocalPost",localpost);
            intent.putExtra("LocalCountry",localcountry);
            intent.putExtra("yourName",post_name);
            intent.putExtra("youraddress",address);
            intent.putExtra("yourpost",post);
            intent.putExtra("yourcountry",country);
            intent.putExtra("yourphone",phone_customer);
            intent.putExtra("Typesend","Quick");
            startActivity(intent);
        }
        else if(typeSend.equals("defualt")){
            Intent intent = new Intent(AddressAdmin.this,ChoosePayment.class);
            intent.putExtra("Name",post_Name);
            intent.putExtra("Phone",Send_phone);
            intent.putExtra("LocalAddress",Send_address);
            intent.putExtra("LocalPost",Send_post);
            intent.putExtra("LocalCountry",Send_country);
            intent.putExtra("yourName",post_name);
            intent.putExtra("youraddress",address);
            intent.putExtra("yourpost",post);
            intent.putExtra("yourcountry",country);
            intent.putExtra("yourphone",phone_customer);
            intent.putExtra("Typesend","defualt");
            startActivity(intent);
        }
    }
    public void MyProfile(){
        mUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Selected").exists()) {
                    if(dataSnapshot.child("Selected").getValue().equals("Customer")){
                        post_name = (String)dataSnapshot.child("Name_Cus").getValue();
                        address = (String)dataSnapshot.child("Address_Cus").getValue();
                        post = (String)dataSnapshot.child("Post_Cus").getValue();
                        country = (String)dataSnapshot.child("Country_Cus").getValue();
                        phone_customer = (String)dataSnapshot.child("Phone_Cus").getValue();
                        selected = (String)dataSnapshot.child("Selected").getValue();
                    }
                    if(dataSnapshot.child("Selected").getValue().equals("Temple")){
                        post_name = (String)dataSnapshot.child("Name_Owner").getValue();
                        address = (String)dataSnapshot.child("Address").getValue();
                        post = (String)dataSnapshot.child("Post").getValue();
                        country = (String)dataSnapshot.child("Country").getValue();
                        phone_customer = (String)dataSnapshot.child("Phone").getValue();
                        selected = (String)dataSnapshot.child("Selected").getValue();
                    }
                    if(dataSnapshot.child("Selected").getValue().equals("Foundation")){
                        post_name = (String)dataSnapshot.child("Name_Owner").getValue();
                        Type_foun = (String) dataSnapshot.child("Type").getValue();
                        address = (String)dataSnapshot.child("Address").getValue();
                        post = (String)dataSnapshot.child("Pos").getValue();
                        country = (String)dataSnapshot.child("Country").getValue();
                        phone_customer = (String)dataSnapshot.child("Phone").getValue();
                        selected = (String)dataSnapshot.child("Selected").getValue();
                    }
                    yourname.setText(post_name);
                    youraddress.setText("ที่อยู่ "+address+" รหัสไปรษณีย์ "+post+"\n"+"จังหวัด "+country);
                    yourphone.setText("เบอร์โทร "+phone_customer);


                } else if (!dataSnapshot.child("Selected").exists()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setInfo(){

        mDataDeposit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataDeposit.child("Name_Deposit").setValue(post_Name);
                mDataDeposit.child("Phone_Deposit").setValue(Send_phone);
                mDataDeposit.child("AddreeLocal_Deposit").setValue(Send_address);
                mDataDeposit.child("PostLocal_Deposit").setValue(Send_post);
                mDataDeposit.child("CountryLocal_Deposit").setValue(Send_country);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
