package test.jaiboondemand.Pay;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by User on 1/10/2018.
 */

public class Sendemail {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String email;
    public Sendemail(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("SendDonate");
        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
    }
    public boolean send(String Subject,String text){
        return true;
    }
}
