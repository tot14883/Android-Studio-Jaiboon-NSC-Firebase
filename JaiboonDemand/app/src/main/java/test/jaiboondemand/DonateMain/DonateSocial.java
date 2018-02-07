package test.jaiboondemand.DonateMain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import test.jaiboondemand.R;

public class DonateSocial extends Fragment {
    private RecyclerView mIBstaList;
    private DatabaseReference mDatabase,databaseReference;
    private Query mDatatype;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View x;
    private SharedPreferences mSharedPreferences;
    private String Province;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x =  inflater.inflate(R.layout.activity_donate_social,container,false);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Province = mSharedPreferences.getString(getString(R.string.setting_Province),"ทั้งหมด");

        mIBstaList = (RecyclerView) x.findViewById(R.id.insta_list);
        mIBstaList.setHasFixedSize(true);
        mIBstaList.setLayoutManager(new LinearLayoutManager(getActivity()));


       databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
       if(Province.equals("ทั้งหมด")) {
           mDatatype = FirebaseDatabase.getInstance().getReference().child("Jaiboon").orderByChild("Type").equalTo("Temple");
       }
       else{
           mDatatype = FirebaseDatabase.getInstance().getReference().child("Jaiboon").orderByChild("Province_Temple").equalTo(Province);
       }
       mDatatype.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
            }
        };



        return x;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Insta,InstaViewHolde> FBRA = new FirebaseRecyclerAdapter<Insta, InstaViewHolde>(
                Insta.class,
                R.layout.insta_row,
                InstaViewHolde.class,
                mDatatype
        ) {
            @Override
            protected void populateViewHolder(final InstaViewHolde viewHolder, Insta model, int position) {
                final String post_key = getRef(position).getKey().toString();
               try {
                   viewHolder.setCountry(model.getCountry());
                   viewHolder.setTitle(model.getTitle());
                   viewHolder.setDesc(model.getName());
                   viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());

                   SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                   DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                   dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                   Date date = new Date(Long.valueOf(model.getTimepost()));
                   String currentTime = dateFormat.format(date);
                   viewHolder.setTime(currentTime);

                   viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent singleInstaActivity = new Intent(getActivity(), SingleInstaActivity.class);
                           singleInstaActivity.putExtra("PostID", post_key);
                           startActivity(singleInstaActivity);
                           getActivity().finish();
                       }
                   });
                   viewHolder.setImageView();
               }catch (Exception e){}
            }
        };
        mIBstaList.setAdapter(FBRA);
        mAuth.addAuthStateListener(mAuthListener);

    }
    public static class InstaViewHolde extends RecyclerView.ViewHolder{
        View mView;
        private ImageView imageView;
        public InstaViewHolde(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setImageView(){
            imageView = (ImageView) mView.findViewById(R.id.overflow);
            imageView.setVisibility(View.INVISIBLE);
        }
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }
        public void setDesc(String Name){
            TextView post_Name = (TextView) mView.findViewById(R.id.text_Name);
            post_Name.setText("Posted by "+Name);
        }
        public void setTime(String time){
            TextView time_post = (TextView) mView.findViewById(R.id.time_post);
            time_post.setText("เวลาโพส "+time);
        }
        public void setCountry(String country){
            TextView count_text = (TextView) mView.findViewById(R.id.text_province);
            count_text.setText("จังหวัด "+country);
        }
        public void setImage(final Context ctx, final String image){
           final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
          // Picasso.with(ctx).load(image).into(post_image);
           Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
               @Override
               public void onSuccess() {

               }

               @Override
               public void onError() {
                   Picasso.with(ctx).load(image).into(post_image);
               }
           });
        }

    }

}

