package test.jaiboondemand.must_product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import test.jaiboondemand.Pay.PayPalCart;
import test.jaiboondemand.R;

public class NeedChoosePay extends AppCompatActivity {
    private String post_key = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_choose_pay);
        post_key = getIntent().getExtras().getString("post_key");
    }

    public void paypalneed(View view){
        Intent intent = new Intent(NeedChoosePay.this, PayPalCart.class);
        intent.putExtra("post_key",post_key);
        startActivity(intent);
    }

    public void bankneed(View view) {

    }
}
