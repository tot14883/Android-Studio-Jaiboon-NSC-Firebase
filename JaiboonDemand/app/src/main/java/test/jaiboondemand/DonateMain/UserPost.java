package test.jaiboondemand.DonateMain;

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

public class UserPost extends Fragment {
    private View x;
    private RecyclerView mRecycle;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FloatingActionButton floatingActionButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x = inflater.inflate(R.layout.activity_user_post, container, false);
        mRecycle = (RecyclerView) x.findViewById(R.id.my_post_list);
        mRecycle.setHasFixedSize(true);
        mRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));

        floatingActionButton = (FloatingActionButton) x.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PostActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
              if(firebaseAuth.getCurrentUser() == null){}
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jaiboon");
        mDatabase.keepSynced(true);
        mDatabase.getKey();
        return  x;
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<Insta,InstaVieHolde> FBRA = new FirebaseRecyclerAdapter<Insta, InstaVieHolde>(
                Insta.class,
                R.layout.insta_row,
                InstaVieHolde.class,
                mDatabase.orderByChild("uid").equalTo(mAuth.getUid())
        ) {
            @Override
            protected void populateViewHolder(final InstaVieHolde viewHolder, Insta model, final int position) {
                 final String post_key = getRef(position).getKey().toString();
                 final int position1 = position;
              try {

                  SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                  dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                  Date date = new Date(Long.valueOf(model.getTimepost()));
                  String currentTime = dateFormat.format(date);
                  viewHolder.setTime(currentTime);
                  viewHolder.setTitle(model.getTitle());
                  viewHolder.setDesc(model.getName());
                  viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                  viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent singInstaActivity = new Intent(getActivity(), SingleInstaActivity.class);
                          singInstaActivity.putExtra("PostID", post_key);
                          startActivity(singInstaActivity);
                      }
                  });
                  viewHolder.setCountry(model.getCountry());
                  viewHolder.Clicked();
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
                                      Intent intent = new Intent(getActivity(), Main2Activity.class);
                                      startActivity(intent);
                                      Toast.makeText(getActivity(), "Delete Success !!!", Toast.LENGTH_LONG).show();
                                  }
                                  if (menuItem.getTitle().equals("Edit")) {
                                      Intent intent = new Intent(getActivity(), EditPost.class);
                                      intent.putExtra("PostID", post_key);
                                      startActivity(intent);
                                  }

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
        mRecycle.setAdapter(FBRA);
        mAuth.addAuthStateListener(mAuthListener);
    }
    public static class InstaVieHolde extends RecyclerView.ViewHolder{
        View mView;
        private ImageView imageView;
        public InstaVieHolde(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void Clicked(){
            imageView = (ImageView) mView.findViewById(R.id.overflow);
        }
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }
        public void setDesc(String Name){
            TextView post_Name = (TextView) mView.findViewById(R.id.text_Name);
            post_Name.setText("Posted by "+Name);
        }
        private void setTime(String time){
            TextView time_post = (TextView) mView.findViewById(R.id.time_post);
            time_post.setText("เวลาโพส "+time);
        }
        public void setCountry(String country){
            TextView count_text = (TextView) mView.findViewById(R.id.text_province);
            count_text.setText("จังหวัด "+country);
        }
        public void setImage(final Context ctx, final String image){
            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
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

