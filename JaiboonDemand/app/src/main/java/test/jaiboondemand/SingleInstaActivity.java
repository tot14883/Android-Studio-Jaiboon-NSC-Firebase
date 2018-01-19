package test.jaiboondemand;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import test.jaiboondemand.map.map_content;
import test.jaiboondemand.must_product.NeedProduct;
import test.jaiboondemand.post_activity.Comment;
import test.jaiboondemand.post_activity.CommentActivity;

public class SingleInstaActivity extends AppCompatActivity {
    private  String post_key;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private ImageView singlePostImage;
    private TextView singlePostTitle,Local_text,local_address,phone_text,type_text,owner_text,text_Desc;
    private CollapsingToolbarLayout mCollapsing = null;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private MenuItem item;
    private  Toolbar toolbar;
    private Button btn_Donate;
    private String user_id,Name;
    private RecyclerView recyclerView;
    private EditText mCommentEditTextView;
    private String facebook_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_insta);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        post_key = getIntent().getExtras().getString("PostID");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon").child(post_key);
        mUser = FirebaseDatabase.getInstance().getReference().child("Users");

        singlePostTitle = (TextView) findViewById(R.id.Title);
        Local_text = (TextView) findViewById(R.id.localtion_text);
        singlePostImage = (ImageView) findViewById(R.id.Image_single);
        btn_Donate = (Button) findViewById(R.id.btn_donate);
        text_Desc = (TextView) findViewById(R.id.text_desc);
        local_address = (TextView) findViewById(R.id.location_address);
        phone_text = (TextView) findViewById(R.id.text_insta_phone);
        type_text = (TextView) findViewById(R.id.text_type_foundation);
        owner_text = (TextView) findViewById(R.id.text_owner_foundation);


        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {

            }
        };

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title,post_desc,post_image,address,post,country,phone,type,owner;
                    post_title = (String) dataSnapshot.child("title").getValue();
                    post_desc = (String) dataSnapshot.child("desc").getValue();
                    post_image = (String) dataSnapshot.child("image").getValue();
                    address = (String) dataSnapshot.child("address").getValue();
                    post = (String) dataSnapshot.child("post").getValue();
                    country = (String) dataSnapshot.child("country").getValue();
                    phone = (String) dataSnapshot.child("phone").getValue();
                    type = (String) dataSnapshot.child("Type").getValue();
                    owner = (String) dataSnapshot.child("username").getValue();
                    facebook_link = (String) dataSnapshot.child("facebooklink").getValue();
                if(dataSnapshot.child("Type").getValue().equals("Foundation")){
                       type_text.setText(type);
                       owner_text.setText(owner);
                    type_text.setVisibility(View.VISIBLE);
                    owner_text.setVisibility(View.VISIBLE);
                }
                else if(!dataSnapshot.child("Type").getValue().equals("Foundation")) {
                     type_text.setVisibility(View.INVISIBLE);
                     owner_text.setVisibility(View.INVISIBLE);
                }
                user_id = (String) dataSnapshot.child("uid").getValue();
                Name = (String) dataSnapshot.child("Name").getValue();
                phone_text.setText("เบอร์โทร "+phone);
                singlePostTitle.setText("หัวข้อ "+post_title);
                Local_text.setText(Name);
                text_Desc.setText("รายละเอียด"+"\n"+post_desc);
                local_address.setText("ที่อยู่ "+address+" ไปรษณีย์ "+post+"\n"+"จังหวัด "+country);

                mCollapsing.setTitle(" ");
                Picasso.with(SingleInstaActivity.this).load(post_image).into(singlePostImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mCommentEditTextView = (EditText) findViewById(R.id.et_comment);
    }

    public void searchlocaltion(View view) {
        Intent map_show = new Intent(SingleInstaActivity.this, map_content.class);
        map_show.putExtra("Local",Name);
        startActivity(map_show);
    }

    public void ShowdonateClicked(View view) {
        Intent needDonate = new Intent(SingleInstaActivity.this, NeedProduct.class);
        needDonate.putExtra("PostID",post_key);
        startActivity(needDonate);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    public void btn_comment(View view) {
        Intent intent = new Intent(SingleInstaActivity.this, CommentActivity.class);
        intent.putExtra("PostID",post_key);
        startActivity(intent);
    }

    public void FaceBookLink(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(facebook_link));
        startActivity(browserIntent);

    }
}
