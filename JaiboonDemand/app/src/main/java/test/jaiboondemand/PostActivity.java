package test.jaiboondemand;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class PostActivity extends AppCompatActivity {
    public static final int GALLERY_REQUEST = 2;
    private Uri uri = null;
    private ImageButton imageButton;
    private EditText editName;
    private EditText editDec;
    private StorageReference storageReference;

    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        editName = (EditText) findViewById(R.id.editName);
        editDec = (EditText) findViewById(R.id.editDesc);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getInstance().getReference().child("Jaiboon");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

    }

    public void imageButtomClicked(View view) {
        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);

        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GALLERY_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){//แทยค่าในรูปภาพเก่า
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            imageButton = (ImageButton) findViewById(R.id.imageButton1);
            imageButton.setImageURI(uri);
        }
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
                               newPost.child("image").setValue(downloadurl.toString());
                               newPost.child("uid").setValue(mCurrentUser.getUid());
                               newPost.child("Type").setValue(dataSnapshot.child("Selected").getValue());
                               newPost.child("Name").setValue(dataSnapshot.child("Name").getValue());
                               newPost.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()) {
                                           Intent mainActivityIntent = new Intent(PostActivity.this, Main2Activity.class);
                                           startActivity(mainActivityIntent);
                                           finish();
                                       }
                                   }
                               });
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
}
