package test.jaiboondemand.DonateMain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class EditPost extends AppCompatActivity {
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
    private CarouselView carouse1;
    private Button image_btn;
    private String post_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(" ");

        post_id = getIntent().getExtras().getString("PostID");

        image_btn = (Button) findViewById(R.id.SelectImage);

        carouse1 = (CarouselView) findViewById(R.id.carouse1);

        editName = (EditText) findViewById(R.id.editName);
        editDec = (EditText) findViewById(R.id.editDesc);

        image_btn.setVisibility(View.INVISIBLE);
        carouse1.setVisibility(View.INVISIBLE);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getInstance().getReference().child("Jaiboon");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        fileDoneList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileDoneList);
        databaseReference.child(post_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = (String)dataSnapshot.child("uid").getValue();
                if(mAuth.getCurrentUser().getUid().equals(uid)){
                    String title = (String)dataSnapshot.child("title").getValue();
                    String desc = (String)dataSnapshot.child("desc").getValue();

                    editName.setText(title);
                    editDec.setText(desc);
                }
                else {
                    Toast.makeText(EditPost.this,"คุณไม่มีสิทธิ์แก้ไขโพสต์",Toast.LENGTH_LONG);
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
            final String titleValue = editName.getText().toString().trim();
            final String titleDesc = editDec.getText().toString().trim();
            final String time = String.valueOf(System.currentTimeMillis());


            if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(titleDesc)) {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.child(post_id).child("title").setValue(titleValue);
                        databaseReference.child(post_id).child("desc").setValue(titleDesc);
                        databaseReference.child(post_id).child("timepost").setValue(time);
                        String key = post_id;
                        Intent post = new Intent(EditPost.this, SingleInstaActivity.class);
                        post.putExtra("PostID", key);
                        startActivity(post);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }

        return true;
    }

}
