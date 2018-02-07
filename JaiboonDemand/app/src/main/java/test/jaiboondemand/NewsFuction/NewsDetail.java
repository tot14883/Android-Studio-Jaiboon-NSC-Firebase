package test.jaiboondemand.NewsFuction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import test.jaiboondemand.DonateMain.PostSetting;
import test.jaiboondemand.R;

public class NewsDetail extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {
    private String post_key;
    private DatabaseReference mData;
    private TextView title_news,desc_news,time_news;
    private ImageView imageView;
    private PostSetting postSetting = new PostSetting();
    private SliderLayout mDemoSlider;
    private ArrayList<String> listUrl = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        CollapsingToolbarLayout mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_news);
        mCollapsing.setTitle(" ");
        mDemoSlider = (SliderLayout) findViewById(R.id.slider_News);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        post_key = getIntent().getExtras().getString("PostNews");

        title_news = (TextView) findViewById(R.id.news_title);
        desc_news = (TextView) findViewById(R.id.news_desc);
        time_news = (TextView) findViewById(R.id.time_post_news);

        mData = FirebaseDatabase.getInstance().getReference().child("News");
        mData.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> listUrl = new ArrayList<>();
                int num_image = 1;
                for(int i = 1;i <postSetting.num_image;i++){
                    if(dataSnapshot.hasChild("imagenews"+String.valueOf(i))) num_image++;
                    else break;
                }
                for(int i = 0;i<num_image;i++){
                    if(i ==0){
                        addimg(dataSnapshot.child("imagenews").getValue().toString());
                    }
                    else if(!dataSnapshot.child("imagenews"+String.valueOf(i)).getValue().toString().equals("-")){
                        addimg(dataSnapshot.child("imagenews"+String.valueOf(i)).getValue().toString());
                    }
                    else{
                        break;
                    }
                }
                String title_news_post = (String) dataSnapshot.child("topicname").getValue();
                String desc_news_post = (String) dataSnapshot.child("descname").getValue();
                String time_news_post = (String) dataSnapshot.child("timenews").getValue();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                Date date = new Date(Long.valueOf(time_news_post));
                String currentTime = dateFormat.format(date);
                time_news.setText(currentTime);
                title_news.setText(title_news_post);
                desc_news.setText(desc_news_post);
                addok();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void HomeBackNews(View view){
        Intent intent = new Intent(NewsDetail.this,NewsStart.class);
        startActivity(intent);
        finish();
    }
    public void CommentNews(View view){
        Intent intent = new Intent(NewsDetail.this,CommentNews.class);
        intent.putExtra("PostID",post_key);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void addimg(String url){
        if(url == null){}
        else{
            if(!url.equals("-")){
                listUrl.add(url);
            }
        }
    }
    public void addok(){
        for (int i = 0; i < listUrl.size(); i++) {
            TextSliderView sliderView = new TextSliderView(this);
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    //.description(listName.get(i))
                    //.setRequestOption(requestOptions)
                    .setBackgroundColor(Color.BLACK)
                    .setProgressBarVisible(false)
                    .setOnSliderClickListener(this);

            //add your extra information
            sliderView.bundle(new Bundle());
            //sliderView.getBundle().putString("extra", listName.get(i));
            mDemoSlider.addSlider(sliderView);
        }

        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }
    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

}
