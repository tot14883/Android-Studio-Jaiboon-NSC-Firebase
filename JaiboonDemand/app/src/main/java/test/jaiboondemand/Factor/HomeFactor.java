package test.jaiboondemand.Factor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class HomeFactor extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private Query mData;
    View x;
    private String type_fac = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x = inflater.inflate(R.layout.activity_home_factor, container, false);
        recyclerView = (RecyclerView) x.findViewById(R.id.recycler_factor_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        type_fac = getActivity().getIntent().getExtras().getString("Type");
        if(type_fac.equals("Home")) {
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate");
        }
        else if(type_fac.equals("Education")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("Education");
        }
        else if(type_fac.equals("Poor")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("Poor");
        }
        else if(type_fac.equals("maintain")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("Maintain");
        }
        else if(type_fac.equals("victim")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("victim");
        }
        else if(type_fac.equals("child")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("child");
        }
        else if(type_fac.equals("animal")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("animal");
        }
        else if(type_fac.equals("disadvantaged")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("disadvantaged");
        }
        else if(type_fac.equals("social")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("social");
        }
        else if(type_fac.equals("older")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("older");
        }
        else if(type_fac.equals("disabled")){
            mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate").orderByChild("Type").equalTo("disabled");
        }
        mData.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<factordonate,FactorViewHolder> adapter = new FirebaseRecyclerAdapter<factordonate,FactorViewHolder>(
                factordonate.class,
                R.layout.factor_post,
                FactorViewHolder.class,
                mData
        ) {

            @Override
            protected void populateViewHolder(FactorViewHolder viewHolder, factordonate model, int position) {
                final String post_key = getRef(position).getKey().toString();
                try {
                    viewHolder.setImagepost();


                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                    Date date = new Date(Long.valueOf(model.getFac_time()));
                    String currentTime = dateFormat.format(date);
                    viewHolder.setTime(currentTime);


                    viewHolder.setTitlefactor(model.getTitle_factor());
                    viewHolder.setPosted(model.getPosted_factor());
                    viewHolder.setImageView(getActivity().getApplicationContext(), model.getImage());
                    viewHolder.setCategory(model.getCategory());
                    viewHolder.setPosted(model.getPosted());
                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent factor = new Intent(getActivity(), FactorDetail.class);
                            factor.putExtra("PostID", post_key);
                            startActivity(factor);
                        }
                    });
                }catch (Exception e){}
            }

            };
        recyclerView.setAdapter(adapter);
        mAuth.addAuthStateListener(mAuthListner);

    }
    public static class FactorViewHolder extends RecyclerView.ViewHolder{
        View view;
        private ImageView imageView;
        public FactorViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
        public void setImagepost(){
            imageView = (ImageView) view.findViewById(R.id.overflow_factor);
            imageView.setVisibility(View.INVISIBLE);
        }
        public void setCategory(String category){
            TextView textView = (TextView) view.findViewById(R.id.textcategory_factor);
            textView.setText("ประเภท "+category);
        }
        public void setTitlefactor(String title){
            TextView textView = (TextView) view.findViewById(R.id.title_factor);
            textView.setText("ชื่อหัวข้อ "+title);
        }
        public void setPosted(String posted){
            TextView textView = (TextView) view.findViewById(R.id.factor_postby);
            textView.setText("Posted by "+posted);
        }
        public void setTime(String time){
            TextView Time_post = (TextView) view.findViewById(R.id.fac_time);
            Time_post.setText("เวลาโพส "+time);
        }
        public void setImageView(final Context ctx,final String image){
            final ImageView imageView = (ImageView) view.findViewById(R.id.image_factor);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(imageView);
                }
            });
        }
    }
}
