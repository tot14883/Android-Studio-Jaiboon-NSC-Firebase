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
    private DatabaseReference mLocal;
    private DatabaseReference mDataDeposit;
    private String typeSend,owner,phone,localname,localaddress,localpost,localcountry;
    private String post_name,address,post,country,phone_customer,Name_Leader,Type_foun,selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_admin);
        typeSend = getIntent().getExtras().getString("TypeSend");
        owner = getIntent().getExtras().getString("Name");
        phone = getIntent().getExtras().getString("Phone");
        localname = getIntent().getExtras().getString("Localname");
        localaddress = getIntent().getExtras().getString("LocalAddress");
        localpost = getIntent().getExtras().getString("LocalPost");
        localcountry = getIntent().getExtras().getString("LocalCountry");



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

        }

       // TextView textView = (TextView) findViewById(R.id.address_deposit);
        //textView.setText("295 หมู่ 7 บ.หนองเดิ่น ต.หนองกอมเกาะ อ.เมือง จ.หนองคาย"+"\n"+"หนองคาย/Nong Khai - เมืองหนองคาย/Mueang Nong Khai - 43000"+"\n"+"Tel.0828662279");

        mDataDeposit =FirebaseDatabase.getInstance().getReference().child("Deposit");//Dataตัวใหม่่
        mData = FirebaseDatabase.getInstance().getReference().child("SendDonate");
        mUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mLocal = FirebaseDatabase.getInstance().getReference().child("Cart");
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    MyProfile();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    public void Next_submit(View view) {
        Intent intent = new Intent(AddressAdmin.this,ChoosePayment.class);
        startActivity(intent);
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
    public void ShowLocal(){
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                  if(snapshot.child("uid").getValue().equals(mAuth.getCurrentUser().getUid()) && snapshot.child(""))
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
