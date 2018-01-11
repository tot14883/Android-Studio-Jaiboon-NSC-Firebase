package test.jaiboondemand.must_product;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import test.jaiboondemand.R;
import test.jaiboondemand.post_activity.ChooseDonate;

public class NeedProduct extends AppCompatActivity {
    private RecyclerView mRcart;
    private TextView pricedonate;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseshow;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String post_key;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_product);
        post_key = getIntent().getExtras().getString("PostID");

        mRcart = (RecyclerView) findViewById(R.id.recycle_need_donate);
        mRcart.setHasFixedSize(true);
        mRcart.setLayoutManager(new LinearLayoutManager(this));

        pricedonate = (TextView) findViewById(R.id.total_price_donate) ;

        mDatabase  = FirebaseDatabase.getInstance().getReference().child("Jaiboon").child(post_key);
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
        pricedonate.setText("0");
        final int[] price = {0};
        FirebaseRecyclerAdapter<NeedDonate,recycleDonate> FBRA = new FirebaseRecyclerAdapter<NeedDonate, recycleDonate>(
                NeedDonate.class,
                R.layout.card_need_donate,
                recycleDonate.class,
                mDatabase.child("DonateProduct")//ตรงนี้
        ) {
            @Override
            protected void populateViewHolder(recycleDonate viewHolder, NeedDonate model, int position) {

                viewHolder.setTitle(model.getNamedonate());
                viewHolder.setPrice(model.getPricedonate());
                price[0] = (price[0] + Integer.valueOf(model.getPricedonate())*Integer.valueOf(model.getAmountdonate()));
                viewHolder.setImage(getApplicationContext(), model.getImagedonate());
                pricedonate.setText(String.valueOf(price[0]));
                viewHolder.setAmount(model.getAmountdonate());
            }
        };
        mRcart.setAdapter(FBRA);
    }

    public static class recycleDonate extends RecyclerView.ViewHolder{
        View mView;
        public recycleDonate(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView Title = (TextView) mView.findViewById(R.id.Name_donate_need);
            Title.setText(title);
        }
        public void setPrice(String price){
            TextView Price = (TextView) mView.findViewById(R.id.Price_Donate_need);
            Price.setText("ราคา "+price+" บาท");
        }
        public void setImage(Context ctx, String image){
            ImageView img_product = (ImageView) mView.findViewById(R.id.Product_donate_need);
            Picasso.with(ctx).load(image).into(img_product);
        }
        public void setAmount(String amount){
            TextView amount_need = (TextView) mView.findViewById(R.id.text_need_amount);
            amount_need.setText(amount);
        }
    }
    public void Donate_Click(View view) {
        Intent intent = new Intent(NeedProduct.this,NeedDonateSend.class);
        intent.putExtra("post_key", post_key);
        startActivity(intent);
    }
}
