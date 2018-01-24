package test.jaiboondemand.Deposit;

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

import test.jaiboondemand.R;
import test.jaiboondemand.post_activity.UploadListAdapter;

public class DepositPostThings extends AppCompatActivity {
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private EditText topic_deposit,desc_deposit,link_deposit;
    private Button select_image;
    private CarouselView carouse1;
    private Uri uri =null;
    private DatabaseReference mDataUser;
    private Depositthingsetting depositthingsetting = new Depositthingsetting();
    private ArrayList<Uri> path;
    private StorageReference storageReference;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_post_things);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_deposit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("ฝากของบริจาค");
        carouse1 = (CarouselView) findViewById(R.id.carouse_deposit);
        select_image = (Button) findViewById(R.id.SelectImage_deposit);
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonClicke(view);
            }
        });

        topic_deposit = (EditText) findViewById(R.id.topic_deposit);
        desc_deposit = (EditText) findViewById(R.id.detail_deposit);
        link_deposit = (EditText) findViewById(R.id.link_facebook_deposit);

        storageReference = FirebaseStorage.getInstance().getReference();
        mData = FirebaseDatabase.getInstance().getReference().child("Deposit");

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        mDataUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        fileDoneList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileDoneList);



    }

    public void imageButtonClicke(View view){
        FishBun.with(DepositPostThings.this)
                .setImageAdapter(new PicassoAdapter())
                .setMaxCount(depositthingsetting.num_image)
                .setMinCount(1)
                .startAlbum();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //preview.onParentResult(requestCode, data);

                    //imageButton.setVisibility(View.INVISIBLE);

                    path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    carouse1.setTransformer(new InverseTimeMachineViewTransformer()); // กำหนดรูปแบบการสไลด์ได้ตามเอกสารใน https://gtomato.github.io/carouselview/
                    carouse1.setAdapter(new Imagedepositsetting(path));
                    //imageButton.setImageURI(path.get(0));
                    //Log.v("Len : ",String.valueOf(path.size()));                   //mediaView.setMedias(medias);
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu_next,menu);
        MenuItem item = menu.findItem(R.id.action_post);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final ArrayList<Uri> download = new ArrayList<Uri>(depositthingsetting.num_image);
        final int[] num = {0};
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(id == R.id.Next) {
              final String topic = topic_deposit.getText().toString().trim();
              final String desc = desc_deposit.getText().toString().trim();
              final String link = link_deposit.getText().toString().trim();

              if(!TextUtils.isEmpty(topic) && !TextUtils.isEmpty(desc)){
                  final int[] image1 = {0};
                  while(image1[0] < path.size()){
                      uri = path.get(image1[0]);
                      StorageReference filePath = storageReference.child("PostDeposit").child(uri.getLastPathSegment());
                      filePath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                          }
                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Toast.makeText(DepositPostThings.this,"Error uploading!",Toast.LENGTH_SHORT).show();

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
                                          if (dataSnapshot.child("Selected").exists()) {
                                              if (dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                                  newPost.child("Foundation_Type").setValue(dataSnapshot.child("Type"));
                                              } else if (!dataSnapshot.child("Selected").getValue().equals("Foundation")) {

                                              }
                                              newPost.child("title").setValue(topic);
                                              newPost.child("desc").setValue(desc);
                                              newPost.child("facebooklink").setValue(link);
                                              newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                              newPost.child("Type").setValue(dataSnapshot.child("Selected").getValue());
                                              newPost.child("Name").setValue(dataSnapshot.child("Name_User").getValue());
                                              newPost.child("username").setValue(dataSnapshot.child("Name_Owner").getValue());
                                              newPost.child("address").setValue(dataSnapshot.child("Address").getValue());
                                              newPost.child("post").setValue(dataSnapshot.child("Post").getValue());
                                              newPost.child("country").setValue(dataSnapshot.child("Country").getValue());
                                              newPost.child("phone").setValue(dataSnapshot.child("Phone").getValue());
                                              for (int i = 0; i < depositthingsetting.num_image; i++) {
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
                                              Intent deposit = new Intent(DepositPostThings.this, AddressPlaceDeposit.class);
                                              deposit.putExtra("Keypost",key);
                                              startActivity(deposit);
                                              finish();

                                          } else if (!dataSnapshot.child("Selected").exists()) {
                                              Intent errorpost = new Intent(DepositPostThings.this,DepositMain.class);
                                              startActivity(errorpost);
                                              finish();
                                              Toast.makeText(DepositPostThings.this, "you not have Info your accounts", Toast.LENGTH_LONG).show();
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
        }
        return super.onOptionsItemSelected(item);
    }
}
