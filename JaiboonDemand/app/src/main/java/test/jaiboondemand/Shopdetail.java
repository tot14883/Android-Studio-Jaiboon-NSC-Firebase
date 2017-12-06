package test.jaiboondemand;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Shopdetail extends AppCompatActivity {
    private String post_key = null;
    private DatabaseReference mDatabase;
    private ImageView ProductImage;
    private TextView productName,productPrice,productDesc;
    private Button addProduct;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        String post_key = getIntent().getExtras().getString("PostID");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ShopJaiboon");

        ProductImage =(ImageView) findViewById(R.id.show_product);
        productName = (TextView) findViewById(R.id.text_name_product);
        productPrice = (TextView) findViewById(R.id.text_price_product);
        productDesc = (TextView) findViewById(R.id.text_des_product);
        addProduct = (Button) findViewById(R.id.button_add_cart);

        mAuth = FirebaseAuth.getInstance();

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pro_name = (String) dataSnapshot.child("nameproduct").getValue();
                String pro_desc = (String) dataSnapshot.child("desproduct").getValue();
                String pro_price = (String) dataSnapshot.child("priceproduct").getValue();
                String pro_image = (String) dataSnapshot.child("imageproduct").getValue();

                productName.setText(pro_name);
                productPrice.setText(pro_price);
                productDesc.setText(pro_desc);

                Picasso.with(Shopdetail.this).load(pro_image).into(ProductImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
