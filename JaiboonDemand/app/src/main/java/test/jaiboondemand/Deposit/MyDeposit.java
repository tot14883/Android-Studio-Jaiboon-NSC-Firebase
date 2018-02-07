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

public class MyDeposit extends Fragment {
    View view;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view =  inflater.inflate(R.layout.activity_my_deposit,container,false);
       recyclerView = (RecyclerView) view.findViewById(R.id.recycler_my_deposit);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       recyclerView.setHasFixedSize(true);

       mAuth = FirebaseAuth.getInstance();

       mData = FirebaseDatabase.getInstance().getReference().child("Deposit");

       mAuthListner = new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

           }
       };


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<DepositFirebase,MyDepositViewHolder> adapter = new FirebaseRecyclerAdapter<DepositFirebase, MyDepositViewHolder>(
                DepositFirebase.class,
                R.layout.card_deposit,
                MyDepositViewHolder.class,
                mData.child(mAuth.getCurrentUser().getUid())//error orderByChild `ซ้อน orderByChild ไม่ได้
        ) {
            @Override
            protected void populateViewHolder(final MyDepositViewHolder viewHolder, DepositFirebase model, final int position) {
                final String post_key = getRef(position).getKey().toString();
                final int position1 = position;
                try {

                    viewHolder.setTitle(model.getTopic_Deposit());
                    viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), DepositDetail.class);
                            intent.putExtra("PostID", post_key);
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
                                        Intent intent = new Intent(getActivity(), DepositMain.class);
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

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                    Date date = new Date(Long.valueOf(model.getTime_deposit()));
                    String currentTime = dateFormat.format(date);

                    viewHolder.setTime(currentTime);
                }catch (Exception e){

                }
            }
        };
        recyclerView.setAdapter(adapter);
        mAuth.addAuthStateListener(mAuthListner);
    }
    public static class MyDepositViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private  ImageView del;
        public MyDepositViewHolder(View itemView) {
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
        public void setTime(String time){
            TextView time_text = (TextView) mView.findViewById(R.id.time_deposit);
            time_text.setText(time);
        }
        public void setImage(final Context ctx,final String image){
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
