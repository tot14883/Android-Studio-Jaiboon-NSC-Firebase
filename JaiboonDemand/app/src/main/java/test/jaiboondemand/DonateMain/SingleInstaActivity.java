package test.jaiboondemand.DonateMain;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import test.jaiboondemand.Deposit.Send;
import test.jaiboondemand.R;
import test.jaiboondemand.map.map_content;
import test.jaiboondemand.must_product.NeedProduct;
import test.jaiboondemand.post_activity.CommentActivity;

public class SingleInstaActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private  String post_key;
    private DatabaseReference mDatabase,mDatabase2;
    private DatabaseReference mUser;
    private TextView singlePostTitle,Local_text,local_address,phone_text,type_text,owner_text,text_Desc,text_time;
    private CollapsingToolbarLayout mCollapsing = null;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private MenuItem item;
    private  Toolbar toolbar;
    private Button btn_Donate;
    private String user_id,Name;
    private RecyclerView recyclerView;
    private EditText mCommentEditTextView;
    private String facebook_link;
    private PostSetting postSetting = new PostSetting();
    private SliderLayout mDemoSlider;
    private ArrayList<String> listUrl = new ArrayList<>();
    private  String post_title,post_desc,post_image,address,post,country,phone,type,owner,time,uid;
    private ImageView imageView;
    private MaterialFavoriteButton favourite;
    private String user_id1;
    private DatabaseReference databaseReference;
    private boolean isFavorite=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_insta);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        post_key = getIntent().getExtras().getString("PostID");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon").child(post_key);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Jaiboon").child(post_key).child("favorite");
        mUser = FirebaseDatabase.getInstance().getReference().child("Users");
        favourite = (MaterialFavoriteButton) findViewById(R.id.btn_favorite);
        favourite.setFavorite(false,false);


        singlePostTitle = (TextView) findViewById(R.id.Title);
        Local_text = (TextView) findViewById(R.id.localtion_text);
        btn_Donate = (Button) findViewById(R.id.btn_donate);
        text_Desc = (TextView) findViewById(R.id.text_desc);
        local_address = (TextView) findViewById(R.id.location_address);
        phone_text = (TextView) findViewById(R.id.text_insta_phone);
        type_text = (TextView) findViewById(R.id.text_type_foundation);
        owner_text = (TextView) findViewById(R.id.text_owner_foundation);
        mDemoSlider = findViewById(R.id.slider);
        imageView = (ImageView) findViewById(R.id.link_facebook);
        text_time = (TextView) findViewById(R.id.check_timeout);


        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        mAuth = FirebaseAuth.getInstance();
        user_id1=mAuth.getUid();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser() == null){
                   mDatabase.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           try {
                               ArrayList<String> listUrl = new ArrayList<>();
                               post_title = (String) dataSnapshot.child("title").getValue();
                               post_desc = (String) dataSnapshot.child("desc").getValue();
                               int num_image = 1;
                               for (int i = 1; i < postSetting.num_image; i++) {
                                   if (dataSnapshot.hasChild("image" + String.valueOf(i))) num_image++;
                                   else break;
                               }
                               for (int i = 0; i < num_image; i++) {
                                   if (i == 0) {
                                       addimg(dataSnapshot.child("image").getValue().toString());
                                   } else if (!dataSnapshot.child("image" + String.valueOf(i)).getValue().toString().equals("-")) {
                                       addimg(dataSnapshot.child("image" + String.valueOf(i)).getValue().toString());
                                   } else {
                                       break;
                                   }
                               }
                               address = (String) dataSnapshot.child("address").getValue();
                               post = (String) dataSnapshot.child("post").getValue();
                               country = (String) dataSnapshot.child("country").getValue();
                               phone = (String) dataSnapshot.child("phone").getValue();
                               type = (String) dataSnapshot.child("Type").getValue();
                               owner = (String) dataSnapshot.child("username").getValue();
                               facebook_link = (String) dataSnapshot.child("facebooklink").getValue();
                               time = (String) dataSnapshot.child("timepost").getValue();
                               if (dataSnapshot.child("Type").getValue().equals("Foundation")) {
                                   type_text.setText(type);
                                   owner_text.setText("ชื่อผู้ดูแล " + owner);
                                   type_text.setVisibility(View.VISIBLE);
                                   owner_text.setVisibility(View.VISIBLE);
                               } else if (!dataSnapshot.child("Type").getValue().equals("Foundation")) {
                                   type_text.setVisibility(View.INVISIBLE);
                                   owner_text.setText("ชื่อผู้ดูแล " + owner);
                                   owner_text.setVisibility(View.VISIBLE);
                               }
                               if (dataSnapshot.child("facebooklink").exists()) {
                                   if (dataSnapshot.child("facebooklink").getValue().equals("-")) {
                                       imageView.setVisibility(View.INVISIBLE);
                                   } else if (!dataSnapshot.child("facebooklink").getValue().equals("-")) {
                                       imageView.setVisibility(View.VISIBLE);
                                   }
                               }
                               user_id = (String) dataSnapshot.child("uid").getValue();
                               Name = (String) dataSnapshot.child("Name").getValue();
                               phone_text.setText("เบอร์โทร " + phone);
                               singlePostTitle.setText("หัวข้อ " + post_title);
                               Local_text.setText(Name);
                               text_Desc.setText("รายละเอียด" + "\n" + post_desc);
                               local_address.setText("ที่อยู่ " + address + " ไปรษณีย์ " + post + "\n" + "จังหวัด " + country);

                               SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                               DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                               dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                               Date date = new Date(Long.valueOf(time));
                               String currentTime = dateFormat.format(date);

                               text_time.setText("วันเวลาโพส " + currentTime);
                               addok();
                               mCollapsing.setTitle(" ");
                           }catch (Exception e){

                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
               }
               else if(firebaseAuth.getCurrentUser() != null){
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                ArrayList<String> listUrl = new ArrayList<>();
                                post_title = (String) dataSnapshot.child("title").getValue();
                                post_desc = (String) dataSnapshot.child("desc").getValue();
                                int num_image = 1;
                                for (int i = 1; i < postSetting.num_image; i++) {
                                    if (dataSnapshot.hasChild("image" + String.valueOf(i))) num_image++;
                                    else break;
                                }
                                for (int i = 0; i < num_image; i++) {
                                    if (i == 0) {
                                        addimg(dataSnapshot.child("image").getValue().toString());
                                    } else if (!dataSnapshot.child("image" + String.valueOf(i)).getValue().toString().equals("-")) {
                                        addimg(dataSnapshot.child("image" + String.valueOf(i)).getValue().toString());
                                    } else {
                                        break;
                                    }
                                }

                                if (dataSnapshot.hasChild("favorite")) {
                                    if (dataSnapshot.child("favorite").hasChild(user_id1)) {
                                        if (dataSnapshot.child("favorite").child(user_id1).getValue().equals(true)) {
                                            favourite.setFavorite(true);
                                            isFavorite = true;
                                        } else {
                                            favourite.setFavorite(false);
                                            isFavorite = false;
                                        }
                                    } else {
                                        favourite.setFavorite(false);
                                        isFavorite = false;
                                    }
                                } else {
                                    favourite.setFavorite(false);
                                    isFavorite = false;
                                    DatabaseReference userRef = mDatabase.getRef().child("favorite").child(user_id1);
                                    userRef.setValue(false);
                                }

                                address = (String) dataSnapshot.child("address").getValue();
                                post = (String) dataSnapshot.child("post").getValue();
                                country = (String) dataSnapshot.child("country").getValue();
                                phone = (String) dataSnapshot.child("phone").getValue();
                                type = (String) dataSnapshot.child("Type").getValue();
                                owner = (String) dataSnapshot.child("username").getValue();
                                facebook_link = (String) dataSnapshot.child("facebooklink").getValue();
                                time = (String) dataSnapshot.child("timepost").getValue();
                                if (dataSnapshot.child("Type").getValue().equals("Foundation")) {
                                    type_text.setText(type);
                                    owner_text.setText("ชื่อผู้ดูแล " + owner);
                                    type_text.setVisibility(View.VISIBLE);
                                    owner_text.setVisibility(View.VISIBLE);
                                } else if (!dataSnapshot.child("Type").getValue().equals("Foundation")) {
                                    type_text.setVisibility(View.INVISIBLE);
                                    owner_text.setText("ชื่อผู้ดูแล " + owner);
                                    owner_text.setVisibility(View.VISIBLE);
                                }
                                if (dataSnapshot.child("facebooklink").exists()) {
                                    if (dataSnapshot.child("facebooklink").getValue().equals("-")) {
                                        imageView.setVisibility(View.INVISIBLE);
                                    } else if (!dataSnapshot.child("facebooklink").getValue().equals("-")) {
                                        imageView.setVisibility(View.VISIBLE);
                                    }
                                }
                                user_id = (String) dataSnapshot.child("uid").getValue();
                                Name = (String) dataSnapshot.child("Name").getValue();
                                phone_text.setText("เบอร์โทร " + phone);
                                singlePostTitle.setText("หัวข้อ " + post_title);
                                Local_text.setText(Name);
                                text_Desc.setText("รายละเอียด" + "\n" + post_desc);
                                local_address.setText("ที่อยู่ " + address + " ไปรษณีย์ " + post + "\n" + "จังหวัด " + country);

                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                                Date date = new Date(Long.valueOf(time));
                                String currentTime = dateFormat.format(date);

                                text_time.setText("วันเวลาโพส " + currentTime);
                                addok();
                                mCollapsing.setTitle(" ");
                            }catch (Exception e){

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };



        mCommentEditTextView = (EditText) findViewById(R.id.et_comment);
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favourite.setFavorite(!isFavorite);
                isFavorite = !isFavorite;
                databaseReference = mDatabase2.getRef().child(user_id1);
                databaseReference.setValue(isFavorite);
            }
        });
    }

    public void searchlocaltion(View view) {
        AlertDialog.Builder b = new AlertDialog.Builder(SingleInstaActivity.this);
        String[] types = {"ดูสถานที่", "ส่งของบริจาค"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:
                        Intent map_show = new Intent(SingleInstaActivity.this, map_content.class);
                        map_show.putExtra("Local",Name);
                        startActivity(map_show);
                        break;
                    case 1:
                        Intent send = new Intent(SingleInstaActivity.this, Send.class);
                        send.putExtra("TypeSend","Quick");
                        send.putExtra("Name",owner);
                        send.putExtra("Phone",phone);
                        send.putExtra("Localname",Name);
                        send.putExtra("LocalAddress",address);
                        send.putExtra("LocalPost",post);
                        send.putExtra("LocalCountry",country);
                        startActivity(send);
                        break;

                }
            }

        });

        b.show();
    }

    public void ShowdonateClicked(View view) {
        Intent needDonate = new Intent(SingleInstaActivity.this, NeedProduct.class);
        needDonate.putExtra("PostID",post_key);
        startActivity(needDonate);

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
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    public void btn_comment(View view) {
        Intent intent = new Intent(SingleInstaActivity.this, CommentActivity.class);
        intent.putExtra("PostID",post_key);
        startActivity(intent);
    }

    public void FaceBookLink(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(facebook_link));
        startActivity(browserIntent);

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

    public void HomeGoback(View view) {
        Intent intent = new Intent(SingleInstaActivity.this,Main2Activity.class);
        startActivity(intent);
        finish();
    }
}
