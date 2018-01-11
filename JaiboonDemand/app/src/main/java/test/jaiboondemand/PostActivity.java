package test.jaiboondemand;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import test.jaiboondemand.post_activity.ChooseDonate;
import test.jaiboondemand.post_activity.UploadListAdapter;

public class PostActivity extends AppCompatActivity {
    public static final int GALLERY_REQUEST = 2;
  //  private static final int RESULT_LOAD_IMAGE = 1;อัพหลายรูป
  //  private Uri fileUri = null;
    private EditText editName;
    private EditText editDec;
    private Uri uri = null;
 //   private int i;
  //  private Uri downloadurl;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        editName = (EditText) findViewById(R.id.editName);
        editDec = (EditText) findViewById(R.id.editDesc);
      //  recyclerView = (RecyclerView) findViewById(R.id.recycle_image_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getInstance().getReference().child("Jaiboon");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
       fileDoneList = new ArrayList<>();
       uploadListAdapter = new UploadListAdapter(fileDoneList);
      /* recyclerView.setLayoutManager(new GridLayoutManager(this,2));
         recyclerView.setHasFixedSize(true);
         recyclerView.setAdapter(uploadListAdapter);*/

    }

    public void imageButtomClicked(View view) {
        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);

        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GALLERY_REQUEST);
     /*   Intent galleryintent = new Intent();
        galleryintent.setType("image/*");
        galleryintent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryintent,"Select Picture"),RESULT_LOAD_IMAGE);
*/
    }

   // @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){//แทยค่าในรูปภาพเก่า
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            imageButton = (ImageButton) findViewById(R.id.imageButton1);
            imageButton.setImageURI(uri);
        }
       /* if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){อัพหลายรูป
            if(data.getClipData() != null) {
                int totalItemSelected = data.getClipData().getItemCount();
                for (i = 0; i < totalItemSelected; i++) {
                    fileUri = data.getClipData().getItemAt(i).getUri();
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();

                    StorageReference filePath = storageReference.child("PostImage").child(fileUri.getLastPathSegment());

                    final int finalI = i;
                    filePath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            uploadListAdapter.notifyDataSetChanged();
                            downloadurl = taskSnapshot.getDownloadUrl();
                        }

                    });
                }

            }

        }*/
    }

    public void submitButtonClicked(View view) {
        final String titleValue = editName.getText().toString().trim();
        final String titleDesc = editDec.getText().toString().trim();

        if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(titleDesc)){
            StorageReference filePath = storageReference.child("PostImage").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadurl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(PostActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                    final DatabaseReference newPost = databaseReference.push();
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Selected").exists()) {
                                newPost.child("title").setValue(titleValue);
                                newPost.child("desc").setValue(titleDesc);
                                newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                newPost.child("Type").setValue(dataSnapshot.child("Selected").getValue());
                                newPost.child("Name").setValue(dataSnapshot.child("Name_Owner").getValue());
                                newPost.child("username").setValue(dataSnapshot.child("Name").getValue());
                                newPost.child("address").setValue(dataSnapshot.child("Address").getValue());
                                newPost.child("post").setValue(dataSnapshot.child("Post").getValue());
                                newPost.child("country").setValue(dataSnapshot.child("Country").getValue());
                                newPost.child("image").setValue(downloadurl.toString());
                                String key = newPost.getKey();
                                Intent ChooseDonate = new Intent(PostActivity.this,ChooseDonate.class);
                                ChooseDonate.putExtra("Keypost",key);
                                startActivity(ChooseDonate);
                                finish();

                            }
                            else if(!dataSnapshot.child("Selected").exists()){
                                Intent errorpost = new Intent(PostActivity.this,Main2Activity.class);
                                startActivity(errorpost);
                                finish();
                                Toast.makeText(PostActivity.this,"you not have Info your accounts",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });
        }
    }
      /*  if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(titleDesc)){
                        Toast.makeText(PostActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                        final DatabaseReference newPost = databaseReference.push();
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("Selected").exists()) {
                                    newPost.child("title").setValue(titleValue);
                                    newPost.child("desc").setValue(titleDesc);
                                    newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                    newPost.child("Type").setValue(dataSnapshot.child("Selected").getValue());
                                    newPost.child("Name").setValue(dataSnapshot.child("Name_User").getValue());
                                    newPost.child("username").setValue(dataSnapshot.child("Name").getValue());
                                    newPost.child("image").setValue(downloadurl.toString());//แก้ตรงนี้
                                    Intent mainActivityIntent = new Intent(PostActivity.this, ChooseDonate.class);
                                    startActivity(mainActivityIntent);
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
    }*/

}
