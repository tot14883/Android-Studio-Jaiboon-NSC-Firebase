package test.jaiboondemand.Nearby;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.jaiboondemand.R;

/**
 * Created by User on 12/30/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<PlacesPOJO.CustomA> stLstStores;
    private List<PlaceModel> models;


    public RecyclerViewAdapter(List<PlacesPOJO.CustomA> stores, List<PlaceModel> storeModels) {

        stLstStores = stores;
        models = storeModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.setData(stLstStores.get(holder.getAdapterPosition()), holder, models.get(holder.getAdapterPosition()));
    }


    @Override
    public int getItemCount() {
        return Math.min(5, stLstStores.size());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView txtStoreName;
        TextView txtStoreAddr;
        TextView txtStoreDist;
        PlaceModel model;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.txtStoreDist = (TextView) itemView.findViewById(R.id.txtPlaceDist);
            this.txtStoreName = (TextView) itemView.findViewById(R.id.txtPlaceName);
            this.txtStoreAddr = (TextView) itemView.findViewById(R.id.txtPlaceAddr);


        }


        public void setData(PlacesPOJO.CustomA info, MyViewHolder holder, PlaceModel storeModel) {


            this.model = storeModel;

            holder.txtStoreDist.setText(model.distance + "\n" + model.duration);
            holder.txtStoreName.setText(info.name);
            holder.txtStoreAddr.setText(info.vicinity);


        }

    }
}
