package test.jaiboondemand.ChooseDonate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import test.jaiboondemand.Deposit.DepositMain;
import test.jaiboondemand.DonateMain.Main2Activity;
import test.jaiboondemand.DonateMain.MainActivity;
import test.jaiboondemand.DonateMain.ProfileCustomer;
import test.jaiboondemand.DonateMain.ProfileFoundation;
import test.jaiboondemand.DonateMain.ProfileTemple;
import test.jaiboondemand.DonateMain.Setting;
import test.jaiboondemand.Factor.FactorMain;
import test.jaiboondemand.NewsFuction.NewsStart;
import test.jaiboondemand.R;

public class ChooseDonate extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    private ImageButton imageButton;
    private TextView text_Profile,text_Type;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mData;
    private MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_donate2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_choose);//Command find id Toolbar to our set
        setSupportActionBar(toolbar);
        setTitle("JaiboonOnDemand");

        mData = FirebaseDatabase.getInstance().getReference().child("Users");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_choose);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff_choose);

        View headerview = mNavigationView.getHeaderView(0);
        imageButton = (ImageButton) headerview.findViewById(R.id.img_profile);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseDonate.this,MainActivity.class);
                intent.putExtra("ChooseDonate","Main");
                startActivity(intent);
            }
        });
        text_Profile = (TextView) headerview.findViewById(R.id.tv_Profile1);
        text_Type = (TextView) headerview.findViewById(R.id.text_type);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final Menu menu = mNavigationView.getMenu();
                item = menu.findItem(R.id.my_account_main);
                if(firebaseAuth.getCurrentUser() == null){
                    item.setVisible(false);
                    text_Profile.setText("Login ?");
                    text_Type.setText(" ");
                    imageButton.setEnabled(true);
                }
                else{
                    mData.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            item.setVisible(true);
                            if (dataSnapshot.child("Selected").exists()) {
                                if (dataSnapshot.child("Selected").getValue().equals("Customer")) {
                                    text_Type.setText("Customer");
                                } else if (!dataSnapshot.child("Selected").getValue().equals("Customer")) {
                                    String type_cus = (String) dataSnapshot.child("Selected").getValue();
                                    text_Type.setText(type_cus);
                                }
                            } else if (!dataSnapshot.child("Selected").exists()) {
                                text_Type.setText("");
                            }
                            String post_name = (String) dataSnapshot.child("Name_User").getValue();
                            text_Profile.setText(post_name);
                            imageButton.setEnabled(false);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };



        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                if(item.getItemId() == R.id.my_account_main){
                    mData.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Selected").exists()){
                                if(dataSnapshot.child("Selected").getValue().equals("Customer")) {
                                    Intent intent = new Intent(ChooseDonate.this, ProfileCustomer.class);
                                    intent.putExtra("Current","Yes");
                                    startActivity(intent);
                                }
                                if(dataSnapshot.child("Selected").getValue().equals("Temple")) {
                                    Intent intent = new Intent(ChooseDonate.this, ProfileTemple.class);
                                    intent.putExtra("Current","Yes");
                                    startActivity(intent);
                                }
                                if(dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                    Intent intent = new Intent(ChooseDonate.this, ProfileFoundation.class);
                                    intent.putExtra("Current","Yes");
                                    startActivity(intent);
                                }
                            }
                            else{
                                AlertDialog.Builder b = new AlertDialog.Builder(ChooseDonate.this);
                                b.setTitle("เลือกชนิดลูกค้า");
                                String[] types = {"General Customer", "Temple","Foundation"};
                                b.setItems(types, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                        switch(which){
                                            case 0:
                                                Intent intent = new Intent(ChooseDonate.this,ProfileCustomer.class);
                                                intent.putExtra("Current","No");
                                                startActivity(intent);
                                                break;
                                            case 1:
                                                Intent intent1 = new Intent(ChooseDonate.this,ProfileTemple.class);
                                                intent1.putExtra("Current","No");
                                                startActivity(intent1);
                                                break;
                                            case 2:
                                                Intent intent2 = new Intent(ChooseDonate.this,ProfileFoundation.class);
                                                intent2.putExtra("Current","No");
                                                startActivity(intent2);
                                                break;
                                        }
                                    }

                                });

                                b.show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if(item.getItemId() == R.id.donate_things){
                    Intent intent = new Intent(ChooseDonate.this,Main2Activity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.donate_deposit){
                    Intent intent = new Intent(ChooseDonate.this,DepositMain.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.donate_factor){
                    Intent intent = new Intent(ChooseDonate.this,FactorMain.class);
                    intent.putExtra("Type","Home");
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.donate_news){
                    Intent intent = new Intent(ChooseDonate.this,NewsStart.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.setting_main){
                    Intent intent = new Intent(ChooseDonate.this,Setting.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.sing_out_main){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ChooseDonate.this);
                    dialog.setTitle("ล๊อคอิน");
                    dialog.setCancelable(true);
                    dialog.setMessage("คุณต้องการออกจากการล๊อคอินหรือไม่?");
                    dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            LoginManager.getInstance().logOut();
                            startActivity(getIntent());
                            Snackbar.make(mDrawerLayout,"ออกจากระบบเรียบร้อย",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    dialog.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    dialog.show();
                }
                return false;
            }
        });

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void deposit_main(View view){
         Intent deposit = new Intent(ChooseDonate.this, DepositMain.class);
         startActivity(deposit);
   }

    public void Donate_main(View view) {
        Intent intent = new Intent(ChooseDonate.this, Main2Activity.class);
        startActivity(intent);
    }

    public void NewsMain(View view) {
        Intent News = new Intent(ChooseDonate.this, NewsStart.class);
        startActivity(News);
    }

    public void FactorMain(View view) {
        Intent factor= new Intent(ChooseDonate.this, FactorMain.class);
        factor.putExtra("Type","Home");
        startActivity(factor);
    }
}
