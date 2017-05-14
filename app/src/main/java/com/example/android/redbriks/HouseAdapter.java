package com.example.android.redbriks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.smartstreet.R;

import java.util.ArrayList;

/**
 * Created by shital on 10/9/16.
 */
public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder>{

    //to test on github
    ArrayList<House> houseArrayList = new ArrayList<House>();

    Context ctx;

    public HouseAdapter(ArrayList<House> houseArrayList, Context ctx) {
        this.houseArrayList = houseArrayList;
        this.ctx = ctx;
    }

    @Override
    public HouseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
        HouseViewHolder houseViewHolder =  new HouseViewHolder(view,ctx,houseArrayList);
        return houseViewHolder;
    }

    @Override
    public void onBindViewHolder(HouseViewHolder holder, int position) {

        House house = houseArrayList.get(position);
        holder.house_image.setImageResource(house.getImage_id());
        holder.rent.setText(String.valueOf(house.getPrice()));
        holder.address.setText(house.getHouse_address());
    }

    @Override
    public int getItemCount() {
        return houseArrayList.size();
    }

    public static class HouseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView house_image;
        TextView house_id, address, rent,city;

        ArrayList<House> houses = new ArrayList<>();
        Context ctx;
        public HouseViewHolder(View view, Context ctx, ArrayList<House> houses) {
            super(view);
            view.setOnClickListener(this);
            this.houses = houses;
            this.ctx = ctx;

            house_image = (ImageView) itemView.findViewById(R.id.house_image);
            rent = (TextView) itemView.findViewById(R.id.rent_id);
            address = (TextView)itemView.findViewById(R.id.address_id);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            House house = this.houses.get(position);

            Intent intent = new Intent(this.ctx,HouseDetails.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.putExtra("House",house);
            this.ctx.startActivity(intent);
        }
    }
}
