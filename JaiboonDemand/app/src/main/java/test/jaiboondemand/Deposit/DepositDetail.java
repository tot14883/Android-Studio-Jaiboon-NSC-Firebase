package test.jaiboondemand.Deposit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

import test.jaiboondemand.R;

public class DepositDetail extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{
    private String post_key = null;
    private DatabaseReference mData;
    private TextView txt_title,txt_status,txt_desc;
    private TextView yourname,youraddress,yourphone;
    private TextView sendname,sendaddress,sendphone;
    private String owner,phone,localname,localaddress,localpost,localcountry;
    private String post_name,address,post,country,phone_customer;
    private CollapsingToolbarLayout mCollapsing;

    private SliderLayout mSliderLayout;
    private Depositthingsetting imagedeposit = new Depositthingsetting();
    private ArrayList<String> listUrl = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_detail);

        mCollapsing = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_deposit);
        mSliderLayout = (SliderLayout) findViewById(R.id.slider_deposit);

        post_key = getIntent().getExtras().getString("PostID");

        mData = FirebaseDatabase.getInstance().getReference().child("Deposit").child(post_key);

        txt_title = (TextView) findViewById(R.id.title_deposit_detail);
        txt_desc = (TextView) findViewById(R.id.desc_deposit_detail);
        txt_status = (TextView) findViewById(R.id.status_deposit_detail);


        yourname = (TextView)  findViewById(R.id.you_send_deposit_detail);
        youraddress = (TextView) findViewById(R.id.you_address_deposit_detail);
        yourphone = (TextView) findViewById(R.id.you_phone_deposit_detail);
        sendname = (TextView) findViewById(R.id.name_send_deposit_detail);
        sendaddress = (TextView) findViewById(R.id.address_send_deposit_detail);
        sendphone = (TextView) findViewById(R.id.phone_send_deposit_detail);

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    ArrayList<String> listUrl = new ArrayList<>();
                    int num_image = 1;
                    for (int i = 1; i < imagedeposit.num_image; i++) {
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
                }catch (Exception e){

                }
                String title = (String) dataSnapshot.child("Topic_Deposit").getValue();
                String desc = (String) dataSnapshot.child("Desc_Deposit").getValue();
                String status = (String ) dataSnapshot.child("Status").getValue();
                post_name = (String)dataSnapshot.child("Name_Cus").getValue();
                address = (String)dataSnapshot.child("Address_Cus").getValue();
                post = (String)dataSnapshot.child("Post_Cus").getValue();
                country = (String)dataSnapshot.child("Country_Cus").getValue();
                phone_customer = (String)dataSnapshot.child("Phone_Cus").getValue();
                owner = (String) dataSnapshot.child("Name_Deposit").getValue();
                localaddress = (String) dataSnapshot.child("AddreeLocal_Deposit").getValue();
                localpost = (String) dataSnapshot.child("PostLocal_Deposit").getValue();
                localcountry = (String) dataSnapshot.child("CountryLocal_Deposit").getValue();
                phone = (String) dataSnapshot.child("Phone_Deposit").getValue();

                txt_title.setText(title);
                txt_desc.setText("รายละเอียดการบริจาค "+"\n"+desc);
                txt_status.setText("สถานะ:"+status);
               yourname.setText(post_name);
               youraddress.setText("ที่อยู่ "+address+" รหัสไปรษณีย์ "+post+"\n"+"จังหวัด "+country);
               yourphone.setText("เบอร์โทร "+phone_customer);
                sendname.setText(owner);
                sendaddress.setText("\n" + "ที่อยู่ " + localaddress + " รหัสไปรษญีย์ " + localpost + "\n" + "จังหวัด" + localcountry);
                sendphone.setText(phone);
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
    public void addimg(String url){
        if(url == null){}
        else{
            if(!url.equals("-")){
                listUrl.add(url);
            }
        }
    }
    public void addok(){
        for(int i = 0;i < listUrl.size();i++){
            TextSliderView sliderView = new TextSliderView(this);
            sliderView.image(listUrl.get(i))
                    .setBackgroundColor(Color.BLACK)
                    .setProgressBarVisible(false)
                    .setOnSliderClickListener(this);

            sliderView.bundle(new Bundle());

            mSliderLayout.addSlider(sliderView);
            mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);

            mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mSliderLayout.setCustomAnimation(new DescriptionAnimation());
            mSliderLayout.setDuration(4000);
            mSliderLayout.addOnPageChangeListener(this);
        }

    }

    public void Bank_Payment(View view){
        Intent intent = new Intent(DepositDetail.this,BankSubmit.class);
        intent.putExtra("Name",owner);
        intent.putExtra("Phone",phone);
        intent.putExtra("LocalAddress",localaddress);
        intent.putExtra("LocalPost",localpost);
        intent.putExtra("LocalCountry",localcountry);
        intent.putExtra("yourName",post_name);
        intent.putExtra("youraddress",address);
        intent.putExtra("yourpost",post);
        intent.putExtra("yourcountry",country);
        intent.putExtra("yourphone",phone_customer);
        intent.putExtra("Typesend","Quick");
        startActivity(intent);

    }
    public void Send_Payment(View view){
        Intent intent = new Intent(DepositDetail.this,SendPostSubmit.class);
        intent.putExtra("Name",owner);
        intent.putExtra("Phone",phone);
        intent.putExtra("LocalAddress",localaddress);
        intent.putExtra("LocalPost",localpost);
        intent.putExtra("LocalCountry",localcountry);
        intent.putExtra("yourName",post_name);
        intent.putExtra("youraddress",address);
        intent.putExtra("yourpost",post);
        intent.putExtra("yourcountry",country);
        intent.putExtra("yourphone",phone_customer);
        intent.putExtra("Typesend","defualt");
        startActivity(intent);
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
