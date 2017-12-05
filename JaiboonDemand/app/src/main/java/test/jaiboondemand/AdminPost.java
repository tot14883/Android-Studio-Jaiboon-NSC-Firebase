package test.jaiboondemand;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminPost extends AppCompatActivity {
    private EditText title,count,price;
    private ImageButton imageButton;
    public static final int GALLERY_REQUEST = 2;
    private Uri uri = null;
    private StorageReference storageReference;
    private Spinner spinner;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post);

        title = (EditText) findViewById(R.id.editName_Shop);
        count = (EditText) findViewById(R.id.editDesc_Shop);
        price = (EditText) findViewById(R.id.edit_price);
        spinner = (Spinner) findViewById(R.id.Spinner_type_shop);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AdminPost.this, R.array.Type_Shop, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = database.getInstance().getReference().child("ShopJaiboon");
    }
    public void imageButtomPosted(View view) {
        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);

        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,GALLERY_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){//แทยค่าในรูปภาพเก่า
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            imageButton = (ImageButton) findViewById(R.id.imageButton_shop);
            imageButton.setImageURI(uri);
        }
    }

    public void submitButtonPosted(View view) {
        final String titleValue = title.getText().toString().trim();
        final String countValue = count.getText().toString().trim();
        final String priceValue = price.getText().toString().trim();
        final String typeValue = spinner.getSelectedItem().toString().trim();
        if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(countValue)){
            StorageReference filePath = storageReference.child("PostImageShop").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(AdminPost.this, "Upload Complete", Toast.LENGTH_LONG).show();
                    DatabaseReference newPost = databaseReference.push();

                    newPost.child("nameproduct").setValue(titleValue);
                    newPost.child("desproduct").setValue(countValue);
                    newPost.child("priceproduct").setValue(priceValue);
                    newPost.child("imageproduct").setValue(downloadurl.toString());
                    newPost.child("type").setValue(typeValue);
                }
            });

            Intent intent = new Intent(AdminPost.this,Main2Activity.class);
            startActivity(intent);

        }
    }


    public void exitButton(View view) {
        Intent intent = new Intent(AdminPost.this,Main2Activity.class);
        startActivity(intent);
    }
}
