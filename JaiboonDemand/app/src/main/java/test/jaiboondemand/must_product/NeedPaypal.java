package test.jaiboondemand.must_product;

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

public class NeedPaypal extends AppCompatActivity {
    private String post_key = null;
    private TextView name_need,address_need,text_total,text_phone,text_owner;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private RecyclerView recyclerView;
    private Integer[] total_price = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_paypal);
        post_key = getIntent().getExtras().getString("post_key");

        name_need = (TextView) findViewById(R.id.name_send_need);
        address_need = (TextView) findViewById(R.id.address_send_need);
        text_total = (TextView) findViewById(R.id.total_price_sent_need);
        text_phone = (TextView) findViewById(R.id.phone);
        text_owner = (TextView) findViewById(R.id.owner_send_need) ;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_pay_sent_need);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    ShowInfomation();
                }
            }
        };
        mData = FirebaseDatabase.getInstance().getReference().child("Jaiboon").child(post_key);
    }
    public void ShowInfomation(){

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String post_name = (String) dataSnapshot.child("Name").getValue();
                    String address = (String) dataSnapshot.child("address").getValue();
                    String post = (String) dataSnapshot.child("post").getValue();
                    String country = (String) dataSnapshot.child("country").getValue();
                    String owner = (String) dataSnapshot.child("username").getValue();
                    String phone =(String) dataSnapshot.child("phone").getValue();
                    name_need.setText(post_name);
                    text_owner.setText("ชื่อผู้ดูแล "+owner);
                    text_phone.setText("เบอร์โทร "+phone);
                    address_need.setText("ที่อยู่ " + address + "\n" + "รหัสไปรษณีย์ " + post + "\n" + "จังหวัด " + country);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<NeedDonate,NeedPaypayViewHolder> adapter = new FirebaseRecyclerAdapter<NeedDonate, NeedPaypayViewHolder>(
                NeedDonate.class,
                R.layout.card_view_submit,
                NeedPaypayViewHolder.class,
                mData.child("DonateProduct")
        ) {
            @Override
            protected void populateViewHolder(NeedPaypayViewHolder viewHolder, NeedDonate model, int position) {
                viewHolder.setNameNeed(model.getNamedonate());
                viewHolder.setPricNeed(model.getPricedonate());
                viewHolder.setImageNeed(getApplicationContext(),model.getImagedonate());
                viewHolder.setAmount(model.getAmountdonate());
                total_price[0] = (total_price[0]+Integer.parseInt(model.getPricedonate())*Integer.parseInt(model.getAmountdonate()));
                text_total.setText(String.valueOf(total_price[0]));
            }
        };
        recyclerView.setAdapter(adapter);
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void Paypalneed(View view) {
    }

    public static class NeedPaypayViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public NeedPaypayViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setNameNeed(String name){
          TextView name_need = (TextView) mView.findViewById(R.id.Name_donate_need_submit);
          name_need.setText(name);
        }
        public void setPricNeed(String price){
          TextView price_need = (TextView) mView.findViewById(R.id.Price_Donate_need_submit) ;
          price_need.setText(price);
        }
        public void setImageNeed(Context ctx,String image){
          ImageView post_image = (ImageView) mView.findViewById(R.id.Product_donate_need_submit);
            Picasso.with(ctx).load(image).into(post_image);
        }
        public void setAmount(String amount){
            TextView amount_need = (TextView) mView.findViewById(R.id.text_need_amount);
            amount_need.setText(amount);

        }
    }

}
