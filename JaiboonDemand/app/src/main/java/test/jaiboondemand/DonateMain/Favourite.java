package test.jaiboondemand.DonateMain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import test.jaiboondemand.R;

/**
 * Created by User on 2/5/2018.
 */

public class Favourite extends AppCompatActivity {
    private RecyclerView mIBstaList;
    private DatabaseReference mDatabase,databaseReference;
    private Query mDatatype;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_social);

        mIBstaList = (RecyclerView) findViewById(R.id.insta_list);
        mIBstaList.setHasFixedSize(true);
        mIBstaList.setLayoutManager(new LinearLayoutManager(Favourite.this));
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatatype = FirebaseDatabase.getInstance().getReference().child("Jaiboon").orderByChild("favorite/"+mAuth.getUid()).equalTo(true);
        mDatatype.keepSynced(true);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Insta,DonateSocial.InstaViewHolde> FBRA = new FirebaseRecyclerAdapter<Insta, DonateSocial.InstaViewHolde>(
                Insta.class,
                R.layout.insta_row,
                DonateSocial.InstaViewHolde.class,
                mDatatype
        ) {
            @Override
            protected void populateViewHolder(final DonateSocial.InstaViewHolde viewHolder, Insta model, int position) {

                final String post_key = getRef(position).getKey().toString();
                try {
                    viewHolder.setCountry(model.getCountry());
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDesc(model.getName());
                    viewHolder.setImage(getApplicationContext(), model.getImage());

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                    Date date = new Date(Long.valueOf(model.getTimepost()));
                    String currentTime = dateFormat.format(date);
                    viewHolder.setTime(currentTime);

                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent singleInstaActivity = new Intent(Favourite.this, SingleInstaActivity.class);
                            singleInstaActivity.putExtra("PostID", post_key);
                            startActivity(singleInstaActivity);
                            finish();
                        }
                    });
                    viewHolder.setImageView();
                }catch (Exception e){}
            }
        };
        mIBstaList.setAdapter(FBRA);
        mAuth.addAuthStateListener(mAuthListener);

    }
    public static class InstaViewHolde extends RecyclerView.ViewHolder{
        View mView;
        private ImageView imageView;
        public InstaViewHolde(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setImageView(){
            imageView = (ImageView) mView.findViewById(R.id.overflow);
            imageView.setVisibility(View.INVISIBLE);
        }
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }
        public void setDesc(String Name){
            TextView post_Name = (TextView) mView.findViewById(R.id.text_Name);
            post_Name.setText("Posted by "+Name);
        }
        public void setTime(String time){
            TextView time_post = (TextView) mView.findViewById(R.id.time_post);
            time_post.setText("เวลาโพส "+time);
        }
        public void setCountry(String country){
            TextView count_text = (TextView) mView.findViewById(R.id.text_province);
            count_text.setText("จังหวัด "+country);
        }
        public void setImage(final Context ctx, final String image){
            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            // Picasso.with(ctx).load(image).into(post_image);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(post_image);
                }
            });
        }

    }

}
