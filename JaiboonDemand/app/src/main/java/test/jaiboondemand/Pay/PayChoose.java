package test.jaiboondemand.Pay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import test.jaiboondemand.R;

public class PayChoose extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_choose);

    }

    public void btnKerry(View view) {
        Intent intent = new Intent(PayChoose.this,PayCashDelivery.class);
        startActivity(intent);
    }

    public void btnPaypal(View view) {
    }

    public void btnBank(View view) {
    }
}
