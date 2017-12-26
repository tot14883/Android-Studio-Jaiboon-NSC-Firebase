package test.jaiboondemand;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import test.jaiboondemand.map.map_content;
import test.jaiboondemand.must_product.NeedProduct;

public class SingleInstaActivity extends AppCompatActivity {
    private  String post_key;
    private DatabaseReference mDatabase;
    private ImageView singlePostImage;
    private TextView singlePostTitle,Local_text,text_Desc;
    private CollapsingToolbarLayout mCollapsing = null;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private MenuItem item;
    private  Toolbar toolbar;
    private Button btn_Donate;
    private ImageButton btn_local;
    private String user_id,Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_insta);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        post_key = getIntent().getExtras().getString("PostID");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon");

        singlePostTitle = (TextView) findViewById(R.id.Title);
        Local_text = (TextView) findViewById(R.id.localtion_text);
        btn_local = (ImageButton) findViewById(R.id.search_local);
        singlePostImage = (ImageView) findViewById(R.id.Image_single);
        btn_Donate = (Button) findViewById(R.id.btn_donate);
        text_Desc = (TextView) findViewById(R.id.text_desc);

        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mAuth = FirebaseAuth.getInstance();

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                user_id = (String) dataSnapshot.child("uid").getValue();
                Name = (String) dataSnapshot.child("Name").getValue();

                singlePostTitle.setText("หัวข้อ "+post_title);
                Local_text.setText(Name);
                text_Desc.setText(post_desc);

                mCollapsing.setTitle(" ");
                Picasso.with(SingleInstaActivity.this).load(post_image).into(singlePostImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void searchlocaltion(View view) {
        Intent map_show = new Intent(SingleInstaActivity.this, map_content.class);
        map_show.putExtra("Local",Name);
        startActivity(map_show);
    }

    public void ShowdonateClicked(View view) {
        Intent needDonate = new Intent(SingleInstaActivity.this, NeedProduct.class);
        startActivity(needDonate);

    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_single_insta,menu);
        item = menu.findItem(R.id.action_delete);
        if(!user_id.equals(mAuth.getUid())){
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
    }*/
}
