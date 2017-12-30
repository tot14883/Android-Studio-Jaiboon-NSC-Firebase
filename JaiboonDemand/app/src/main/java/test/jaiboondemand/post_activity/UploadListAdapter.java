package test.jaiboondemand.post_activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import test.jaiboondemand.R;

/**
 * Created by User on 12/29/2017.
 */

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder>{
    public List<String> fileDoneList;
    private ImageButton imageButton;
    public UploadListAdapter(List<String> fileDoneList){
        this.fileDoneList = fileDoneList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_picture_post,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return fileDoneList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
         View mView;
         public ImageView fileDoneView;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            fileDoneView = (ImageView) mView.findViewById(R.id.post_image_select);
        }
    }
}
