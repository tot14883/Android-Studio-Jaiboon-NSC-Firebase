package test.jaiboondemand.Factor;

import android.app.Activity;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import test.jaiboondemand.R;

/**
 * Created by User on 1/29/2018.
 */

public class ImagePostFactor extends RecyclerView.Adapter<ImagePostFactor.ViewHolder>{
    private int itemHeight;
    private ArrayList<Uri> data;

    public ImagePostFactor(ArrayList<Uri> data) {
        this.data = data;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Activity context = (Activity) recyclerView.getContext();
        Point windowDimensions = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(windowDimensions);
        itemHeight = Math.round(windowDimensions.y * 0.6f);
    }

    @Override
    public ImagePostFactor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_gallery, parent, false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                itemHeight);
        v.setLayoutParams(params);
        return new ImagePostFactor.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImagePostFactor.ViewHolder holder, int position) {
        Picasso.with(holder.itemView.getContext())
                .load(data.get(position))
                .resize(500, 500 )
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        // private View overlay;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);

        }
    }
}
