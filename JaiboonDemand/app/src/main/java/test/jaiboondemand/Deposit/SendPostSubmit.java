package test.jaiboondemand.Deposit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

import test.jaiboondemand.Pay.EmailSetting;
import test.jaiboondemand.R;

public class SendPostSubmit extends AppCompatActivity {
    private String owner,phone,localname,localaddress,localpost,localcountry,post_name,address,post,country,phone_customer;
    private EmailSetting emailSetting = new EmailSetting();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    File file;
    Uri fileUri;
    int storage = 0;
    final int RC_TAKE_PHOTO = 1;
    private String finalFile;
    private String type_post = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post_submit);
        TextView textView = (TextView) findViewById(R.id.local_admin);
        textView.setText("295 หมู่ 7 บ.หนองเดิ่น ต.หนองกอมเกาะ อ.เมือง จ.หนองคาย"+"\n"+"หนองคาย/Nong Khai - เมืองหนองคาย/Mueang Nong Khai - 43000"+"\n"+"Tel.0828662279");
        type_post = getIntent().getExtras().getString("Typesend");
        owner = getIntent().getExtras().getString("Name");
        phone = getIntent().getExtras().getString("Phone");
        localname = getIntent().getExtras().getString("Localname");
        localaddress = getIntent().getExtras().getString("LocalAddress");
        localpost = getIntent().getExtras().getString("LocalPost");
        localcountry = getIntent().getExtras().getString("LocalCountry");
        post_name = getIntent().getExtras().getString("yourName");
        address = getIntent().getExtras().getString("youraddress");
        post = getIntent().getExtras().getString("yourpost");
        country = getIntent().getExtras().getString("yourcountry");
        phone_customer = getIntent().getExtras().getString("yourphone");

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }*/

    public void Button_main_Deposit(View view) {
      sendemail();
    }
    private void sendemail(){
        String email = mAuth.getCurrentUser().getEmail();
        String text = "คุณ "+post_name+"\nที่อยู่ "+address+" ไปรษณีย์ "+post+" จังหวัด "+country+"\nเบอร ์"+phone_customer+
                "\nที่อยู่ผู้จัดส่ง "+ owner+"\nที่อยู่ "+localname+" "+localaddress+" ไปรษณีย์ "+localpost+" จังหวัด "+localcountry
                +"\nเบอร์โทร "+phone;
        BackgroundMail send = BackgroundMail.newBuilder(getApplicationContext())
                .withUsername(emailSetting.Username)
                .withPassword(emailSetting.Password)
                .withSenderName("JaiboonDemand")
                .withMailTo(email)
                .withMailBcc(emailSetting.Username)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("แจ้งการชำระเงินผ่านธนาคา"+email)
                .withBody(text)
                .withAttachments(finalFile)
                .withProcessVisibility(false)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"เรียบร้อย กรุณาตรวจสอบอีเมลของท่าน",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SendPostSubmit.this,DepositMain.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
    }
}
