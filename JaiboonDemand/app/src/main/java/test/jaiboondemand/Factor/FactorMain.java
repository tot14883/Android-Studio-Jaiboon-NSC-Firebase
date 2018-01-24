package test.jaiboondemand.Factor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import test.jaiboondemand.DonateMain.MainActivity;
import test.jaiboondemand.R;

public class FactorMain extends AppCompatActivity {
    private ImageButton imageButton;
    private TextView text_profile,text_type;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factor_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_factor);
        setSupportActionBar(toolbar);
        setTitle("บริจาคปัจจัย");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_News);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff_News) ;

        View headerview = mNavigationView.getHeaderView(0);
        imageButton = (ImageButton) headerview.findViewById(R.id.img_profile);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FactorMain.this,MainActivity.class);
                startActivity(intent);
            }
        });
        text_profile = (TextView) headerview.findViewById(R.id.tv_Profile1);
        text_type = (TextView) headerview.findViewById(R.id.text_type);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
      //  mFragmentTransaction.replace(R.id.containerView_factor,new TabHostDonate()).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }
}
