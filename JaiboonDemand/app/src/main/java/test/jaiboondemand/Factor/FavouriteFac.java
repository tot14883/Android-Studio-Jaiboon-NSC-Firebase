package test.jaiboondemand.Factor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
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
 * Created by User on 2/6/2018.
 */

public class FavouriteFac extends AppCompatActivity{
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private Query mData;
    View x;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourtie);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);//Command find id Toolbar to our set
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_favourite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FavouriteFac.this));
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("favorite/"+mAuth.getUid()).equalTo(true);
        mData.keepSynced(true);
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<factordonate,HomeFactor.FactorViewHolder> adapter = new FirebaseRecyclerAdapter<factordonate,HomeFactor.FactorViewHolder>(
                factordonate.class,
                R.layout.factor_post,
                HomeFactor.FactorViewHolder.class,
                mData
        ) {

            @Override
            protected void populateViewHolder(HomeFactor.FactorViewHolder viewHolder, factordonate model, int position) {
                final String post_key = getRef(position).getKey().toString();
                try {
                    viewHolder.setImagepost();


                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                    Date date = new Date(Long.valueOf(model.getFac_time()));
                    String currentTime = dateFormat.format(date);
                    viewHolder.setTime(currentTime);


                    viewHolder.setTitlefactor(model.getTitle_factor());
                    viewHolder.setPosted(model.getPosted_factor());
                    viewHolder.setImageView(getApplicationContext(), model.getImage());
                    viewHolder.setCategory(model.getCategory());
                    viewHolder.setPosted(model.getPosted());
                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent factor = new Intent(FavouriteFac.this, FactorDetail.class);
                            factor.putExtra("PostID", post_key);
                            startActivity(factor);
                        }
                    });
                }catch (Exception e){}
            }

        };
        recyclerView.setAdapter(adapter);
        mAuth.addAuthStateListener(mAuthListner);

    }
    public static class FactorViewHolder extends RecyclerView.ViewHolder{
        View view;
        private ImageView imageView;
        public FactorViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
        public void setImagepost(){
            imageView = (ImageView) view.findViewById(R.id.overflow_factor);
            imageView.setVisibility(View.INVISIBLE);
        }
        public void setCategory(String category){
            TextView textView = (TextView) view.findViewById(R.id.textcategory_factor);
            textView.setText("ประเภท "+category);
        }
        public void setTitlefactor(String title){
            TextView textView = (TextView) view.findViewById(R.id.title_factor);
            textView.setText("ชื่อหัวข้อ "+title);
        }
        public void setPosted(String posted){
            TextView textView = (TextView) view.findViewById(R.id.factor_postby);
            textView.setText("Posted by "+posted);
        }
        public void setTime(String time){
            TextView Time_post = (TextView) view.findViewById(R.id.fac_time);
            Time_post.setText("เวลาโพส "+time);
        }
        public void setImageView(final Context ctx,final String image){
            final ImageView imageView = (ImageView) view.findViewById(R.id.image_factor);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(imageView);
                }
            });
        }
    }
}
