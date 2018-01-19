package test.jaiboondemand.shop_type;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import test.jaiboondemand.Insta;
import test.jaiboondemand.R;

public class SuitShop extends Fragment {
    private RecyclerView mSList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View x;
    private Query mQuery;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x =  inflater.inflate(R.layout.activity_suit_shop,container,false);

        mSList = (RecyclerView) x.findViewById(R.id.recycle_suit);
        mSList.setHasFixedSize(true);
        mSList.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ShopJaiboon");
        mDatabase.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
            }
        };
        mQuery = FirebaseDatabase.getInstance().getReference().child("ShopJaiboon").orderByChild("typeproduct").equalTo("Suit");
        mQuery.keepSynced(true);
        return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Insta,HomeShop.RecycleAdapter> FBRA = new FirebaseRecyclerAdapter<Insta, HomeShop.RecycleAdapter>(
                Insta.class,
                R.layout.content_main2,
                HomeShop.RecycleAdapter.class,
                mQuery
        ) {
            @Override
            protected void populateViewHolder(final HomeShop.RecycleAdapter viewHolder, Insta model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setPTitle(model.getNameproduct());
                viewHolder.setCount(model.getPriceproduct());
                viewHolder.setDesc(model.getDescproduct());
                viewHolder.setProImage(getActivity().getApplicationContext(),model.getImageproduct());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shop_activity = new Intent(getActivity(),Shopdetail.class);
                        shop_activity.putExtra("PostID", post_key);
                        startActivity(shop_activity);
                    }
                });
            }
        };
        mSList.setAdapter(FBRA);
        mAuth.addAuthStateListener(mAuthListener);
    }
    public static class RecycleAdapter extends RecyclerView.ViewHolder{
        View mView;
        public RecycleAdapter(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setPTitle(String Ptitle){
            TextView title_product = (TextView) mView.findViewById(R.id.title_Product);
            title_product.setText(Ptitle);
        }
        public void setCount(String PCount){
            TextView Count_product = (TextView) mView.findViewById(R.id.count_Product);
            Count_product.setText(PCount+" บาท");

        }
        public void setDesc(String desc){
            TextView desc_product = (TextView)mView.findViewById(R.id.title_Desc);
            desc_product.setText(desc);
        }
        public void setProImage(final Context ctx, final String image){
            final ImageView img_product = (ImageView) mView.findViewById(R.id.Img_Product);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(img_product, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(img_product);
                }
            });
        }
    }
}
