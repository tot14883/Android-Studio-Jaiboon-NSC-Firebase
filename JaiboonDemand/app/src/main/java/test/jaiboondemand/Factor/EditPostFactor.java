package test.jaiboondemand.Factor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gtomato.android.ui.widget.CarouselView;

import java.util.ArrayList;
import java.util.List;

import test.jaiboondemand.R;
import test.jaiboondemand.post_activity.UploadListAdapter;

/**
 * Created by User on 2/4/2018.
 */

public class EditPostFactor extends AppCompatActivity {
    public EditText edt_title,edt_desc,edt_acc;
    private Uri uri = null;
    private Button button_image;
    private StorageReference storageReference;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;
    private factorimage imagefac = new factorimage();
    private ArrayList<Uri> path;
    private CarouselView carouselView;
    private DatabaseReference mDataUser;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;
    private Spinner spinner;
    private String post_key = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_factor);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_post_factor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Post");

        post_key = getIntent().getExtras().getString("PostID");

        button_image = (Button) findViewById(R.id.SelectImage_factor);
        carouselView = (CarouselView) findViewById(R.id.carouse_factor);
        button_image.setVisibility(View.INVISIBLE);
        carouselView.setVisibility(View.VISIBLE);

        edt_title = (EditText) findViewById(R.id.edt_post_title);
        edt_desc = (EditText) findViewById(R.id.edt_post_desc);
        edt_acc = (EditText) findViewById(R.id.edt_post_account);

        spinner = (Spinner) findViewById(R.id.spinner_type_factor);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditPostFactor.this, R.array.type_factor, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        storageReference = FirebaseStorage.getInstance().getReference();
        mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        mDataUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        fileDoneList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileDoneList);
        mData.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               String uid = (String) dataSnapshot.child("uid").getValue();
               if(mAuth.getCurrentUser().getUid().equals(uid)){
                   String title = (String) dataSnapshot.child("title_factor").getValue();
                   String desc = (String) dataSnapshot.child("desc_factor").getValue();
                   String acc = (String) dataSnapshot.child("account").getValue();

                   edt_title.setText(title);
                   edt_desc.setText(desc);
                   edt_acc.setText(acc);
                }
               else {
                   Toast.makeText(EditPostFactor.this,"คุณไม่มีสิทธิ์แก้ไขโพสต์",Toast.LENGTH_LONG).show();
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_tab_host_feed,menu);
        MenuItem item = menu.findItem(R.id.action_post);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(id == R.id.action_post){
            final String titleValue = edt_title.getText().toString().trim();
            final String descValue = edt_desc.getText().toString().trim();
            final String accValue = edt_acc.getText().toString().trim();
            final String typeValue = spinner.getSelectedItem().toString().trim();
            final String Time = String.valueOf(System.currentTimeMillis());
            if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(descValue) && !TextUtils.isEmpty(accValue)){
                                mDataUser.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("Selected").exists()){
                                            if(dataSnapshot.child("Selected").getValue().equals("Foundation")){
                                                mData.child(post_key).child("Foundation_Type").setValue(dataSnapshot.child("Type"));
                                            }
                                            else if (!dataSnapshot.child("Selected").getValue().equals("Foundation")) {

                                            }
                                            mData.child(post_key).child("Name").setValue(dataSnapshot.child("Name_User").getValue());
                                            mData.child(post_key).child("title_factor").setValue(titleValue);
                                            mData.child(post_key).child("desc_factor").setValue(descValue);
                                            mData.child(post_key).child("account").setValue(accValue);
                                            mData.child(post_key).child("uid").setValue(mAuth.getCurrentUser().getUid());
                                            mData.child(post_key).child("Type_Selected").setValue(dataSnapshot.child("Selected").getValue());
                                            mData.child(post_key).child("Category").setValue(typeValue);
                                            mData.child(post_key).child("fac_time").setValue(Time);
                                            mData.child(post_key).child("posted").setValue(dataSnapshot.child("Name_User").getValue());
                                            if(typeValue.equals("การศึกษา")){
                                                mData.child(post_key).child("Type").setValue("Education");
                                            }
                                            if(typeValue.equals("ช่วเหลือผู้ยากไร้")){
                                                mData.child(post_key).child("Type").setValue("Poor");
                                            }
                                            if(typeValue.equals("บำรุงรักษาวัด")){
                                                mData.child(post_key).child("Type").setValue("Maintain");
                                            }
                                            if(typeValue.equals("ช่วเหลือผู้ประสบภัย")){
                                                mData.child(post_key).child("Type").setValue("victim");
                                            }
                                            if(typeValue.equals("ช่วยเหลือเด็ก")){
                                                mData.child(post_key).child("Type").setValue("child");
                                            }
                                            if(typeValue.equals("ช่วเหลือสัตว์")){
                                                mData.child(post_key).child("Type").setValue("animal");
                                            }
                                            if(typeValue.equals("ช่วยผู้ด้อยโอกาส")){
                                                mData.child(post_key).child("Type").setValue("disadvantaged");
                                            }
                                            if(typeValue.equals("ช่วเหลือสังคม")){
                                                mData.child(post_key).child("Type").setValue("social");
                                            }
                                            if(typeValue.equals("ช่วยเหลือคนชรา")){
                                                mData.child(post_key).child("Type").setValue("older");
                                            }
                                            if(typeValue.equals("ช่วเหลือคนพิการ")){
                                                mData.child(post_key).child("Type").setValue("disabled");
                                            }
                                            mData.child(post_key).child("username").setValue(dataSnapshot.child("Name_Owner").getValue());
                                            mData.child(post_key).child("address").setValue(dataSnapshot.child("Address").getValue());
                                            mData.child(post_key).child("post").setValue(dataSnapshot.child("Post").getValue());
                                            mData.child(post_key).child("country").setValue(dataSnapshot.child("Country").getValue());
                                            mData.child(post_key).child("phone").setValue(dataSnapshot.child("Phone").getValue());
                                            mData.child(post_key).child("facebooklink").setValue(dataSnapshot.child("facebooklink").getValue());

                                            Intent intent = new Intent(EditPostFactor.this, FactorMain.class);
                                            intent.putExtra("Type","Home");
                                            startActivity(intent);
                                            finish();
                                        }else if(!dataSnapshot.child("Selected").exists()){
                                            Intent errorpost = new Intent(EditPostFactor.this, FactorMain.class);
                                            startActivity(errorpost);
                                            finish();
                                            Toast.makeText(EditPostFactor.this, "you not have Info your accounts", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
