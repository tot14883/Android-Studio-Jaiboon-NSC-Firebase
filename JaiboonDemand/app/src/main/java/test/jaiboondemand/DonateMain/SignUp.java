package test.jaiboondemand.DonateMain;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import test.jaiboondemand.R;

public class SignUp extends Fragment {
    private EditText userName,Email,Password,Con_Pass;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private View x;
    private String chooseDonate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x =  inflater.inflate(R.layout.activity_sign_up,null);
        chooseDonate = getActivity().getIntent().getExtras().getString("ChooseDonate");


        userName = (EditText) x.findViewById(R.id.user_name);
        Email = (EditText) x.findViewById(R.id.email_users);
        Password = (EditText) x.findViewById(R.id.pass_users);
        Con_Pass = (EditText) x.findViewById(R.id.Con_passuser);
        RegisterButtonClicked();

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(getActivity());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        return x;
    }
    public void RegisterButtonClicked(){
        btnRegister = (Button) x.findViewById(R.id.btnsuccess_regis);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String name = userName.getText().toString().trim();
                final String email = Email.getText().toString().trim();
                String pass = Password.getText().toString().trim();
                String conpass = Con_Pass.getText().toString().trim();
                if (Con_Pass.getText().toString().equals(Password.getText().toString())) {
                  if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(conpass)) {
                      mProgress.setMessage("Sign Up ...");
                      mProgress.show();

                      mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String user_email = mAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = mDatabase.child(user_email);
                                    current_user_db.child("Name_User").setValue(name);
                                    current_user_db.child("ID").setValue(email);
                                    mProgress.dismiss();

                                    Toast.makeText(getContext(), "Success !!", Toast.LENGTH_SHORT).show();

                                }
                            }

                        });
                    }
                }else {
                    Toast.makeText(getContext(), "Pass don't match !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
