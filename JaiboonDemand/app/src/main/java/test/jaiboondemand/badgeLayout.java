package test.jaiboondemand;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class badgeLayout extends AppCompatActivity {
    private RecyclerView mRcart;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView cart_delete;
    private TextView total_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_layout);

        mRcart = (RecyclerView) findViewById(R.id.recycle_cart);
        mRcart.setHasFixedSize(true);
        mRcart.setLayoutManager(new LinearLayoutManager(this));

        total_cart = (TextView) findViewById(R.id.total_price);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Cart");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String Total_price = (String)dataSnapshot.child("priceproduct").getValue();
                    int total_price = Integer.parseInt(Total_price);
                    total_cart.setText(String.valueOf(total_price));
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

        FirebaseRecyclerAdapter<Insta,RecycleCart> FBRA = new FirebaseRecyclerAdapter<Insta, RecycleCart>(
                Insta.class,
                R.layout.row_cart,
                RecycleCart.class,
                mDatabase.orderByChild("uid").equalTo(mAuth.getUid())
        ) {
            @Override
            protected void populateViewHolder(RecycleCart viewHolder, Insta model, int position) {
                final String post_key = getRef(position).getKey().toString();
                final int position1 = position;

                viewHolder.setName(model.getNameproduct());
                viewHolder.setprice(model.getPriceproduct());
                viewHolder.setImage(getApplicationContext(),model.getImageproduct());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shop_activity = new Intent(badgeLayout.this,Shopdetail.class);
                        shop_activity.putExtra("PostID", post_key);
                        startActivity(shop_activity);
                    }
                });
                viewHolder.Clicked();
                viewHolder.cart_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(badgeLayout.this);
                        dialog.setTitle("ออกจากการใช้งาน");
                        dialog.setCancelable(true);
                        dialog.setMessage("คุณต้องการลบการทำรายการหรือไม่?");
                        dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getRef(position1).removeValue();
                            }
                        });
                        dialog.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        dialog.show();
                    }
                });
            }
        };
        mRcart.setAdapter(FBRA);

    }
    public static class RecycleCart extends RecyclerView.ViewHolder{
        View mView;
        private ImageView cart_delete;
        public RecycleCart(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void Clicked(){
           cart_delete = (ImageView) mView.findViewById(R.id.delete_cart);
        }
        public void setName(String nameproduct){
            TextView text_name = (TextView) mView.findViewById(R.id.product_name);
            text_name.setText(nameproduct);
        }
        public void setprice(String priceproduct){
            TextView text_price = (TextView) mView.findViewById(R.id.product_price);
            text_price.setText(priceproduct+" บาท");
        }
        public void setImage(Context ctx,String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.product_photo);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
}
