package test.jaiboondemand.Deposit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import test.jaiboondemand.R;

public class SuccessDeposit extends Fragment {
    View view;
    private Query mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_success_deposit,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_success_deposit);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        mData = FirebaseDatabase.getInstance().getReference().child("Deposit").orderByChild("Status").equalTo("Success");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<DepositFirebase,SuccessDepositViewHolder> adapter = new FirebaseRecyclerAdapter<DepositFirebase, SuccessDepositViewHolder>(
                DepositFirebase.class,
                R.layout.card_deposit,
                SuccessDepositViewHolder.class,
                mData
        ) {
            @Override
            protected void populateViewHolder(final SuccessDepositViewHolder viewHolder, DepositFirebase model, final int position) {
                final String post_key = getRef(position).getKey().toString();
                final int position1 = position;

                viewHolder.setTitle(model.getTopic_Deposit());
                viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),DepositDetail.class);
                        intent.putExtra("PostID",post_key);
                        startActivity(intent);
                    }
                });
                viewHolder.setImageDel();
                viewHolder.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(getActivity(), viewHolder.del);
                        popup.getMenuInflater().inflate(R.menu.menu_delete_deposit, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                if (menuItem.getTitle().equals("Delete")) {
                                    getRef(position1).removeValue();
                                    Intent intent = new Intent(getActivity(),DepositMain.class);
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
            }
        };
        recyclerView.setAdapter(adapter);
        mAuth.addAuthStateListener(mAuthListener);
    }

    public static class SuccessDepositViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private ImageView del;
        public SuccessDepositViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView textView = (TextView) mView.findViewById(R.id.title_deposit);
            textView.setText(title);
        }
        public void setImageDel(){
            del = (ImageView) mView.findViewById(R.id.overflow_deposit);
        }
        public void setImage(final Context ctx, final String image){
            final ImageView post_image = (ImageView) mView.findViewById(R.id.post_image_deposit);
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
