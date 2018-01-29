package test.jaiboondemand.Factor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class FactorMain extends AppCompatActivity {
    private ImageButton imageButton;
    private TextView text_profile,text_type;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private MenuItem item,item1,item2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factor_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_factor);
        setSupportActionBar(toolbar);
        setTitle("บริจาคปัจจัย");
        String type_fac = getIntent().getExtras().getString("Type");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_factor);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff_factor) ;

        mData = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                   final Menu menu = mNavigationView.getMenu();
                   item = menu.findItem(R.id.myaccount_factor);
                   item1 = menu.findItem(R.id.Post_factor);
                   item2 = menu.findItem(R.id.SignOut_factor);
                if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(FactorMain.this,"You not login",Toast.LENGTH_LONG).show();
                    item.setVisible(false);
                    item1.setVisible(false);
                    item2.setVisible(false);
                    text_profile.setText("Login ?");
                    text_type.setText("");
                    imageButton.setEnabled(true);
                }
                else{
                    mData.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Selected").exists()){
                                if(dataSnapshot.child("Selected").getValue().equals("Customer")){
                                    item1.setVisible(false);
                                    text_type.setText("Customer");
                                }
                                else if(!dataSnapshot.child("Selected").getValue().equals("Customer")){
                                    item1.setVisible(true);
                                    String type_cus = (String) dataSnapshot.child("Selected").getValue();
                                    text_type.setText(type_cus);
                                }
                            }
                            else if(!dataSnapshot.child("Selected").exists()){
                                text_type.setText("");
                            }
                            String post_name = (String) dataSnapshot.child("Name_User").getValue();
                            text_profile.setText(post_name);
                            imageButton.setEnabled(false);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

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
        mFragmentTransaction.replace(R.id.containerView_factor,new HomeFactor()).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                if(item.getItemId() == R.id.Home_Factor){
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView_factor,new
                            HomeFactor()).commit();
                }
                if(item.getItemId() == R.id.category_factor){
                    Intent intent = new Intent(FactorMain.this,FacCategory.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.myaccount_factor){
                    mData.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                         if(dataSnapshot.child("Selected").exists()){
                             if(dataSnapshot.child("Selected").getValue().equals("Customer")) {
                                 Intent intent = new Intent(FactorMain.this, ProfileCustomer.class);
                                 intent.putExtra("Current","Yes");
                                 startActivity(intent);
                             }
                             if(dataSnapshot.child("Selected").getValue().equals("Temple")) {
                                 Intent intent = new Intent(FactorMain.this, ProfileTemple.class);
                                 intent.putExtra("Current","Yes");
                                 startActivity(intent);
                             }
                             if(dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                 Intent intent = new Intent(FactorMain.this, ProfileFoundation.class);
                                 intent.putExtra("Current","Yes");
                                 startActivity(intent);
                             }
                         }
                         else{
                             AlertDialog.Builder b = new AlertDialog.Builder(FactorMain.this);
                             b.setTitle("เลือกชนิดลูกค้า");
                             String[] types = {"General Customer", "Temple","Foundation"};
                             b.setItems(types, new DialogInterface.OnClickListener() {

                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {

                                     dialog.dismiss();
                                     switch(which){
                                         case 0:
                                             Intent intent = new Intent(FactorMain.this,ProfileCustomer.class);
                                             intent.putExtra("Current","No");
                                             startActivity(intent);
                                             break;
                                         case 1:
                                             Intent intent1 = new Intent(FactorMain.this,ProfileTemple.class);
                                             intent1.putExtra("Current","No");
                                             startActivity(intent1);
                                             break;
                                         case 2:
                                             Intent intent2 = new Intent(FactorMain.this,ProfileFoundation.class);
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
                if(item.getItemId() == R.id.setting_factor){
                    Intent intent = new Intent(FactorMain.this, Setting.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.Post_factor){
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView_factor,new
                            Factorpost()).commit();
                }
                if(item.getItemId() == R.id.SignOut_factor){
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(FactorMain.this);
                    dialog.setTitle("ออกจากระบบ");
                    dialog.setCancelable(true);
                    dialog.setMessage("คุณต้องการออกจากระบบหรือไม่ ?");
                    dialog.setPositiveButton("ใช้", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                          mAuth.signOut();
                            LoginManager.getInstance().logOut();
                            startActivity(getIntent());
                            Snackbar.make(mDrawerLayout,"ออกจากระบบเรียบร้อย",Snackbar.LENGTH_LONG).show();
                        }
                    });
                    dialog.setNegativeButton("ไม่ใข้", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.cancel();
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
        mAuth.addAuthStateListener(mAuthListner);
    }
}
