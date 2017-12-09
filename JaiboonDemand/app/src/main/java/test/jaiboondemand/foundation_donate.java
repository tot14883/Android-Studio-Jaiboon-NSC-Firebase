package test.jaiboondemand;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class foundation_donate extends Fragment {
    private View x;
    private DatabaseReference databaseReference;
    private RecyclerView mRecycle;
    private Query mQuery;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x =  inflater.inflate(R.layout.activity_foundation_donate,container,false);

        mRecycle = (RecyclerView) x.findViewById(R.id.my_post2_list);
        mRecycle.setHasFixedSize(true);
        mRecycle.setLayoutManager(new GridLayoutManager(getActivity(),2));



        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


            }
        };

        mQuery = FirebaseDatabase.getInstance().getReference().child("Jaiboon").orderByChild("Type").equalTo("Foundation");
        mQuery.keepSynced(true);

        return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Insta,InstaViewHold> FBRA = new FirebaseRecyclerAdapter<Insta, InstaViewHold>(
                Insta.class,
                R.layout.insta_row,
                InstaViewHold.class,
                mQuery
        ) {
            @Override
            protected void populateViewHolder(final InstaViewHold viewHolder, Insta model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getName());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleInstaActivity = new Intent(getActivity(), SingleInstaActivity.class);
                        singleInstaActivity.putExtra("PostID", post_key);
                        startActivity(singleInstaActivity);
                    }
                });
                viewHolder.setImageView();
            }
        };
        mRecycle.setAdapter(FBRA);


    }
    public static class InstaViewHold extends RecyclerView.ViewHolder{
        View mView;
        private ImageView imageView;
        public InstaViewHold(View itemView) {
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
            post_Name.setText("by "+Name);
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
