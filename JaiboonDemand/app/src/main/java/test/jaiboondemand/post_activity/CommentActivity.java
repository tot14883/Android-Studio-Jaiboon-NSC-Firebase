package test.jaiboondemand.post_activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import test.jaiboondemand.R;

public class CommentActivity extends AppCompatActivity {
    private DatabaseReference mData;
    private DatabaseReference mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private RecyclerView recyclerView;
    private EditText mCommentEditTextView;
    private String post_key = null;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_comment);//Command find id Toolbar to our set
        setSupportActionBar(toolbar);
        setTitle("Comment");


        recyclerView = (RecyclerView) findViewById(R.id.comment_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        post_key = getIntent().getExtras().getString("PostID");

        mData = FirebaseDatabase.getInstance().getReference().child("Jaiboon").child(post_key);
        mUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mCommentEditTextView = (EditText) findViewById(R.id.et_comment);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null ){
                    mCommentEditTextView.setVisibility(View.INVISIBLE);
                    findViewById(R.id.iv_send).setVisibility(View.INVISIBLE);
                    findViewById(R.id.card_comment).setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Comment,CommentHolder> comentAdapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.row_comment,
                CommentHolder.class,
                mData.child("Comment")
        ) {
            @Override
            protected void populateViewHolder(final CommentHolder viewHolder, Comment model, int position) {
                String name = model.getUser();
                final String[] name2 = {""};
                mUser.child(name).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name2[0] = dataSnapshot.child("Name_User").getValue().toString();
                        viewHolder.setUsername(name2[0]);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                Date date = new Date(Long.valueOf(model.getTimeCreated()));
                String currentTime = dateFormat.format(date);

                viewHolder.setComment(model.getComment());
                viewHolder.setTime(currentTime);
            }
        };
        recyclerView.setAdapter(comentAdapter);
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void CommentPost(View view) {
        final String uid = mAuth.getCurrentUser().getUid().toString();
        final String strComment = mCommentEditTextView.getText().toString();
        if(!strComment.equals("")) {
            final String time = String.valueOf(System.currentTimeMillis());
            final DatabaseReference mComment = mData.child("Comment");
            final DatabaseReference newPost = mComment.push();
            final String comment_key = newPost.push().getKey();
            mData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mComment.child(comment_key).child("comment").setValue(strComment);
                    mComment.child(comment_key).child("User").setValue(uid);
                    mComment.child(comment_key).child("timeCreated").setValue(time);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mCommentEditTextView.setText("");
        }else if(strComment.equals("")){

        }


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
