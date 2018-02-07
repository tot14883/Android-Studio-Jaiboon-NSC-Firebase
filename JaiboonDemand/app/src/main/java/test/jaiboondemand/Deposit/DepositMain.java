package test.jaiboondemand.Deposit;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import test.jaiboondemand.DonateMain.MainActivity;
import test.jaiboondemand.DonateMain.ProfileCustomer;
import test.jaiboondemand.DonateMain.ProfileFoundation;
import test.jaiboondemand.DonateMain.ProfileTemple;
import test.jaiboondemand.DonateMain.Setting;
import test.jaiboondemand.R;

public class DepositMain extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView txt_name,txt_type;
    private ImageButton btn_image;
    private MenuItem item,item1;
    private DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_Deposit);//Command find id Toolbar to our set
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_Deposit);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff_Deposit) ;

        mData = FirebaseDatabase.getInstance().getReference().child("Users");

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_deposit);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DepositMain.this,Send.class);
                intent.putExtra("TypeSend","defualt");
                startActivity(intent);
            }
        });

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView_Deposit,new DepositSend()).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                if(item.getItemId() == R.id.home_deposit){
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView_Deposit,new
                            DepositSend()).commit();
                }
                if (item.getItemId() == R.id.myaccount_deposit) {
                    mData.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Selected").exists()){
                                if(dataSnapshot.child("Selected").getValue().equals("Customer")) {
                                    Intent intent = new Intent(DepositMain.this, ProfileCustomer.class);
                                    intent.putExtra("Current","Yes");
                                    startActivity(intent);
                                }
                                if(dataSnapshot.child("Selected").getValue().equals("Temple")) {
                                    Intent intent = new Intent(DepositMain.this, ProfileTemple.class);
                                    intent.putExtra("Current","Yes");
                                    startActivity(intent);
                                }
                                if(dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                    Intent intent = new Intent(DepositMain.this, ProfileFoundation.class);
                                    intent.putExtra("Current","Yes");
                                    startActivity(intent);
                                }
                            }
                            else {
                                AlertDialog.Builder b = new AlertDialog.Builder(DepositMain.this);
                                b.setTitle("เลือกชนิดลูกค้า");
                                String[] types = {"General Customer", "Temple", "Foundation"};
                                b.setItems(types, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                        switch (which) {
                                            case 0:
                                                Intent intent = new Intent(DepositMain.this, ProfileCustomer.class);
                                                intent.putExtra("Current", "No");
                                                startActivity(intent);
                                                break;
                                            case 1:
                                                Intent intent1 = new Intent(DepositMain.this, ProfileTemple.class);
                                                intent1.putExtra("Current", "No");
                                                startActivity(intent1);
                                                break;
                                            case 2:
                                                Intent intent2 = new Intent(DepositMain.this, ProfileFoundation.class);
                                                intent2.putExtra("Current", "No");
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
                if(item.getItemId() == R.id.setting_deposit){
                    Intent intent = new Intent(DepositMain.this,Setting.class);
                    intent.putExtra("Send","Deposit");
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId() == R.id.log_out_deposit){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DepositMain.this);
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

        View headerview = mNavigationView.getHeaderView(0);
        btn_image = (ImageButton) headerview.findViewById(R.id.img_profile);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DepositMain.this, MainActivity.class);
                intent.putExtra("ChooseDonate","Deposit");
                startActivity(intent);
            }
        });
        txt_name = (TextView) headerview.findViewById(R.id.tv_Profile1);
        txt_type = (TextView) headerview.findViewById(R.id.text_type);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final Menu menu = mNavigationView.getMenu();
                item = menu.findItem(R.id.log_out_deposit);
                item1 = menu.findItem(R.id.myaccount_deposit);
                if(firebaseAuth.getCurrentUser() != null){
                    item.setVisible(true);
                    item1.setVisible(true);
                    mData.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                              if(dataSnapshot.child("Selected").exists()){
                                  if(dataSnapshot.child("Selected").getValue().equals("Customer")){
                                      txt_type.setText("Customer");
                                  }
                                  else if(!dataSnapshot.child("Selected").getValue().equals("Customer")){
                                      String type_cus = (String) dataSnapshot.child("Selected").getValue();
                                      txt_type.setText(type_cus);
                                  }
                              }
                              else if(!dataSnapshot.child("Selected").exists()){
                                  txt_type.setText("");
                              }
                            String post_name = (String) dataSnapshot.child("Name_User").getValue();
                              txt_name.setText(post_name);
                              btn_image.setEnabled(false);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(DepositMain.this,"You not Login",Toast.LENGTH_LONG).show();
                    item1.setVisible(false);
                    item.setVisible(false);

                    txt_name.setText("Login ?");
                    txt_type.setText("");
                    btn_image.setEnabled(true);
                }
            }
        };

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
}
