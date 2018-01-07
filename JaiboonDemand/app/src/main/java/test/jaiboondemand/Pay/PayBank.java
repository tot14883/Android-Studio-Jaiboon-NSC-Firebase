package test.jaiboondemand.Pay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import test.jaiboondemand.R;

public class PayBank extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bank);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_send_bank);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(PayBank.this));

        textView = (TextView) findViewById(R.id.total_price_send_bank);

        mData = FirebaseDatabase.getInstance().getReference().child("Cart");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer[] total_price = {0};
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    if (d.child("uid").getValue().equals(mAuth.getCurrentUser().getUid())) {
                        String Total_price = (String) String.valueOf(d.child("priceproduct").getValue());
                        String amount = (String) String.valueOf(d.child("amount").getValue());
                        total_price[0] = (total_price[0] + Integer.parseInt(Total_price) * Integer.parseInt(amount));
                    }
                }
                textView.setText(String.valueOf(total_price[0]));
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
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<ProductSend,PayBankViewHolder> adapter = new FirebaseRecyclerAdapter<ProductSend, PayBankViewHolder>(
                ProductSend.class,
                R.layout.row_pay_cash,
                PayBankViewHolder.class,
                mData.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid())

        ) {
            @Override
            protected void populateViewHolder(PayBankViewHolder viewHolder, ProductSend model, int position) {
                viewHolder.setName(model.getNameproduct());
                viewHolder.setImage(getApplicationContext(),model.getImageproduct());
                viewHolder.setprice(model.getPriceproduct());
                viewHolder.setAmout(model.getAmount());
            }
        };
        recyclerView.setAdapter(adapter);
    }
    public static class PayBankViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public PayBankViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setAmout(String amount){
            TextView text_amount = (TextView) mView.findViewById(R.id.text_amount);
            text_amount.setText(amount);
        }
        public void setName(String nameproduct){
            TextView text_name = (TextView) mView.findViewById(R.id.product_name_send);
            text_name.setText(nameproduct);
        }
        public void setprice(String priceproduct){
            TextView text_price = (TextView) mView.findViewById(R.id.product_price_send);
            text_price.setText(priceproduct+" บาท");
        }
        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.product_photo_send);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
}
