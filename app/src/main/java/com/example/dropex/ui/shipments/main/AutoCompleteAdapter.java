package com.example.dropex.ui.shipments.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropex.R;
import com.graphhopper.directions.api.client.model.GeocodingLocation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AutoCompleteAdapter  extends RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder> {
private ArrayList<GeocodingLocation> locations=new ArrayList<>();
    private OnItemClickListener listener;

    public ArrayList<GeocodingLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<GeocodingLocation> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public AutoCompleteAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AutoCompleteAdapter.ViewHolder holder, int position) {
        GeocodingLocation currentLocation=locations.get(position);
        holder.locationName.setText(currentLocation.getName());
        holder.locationHint.setText(currentLocation.getStreet());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView locationName;
        public TextView locationHint;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            locationName=itemView.findViewById(R.id.location_name);
            locationHint=itemView.findViewById(R.id.location_desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(locations.get(position));

                    }
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(GeocodingLocation location);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
