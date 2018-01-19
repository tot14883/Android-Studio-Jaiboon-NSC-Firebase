package test.jaiboondemand;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import test.jaiboondemand.post_activity.ChooseDonate;
import test.jaiboondemand.post_activity.UploadListAdapter;

public class PostActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(" ");

        editName = (EditText) findViewById(R.id.editName);
        editDec = (EditText) findViewById(R.id.editDesc);
        editFacebook = (EditText) findViewById(R.id.Edit_Facebook_link);

        editDate = (EditText) findViewById(R.id.Edit_date_alarm);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    DatePickerFragment fragment = new DatePickerFragment();
                    fragment.show(getSupportFragmentManager(),"date");

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getInstance().getReference().child("Jaiboon");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
       fileDoneList = new ArrayList<>();
       uploadListAdapter = new UploadListAdapter(fileDoneList);

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
            final String facebook_page = editFacebook.getText().toString().trim();

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
                                    if(dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                        newPost.child("Foundation_Type").setValue(dataSnapshot.child("Type"));
                                    }
                                    else if(!dataSnapshot.child("Selected").getValue().equals("Foundation")) {

                                    }
                                    newPost.child("title").setValue(titleValue);
                                    newPost.child("desc").setValue(titleDesc);
                                    newPost.child("facebooklink").setValue(facebook_page);
                                    newPost.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                    newPost.child("Type").setValue(dataSnapshot.child("Selected").getValue());
                                    newPost.child("Name").setValue(dataSnapshot.child("Name_User").getValue());
                                    newPost.child("username").setValue(dataSnapshot.child("Name_Owner").getValue());
                                    newPost.child("address").setValue(dataSnapshot.child("Address").getValue());
                                    newPost.child("post").setValue(dataSnapshot.child("Post").getValue());
                                    newPost.child("country").setValue(dataSnapshot.child("Country").getValue());
                                    newPost.child("phone").setValue(dataSnapshot.child("Phone").getValue());
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        editDate.setText(dateFormat.format(calendar.getTime()));
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
             Calendar cal = new GregorianCalendar(year,month,day);
             setDate(cal);
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }


    }
}
