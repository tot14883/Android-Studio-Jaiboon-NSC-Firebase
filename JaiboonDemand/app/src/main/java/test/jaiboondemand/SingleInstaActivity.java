package test.jaiboondemand;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleInstaActivity extends AppCompatActivity {
    private  String post_key;
    private DatabaseReference mDatabase;
    private ImageView singlePostImage;
    private TextView singlePostDesc;
    private CollapsingToolbarLayout mCollapsing = null;
    private FirebaseAuth mAuth;
    private MenuItem item;
    private  Toolbar toolbar;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_insta);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        post_key = getIntent().getExtras().getString("PostID");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon");

        singlePostDesc = (TextView) findViewById(R.id.singleDesc);
        singlePostImage = (ImageView) findViewById(R.id.Image_single);

        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mAuth = FirebaseAuth.getInstance();

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                user_id = (String) dataSnapshot.child("uid").getValue();


                mCollapsing.setTitle(post_title);
                singlePostDesc.setText(post_desc);
                Picasso.with(SingleInstaActivity.this).load(post_image).into(singlePostImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_single_insta,menu);
        item = menu.findItem(R.id.action_delete);
        if(user_id.equals(mAuth.getUid())== false){
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_delete){
            mDatabase.child(post_key).removeValue();
            Intent mainIntent = new Intent(this,Main2Activity.class);
            startActivity(mainIntent);
            Toast.makeText(SingleInstaActivity.this,"Delete Success",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
