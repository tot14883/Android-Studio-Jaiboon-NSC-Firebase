package test.jaiboondemand.shop_type;

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

public class Shopdetail extends AppCompatActivity {
    private String post_key = null;
    private DatabaseReference mDataUsers;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatapush;
    private ImageView ProductImage;
    private TextView productName,productPrice,productDesc;
    private Button addProduct;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String pro_name,pro_desc,pro_price,pro_image,pro_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        post_key = getIntent().getExtras().getString("PostID");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ShopJaiboon");
        mDatabase.keepSynced(true);

        mDataUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        ProductImage =(ImageView) findViewById(R.id.show_product);
        productName = (TextView) findViewById(R.id.text_name_product);
        productPrice = (TextView) findViewById(R.id.text_price_product);
        productDesc = (TextView) findViewById(R.id.text_des_product);
        addProduct = (Button) findViewById(R.id.button_add_cart);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatapush = FirebaseDatabase.getInstance().getReference().child("Cart");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    addProduct.setVisibility(View.INVISIBLE);
                }
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

                Picasso.with(Shopdetail.this).load(pro_image).networkPolicy(NetworkPolicy.OFFLINE).into(ProductImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(Shopdetail.this).load(pro_image).into(ProductImage);
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

    public void AddcartClicked(View view) {
        final String user_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference newPost = mDatapush.push();
          mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                      newPost.child("nameproduct").setValue(pro_name);
                      newPost.child("descproduct").setValue(pro_desc);
                      newPost.child("priceproduct").setValue(pro_price);
                      newPost.child("imageproduct").setValue(pro_image);
                      newPost.child("keyproduct").setValue(post_key);
                      newPost.child("amount").setValue("1");
                      newPost.child("uid").setValue(user_id);
                      Toast.makeText(Shopdetail.this,"Add into Cart",Toast.LENGTH_LONG).show();
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
    }
}
