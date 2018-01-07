package test.jaiboondemand.News;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import test.jaiboondemand.R;

public class NewsDetail extends AppCompatActivity {
    private String post_key;
    private DatabaseReference mData;
    private TextView title_news,desc_news;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_news);
        setSupportActionBar(toolbar);

        post_key = getIntent().getExtras().getString("PostNews");

        title_news = (TextView) findViewById(R.id.news_title);
        desc_news = (TextView) findViewById(R.id.news_desc);
        imageView = (ImageView) findViewById(R.id.Image_single_news);

        mData = FirebaseDatabase.getInstance().getReference().child("News");
        mData.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title_news_post = (String) dataSnapshot.child("topicname").getValue();
                String desc_news_post = (String) dataSnapshot.child("descname").getValue();
                String image_news_post =(String) dataSnapshot.child("imagenews").getValue();

                title_news.setText(title_news_post);
                desc_news.setText(desc_news_post);
                Picasso.with(NewsDetail.this).load(image_news_post).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        CollapsingToolbarLayout mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_news);
        mCollapsing.setTitle(" ");
    }
}
