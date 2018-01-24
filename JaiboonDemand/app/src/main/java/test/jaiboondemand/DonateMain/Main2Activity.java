package test.jaiboondemand.DonateMain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import test.jaiboondemand.Admin.AdminPost;
import test.jaiboondemand.Nearby.NearbyActivity;
import test.jaiboondemand.R;
import test.jaiboondemand.shop_type.ShopActivity;

public class Main2Activity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private Button button;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;
    private MenuItem menuItem;
    private TextView textView,textView1;
    private RecyclerView mIBstaList;
    private ImageButton imageButton;
    public static Toolbar toolbar;
    private MenuItem item,item1,item2,item3,item4,item5,item6,item7,item8,item9;
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private String Languages;
    public static Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);//Command find id Toolbar to our set
        setSupportActionBar(toolbar);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Languages = mSharedPreferences.getString(getString(R.string.setting_languages),"Select");
        if(Languages.equals("English")){
            mContext = LocaleHelper.setLocale(Main2Activity.this, "en");
            resources = mContext.getResources();

        }
        else if(Languages.equals("Thai") ){
            mContext = LocaleHelper.setLocale(Main2Activity.this, "th");
            resources = mContext.getResources();

        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        setTitleNevigator();


        View headerview = mNavigationView.getHeaderView(0);
        imageButton = (ImageButton) headerview.findViewById(R.id.img_profile);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                intent.putExtra("ChooseDonate","Jaiboon");
                startActivity(intent);
            }
        });
        textView = (TextView) headerview.findViewById(R.id.tv_Profile1);
        textView1 = (TextView) headerview.findViewById(R.id.text_type);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final Menu menu = mNavigationView.getMenu();
                item = menu.findItem(R.id.Log_Out);
                item2 = menu.findItem(R.id.my_post);
                item3 = menu.findItem(R.id.my_account);
                item6 = menu.findItem(R.id.my_post_Admin);
                item7 = menu.findItem(R.id.nav_update_shop);
                item8 = menu.findItem(R.id.my_manager_admin);
                item9 = menu.findItem(R.id.To_Do_List);

                if(firebaseAuth.getCurrentUser() == null ){
                    Toast.makeText(Main2Activity.this,"You not login",Toast.LENGTH_LONG).show();
                    item2.setVisible(false);
                    item3.setVisible(false);
                    item.setVisible(false);
                    item6.setVisible(false);
                    item7.setVisible(false);
                    item8.setVisible(false);
                    item9.setVisible(false);

                    textView.setText("Login ?");
                    textView1.setText("");
                    imageButton.setEnabled(true);
                }
                else{
                    mDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Level").exists()){
                                CheckAdmin();
                            }
                            else if(!dataSnapshot.child("Level").exists()){
                                 item3.setVisible(true);
                                 item.setVisible(true);
                                 item2.setVisible(false);
                                 item6.setVisible(false);
                                 item7.setVisible(false);
                                 item8.setVisible(false);
                               if(dataSnapshot.child("Selected").exists()){
                                   if(dataSnapshot.child("Selected").getValue().equals("Customer")){
                                       item2.setVisible(false);
                                       textView1.setText("Customer");
                                   }
                                   else if(!dataSnapshot.child("Selected").getValue().equals("Customer")){
                                       item2.setVisible(true);
                                       String type_cus =(String) dataSnapshot.child("Selected").getValue();
                                       textView1.setText(type_cus);
                                   }
                               }
                               else if(!dataSnapshot.child("Selected").exists()){
                                     textView1.setText("");
                                }
                            }
                            String post_name = (String)dataSnapshot.child("Name_User").getValue();
                            textView.setText(post_name);
                            imageButton.setEnabled(false);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabHostDonate()).commit();

        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();



                 if (menuItem.getItemId() == R.id.Shopping_list)
                 {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new
                            ShopActivity()).commit();

                }
                if(menuItem.getItemId() == R.id.my_post){
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new
                            UserPost()).commit();
                }
                if(menuItem.getItemId() == R.id.Nearby_place){
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new
                            NearbyActivity()).commit();
                }
                if(menuItem.getItemId()== R.id.my_account) {
                    mDatabaseUsers.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Selected").exists()){
                                if(dataSnapshot.child("Selected").getValue().equals("Customer")) {
                                    Intent intent = new Intent(Main2Activity.this, ProfileCustomer.class);
                                    intent.putExtra("Current","Yes");
                                    startActivity(intent);
                                }
                                if(dataSnapshot.child("Selected").getValue().equals("Temple")) {
                                    Intent intent = new Intent(Main2Activity.this, ProfileTemple.class);
                                    intent.putExtra("Current","Yes");
                                    startActivity(intent);
                                }
                                if(dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                    Intent intent = new Intent(Main2Activity.this, ProfileFoundation.class);
                                    intent.putExtra("Current","Yes");
                                    startActivity(intent);
                                }
                            }
                            else{
                                AlertDialog.Builder b = new AlertDialog.Builder(Main2Activity.this);
                                b.setTitle("เลือกชนิดลูกค้า");
                                String[] types = {"General Customer", "Temple","Foundation"};
                                b.setItems(types, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                        switch(which){
                                            case 0:
                                                Intent intent = new Intent(Main2Activity.this,ProfileCustomer.class);
                                                intent.putExtra("Current","No");
                                                startActivity(intent);
                                                break;
                                            case 1:
                                                Intent intent1 = new Intent(Main2Activity.this,ProfileTemple.class);
                                                intent1.putExtra("Current","No");
                                                startActivity(intent1);
                                                break;
                                            case 2:
                                                Intent intent2 = new Intent(Main2Activity.this,ProfileFoundation.class);
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
                if (menuItem.getItemId() == R.id.nav_home) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new
                            TabHostDonate()).commit(); }

                if(menuItem.getItemId() == R.id.Log_Out){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
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
                if(menuItem.getItemId()== R.id.nav_update_shop){
                     Intent intent = new Intent(Main2Activity.this,AdminPost.class);
                     startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.setting) {
                    Intent intent = new Intent(Main2Activity.this,Setting.class);
                    startActivity(intent);
                }
                if (menuItem.getItemId() == R.id.my_manager_admin){}
                if(menuItem.getItemId() == R.id.my_post_Admin){
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new
                            AdminManager()).commit();
                }
                return false;
            }

        });


        /**
         * Setup Drawer Toggle of the Toolbar
         */
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart,menu);
        item = menu.findItem(R.id.action_cart);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if (id == R.id.action_cart) {
            Intent cart = new Intent(Main2Activity.this,badgeLayout.class);
            startActivity(cart);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CheckAdmin() {
                    final Menu menu = mNavigationView.getMenu();
                    item = menu.findItem(R.id.Log_Out);
                    item2 = menu.findItem(R.id.my_post);
                    item3 = menu.findItem(R.id.my_account);
                    item4 = menu.findItem(R.id.To_Do_List);
                    item5 = menu.findItem(R.id.needs_help);
                    item6 = menu.findItem(R.id.my_post_Admin);
                    item7 = menu.findItem(R.id.nav_update_shop);
                    item8 = menu.findItem(R.id.my_manager_admin);
                    item1.setVisible(false);
                    item2.setVisible(false);
                    item3.setVisible(false);
                    item4.setVisible(false);
                    item5.setVisible(false);
                    item6.setVisible(true);
                    item7.setVisible(true);
                    item8.setVisible(true);
    }
    public void setTitleNevigator(){
        MenuItem nav_shop,nav_home,nav_nearby,nav_history,nav_need_help,nav_setting,nav_my_account,nav_mypost,nav_mypostadmin,nav_uploadproduct,nav_sign_out,nav_depositadmin;
        Menu menu = mNavigationView.getMenu();
        nav_shop = menu.findItem(R.id.Shopping_list);
        nav_home = menu.findItem(R.id.nav_home);
        nav_nearby = menu.findItem(R.id.Nearby_place);
        nav_history = menu.findItem(R.id.To_Do_List);
        nav_need_help = menu.findItem(R.id.needs_help);
        nav_setting = menu.findItem(R.id.setting);
        nav_my_account = menu.findItem(R.id.my_account);
        nav_mypost = menu.findItem(R.id.my_post);
        nav_mypostadmin = menu.findItem(R.id.my_post_Admin);
        nav_uploadproduct = menu.findItem(R.id.nav_update_shop);
        nav_depositadmin = menu.findItem(R.id.my_manager_admin);
        nav_sign_out = menu.findItem(R.id.Log_Out);

        nav_shop.setTitle(R.string.Shop);
        nav_home.setTitle(R.string.Home);
        nav_nearby.setTitle(R.string.Nearby);
        nav_history.setTitle(R.string.History);
        nav_need_help.setTitle(R.string.Needs_Help);
        nav_setting.setTitle(R.string.Settings);
        nav_my_account.setTitle(R.string.MyAccount);
        nav_mypost.setTitle(R.string.Mypost);
        nav_mypostadmin.setTitle(R.string.AdminPost);
        nav_uploadproduct.setTitle(R.string.UploadProduct);
        nav_depositadmin.setTitle(R.string.AdminDeposit);
        nav_sign_out.setTitle(R.string.SingOut);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}