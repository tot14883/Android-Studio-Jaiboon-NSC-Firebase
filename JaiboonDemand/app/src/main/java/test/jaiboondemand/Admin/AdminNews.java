package test.jaiboondemand.Admin;

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
import android.widget.Button;
import android.widget.EditText;
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
import test.jaiboondemand.DonateMain.Main2Activity;
import test.jaiboondemand.DonateMain.PostSetting;
import test.jaiboondemand.R;
import test.jaiboondemand.post_activity.UploadListAdapter;

public class AdminNews extends AppCompatActivity {
    public static final int GALLERY_REQUEST = 2;
    private EditText edt_topic,edt_news_desc;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private Uri uri = null;
    private PostSetting postSetting = new PostSetting();
    private ArrayList<Uri> path;
    private CarouselView carouselView;
    private Button image_btn;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_post_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(" ");

        edt_topic = (EditText) findViewById(R.id.edt_name_topic);
        edt_news_desc = (EditText) findViewById(R.id.edt_desc_news);
        carouselView = (CarouselView) findViewById(R.id.carouse_News);
        image_btn = (Button)findViewById(R.id.SelectImage_New);
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelected(view);
            }
        });
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");

        mAuth = FirebaseAuth.getInstance();

        fileDoneList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileDoneList);
    }

    public void imageSelected(View view){
        FishBun.with(AdminNews.this)
                .setImageAdapter(new PicassoAdapter())
                .setMaxCount(postSetting.num_image)
                .setMinCount(1)
                .startAlbum();
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){//แทยค่าในรูปภาพเก่า

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
        final ArrayList<Uri> dowload = new ArrayList<Uri>(postSetting.num_image);
        final int[] num = {0};
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(id == R.id.action_post){
            final String topicValue = edt_topic.getText().toString().trim();
            final String descValue = edt_news_desc.getText().toString().trim();
            final String time = String.valueOf(System.currentTimeMillis());
            if(!TextUtils.isEmpty(topicValue) && !TextUtils.isEmpty(descValue)) {
                final int[] image1 = {0};
                while (image1[0] < path.size()) {
                    uri = path.get(image1[0]);
                    Log.v("numpp", String.valueOf(path.size()));
                    StorageReference filePath = storageReference.child("PostNewsImage").child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminNews.this, "Error uploading!", Toast.LENGTH_SHORT).show();

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
                            dowload.add(uri);
                            num[0]++;
                            if (num[0] == path.size()) {
                                final DatabaseReference newPost = mDatabase.push();

                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        newPost.child("topicname").setValue(topicValue);
                                        newPost.child("descname").setValue(descValue);
                                        newPost.child("timenews").setValue(time);
                                        for (int i = 0; i < postSetting.num_image; i++) {
                                            if (i < path.size()) {
                                                if (i == 0) {
                                                    newPost.child("imagenews").setValue(dowload.get(i).toString());
                                                } else {
                                                    newPost.child("imagenews" + String.valueOf(i)).setValue(dowload.get(i).toString());
                                                }
                                            } else {
                                                newPost.child("imagenews" + String.valueOf(i)).setValue("-");
                                            }
                                        }
                                        Intent intent = new Intent(AdminNews.this, Main2Activity.class);
                                        startActivity(intent);
                                        finish();
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
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
