package test.jaiboondemand.DonateMain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import test.jaiboondemand.R;
import test.jaiboondemand.post_activity.UploadListAdapter;

public class PostActivity extends AppCompatActivity{
    public static final int GALLERY_REQUEST = 2;
    private EditText editName;
    private EditText editDec,editDate,editFacebook;
    private Uri uri = null;
    private StorageReference storageReference;
    private ImageButton imageButton;//อัพหลายรูปแก้เป็น Button ธรรมดา
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private PostSetting postSetting = new PostSetting();
    private ArrayList<Uri> path;
    private CarouselView carouse1;
    private Button image_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(" ");

        image_btn = (Button) findViewById(R.id.SelectImage);
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtomClicked(view);
            }
        });
        carouse1 = (CarouselView) findViewById(R.id.carouse1);

        editName = (EditText) findViewById(R.id.editName);
        editDec = (EditText) findViewById(R.id.editDesc);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getInstance().getReference().child("Jaiboon");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
       fileDoneList = new ArrayList<>();
       uploadListAdapter = new UploadListAdapter(fileDoneList);

    }

    public void imageButtomClicked(View view) {
        FishBun.with(PostActivity.this)
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
                    carouse1.setTransformer(new InverseTimeMachineViewTransformer()); // กำหนดรูปแบบการสไลด์ได้ตามเอกสารใน https://gtomato.github.io/carouselview/
                    carouse1.setAdapter(new ImagePostAdapter(path));
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
        final ArrayList<Uri> download = new ArrayList<Uri>(postSetting.num_image);
        final int[] num = {0};
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(id == R.id.action_post){
            final String titleValue = editName.getText().toString().trim();
            final String titleDesc = editDec.getText().toString().trim();
            final String time = String.valueOf(System.currentTimeMillis());

            if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(titleDesc)) {
                final int[] image1 = {0};
                while (image1[0] < path.size()) {
                    uri = path.get(image1[0]);
                    Log.v("numpp", String.valueOf(path.size()));

                    StorageReference filePath = storageReference.child("PostImage").child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostActivity.this,"Error uploading!",Toast.LENGTH_SHORT).show();

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

                            if(num[0] == path.size()) {
                                final DatabaseReference newPost = databaseReference.push();
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("Selected").exists()) {
                                            if (dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                                newPost.child("Foundation_Type").setValue(dataSnapshot.child("Type").getValue());
                                            } else if (!dataSnapshot.child("Selected").getValue().equals("Foundation")) {

                                            }
                                            newPost.child("title").setValue(titleValue);
                                            newPost.child("desc").setValue(titleDesc);
                                            newPost.child("facebooklink").setValue(dataSnapshot.child("facebooklink").getValue());
                                            newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                            newPost.child("Type").setValue(dataSnapshot.child("Selected").getValue());
                                            newPost.child("Name").setValue(dataSnapshot.child("Name_User").getValue());
                                            newPost.child("username").setValue(dataSnapshot.child("Name_Owner").getValue());
                                            newPost.child("address").setValue(dataSnapshot.child("Address").getValue());
                                            newPost.child("post").setValue(dataSnapshot.child("Post").getValue());
                                            newPost.child("country").setValue(dataSnapshot.child("Country").getValue());
                                            newPost.child("phone").setValue(dataSnapshot.child("Phone").getValue());
                                            newPost.child("timepost").setValue(time);
                                            for (int i = 0; i < postSetting.num_image; i++) {
                                                if (i < path.size()) {
                                                    if (i == 0) {
                                                        newPost.child("image").setValue(download.get(i).toString());
                                                    } else {
                                                        newPost.child("image" + String.valueOf(i)).setValue(download.get(i).toString());
                                                    }

                                                } else {
                                                    newPost.child("image" + String.valueOf(i)).setValue("-");
                                                }
                                            }
                                            String key = newPost.getKey();
                                            Intent ChooseDonate = new Intent(PostActivity.this, test.jaiboondemand.post_activity.ChooseDonate.class);
                                            ChooseDonate.putExtra("Keypost",key);
                                            startActivity(ChooseDonate);
                                            finish();

                                        } else if (!dataSnapshot.child("Selected").exists()) {
                                            Intent errorpost = new Intent(PostActivity.this, Main2Activity.class);
                                            startActivity(errorpost);
                                            finish();
                                            Toast.makeText(PostActivity.this, "you not have Info your accounts", Toast.LENGTH_LONG).show();
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
