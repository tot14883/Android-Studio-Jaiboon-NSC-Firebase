package test.jaiboondemand.Deposit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;

import test.jaiboondemand.R;

public class SuccessDeposit extends Fragment {
    View view;
    private Query mData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_success_deposit,container,false);
        return view;
    }
}
