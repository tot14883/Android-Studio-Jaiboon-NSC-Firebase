package test.jaiboondemand;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
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

import test.jaiboondemand.map.map_content;
import test.jaiboondemand.must_product.NeedProduct;
import test.jaiboondemand.post_activity.Comment;

public class SingleInstaActivity extends AppCompatActivity {
    private  String post_key;
    private DatabaseReference mDatabase;
    private DatabaseReference mDataUser;
    private DatabaseReference mDatacomment;
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
    private RecyclerView recyclerView;
    private EditText mCommentEditTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_insta);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        post_key = getIntent().getExtras().getString("PostID");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon");
        mDataUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatacomment = FirebaseDatabase.getInstance().getReference().child("Comment");

        singlePostTitle = (TextView) findViewById(R.id.Title);
        Local_text = (TextView) findViewById(R.id.localtion_text);
        btn_local = (ImageButton) findViewById(R.id.search_local);
        singlePostImage = (ImageView) findViewById(R.id.Image_single);
        btn_Donate = (Button) findViewById(R.id.btn_donate);
        text_Desc = (TextView) findViewById(R.id.text_desc);

        recyclerView = (RecyclerView) findViewById(R.id.comment_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(SingleInstaActivity.this));
        recyclerView.hasFixedSize();


        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null ){
                    mCommentEditTextView.setVisibility(View.INVISIBLE);
                    findViewById(R.id.iv_send).setVisibility(View.INVISIBLE);
                    findViewById(R.id.card_comment).setVisibility(View.INVISIBLE);
                }
            }
        };

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
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Comment,CommentHolder> comentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {
                viewHolder.setUsername(model.getUser());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(model.getTimeCreated());
            }
        };
        recyclerView.setAdapter(comentAdapter);

    }

    public void CommentPost(View view) {//คอมเม้น Error

        final String uid = mAuth.getCurrentUser().getUid();
        final String strComment = mCommentEditTextView.getText().toString();
        final DatabaseReference newPost = mDatacomment.push();
        final String comment_key = newPost.push().getKey();
        mDatabase .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child(post_key).child("Comment").child(comment_key).child("comment").setValue(strComment);
                mDatabase.child(post_key).child("Comment").child(comment_key).child("User").setValue(uid);
                mDatabase.child(post_key).child("Comment").child(comment_key).child("timeCreated").setValue(System.currentTimeMillis());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static class CommentHolder extends RecyclerView.ViewHolder{
        TextView usernameTextView;
        TextView timeTextView;
        TextView commentTextView;

        public CommentHolder(View itemView) {
            super(itemView);
            usernameTextView = (TextView) itemView.findViewById(R.id.tv_username);
            timeTextView = (TextView) itemView.findViewById(R.id.tv_time);
            commentTextView = (TextView) itemView.findViewById(R.id.tv_comment);
        }
        public void setUsername(String username){
            usernameTextView.setText(username);
        }
        public void setTime(String time){
            timeTextView.setText(time);
        }
        public void setComment(String comment){
            commentTextView.setText(comment);
        }
    }
}
