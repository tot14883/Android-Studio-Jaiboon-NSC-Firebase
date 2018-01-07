package test.jaiboondemand.Deposit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import test.jaiboondemand.R;

public class DepositPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_deposit);//Command find id Toolbar to our set
        setSupportActionBar(toolbar);
        setTitle("ฝากของบริจาค");
    }
}
