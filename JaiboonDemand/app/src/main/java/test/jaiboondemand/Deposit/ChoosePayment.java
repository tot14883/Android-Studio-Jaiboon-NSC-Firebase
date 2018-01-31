package test.jaiboondemand.Deposit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import test.jaiboondemand.R;

public class ChoosePayment extends AppCompatActivity {
    private String type_post = null;
    private String owner,phone,localname,localaddress,localpost,localcountry,post_name,address,post,country,phone_customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);
        type_post = getIntent().getExtras().getString("Typesend");
        owner = getIntent().getExtras().getString("Name");
        phone = getIntent().getExtras().getString("Phone");
        if(type_post.equals("Quick")) {
            localname = getIntent().getExtras().getString("Localname");
        }
        else if(type_post.equals("defualt")){
            localname = "";
        }
        localaddress = getIntent().getExtras().getString("LocalAddress");
        localpost = getIntent().getExtras().getString("LocalPost");
        localcountry = getIntent().getExtras().getString("LocalCountry");
        post_name = getIntent().getExtras().getString("yourName");
        address = getIntent().getExtras().getString("youraddress");
        post = getIntent().getExtras().getString("yourpost");
        country = getIntent().getExtras().getString("yourcountry");
        phone_customer = getIntent().getExtras().getString("yourphone");

    }

    public void Bank_Payment(View view) {
        Intent intent = new Intent(ChoosePayment.this,BankSubmit.class);
            intent.putExtra("Name",owner);
            intent.putExtra("Phone",phone);
            intent.putExtra("Localname",localname);
            intent.putExtra("LocalAddress",localaddress);
            intent.putExtra("LocalPost",localpost);
            intent.putExtra("LocalCountry",localcountry);
            intent.putExtra("yourName",post_name);
            intent.putExtra("youraddress",address);
            intent.putExtra("yourpost",post);
            intent.putExtra("yourcountry",country);
            intent.putExtra("yourphone",phone_customer);
            intent.putExtra("Typesend","Quick");
            startActivity(intent);
    }

    public void Send_Payment(View view) {
        Intent intent = new Intent(ChoosePayment.this,SendPostSubmit.class);
        intent.putExtra("Name",owner);
        intent.putExtra("Phone",phone);
        intent.putExtra("Localname",localname);
        intent.putExtra("LocalAddress",localaddress);
        intent.putExtra("LocalPost",localpost);
        intent.putExtra("LocalCountry",localcountry);
        intent.putExtra("yourName",post_name);
        intent.putExtra("youraddress",address);
        intent.putExtra("yourpost",post);
        intent.putExtra("yourcountry",country);
        intent.putExtra("yourphone",phone_customer);
        intent.putExtra("Typesend","defualt");
        startActivity(intent);
    }
}
