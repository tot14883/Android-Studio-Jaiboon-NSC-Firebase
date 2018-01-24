package test.jaiboondemand.Deposit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
    private TextView address_send;
    private DatabaseReference mDataDeposit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_admin);
        TextView textView = (TextView) findViewById(R.id.address_admin);
        textView.setText("295 หมู่ 7 บ.หนองเดิ่น ต.หนองกอมเกาะ อ.เมือง จ.หนองคาย"+"\n"+"หนองคาย/Nong Khai - เมืองหนองคาย/Mueang Nong Khai - 43000"+"\n"+"Tel.0828662279");

        address_send = (TextView) findViewById(R.id.address_send_deposit);
        mDataDeposit =FirebaseDatabase.getInstance().getReference().child("Deposit");//Dataตัวใหม่่
        mData = FirebaseDatabase.getInstance().getReference().child("SendDonate");
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    ShowAddress();
                }
            }
        };
    }
    public void ShowAddress(){
        final String[] key = new String[1];
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                 if(snapshot.child("uid").getChildren().equals(mAuth.getUid())&&snapshot.child("select").getValue().equals(true)){
                     key[0] = snapshot.getKey();
                     String post_name = (String) snapshot.child("NameSend").getValue();
                     String address = (String) snapshot.child("AddSend").getValue();
                     String post = (String) snapshot.child("PostSend").getValue();
                     String country = (String) snapshot.child("ProSend").getValue();
                     String Phone = "";//(String)dataSnapshot.child("Phone").getValue();
                     address_send.setText(post_name+"/n"+"เบอร์โทร " + Phone+"\n"+"ที่อยู่ " + address + "\n" + "รหัสไปรษณีย์ " + post + "\n" + "จังหวัด " + country);
                     break;
                 }
             }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }
}
