package test.jaiboondemand.Pay;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import test.jaiboondemand.BuildConfig;
import test.jaiboondemand.R;

public class PayBankEvidence extends AppCompatActivity {
    private Button bn_take,bn_ok;
    private String Name,Address,Phone,Text,Name2,Address2,Phone2;
    private int SELECT_PICTURE=1;
    private String finalFile;
    private EmailSetting getemail=new EmailSetting();
    File file;
    Uri fileUri;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    final int RC_TAKE_PHOTO = 1;
    int storage = 0;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bank_evidence);
        Name=getIntent().getExtras().getString("Name");
        Address=getIntent().getExtras().getString("Address");
        Phone=getIntent().getExtras().getString("Phone");
        Text=getIntent().getExtras().getString("Text");
        Name2 = getIntent().getExtras().getString("Name2");
        Address2 = getIntent().getExtras().getString("Address2");
        Phone2 = getIntent().getExtras().getString("Phone2");

        bn_ok=(Button) findViewById(R.id.send_btn);
        bn_take=(Button) findViewById(R.id.takephoto_btn);
        imageView = (ImageView) findViewById(R.id.take_photo_evidence);

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
        text="คุณ "+Name+"\nที่อยู่ "+Address+"\nเบอร์ "+Phone+"\n\nที่อยู่จัดส่ง"+Name2+"\n"+Address2+"\nเบอร์"+Phone2+"\n\nได้ซื้อสินค้าดังนี้\n"+Text;
        BackgroundMail send = BackgroundMail.newBuilder(getApplicationContext())
                .withUsername(getemail.Username)
                .withPassword(getemail.Password)
                .withSenderName("JaiboonDemand")
                .withMailTo(email)
                .withMailBcc(getemail.Username)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("แจ้งการชำระเงินผ่านธนาคาร"+email)
                .withBody(text)
                .withAttachments(finalFile)
                .withProcessVisibility(false)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"เรียบร้อย กรุณาตรวจสอบอีเมลของท่าน",Toast.LENGTH_LONG).show();
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
            if(storage ==1) {
                finalFile = String.valueOf(file);
                Picasso.with(getApplicationContext()).load(fileUri).into(imageView);
            }
            else{
                Uri image = data.getData();
                finalFile = getPath(this,image);
                Picasso.with(getApplicationContext()).load(image).into(imageView);
            }
        }

    }
    public String getPath(final Context context,final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if(isKitKat && DocumentsContract.isDocumentUri(context,uri)){
            if(isExternalStorageDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split =docId.split(":");
                final String type = split[0];

                if("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory()+"/"+split[1];
                }
            }
            else if(isDownloadsDocument(uri)){
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId( Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);

            }
            else if(isMediaDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if("image".equals(type)){
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if("video".equals(type)){
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if("audio".equals(type)){
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{ split[1]};

                return  getDataColumn(context,contentUri,selection,selectionArgs);
            }

        }
        else  if("content".equalsIgnoreCase(uri.getScheme())){
            if(isGooglePhotosUri(uri))
                return  uri.getLastPathSegment();

            return getDataColumn(context,uri,null,null);
        }
        else if("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;

    }
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
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
    public void take(){
        storage =1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        //fileUri = Uri.fromFile(file);
        fileUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }
    private void takePhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PayBankEvidence.this);
        builder.setTitle("แนปสลิป");
        builder.setCancelable(true);
        builder.setMessage("เลือกช่องทาง ?");
        builder.setPositiveButton("กล้อง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                take();
            }
        });
        builder.setNegativeButton("รูปภาพ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                storage = 2;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),RC_TAKE_PHOTO);
            }
        });
        builder.show();

    }
}
