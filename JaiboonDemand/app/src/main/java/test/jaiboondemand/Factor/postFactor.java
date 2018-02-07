package test.jaiboondemand.Factor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gtomato.android.ui.transformer.InverseTimeMachineViewTransformer;
import com.gtomato.android.ui.widget.CarouselView;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.PicassoAdapter;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;
import java.util.List;

import test.jaiboondemand.DonateMain.ImagePostAdapter;
import test.jaiboondemand.R;
import test.jaiboondemand.post_activity.UploadListAdapter;

public class postFactor extends AppCompatActivity {
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
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_factor);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_post_factor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Post");

        mProgress = new ProgressDialog(postFactor.this);

        button_image = (Button) findViewById(R.id.SelectImage_factor);
        button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              imagebuttomimage(view);
            }
        });

        carouselView = (CarouselView) findViewById(R.id.carouse_factor);

        edt_title = (EditText) findViewById(R.id.edt_post_title);
        edt_desc = (EditText) findViewById(R.id.edt_post_desc);
        edt_acc = (EditText) findViewById(R.id.edt_post_account);

        spinner = (Spinner) findViewById(R.id.spinner_type_factor);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(postFactor.this, R.array.type_factor, android.R.layout.simple_spinner_item);
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
    }
    public void imagebuttomimage(View view){
        FishBun.with(postFactor.this)
                .setImageAdapter(new PicassoAdapter())
                .setMaxCount(imagefac.num_image)
                .setMinCount(1)
                .startAlbum();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    carouselView.setTransformer(new InverseTimeMachineViewTransformer()); // กำหนดรูปแบบการสไลด์ได้ตามเอกสารใน https://gtomato.github.io/carouselview/
                    carouselView.setAdapter(new ImagePostAdapter(path));
                    break;
                }
        }
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
        final ArrayList<Uri> download = new ArrayList<Uri>(imagefac.num_image); // สร้าง ArrayList สำหรับเก็บลิงค์รูปภาพที่อัพโหลด
        final int[] num = {0};

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
                mProgress.setMessage("Posting.....");
                mProgress.show();
                final int[] image1 = {0};
                while(image1[0] < path.size()){
                    uri = path.get(image1[0]);
                    Log.v("numpp",String.valueOf(path.size()));

                    StorageReference filePath = storageReference.child("PostImageFac").child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(postFactor.this,"Error Uploading",Toast.LENGTH_LONG).show();
                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uri = taskSnapshot.getDownloadUrl();
                            download.add(uri);
                            num[0]++;

                            if(num[0] == path.size()){
                                final DatabaseReference newPost = mData.push();
                                mDataUser.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("Selected").exists()){
                                            if(dataSnapshot.child("Selected").getValue().equals("Foundation")){
                                                newPost.child("Foundation_Type").setValue(dataSnapshot.child("Type"));
                                            }
                                            else if (!dataSnapshot.child("Selected").getValue().equals("Foundation")) {

                                            }
                                            newPost.child("Name").setValue(dataSnapshot.child("Name_User").getValue());
                                            newPost.child("title_factor").setValue(titleValue);
                                            newPost.child("desc_factor").setValue(descValue);
                                            newPost.child("account").setValue(accValue);
                                            newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                            newPost.child("Type_Selected").setValue(dataSnapshot.child("Selected").getValue());
                                            newPost.child("Category").setValue(typeValue);
                                            newPost.child("fac_time").setValue(Time);
                                            newPost.child("posted").setValue(dataSnapshot.child("Name_User").getValue());
                                            if(typeValue.equals("การศึกษา")){
                                                newPost.child("Type").setValue("Education");
                                            }
                                            if(typeValue.equals("ช่วเหลือผู้ยากไร้")){
                                                newPost.child("Type").setValue("Poor");
                                            }
                                            if(typeValue.equals("บำรุงรักษาวัด")){
                                                newPost.child("Type").setValue("Maintain");
                                            }
                                            if(typeValue.equals("ช่วเหลือผู้ประสบภัย")){
                                                newPost.child("Type").setValue("victim");
                                            }
                                            if(typeValue.equals("ช่วยเหลือเด็ก")){
                                                newPost.child("Type").setValue("child");
                                            }
                                            if(typeValue.equals("ช่วเหลือสัตว์")){
                                                newPost.child("Type").setValue("animal");
                                            }
                                            if(typeValue.equals("ช่วยผู้ด้อยโอกาส")){
                                                newPost.child("Type").setValue("disadvantaged");
                                            }
                                            if(typeValue.equals("ช่วเหลือสังคม")){
                                                newPost.child("Type").setValue("social");
                                            }
                                            if(typeValue.equals("ช่วยเหลือคนชรา")){
                                                newPost.child("Type").setValue("older");
                                            }
                                            if(typeValue.equals("ช่วเหลือคนพิการ")){
                                                newPost.child("Type").setValue("disabled");
                                            }
                                            newPost.child("username").setValue(dataSnapshot.child("Name_Owner").getValue());
                                            newPost.child("address").setValue(dataSnapshot.child("Address").getValue());
                                            newPost.child("post").setValue(dataSnapshot.child("Post").getValue());
                                            newPost.child("country").setValue(dataSnapshot.child("Country").getValue());
                                            newPost.child("phone").setValue(dataSnapshot.child("Phone").getValue());
                                            newPost.child("facebooklink").setValue(dataSnapshot.child("facebooklink").getValue());
                                            for(int i = 0;i < imagefac.num_image;i++){
                                                if(i < path.size()){
                                                    if(i == 0){
                                                        newPost.child("image").setValue(download.get(i).toString());
                                                    }
                                                    else {
                                                        newPost.child("image" + String.valueOf(i)).setValue(download.get(i).toString());
                                                    }
                                                }else{
                                                    newPost.child("image" + String.valueOf(i)).setValue("-");
                                                }
                                            }
                                            mProgress.dismiss();
                                            Intent intent = new Intent(postFactor.this, FactorMain.class);
                                            intent.putExtra("Type","Home");
                                            startActivity(intent);
                                            finish();
                                        }else if(!dataSnapshot.child("Selected").exists()){
                                            Intent errorpost = new Intent(postFactor.this, FactorMain.class);
                                            startActivity(errorpost);
                                            finish();
                                            Toast.makeText(postFactor.this, "you not have Info your accounts", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });
                    image1[0]++;
                }

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
