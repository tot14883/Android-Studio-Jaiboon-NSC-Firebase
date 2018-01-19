package test.jaiboondemand.map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import test.jaiboondemand.R;

public class map_content extends AppCompatActivity {
    private String map_local;
    private WebView myWebView;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        map_local = getIntent().getExtras().getString("Local");
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+map_local);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        super.onCreate(savedInstanceState);
        startActivity(mapIntent);

    }
}
