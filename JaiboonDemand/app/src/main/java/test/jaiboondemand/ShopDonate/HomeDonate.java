package test.jaiboondemand.ShopDonate;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import test.jaiboondemand.DonateMain.Insta;
import test.jaiboondemand.R;

public class HomeDonate extends Fragment {
    View x;
    private RecyclerView mRecycler;
    private DatabaseReference mDatabase;
    private String Key = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x = inflater.inflate(R.layout.activity_home_donate,container,false);
        mRecycler = (RecyclerView) x.findViewById(R.id.Recycle_donate);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ShopJaiboon");
        Key = getActivity().getIntent().getExtras().getString("Keypost");


     return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Insta,RecycleAdapter> FBRA = new FirebaseRecyclerAdapter<Insta, RecycleAdapter>(
                Insta.class,
                R.layout.content_main2,
                RecycleAdapter.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(RecycleAdapter viewHolder, Insta model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getNameproduct());
                viewHolder.setCount(model.getPriceproduct());
                viewHolder.setProImage(getActivity().getApplicationContext(),model.getImageproduct());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shop_activity = new Intent(getActivity(),DonateDetail.class);
                        shop_activity.putExtra("PostID", post_key);
                        shop_activity.putExtra("Keypost",Key);
                        startActivity(shop_activity);
                    }
                });
            }
        };
        mRecycler.setAdapter(FBRA);

    }
    public static class RecycleAdapter extends RecyclerView.ViewHolder{
        View mView;
        public RecycleAdapter(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String Ptitle){
            TextView title_product = (TextView) mView.findViewById(R.id.title_Product);
            title_product.setText(Ptitle);
        }
        public void setCount(String Pcount){
            TextView Count_product = (TextView) mView.findViewById(R.id.count_Product);
            Count_product.setText(Pcount);
        }
        public void setProImage(Context ctx,String image){
            ImageView img_product = (ImageView) mView.findViewById(R.id.Img_Product);
            Picasso.with(ctx).load(image).into(img_product);
        }
    }
}
