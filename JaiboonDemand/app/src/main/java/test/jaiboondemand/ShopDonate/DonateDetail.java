package test.jaiboondemand.ShopDonate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import test.jaiboondemand.R;
import test.jaiboondemand.Shopdetail;
import test.jaiboondemand.post_activity.ChooseDonate;

public class DonateDetail extends AppCompatActivity {
    private DatabaseReference mData;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private ImageView ProductImage;
    private TextView productName,productPrice,productDesc;
    private Button addProduct;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatedonate;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String pro_name,pro_desc,pro_price,pro_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_detail);
        String post_key = getIntent().getExtras().getString("PostID");

        ProductImage =(ImageView) findViewById(R.id.show_product1);
        productName = (TextView) findViewById(R.id.text_name_product1);
        productPrice = (TextView) findViewById(R.id.text_price_product1);
        productDesc = (TextView) findViewById(R.id.text_des_product1);
        addProduct = (Button) findViewById(R.id.button_add_cart1);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ShopJaiboon");
        mDatabase.keepSynced(true);

        mDatedonate = FirebaseDatabase.getInstance().getReference().child("Jaiboon");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pro_name = (String) dataSnapshot.child("nameproduct").getValue();
                pro_desc = (String) dataSnapshot.child("desproduct").getValue();
                pro_price = (String) dataSnapshot.child("priceproduct").getValue();
                pro_image = (String) dataSnapshot.child("imageproduct").getValue();

                productName.setText(pro_name);
                productPrice.setText(pro_price);
                productDesc.setText(pro_desc);

                Picasso.with(DonateDetail.this).load(pro_image).networkPolicy(NetworkPolicy.OFFLINE).into(ProductImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(DonateDetail.this).load(pro_image).into(ProductImage);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void donateClicked(View view) {
        final String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference newPost = mDatedonate.push();
        mDatedonate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newPost.child("namedonate").setValue(pro_name);
                newPost.child("pricedonate").setValue(pro_price);
                newPost.child("imagedonate").setValue(pro_image);

                Toast.makeText(DonateDetail.this,"Add into Cart",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent intent = new Intent(DonateDetail.this, ChooseDonate.class);
        startActivity(intent);
        finish();
    }
}
