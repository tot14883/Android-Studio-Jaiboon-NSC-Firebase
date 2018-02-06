package test.jaiboondemand.Factor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
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
import java.util.TimeZone;
import java.util.Date;

import test.jaiboondemand.R;

public class CommentFactor extends AppCompatActivity {
    private String post_key = null;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference mUser;
    private EditText mCommentEditTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_factor);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_comment_factor);
        setSupportActionBar(toolbar);
        setTitle("Comment");

        recyclerView = (RecyclerView) findViewById(R.id.comment_recycleview_factor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        post_key = getIntent().getExtras().getString("PostID");

        mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").child(post_key);
        mUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mCommentEditTextView = (EditText) findViewById(R.id.et_comment_factor);

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              if(firebaseAuth.getCurrentUser() == null){
                  mCommentEditTextView.setVisibility(View.INVISIBLE);
                  findViewById(R.id.iv_send_factor).setVisibility(View.INVISIBLE);
                  findViewById(R.id.card_comment_factor).setVisibility(View.INVISIBLE);
              }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<CommentFac,CommentFactorHolder> adapter = new FirebaseRecyclerAdapter<CommentFac, CommentFactorHolder>(
                CommentFac.class,
                R.layout.row_comment,
                CommentFactorHolder.class,
                mData.child("Comment")
        ) {
            @Override
            protected void populateViewHolder(final CommentFactorHolder viewHolder, CommentFac model, int position) {
                try{
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
                    if(model.getTimeCreated() != null){
                        Date date = new Date(Long.valueOf(model.getTimeCreated()));
                        String currentTime = dateFormat.format(date);
                        viewHolder.setTimeText(currentTime);
                    }
                    viewHolder.setCommenttext(model.getComment());
                }catch (Exception e){
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            }
        };
        recyclerView.setAdapter(adapter);
        mAuth.addAuthStateListener(mAuthListner);
    }
    public static class CommentFactorHolder extends RecyclerView.ViewHolder{
        TextView Username;
        TextView TimeText;
        TextView Commenttext;
        public CommentFactorHolder(View itemView) {
            super(itemView);
            Username = (TextView) itemView.findViewById(R.id.tv_username);
            TimeText = (TextView) itemView.findViewById(R.id.tv_time);
            Commenttext = (TextView) itemView.findViewById(R.id.tv_comment);
        }
        public void setUsername(String username){
            Username.setText(username);
        }
        public void setTimeText(String time){
            TimeText.setText(time);
        }
        public void setCommenttext(String comment){
            Commenttext.setText(comment);
        }
    }
    public void CommentPost(View view) {
        final String uid = mAuth.getCurrentUser().getUid().toString();
        final String strComment = mCommentEditTextView.getText().toString();
        if(!strComment.equals("")){
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
        }else if(strComment.equals("")){}
    }
}
