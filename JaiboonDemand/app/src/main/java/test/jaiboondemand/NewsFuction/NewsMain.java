package test.jaiboondemand.NewsFuction;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import test.jaiboondemand.R;

public class NewsMain extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    View x;
    private RecyclerView recyclerView;
    private DatabaseReference mDataAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x = inflater.inflate(R.layout.activity_news_main, container, false);

        recyclerView = (RecyclerView) x.findViewById(R.id.recycle_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.hasFixedSize();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("News");
        mDatabase.keepSynced(true);

        mDataAuth = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<News,NewsViewHolder> adapter = new FirebaseRecyclerAdapter<News, NewsViewHolder>(
                News.class,
                R.layout.row_news,
                NewsViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final NewsViewHolder viewHolder, News model, int position) {
                final String post_key = getRef(position).getKey().toString();
                final int position1 = position;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                    Date date = new Date(Long.valueOf(model.getTimenews()));
                    String currentTime = dateFormat.format(date);

                    viewHolder.setTitleNews(model.getTopicname());
                    viewHolder.setDescTitleNews(currentTime);
                    viewHolder.setImageNews(getActivity().getApplicationContext(), model.getImagenews());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent news = new Intent(getActivity(), NewsDetail.class);
                            news.putExtra("PostNews", post_key);
                            startActivity(news);
                            getActivity().finish();
                        }
                    });

                    viewHolder.setImageInView();

                }catch (Exception e){

                }


            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        recyclerView.setAdapter(adapter);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private  ImageView imageView;
        public NewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setImageInView(){
          imageView = (ImageView) mView.findViewById(R.id.overflow_news);
             imageView.setVisibility(View.INVISIBLE);
        }

        public void setTitleNews(String title){
            TextView titlenews = (TextView) mView.findViewById(R.id.textTitle_news);
            titlenews.setText(title);
        }
        public void setDescTitleNews(String time){
           TextView timenews = (TextView) mView.findViewById(R.id.text_time);
           timenews.setText(time);
        }
        public void setImageNews(final Context ctx,final String image){
           final ImageView image_news = (ImageView) mView.findViewById(R.id.post_image_news);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(image_news, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(image_news);
                }
            });
        }
    }
}