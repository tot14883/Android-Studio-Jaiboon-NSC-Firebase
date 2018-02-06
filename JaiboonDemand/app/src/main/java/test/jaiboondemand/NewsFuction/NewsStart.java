package test.jaiboondemand.NewsFuction;


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
import android.widget.ExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import test.jaiboondemand.Admin.AdminManager;
import test.jaiboondemand.DonateMain.MainActivity;
import test.jaiboondemand.DonateMain.ProfileCustomer;
import test.jaiboondemand.DonateMain.ProfileFoundation;
import test.jaiboondemand.DonateMain.ProfileTemple;
import test.jaiboondemand.DonateMain.Setting;
import test.jaiboondemand.R;

public class NewsStart extends AppCompatActivity{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    ExpandableListAdapter mMenuAdapter;
    private ImageButton mImageLogin;
    private TextView textName,textType;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private MenuItem item,item1,item2,item3,item4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_news);//Command find id Toolbar to our set
        setSupportActionBar(toolbar);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setTitle("News");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mData = FirebaseDatabase.getInstance().getReference().child("Users");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_News);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff_News) ;

        View headerview = mNavigationView.getHeaderView(0);
        mImageLogin = (ImageButton) headerview.findViewById(R.id.img_profile);
        mImageLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsStart.this,MainActivity.class);
                intent.putExtra("ChooseDonate","News");
                startActivity(intent);
            }
        });
        textName = (TextView) headerview.findViewById(R.id.tv_Profile1);
        textType = (TextView) headerview.findViewById(R.id.text_type);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               final Menu menu = mNavigationView.getMenu();
                item = menu.findItem(R.id.singout);
                item1 = menu.findItem(R.id.my_account);
                item2 = menu.findItem(R.id.my_post_Admin);
                item3 = menu.findItem(R.id.nav_update_shop);
                item4 = menu.findItem(R.id.my_manager_admin);
                if(firebaseAuth.getCurrentUser() == null ){
                    Toast.makeText(NewsStart.this,"You not login",Toast.LENGTH_LONG).show();
                    item1.setVisible(false);
                    item2.setVisible(false);
                    item3.setVisible(false);
                    item.setVisible(false);
                    item4.setVisible(false);

                    textName.setText("Login ?");
                    textType.setText("");
                    mImageLogin.setEnabled(true);
                }
                else{
                    mData.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Level").exists()){
                                CheckAdmin();
                                String post_name = (String) dataSnapshot.child("Name_User").getValue();
                                textName.setText(post_name);
                                mImageLogin.setEnabled(false);
                                textType.setText("");
                            }
                            else if(!dataSnapshot.child("Level").exists()){
                                item1.setVisible(true);
                                item2.setVisible(false);
                                item3.setVisible(false);
                                item.setVisible(true);
                                item4.setVisible(false);
                            }
                            if(!dataSnapshot.child("Selected").exists()){
                                textType.setText("");
                            }
                            else  if(dataSnapshot.child("Selected").exists()) {
                                String post_name = (String) dataSnapshot.child("Name_User").getValue();
                                String type_cus = (String) dataSnapshot.child("Selected").getValue();
                                textType.setText(type_cus);
                                textName.setText(post_name);
                                mImageLogin.setEnabled(false);
                            }
                    }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView_News,new NewsMain()).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                if(item.getItemId() == R.id.Home_News){
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView_News,new
                            NewsMain()).commit();
                }
               if(item.getItemId() == R.id.singout){
                   AlertDialog.Builder dialog = new AlertDialog.Builder(NewsStart.this);
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
               if (item.getItemId() == R.id.setting_news){
                   Intent intent = new Intent(NewsStart.this,Setting.class);
                   intent.putExtra("Send","News");
                   startActivity(intent);
               }
               if(item.getItemId() == R.id.my_account){
                   mData.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           if(dataSnapshot.child("Selected").exists()){
                               if(dataSnapshot.child("Selected").getValue().equals("Customer")) {
                                   Intent intent = new Intent(NewsStart.this, ProfileCustomer.class);
                                   intent.putExtra("Current","Yes");
                                   startActivity(intent);
                               }
                               if(dataSnapshot.child("Selected").getValue().equals("Temple")) {
                                   Intent intent = new Intent(NewsStart.this, ProfileTemple.class);
                                   intent.putExtra("Current","Yes");
                                   startActivity(intent);
                               }
                               if(dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                   Intent intent = new Intent(NewsStart.this, ProfileFoundation.class);
                                   intent.putExtra("Current","Yes");
                                   startActivity(intent);
                               }
                           }
                           else{
                               AlertDialog.Builder b = new AlertDialog.Builder(NewsStart.this);
                               b.setTitle("เลือกชนิดลูกค้า");
                               String[] types = {"General Customer", "Temple","Foundation"};
                               b.setItems(types, new DialogInterface.OnClickListener() {

                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {

                                       dialog.dismiss();
                                       switch(which){
                                           case 0:
                                               Intent intent = new Intent(NewsStart.this,ProfileCustomer.class);
                                               intent.putExtra("Current","No");
                                               startActivity(intent);
                                               break;
                                           case 1:
                                               Intent intent1 = new Intent(NewsStart.this,ProfileTemple.class);
                                               intent1.putExtra("Current","No");
                                               startActivity(intent1);
                                               break;
                                           case 2:
                                               Intent intent2 = new Intent(NewsStart.this,ProfileFoundation.class);
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
               if(item.getItemId() == R.id.my_post_Admin){
                   FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                   xfragmentTransaction.replace(R.id.containerView_News,new
                           AdminManager()).commit();
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

    public void CheckAdmin() {
        final Menu menu = mNavigationView.getMenu();
        item = menu.findItem(R.id.singout);
        item1 = menu.findItem(R.id.my_account);
        item2 = menu.findItem(R.id.my_post_Admin);
        item3 = menu.findItem(R.id.nav_update_shop);
        item4 = menu.findItem(R.id.my_manager_admin);
        item1.setVisible(false);
        item2.setVisible(true);
        item3.setVisible(true);
        item4.setVisible(true);
        item.setVisible(true);
    }

}
