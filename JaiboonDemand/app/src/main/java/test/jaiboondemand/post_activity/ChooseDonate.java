package test.jaiboondemand.post_activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import test.jaiboondemand.DonateMain.Main2Activity;
import test.jaiboondemand.R;
import test.jaiboondemand.ShopDonate.Main3Activity;
import test.jaiboondemand.must_product.NeedDonate;

public class ChooseDonate extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private DatabaseReference mDatadonate;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatapush;
    private DatabaseReference mDatauser;
    private RecyclerView recyclerView;
    private Button btn_add;
    private String Key = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_donate);
        Key = getIntent().getExtras().getString("Keypost");
        recyclerView = (RecyclerView) findViewById(R.id.choose_product);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChooseDonate.this));

        btn_add = (Button) findViewById(R.id.Button_choosen);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseDonate.this,Main3Activity.class);
                intent.putExtra("Keypost",Key);
                startActivity(intent);
            }
        });

        mDatauser = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatadonate = FirebaseDatabase.getInstance().getReference().child("Jaiboon").child(Key);
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
                mDatadonate.child("DonateProduct")

        ) {
            @Override
            protected void populateViewHolder(DonateHolder viewHolder, NeedDonate model, final int position) {
                final int position1 = position;
                final int[] num1 = {1};
                viewHolder.setTitle(model.getNamedonate());
                viewHolder.setPrice(model.getPricedonate());
                viewHolder.setImage(getApplicationContext(), model.getImagedonate());
                viewHolder.spinner_need.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                         if(num1[0] != 1){
                             String item = adapterView.getItemAtPosition(i).toString();
                             getRef(position1).child("amountdonate").setValue(item);
                         }
                         else{
                             getRef(position1).child("amountdonate").setValue("1");
                         }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);

    }
    public static class DonateHolder extends RecyclerView.ViewHolder{
        View mView;
        private Spinner spinner_need;
        private int spinner_sel,cost;
        public DonateHolder(View itemView) {
            super(itemView);
            mView = itemView;
            spinner_need = (Spinner) mView.findViewById(R.id.spinner_choose_need);
            spinner_sel = spinner_need.getSelectedItemPosition();
            String[] value = mView.getResources().getStringArray(R.array.amountNeed);
            cost = Integer.valueOf(value[spinner_sel]);
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
