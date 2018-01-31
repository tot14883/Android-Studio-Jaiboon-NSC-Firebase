package test.jaiboondemand.Deposit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import test.jaiboondemand.Pay.PlaceSend;
import test.jaiboondemand.Pay.SendPlace;
import test.jaiboondemand.R;

public class AddressPlaceDeposit extends AppCompatActivity {
    private TextView text_new_place;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String Key_ok = null;
    private int Num = 0,NumAll = 0;
    private String post_key = null;
    private DatabaseReference mDataDeposit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_place_deposit);
         post_key = getIntent().getExtras().getString("post_key");


         text_new_place = (TextView) findViewById(R.id.text_new_place_deposit);
         recyclerView = (RecyclerView) findViewById(R.id.recycle_place_send_deposit);
         recyclerView.setLayoutManager(new LinearLayoutManager(AddressPlaceDeposit.this));
         recyclerView.hasFixedSize();
         try{
             Key_ok = getIntent().getExtras().getString("key");
             Num = getIntent().getExtras().getInt("Num");

         }catch (Exception e){
             Key_ok = null;
             Num = 0;
         }
        mDataDeposit = FirebaseDatabase.getInstance().getReference().child("Deposit");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("SendDonate");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                }
                else if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(AddressPlaceDeposit.this,"Not login?",Toast.LENGTH_LONG).show();
                }
            }
        };

        text_new_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressPlaceDeposit.this, PlaceSend.class);
                intent.putExtra("Typesend","Deposit");
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<SendPlace,PlaceViewHolder> adapter = new FirebaseRecyclerAdapter<SendPlace,PlaceViewHolder>(
                SendPlace.class,
                R.layout.row_send_place,
                PlaceViewHolder.class,
                mDatabase.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid())
        ) {
            @Override
            protected void populateViewHolder(final PlaceViewHolder viewHolder, SendPlace model, final int position) {
                final String key = this.getRef(position).getKey();
                final DatabaseReference select = this.getRef(position);
                if (Num == 1) {
                    if (!Key_ok.equals(key)) {
                        select.child("select").setValue(false);
                        viewHolder.checkBox.setChecked(false);
                    }
                    else{
                        viewHolder.checkBox.setChecked(true);
                    }

                }
                viewHolder.setNamePlace(model.getNameSend());
                viewHolder.setAddPlace(model.getAddSend(),model.getPostSend(),model.getProSend());

                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked) {
                            Key_ok = key;
                            select.child("select").setValue(true);
                            Intent intent = new Intent(AddressPlaceDeposit.this,AddressPlaceDeposit.class);
                            intent.putExtra("key",key);
                            intent.putExtra("post_key",post_key);
                            intent.putExtra("Num",1);
                            startActivity(intent);
                            finish();
                        }
                        else if(!isChecked) {
                            select.child("select").setValue(false);
                        }

                    }

                });
                NumAll++;
            }
        };
        recyclerView.setAdapter(adapter);
    }
    public static class PlaceViewHolder extends RecyclerView.ViewHolder{
        View mView;
        RadioButton checkBox;
        public PlaceViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            checkBox = (RadioButton) mView.findViewById(R.id.check_send_place);
        }
        public void setNamePlace(String NamePlace){
            TextView textname = (TextView) mView.findViewById(R.id.name_place);
            textname.setText(NamePlace);
        }
        public void setAddPlace(String Address,String Post,String Province){
            TextView textAdd = (TextView) mView.findViewById(R.id.location_places);
            textAdd.setText(Address+" "+Post+"\n"+Province);
        }
    }

    public void ButtonNext(View view) {
        Intent intent = new Intent(AddressPlaceDeposit.this,AddressAdmin.class);
        intent.putExtra("TypeSend","defualt");
        intent.putExtra("post_key",post_key);
        startActivity(intent);
    }
}
