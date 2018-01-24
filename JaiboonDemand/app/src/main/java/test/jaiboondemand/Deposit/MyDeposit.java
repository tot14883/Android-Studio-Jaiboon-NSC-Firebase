package test.jaiboondemand.Deposit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test.jaiboondemand.R;

public class MyDeposit extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view =  inflater.inflate(R.layout.activity_my_deposit,container,false);




        return view;
    }
}
