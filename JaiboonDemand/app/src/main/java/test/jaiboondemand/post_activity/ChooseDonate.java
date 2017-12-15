package test.jaiboondemand.post_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import test.jaiboondemand.DonateSocial;
import test.jaiboondemand.HomeShop;
import test.jaiboondemand.Insta;
import test.jaiboondemand.Main2Activity;
import test.jaiboondemand.PostActivity;
import test.jaiboondemand.R;
import test.jaiboondemand.ShopActivity;
import test.jaiboondemand.ShopDonate.Main3Activity;
import test.jaiboondemand.must_product.NeedDonate;

public class ChooseDonate extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private DatabaseReference mDatadonate;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatapush;
    private RecyclerView recyclerView;
    private Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_donate);
        recyclerView = (RecyclerView) findViewById(R.id.choose_product);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChooseDonate.this));

        btn_add = (Button) findViewById(R.id.Button_choosen);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseDonate.this,Main3Activity.class);
                startActivity(intent);
            }
        });


        mDatadonate = FirebaseDatabase.getInstance().getReference().child("JaiboonProduct");
        mDatapush = FirebaseDatabase.getInstance().getReference().child("Jaiboon");
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
        FirebaseRecyclerAdapter<NeedDonate,DonateHolder> FBRA = new FirebaseRecyclerAdapter<NeedDonate, DonateHolder>(
                NeedDonate.class,
                R.layout.card_donate,
                DonateHolder.class,
                mDatadonate

        ) {
            @Override
            protected void populateViewHolder(DonateHolder viewHolder, NeedDonate model, int position) {
                viewHolder.setTitle(model.getNamedonate());
                viewHolder.setPrice(model.getPricedonate());
                viewHolder.setImage(getApplicationContext(), model.getImagedonate());
            }
        };
        recyclerView.setAdapter(FBRA);

    }
    public static class DonateHolder extends RecyclerView.ViewHolder{
        View mView;
        public DonateHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView Title = (TextView) mView.findViewById(R.id.Name_donate);
            Title.setText(title);
        }
        public void setPrice(String price){
            TextView Price = (TextView) mView.findViewById(R.id.Price_Donate);
            Price.setText(price);
        }
        public void setImage(Context ctx, String image){
            ImageView img_product = (ImageView) mView.findViewById(R.id.Product_donate);
            Picasso.with(ctx).load(image).into(img_product);
        }
    }
    public void btn_choose(View view) {

    }


    public void Post_donate(View view){
                 Intent intent = new Intent(ChooseDonate.this,Main2Activity.class);
                 startActivity(intent);
                 finish();

    }
}
