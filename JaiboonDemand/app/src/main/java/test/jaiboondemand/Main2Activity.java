package test.jaiboondemand;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.nfc.Tag;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private TextView textView;
    private RecyclerView mIBstaList;
    private ImageButton imageButton;
    private MenuItem item,item1,item2,item3,item4,item5,item6,item7,item8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);//Command find id Toolbar to our set
        setSupportActionBar(toolbar);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

        View headerview = mNavigationView.getHeaderView(0);
        imageButton = (ImageButton) headerview.findViewById(R.id.img_profile);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        textView = (TextView) headerview.findViewById(R.id.tv_Profile1);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final Menu menu = mNavigationView.getMenu();
                item = menu.findItem(R.id.Log_Out);
                item1 = menu.findItem(R.id.add_deposit);
                item2 = menu.findItem(R.id.my_post);
                item3 = menu.findItem(R.id.my_account);
                item6 = menu.findItem(R.id.my_post_Admin);
                item7 = menu.findItem(R.id.nav_update_shop);
                item8 = menu.findItem(R.id.my_manager_admin);
                if(firebaseAuth.getCurrentUser() == null ){
                    Toast.makeText(Main2Activity.this,"You not login",Toast.LENGTH_LONG).show();
                    item1.setVisible(false);
                    item2.setVisible(false);
                    item3.setVisible(false);
                    item.setVisible(false);
                    item6.setVisible(false);
                    item7.setVisible(false);
                    item8.setVisible(false);

                    textView.setText("Login ?");
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
                                item1.setVisible(true);
                                item2.setVisible(true);
                                item3.setVisible(true);
                                item.setVisible(true);
                                item6.setVisible(false);
                                item7.setVisible(false);
                                item8.setVisible(false);
                            }
                            String post_name = (String)dataSnapshot.child("Name").getValue();
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



                 if (menuItem.getItemId() == R.id.Shopping_list) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView,new
                            ShopActivity()).commit();

                }
                if(menuItem.getItemId() == R.id.my_post){

                }
                if(menuItem.getItemId() == R.id.add_deposit){

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
                if (menuItem.getItemId() == R.id.my_manager_admin){}
                if(menuItem.getItemId() == R.id.my_post_Admin){}
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
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CheckAdmin() {
                    final Menu menu = mNavigationView.getMenu();
                    item = menu.findItem(R.id.Log_Out);
                    item1 = menu.findItem(R.id.add_deposit);
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
   /* public void onBackPressed() {//When Click Button BackPressed show command Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ออกจากการใช้งาน");
        dialog.setIcon(R.mipmap.logo);
        dialog.setCancelable(true);
        dialog.setMessage("คุณต้องการออกจากการทำรายการหรือไม่?");
        dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                System.exit(0);

            }
        });
        dialog.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }*/
}