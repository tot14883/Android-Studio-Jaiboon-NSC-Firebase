package test.jaiboondemand.Factor;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import test.jaiboondemand.R;

public class FactorDetail extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar toolbar;
    private String post_key = null;
    private TextView txt_title,txt_local,txt_desc,txt_address,txt_phone,txt_type,txt_owner,txt_time,txt_acc;
    private factorimage imagefac = new factorimage();
    private SliderLayout mDemoSlider;
    private ArrayList<String> listUrl = new ArrayList<>();
    private CollapsingToolbarLayout mCollapsing;
    private ImageButton face_link;
    private String facebook_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factor_detail);
        mDemoSlider = (SliderLayout) findViewById(R.id.silder_factor);

        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_factor);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        post_key = getIntent().getExtras().getString("PostID");

        face_link = (ImageButton) findViewById(R.id.link_facebook_factor);
        face_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook_link));
                startActivity(browserIntent);
            }
        });

        txt_title = (TextView) findViewById(R.id.Title_factor);
        txt_local = (TextView) findViewById(R.id.localtion_factor);
        txt_desc = (TextView) findViewById(R.id.text_desc);
        txt_address = (TextView) findViewById(R.id.location_address_factor);
        txt_phone = (TextView) findViewById(R.id.factor_phone);
        txt_type = (TextView) findViewById(R.id.factor_type_foundation);
        txt_owner = (TextView) findViewById(R.id.text_owner_foundation);
        txt_time = (TextView) findViewById(R.id.post_time_factor);
        txt_acc = (TextView) findViewById(R.id.text_account);



        mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").child(post_key);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              ArrayList<String> listUrl = new ArrayList<>();
              String post_title,post_desc,post_image,address,post_number,country,phone,type,owner,uid,account,name_local;
              post_title = (String) dataSnapshot.child("title_factor").getValue();
              post_desc = (String) dataSnapshot.child("desc_factor").getValue();
              account = (String) dataSnapshot.child("account").getValue();
              type =(String) dataSnapshot.child("Category").getValue();
              owner = (String) dataSnapshot.child("username").getValue();
              address = (String) dataSnapshot.child("address").getValue();
              post_number = (String) dataSnapshot.child("post").getValue();
              country = (String) dataSnapshot.child("country").getValue();
              phone = (String) dataSnapshot.child("phone").getValue();
              int num_image = 1;
              for(int i = 1;i<imagefac.num_image;i++){
                  if(dataSnapshot.hasChild("image"+String.valueOf(i))) num_image++;
                  else break;
              }
              for(int i = 0;i<num_image;i++){
                  if(i == 0){
                    addimg(dataSnapshot.child("image").getValue().toString());
                  }
                  else if(!dataSnapshot.child("image"+String.valueOf(i)).getValue().toString().equals("-")){
                      addimg(dataSnapshot.child("image"+String.valueOf(i)).getValue().toString());
                  }
                  else{
                      break;
                  }
              }
              name_local = (String) dataSnapshot.child("Name").getValue();
              txt_title.setText(post_title);
              txt_desc.setText(post_desc);
              txt_local.setText(name_local);
              txt_address.setText("ที่อยู่ "+address+" ไปรษณีย์ "+post_number+"\n"+"จังหวัด "+country);
              txt_desc.setText("รายละเอียด"+"\n"+post_desc);
              txt_phone.setText("เบอร์โทร "+phone);
              txt_owner.setText("ชื่อผู้ดูแล "+owner);
              txt_type.setText("ประเภท "+type);
              txt_acc.setText(account);
              addok();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void addimg(String url){
        if(url == null){
        }
        else{
            if(!url.equals("-")){
                listUrl.add(url);
            }
        }
    }
    public void btn_comment_factor(View view){
       Intent intent = new Intent(FactorDetail.this,CommentFactor.class);
       intent.putExtra("PostID",post_key);
       startActivity(intent);
    }
    public void addok(){
        for(int i = 0;i< listUrl.size();i++){
            TextSliderView sliderView = new TextSliderView(this);

            sliderView.image(listUrl.get(i))
                    .setBackgroundColor(Color.BLACK)
                    .setProgressBarVisible(false)
                    .setOnSliderClickListener(this);

            sliderView.bundle(new Bundle());
            mDemoSlider.addSlider(sliderView);

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);
        }
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
