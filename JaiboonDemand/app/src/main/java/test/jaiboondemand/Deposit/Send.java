package test.jaiboondemand.Deposit;

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

import test.jaiboondemand.R;

public class Send extends AppCompatActivity {
    private EditText topic_text,desc_text;
    private Button add_image;
    private DatabaseReference mUsers;
    private CarouselView carousel_image;
    private Toolbar toolbar;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private Depositthingsetting depositSend = new Depositthingsetting();
    private ArrayList<Uri> path;
    private Uri uri = null;
    private StorageReference storageReference;
    private String typeSend,owner,phone,localname,localaddress,localpost,localcountry;
    private String post_name,address,post,country,phone_customer,Name_Leader,Type_foun,selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
       toolbar = (Toolbar) findViewById(R.id.toolbar_main_send_deposit);
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayShowTitleEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);
       setTitle("ฝากสิ่งของที่ต้องการบริจาค");

       typeSend = getIntent().getExtras().getString("TypeSend");
        owner = getIntent().getExtras().getString("Name");
        phone = getIntent().getExtras().getString("Phone");
        localname = getIntent().getExtras().getString("Localname");
        localaddress = getIntent().getExtras().getString("LocalAddress");
        localpost = getIntent().getExtras().getString("LocalPost");
        localcountry = getIntent().getExtras().getString("LocalCountry");


        add_image = (Button) findViewById(R.id.add_image);
        add_image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FishBun.with(Send.this)
                       .setImageAdapter(new PicassoAdapter())
                       .setMaxCount(depositSend.num_image)
                       .setMinCount(1)
                       .startAlbum();
           }
       });

       topic_text = (EditText) findViewById(R.id.topic_deposit);
       desc_text = (EditText) findViewById(R.id.desc_deposit);

       carousel_image = (CarouselView) findViewById(R.id.carouse_deposit);

       mAuth = FirebaseAuth.getInstance();
       mAuthListner = new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser() != null) {
                   mUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           if (dataSnapshot.child("Selected").exists()) {
                               if(dataSnapshot.child("Selected").getValue().equals("Customer")){
                                   post_name = (String)dataSnapshot.child("Name_Cus").getValue();
                                   address = (String)dataSnapshot.child("Address_Cus").getValue();
                                   post = (String)dataSnapshot.child("Post_Cus").getValue();
                                   country = (String)dataSnapshot.child("Country_Cus").getValue();
                                   phone_customer = (String)dataSnapshot.child("Phone_Cus").getValue();
                                   selected = (String)dataSnapshot.child("Selected").getValue();
                               }
                               if(dataSnapshot.child("Selected").getValue().equals("Temple")){
                                   post_name = (String)dataSnapshot.child("Name_User").getValue();
                                   Name_Leader = (String)dataSnapshot.child("Name_Owner").getValue();
                                   address = (String)dataSnapshot.child("Address").getValue();
                                   post = (String)dataSnapshot.child("Post").getValue();
                                   country = (String)dataSnapshot.child("Country").getValue();
                                   phone_customer = (String)dataSnapshot.child("Phone").getValue();
                                   selected = (String)dataSnapshot.child("Selected").getValue();
                               }
                               if(dataSnapshot.child("Selected").getValue().equals("Foundation")){
                                   post_name = (String)dataSnapshot.child("Name_User").getValue();
                                   Name_Leader = (String)dataSnapshot.child("Name_Owner").getValue();
                                   Type_foun = (String) dataSnapshot.child("Type").getValue();
                                   address = (String)dataSnapshot.child("Address").getValue();
                                   post = (String)dataSnapshot.child("Pos").getValue();
                                   country = (String)dataSnapshot.child("Country").getValue();
                                   phone_customer = (String)dataSnapshot.child("Phone").getValue();
                                   selected = (String)dataSnapshot.child("Selected").getValue();
                               }

                           } else if (!dataSnapshot.child("Selected").exists()) {

                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
               }
           }
       };

       mUsers = FirebaseDatabase.getInstance().getReference().child("Users");
       mData = FirebaseDatabase.getInstance().getReference().child("Deposit");
       storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //preview.onParentResult(requestCode, data);

                    //imageButton.setVisibility(View.INVISIBLE);

                    path = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    carousel_image.setTransformer(new InverseTimeMachineViewTransformer()); // กำหนดรูปแบบการสไลด์ได้ตามเอกสารใน https://gtomato.github.io/carouselview/
                    carousel_image.setAdapter(new Imagedepositsetting(path));
                    //imageButton.setImageURI(path.get(0));
                    //Log.v("Len : ",String.valueOf(path.size()));                   //mediaView.setMedias(medias);
                    break;
                }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        final ArrayList<Uri> download = new ArrayList<Uri>(depositSend.num_image);
        final int[] num = {0};
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(item.getItemId() == R.id.Next){
            final String topic = topic_text.getText().toString().trim();
            final String desc = desc_text.getText().toString().trim();
            if(!TextUtils.isEmpty(topic) && !TextUtils.isEmpty(desc)){
                final int[] image1 = {0};
                while(image1[0] < path.size()){
                    uri = path.get(image1[0]);
                    Log.v("numpicture",String.valueOf(path.size()));
                    StorageReference filepath = storageReference.child("DepositImage").child(uri.getLastPathSegment());
                    filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Send.this,"Error Uploading",Toast.LENGTH_LONG).show();
                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.print("Upload is paused");
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
                              mUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(DataSnapshot dataSnapshot) {
                                         newPost.child("Topic_Deposit").setValue(topic);
                                         newPost.child("Desc_Deposit").setValue(desc);
                                         for (int i = 0; i < depositSend.num_image; i++) {
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
                                     String user = (String) dataSnapshot.child("Name_User").getValue();
                                      newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                      newPost.child("Name_User").setValue(user);
                                      newPost.child("Name_Cus").setValue(post_name);
                                      newPost.child("Address_Cus").setValue(address);
                                      newPost.child("Post_Cus").setValue(post);
                                      newPost.child("Country_Cus").setValue(country);
                                      newPost.child("Phone_Cus").setValue(phone_customer);
                                      newPost.child("Status").setValue("Waiting");
                                      if(dataSnapshot.child("Selected").exists()) {
                                          if (dataSnapshot.child("Selected").getValue().equals("Customer")) {
                                              newPost.child("Selected").setValue(selected);

                                          }
                                          if (dataSnapshot.child("Selected").getValue().equals("Temple")) {
                                              newPost.child("Name_Owner").setValue(Name_Leader);
                                              newPost.child("Selected").setValue(selected);
                                          }
                                          if (dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                              newPost.child("Name_Owner").setValue(Name_Leader);
                                              newPost.child("TypeFoun").setValue(Type_foun);
                                              newPost.child("Selected").setValue(selected);
                                          }
                                      }
                                     if(typeSend.equals("Quick")) {
                                         Intent intent = new Intent(Send.this,AddressAdmin.class);
                                         newPost.child("NameLocal_Deposit").setValue(localname);
                                         newPost.child("Name_Deposit").setValue(owner);
                                         newPost.child("Phone_Deposit").setValue(phone);
                                         newPost.child("AddreeLocal_Deposit").setValue(localaddress);
                                         newPost.child("PostLocal_Deposit").setValue(localpost);
                                         newPost.child("CountryLocal_Deposit").setValue(localcountry);
                                         intent.putExtra("Name",owner);
                                         intent.putExtra("Phone",phone);
                                         intent.putExtra("Localname",localname);
                                         intent.putExtra("LocalAddress",localaddress);
                                         intent.putExtra("LocalPost",localpost);
                                         intent.putExtra("LocalCountry",localcountry);
                                         intent.putExtra("TypeSend","Quick");
                                         startActivity(intent);
                                     }
                                     else if(typeSend.equals("defualt")){
                                          Intent intent = new Intent(Send.this,AddressPlaceDeposit.class);
                                          startActivity(intent);
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
