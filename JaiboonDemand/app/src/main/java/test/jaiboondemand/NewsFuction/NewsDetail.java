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

import java.util.ArrayList;

import test.jaiboondemand.DonateMain.PostSetting;
import test.jaiboondemand.R;

public class NewsDetail extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {
    private String post_key;
    private DatabaseReference mData;
    private TextView title_news,desc_news;
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
        //imageView = (ImageView) findViewById(R.id.Image_single_news);

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
                        addimg(dataSnapshot.child("image"+String.valueOf(i)).getValue().toString());
                    }
                    else{
                        break;
                    }
                }
                String title_news_post = (String) dataSnapshot.child("topicname").getValue();
                String desc_news_post = (String) dataSnapshot.child("descname").getValue();
                String image_news_post =(String) dataSnapshot.child("imagenews").getValue();

                title_news.setText(title_news_post);
                desc_news.setText(desc_news_post);
                addok();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
