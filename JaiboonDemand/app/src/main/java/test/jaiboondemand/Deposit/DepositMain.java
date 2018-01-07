package test.jaiboondemand.Deposit;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import test.jaiboondemand.PostActivity;
import test.jaiboondemand.R;

public class DepositMain extends Fragment {
    View x;
    private RecyclerView recyclerView;
    private FloatingActionButton fab_deposit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x = inflater.inflate(R.layout.activity_deposit_main, container, false);

        recyclerView = (RecyclerView) x.findViewById(R.id.my_deposit);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.hasFixedSize();

        fab_deposit = (FloatingActionButton) x.findViewById(R.id.fab_deposit);
        fab_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PostActivity.class);
                startActivity(intent);
            }
        });

        return  x;
    }
}
