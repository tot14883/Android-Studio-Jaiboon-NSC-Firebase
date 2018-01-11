package test.jaiboondemand.Pay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import test.jaiboondemand.Main2Activity;
import test.jaiboondemand.Nearby.PlaceModel;
import test.jaiboondemand.ProfileCustomer;
import test.jaiboondemand.ProfileFoundation;
import test.jaiboondemand.ProfileTemple;
import test.jaiboondemand.R;

public class PayMoney extends AppCompatActivity {
    private TextView  text_new_place;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_money);
        text_new_place = (TextView) findViewById(R.id.text_new_place);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_place_send);
        recyclerView.setLayoutManager(new LinearLayoutManager(PayMoney.this));
        recyclerView.hasFixedSize();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("SendDonate");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                }
                else if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(PayMoney.this,"Not login?",Toast.LENGTH_LONG).show();
                }
            }
        };

        text_new_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent intent = new Intent(PayMoney.this, PlaceSend.class);
                  startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<SendPlace,PlaceViewHolder> adapter = new FirebaseRecyclerAdapter<SendPlace, PlaceViewHolder>(
                SendPlace.class,
                R.layout.row_send_place,
                PlaceViewHolder.class,
                mDatabase.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid())
        ) {
            @Override
            protected void populateViewHolder(final PlaceViewHolder viewHolder, SendPlace model, final int position) {
                final DatabaseReference select =this.getRef(position);
                select.child("select").setValue(false);
                final int[] clicked = {0};
                clicked[0] = 0;
                viewHolder.setNamePlace(model.getNameSend());
                viewHolder.setAddPlace(model.getAddSend(),model.getPostSend(),model.getProSend());

                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked) {
                            select.child("select").setValue(true);
                        }
                        else if(!isChecked) {
                            select.child("select").setValue(false);
                        }

                    }

                });
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
       Intent intent = new Intent(PayMoney.this,PayChoose.class);
       startActivity(intent);
    }

    public void Restart(){
        Intent intent = getIntent();
        startActivity(intent);
    }
}
