package test.jaiboondemand.DonateMain;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import test.jaiboondemand.Deposit.DepositMain;
import test.jaiboondemand.Factor.FactorMain;
import test.jaiboondemand.NewsFuction.NewsStart;
import test.jaiboondemand.R;

/**
 * Created by User on 1/11/2018.
 */

public class Setting extends PreferenceActivity{
    private String send = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingFramgment()).commit();
        send = getIntent().getExtras().getString("Send");
    }
    public static class SettingFramgment extends PreferenceFragment{
        private String send = null;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        if(send.equals("News")){
            Intent intent = new Intent(getApplicationContext(), NewsStart.class);
            startActivity(intent);
        }
        if(send.equals("Main2Activity")) {
            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
            startActivity(intent);
        }
        if(send.equals("Factor")){
            Intent intent = new Intent(getApplicationContext(), FactorMain.class);
            intent.putExtra("Type","Home");
            startActivity(intent);
        }
        if(send.equals("Deposit")){
            Intent intent = new Intent(getApplicationContext(), DepositMain.class);
            startActivity(intent);
        }
    }
}
