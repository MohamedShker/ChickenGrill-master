package tm.shker.mohamed.chickengrill.Adapters;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Objects.DeliveryArea;
import tm.shker.mohamed.chickengrill.R;

/**
 * Created by mohamed on 18/11/2016.
 */

public class DeliveryAreaAdapter extends RecyclerView.Adapter<DeliveryAreaAdapter.DeliveryAreaViewHolder> {

    private ArrayList<DeliveryArea> data;
    private LayoutInflater inflater;
    private Context context;

    public DeliveryAreaAdapter(ArrayList<DeliveryArea> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DeliveryAreaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.delivery_area,parent,false);
        return new DeliveryAreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DeliveryAreaViewHolder holder, int position) {
        DeliveryArea deliveryArea = data.get(position);
        holder.tvareaName.setText(deliveryArea.getAreaName());
        holder.tvdeliveryCost.setText(deliveryArea.getDeliveryCost() + "₪");
        holder.tvminDelivery.setText(deliveryArea.getMinDelivery() + "₪");
        holder.tvdeliveryTime.setText(deliveryArea.getDeliveryTime() + "דקות");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class DeliveryAreaViewHolder extends RecyclerView.ViewHolder{

        TextView tvareaName;
        TextView tvdeliveryCost;
        TextView tvminDelivery;
        TextView tvdeliveryTime;

        public DeliveryAreaViewHolder(View currView) {
            super(currView);
            tvareaName = (TextView) currView.findViewById(R.id.tvareaName);
            tvdeliveryCost = (TextView) currView.findViewById(R.id.tvdeliveryCost);
            tvminDelivery = (TextView) currView.findViewById(R.id.tvminDelivery);
            tvdeliveryTime = (TextView) currView.findViewById(R.id.tvdeliveryTime);
        }
    }
}
