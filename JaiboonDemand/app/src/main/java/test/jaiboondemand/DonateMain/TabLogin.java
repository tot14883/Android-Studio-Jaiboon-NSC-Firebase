package test.jaiboondemand.DonateMain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import test.jaiboondemand.Choose.ChooseDonate;
import test.jaiboondemand.Deposit.DepositMain;
import test.jaiboondemand.Factor.FactorMain;
import test.jaiboondemand.NewsFuction.NewsStart;
import test.jaiboondemand.R;

public class TabLogin extends Fragment {
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG ="LoginActivity";
    private View x;
    private TextView forgetAc;
    private EditText emailUsers;
    private EditText passUsers;
    private Button btnSignin;
    private LoginButton fb_Loign;
    private SignInButton google_Login;
    private ProgressDialog mProgress;
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private DatabaseReference mDBusers;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  String chooseDonate;
    private Button fb_btn;
    private Button gg_btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        x = inflater.inflate(R.layout.activity_tab_login,container, false);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        chooseDonate = getActivity().getIntent().getExtras().getString("ChooseDonate");
        fb_btn = (Button) x.findViewById(R.id.facebookView);
        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(view.getId() == R.id.facebookView){
                  fb_Loign.performClick();
               }
            }
        });
        gg_btn = (Button) x.findViewById(R.id.googleview);
        gg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.googleview){
                    mProgress.setMessage("Sign Up ...");
                    mProgress.show();
                    signIn();
                }
            }
        });

        mProgress = new ProgressDialog(getActivity());

        callbackManager = CallbackManager.Factory.create();
        fb_Loign= (LoginButton) x.findViewById(R.id.login_button);
        fb_Loign.setFragment(this);
        fb_Loign.setReadPermissions("email","public_profile");
        fb_Loign.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mProgress.setMessage("Sign Up ...");
                mProgress.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.d("","facebook:onSuccess"+loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("TAG","facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG","facebook:onError",error);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               FirebaseUser user = firebaseAuth.getCurrentUser();
               if(user != null){
                   Log.d("","onAuthSataeChanged:signed_in"+user.getUid());
                   if(chooseDonate.equals("Jaiboon")) {
                       Intent intent = new Intent(getActivity(), Main2Activity.class);
                       startActivity(intent);
                       getActivity().finish();
                   }
                   if(chooseDonate.equals("News")){
                       Intent intent = new Intent(getActivity(), NewsStart.class);
                       startActivity(intent);
                       getActivity().finish();
                   }
                   if(chooseDonate.equals("Main")){
                       Intent intent = new Intent(getActivity(), ChooseDonate.class);
                       startActivity(intent);
                       getActivity().finish();
                   }
                   if(chooseDonate.equals("Deposit")){
                       Intent intent = new Intent(getActivity(), DepositMain.class);
                       startActivity(intent);
                       getActivity().finish();
                   }
                   if(chooseDonate.equals("Factor")){
                       Intent intent = new Intent(getActivity(), FactorMain.class);
                       startActivity(intent);
                       getActivity().finish();
                   }


               }else{
                   Log.d("TG","SIGNED OUT");

               }
            }
        };

        LoginUser();
        forgetAc = (TextView) x.findViewById(R.id.text_reset);
        forgetAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Forget_Account.class);
                startActivity(intent);
            }
        });

        google_Login = (SignInButton) x.findViewById(R.id.btn_sign_in);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                         Toast.makeText(getActivity(),"You Got an Error",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        google_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setMessage("Sign Up ...");
                mProgress.show();
                signIn();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mDBusers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDBusers.keepSynced(true);

        emailUsers.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        emailUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    emailUsers.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    emailUsers.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail_outline, 0, 0, 0);

                }

            }
        });
        passUsers.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        passUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    passUsers.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    passUsers.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline, 0, 0, 0);
                }
            }
        });
        TextView reset = (TextView) x.findViewById(R.id.text_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),ResetPassword.class);
                getActivity().finish();
                startActivity(i);
            }
        });
        return x;
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void LoginUser(){
        emailUsers = (EditText) x.findViewById(R.id.et_MailLog);
        passUsers = (EditText) x.findViewById(R.id.et_PassLog);

        btnSignin = (Button) x.findViewById(R.id.btn_Login);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setMessage("Sign Up.....");
                mProgress.show();
                String email = emailUsers.getText().toString().trim();
                String Pass = passUsers.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(Pass)) {
                        mAuth.signInWithEmailAndPassword(email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    checkUserExists();
                                } else {
                                    Toast.makeText(getActivity(), "Email or PassWord Incorrect !!!!", Toast.LENGTH_LONG).show();
                                    mProgress.dismiss();
                                }
                            }
                        });
                    } else {
                    }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mProgress != null && mProgress.isShowing()){
            mProgress.cancel();
        }
    }

    private void handleFacebookAccessToken(AccessToken token){
        Log.d("","handleFacebookAccessToken"+token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("","signIWithCredential:onComplete:"+task.isSuccessful());
                final String user_id = mAuth.getCurrentUser().getUid();
                String name = mAuth.getCurrentUser().getDisplayName();
                DatabaseReference current_user_do = mDBusers.child(user_id);
                current_user_do.child("Name_User").setValue(name);
                current_user_do.child("ID").setValue(mAuth.getCurrentUser().getEmail());
                mProgress.dismiss();
                if(!task.isSuccessful()){
                    Log.w("","signInWithCredentail",task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final String user_id = mAuth.getCurrentUser().getUid();
                            String name = mAuth.getCurrentUser().getDisplayName();
                            DatabaseReference current_user_do = mDBusers.child(user_id);
                            current_user_do.child("Name_User").setValue(name);
                            current_user_do.child("ID").setValue(mAuth.getCurrentUser().getEmail());
                            mProgress.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
                });
    }

    public void checkUserExists(){
        mDBusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())){
                    if(chooseDonate.equals("Jaiboon")) {
                        Intent loginIntent = new Intent(getActivity(),Main2Activity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().finish();
                        startActivity(loginIntent);
                        mProgress.dismiss();
                    }
                    if(chooseDonate.equals("News")){
                        Intent loginIntent = new Intent(getActivity(),NewsStart.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().finish();
                        startActivity(loginIntent);
                        mProgress.dismiss();
                    }
                    if(chooseDonate.equals("Main")){
                        Intent loginIntent = new Intent(getActivity(),ChooseDonate.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().finish();
                        startActivity(loginIntent);
                        mProgress.dismiss();
                    }
                    if(chooseDonate.equals("Deposit")){
                        Intent intent = new Intent(getActivity(), DepositMain.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    if(chooseDonate.equals("Factor")){
                        Intent intent = new Intent(getActivity(), FactorMain.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
