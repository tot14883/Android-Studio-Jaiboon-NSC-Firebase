package test.jaiboondemand.must_product;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import test.jaiboondemand.R;

public class NeedDonateSend extends AppCompatActivity {
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView total_need,text_name,text_address;
    private RecyclerView recyclerView;
    private String post_key;
    private DatabaseReference mDataAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_donate_send);
        post_key = getIntent().getExtras().getString("post_key");
        recyclerView = (RecyclerView) findViewById(R.id.recycle_need_product);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        total_need = (TextView) findViewById(R.id.total_price_need);
        text_name = (TextView) findViewById(R.id.text_name_need);
        text_address = (TextView) findViewById(R.id.text_address_need);

        mData = FirebaseDatabase.getInstance().getReference().child("Jaiboon");

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   String name = (String) dataSnapshot.child("Name").getValue();
                   String address = (String) dataSnapshot.child("address").getValue();
                   String post = (String)dataSnapshot.child("post").getValue();
                   String country = (String)dataSnapshot.child("country").getValue();

                   text_name.setText(name);
                   text_address.setText(address+"\n"+post+"\n"+country);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        total_need.setText("0");
        final int[] price = {0};
        FirebaseRecyclerAdapter<NeedDonate,NeedDonateSendViewHolder> adapter = new FirebaseRecyclerAdapter<NeedDonate, NeedDonateSendViewHolder>(
                NeedDonate.class,
                R.layout.row_donate_need,
                NeedDonateSendViewHolder.class,
                mData.child("DonateProduct")
        ) {
            @Override
            protected void populateViewHolder(NeedDonateSendViewHolder viewHolder, NeedDonate model, int position) {
                viewHolder.setNameNeed(model.getNamedonate());
                viewHolder.setPriceNeed(model.getPricedonate());
                price[0] = (price[0] + Integer.valueOf(model.getPricedonate())*Integer.valueOf(model.getAmountdonate()));
                viewHolder.setImageNeed(getApplicationContext(), model.getImagedonate());
                total_need.setText(String.valueOf(price[0]));
                viewHolder.setAmount(model.getAmountdonate());
            }
        };
        recyclerView.setAdapter(adapter);

        mAuth.addAuthStateListener(mAuthListener);
    }//row_donate_need
    public static class NeedDonateSendViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public NeedDonateSendViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setNameNeed(String name){
            TextView name_need = (TextView) mView.findViewById(R.id.product_name_need);
            name_need.setText(name);
        }
        public void setPriceNeed(String price){
            TextView price_need = (TextView) mView.findViewById(R.id.product_price_need);
            price_need.setText(price);
        }
        public void setImageNeed(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.product_photo_need);
            Picasso.with(ctx).load(image).into(post_image);
        }
        public void setAmount(String amount){
            TextView amount_need = (TextView) mView.findViewById(R.id.amount_need_donate);
            amount_need.setText("จำนวนที่ต้องการ "+"\n"+amount);
        }
    }

    public void btnneed(View view) {
        Intent intent = new Intent(NeedDonateSend.this,NeedChoosePay.class);
        intent.putExtra("post_key",post_key);
        startActivity(intent);
    }
}
