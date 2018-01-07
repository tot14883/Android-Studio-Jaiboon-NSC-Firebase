package test.jaiboondemand.Pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

import test.jaiboondemand.R;

public class PayPalCart extends AppCompatActivity {
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView text_total;
    private RecyclerView recyclerView;
    PayPalConfiguration m_configuration;
    String m_paypalClientId = "AcK1v_UJFjSusJgC0PqqwmDAHltZgZx34HsWp7O_3sch19tTr27yy5C-lC075s7B47PVFLI6vfxRgpOf";
    Intent m_service;
    int m_paypalRequestCode = 1000000;
    private Integer[] total_price = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_cart);
        text_total = (TextView) findViewById(R.id.total_price_sent);

        m_configuration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(m_paypalClientId);

        m_service = new Intent(this,PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration);
        startService(m_service);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_pay_sent);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mData = FirebaseDatabase.getInstance().getReference().child("Cart");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    if (d.child("uid").getValue().equals(mAuth.getCurrentUser().getUid())){
                        String Total_price = (String) String.valueOf(d.child("priceproduct").getValue());
                        String amount = (String) String.valueOf(d.child("amount").getValue());
                        total_price[0] = (total_price[0]+Integer.parseInt(Total_price)*Integer.parseInt(amount));
                    }
                }
                text_total.setText(String.valueOf(total_price[0]));
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
        FirebaseRecyclerAdapter<ProductSend,PaypalCartViewHolder> adapter = new FirebaseRecyclerAdapter<ProductSend, PaypalCartViewHolder>(
                ProductSend.class,
                R.layout.row_pay_cash,
                PaypalCartViewHolder.class,
                mData.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid())
        ) {
            @Override
            protected void populateViewHolder(PaypalCartViewHolder viewHolder, ProductSend model, int position) {
                viewHolder.setName(model.getNameproduct());
                viewHolder.setImage(getApplicationContext(),model.getImageproduct());
                viewHolder.setprice(model.getPriceproduct());
                viewHolder.setAmout(model.getAmount());
            }
        };
        recyclerView.setAdapter(adapter);
        mAuth.addAuthStateListener(mAuthListener);
    }
    public static class PaypalCartViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public PaypalCartViewHolder(View itemView) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == m_paypalRequestCode){
            if (resultCode == Activity.RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            }
        }
    }

    public void Paypal(View view) {
        PayPalPayment cart = new PayPalPayment(new BigDecimal(String.valueOf(total_price[0])),"Bath","Cary"
        ,PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this,PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,cart);
        startActivityForResult(intent,m_paypalRequestCode);
    }

}
