package test.jaiboondemand.Admin;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import test.jaiboondemand.DonateMain.Main2Activity;
import test.jaiboondemand.R;

public class AdminNews extends AppCompatActivity {
    public static final int GALLERY_REQUEST = 2;
    private EditText edt_topic,edt_news_desc;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private Uri uri = null;
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news);
        edt_topic = (EditText) findViewById(R.id.edt_name_topic);
        edt_news_desc = (EditText) findViewById(R.id.edt_desc_news);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");

        mAuth = FirebaseAuth.getInstance();
    }

    public void imageButtomClicked(View view) {
        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);

        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            imageButton = (ImageButton) findViewById(R.id.imageButton_news);
            imageButton.setImageURI(uri);

        }
    }

    public void PostNews(View view) {
        final String topicValue = edt_topic.getText().toString().trim();
        final String descValue = edt_news_desc.getText().toString().trim();

        if(!TextUtils.isEmpty(topicValue) && !TextUtils.isEmpty(descValue)){
            final StorageReference filePath = storageReference.child("PostNewsImage").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadurl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(AdminNews.this,"Upload Complete",Toast.LENGTH_LONG);
                    final DatabaseReference newPost = mDatabase.push();

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("topicname").setValue(topicValue);
                            newPost.child("descname").setValue(descValue);
                            newPost.child("imagenews").setValue(downloadurl.toString());
                            Intent intent = new Intent(AdminNews.this, Main2Activity.class);
                            startActivity(intent);
                            finish();
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
