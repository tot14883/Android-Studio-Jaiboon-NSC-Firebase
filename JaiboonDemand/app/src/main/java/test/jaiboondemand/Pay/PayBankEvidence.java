package test.jaiboondemand.Pay;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;

import test.jaiboondemand.R;

public class PayBankEvidence extends AppCompatActivity {
    private Button bn_take,bn_ok;
    private String Name,Address,Phone,Text;
    private int SELECT_PICTURE=1;
    private String finalFile;
    private EmailSetting getemail=new EmailSetting();
    static final int REQUEST_IMAGE_CAPTURE = 100;
    File file;
    Uri fileUri;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    final int RC_TAKE_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bank_evidence);
        Name=getIntent().getExtras().getString("Name");
        Address=getIntent().getExtras().getString("Address");
        Phone=getIntent().getExtras().getString("Phone");
        Text=getIntent().getExtras().getString("Text");
        bn_ok=(Button) findViewById(R.id.send_btn);
        bn_take=(Button) findViewById(R.id.takephoto_btn);

        bn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendemail();
            }
        });
        bn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }
    private void sendemail() {
        String email = mAuth.getCurrentUser().getEmail();
        String text="";
        text="คุณ "+Name+"\nที่อยู่ "+Address+"\nเบอร์ "+Phone+"\nได้ซื้อสินค้าดังนี้\n"+Text;
        // Log.v("File5: ",finalFile);
        BackgroundMail send = BackgroundMail.newBuilder(getApplicationContext())
                .withUsername(getemail.Username)
                .withPassword(getemail.Password)
                .withSenderName("JaiboonDemand")
                .withMailTo("wannaphong@kkumail.com")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("แจ้งการชำระเงินผ่านธนาคาร")
                .withBody(text)
                .withAttachments(finalFile)
                .withProcessVisibility(false)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"เรียบร้อย กรุณาตรวจสอบอีเมลของท่าน",Toast.LENGTH_LONG);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==1&&resultCode == RESULT_OK) {
            finalFile = String.valueOf(file);
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return getRealPathFromURI(Uri.parse(path));
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, RC_TAKE_PHOTO);
    }
}
