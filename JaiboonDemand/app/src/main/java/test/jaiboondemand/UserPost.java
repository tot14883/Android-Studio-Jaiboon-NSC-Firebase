package test.jaiboondemand;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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
            protected void populateViewHolder(InstaVieHolde viewHolder, Insta model, int position) {
                 final String post_key = getRef(position).getKey().toString();
                 viewHolder.setTitle(model.getTitle());
                 viewHolder.setDesc(model.getDesc());
                 viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());
                 viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Intent singInstaActivity = new Intent(getActivity(),SingleInstaActivity.class);
                         singInstaActivity.putExtra("PostID",post_key);
                         startActivity(singInstaActivity);
                     }
                 });
            }
        };
        mRecycle.setAdapter(FBRA);
        mAuth.addAuthStateListener(mAuthListener);
    }
    public static class InstaVieHolde extends RecyclerView.ViewHolder{
        View mView;
        public InstaVieHolde(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.textDescription);
            post_desc.setText(desc);
        }
        public void setImage(Context ctx,String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
}

