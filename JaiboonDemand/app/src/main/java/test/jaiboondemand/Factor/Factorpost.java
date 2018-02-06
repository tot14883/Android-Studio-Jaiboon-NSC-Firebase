package test.jaiboondemand.Factor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import test.jaiboondemand.R;

public class Factorpost extends Fragment {
    private View x;
    private RecyclerView recyclerView;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FloatingActionButton floatingActionButton;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x = inflater.inflate(R.layout.activity_factorpost, container, false);
        recyclerView = (RecyclerView) x.findViewById(R.id.recycler_post_factor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        floatingActionButton = (FloatingActionButton) x.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(getActivity(),postFactor.class);
                 intent.putExtra("Type","Home");
                 startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        mData = FirebaseDatabase.getInstance().getReference().child("FactorDonate");
        mData.keepSynced(true);
        return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<factordonate,FactorPostViewHolder> adapter = new FirebaseRecyclerAdapter<factordonate, FactorPostViewHolder>(
                factordonate.class,
                R.layout.factor_post,
                FactorPostViewHolder.class,
                mData.orderByChild("uid").equalTo(mAuth.getUid())
        ) {
            @Override
            protected void populateViewHolder(final FactorPostViewHolder viewHolder, factordonate model, int position) {
                final String post_key = getRef(position).getKey().toString();
                final int position1 = position;
               try {

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

                   viewHolder.setImageOverflow();
                   viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           PopupMenu popup = new PopupMenu(getActivity(), viewHolder.imageView);
                           popup.getMenuInflater().inflate(R.menu.menu_delete_post, popup.getMenu());
                           popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                               @Override
                               public boolean onMenuItemClick(MenuItem menuItem) {
                                   if (menuItem.getTitle().equals("Delete")) {
                                       getRef(position1).removeValue();
                                       Intent intent = new Intent(getActivity(), FactorMain.class);
                                       intent.putExtra("Type", "Home");
                                       startActivity(intent);
                                       getActivity().finish();
                                   }
                                   if (menuItem.getTitle().equals("Edit")){
                                       Intent intent = new Intent(getActivity(),EditPostFactor.class);
                                       intent.putExtra("PostID",post_key);
                                       startActivity(intent);
                                       getActivity().finish();
                                   }
                                   Toast.makeText(getActivity(), "Delete Success !!!", Toast.LENGTH_LONG).show();
                                   return true;
                               }
                           });
                           popup.show();
                       }
                   });
               }catch (Exception e){

               }
            }
        };
        recyclerView.setAdapter(adapter);
        mAuth.addAuthStateListener(mAuthListener);
    }
    public static class FactorPostViewHolder extends RecyclerView.ViewHolder {
        View view;
        private ImageView imageView;
        public FactorPostViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }//setVisisbility
        public void setImageOverflow(){
            imageView = (ImageView) view.findViewById(R.id.overflow_factor);
        }
        public void setCategory(String category) {
            TextView textView = (TextView) view.findViewById(R.id.textcategory_factor);
            textView.setText("ประเภท "+category);
        }

        public void setTitlefactor(String title) {
            TextView textView = (TextView) view.findViewById(R.id.title_factor);
            textView.setText("ชื่อหัวข้อ "+title);
        }

        public void setPosted(String posted) {
            TextView textView = (TextView) view.findViewById(R.id.factor_postby);
            textView.setText("Posted by "+posted);
        }
        public void setTime(String time){
            TextView Time_post = (TextView) view.findViewById(R.id.fac_time);
            Time_post.setText("เวลาที่โพส "+time);
        }
        public void setImageView(final Context ctx, final String image) {
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
