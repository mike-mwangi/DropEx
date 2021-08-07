package com.example.dropex.ui.shipments.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dropex.R;
import com.graphhopper.directions.api.client.model.GeocodingLocation;

import java.util.ArrayList;

public class AutoCompleteAdapter  extends ArrayAdapter<GeocodingLocation> {

    private ArrayList<GeocodingLocation> locations=new ArrayList<>();
    private static LayoutInflater inflater = null;

    public ArrayList<GeocodingLocation> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<GeocodingLocation> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    public AutoCompleteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<GeocodingLocation> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        locations=objects;
    }

    public int getCount() {
        return locations.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public GeocodingLocation getCurrentLocation(int position) {
        return locations.get(position);
    }

    public void removeItem(int position) {
        locations.remove(position);
    }

    public static class ViewHolder {
        public TextView txtName;
        public TextView txtProfession;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.location_list_item, null);


            holder = new ViewHolder();
            holder.txtName = (TextView) vi.findViewById(R.id.location_name);
            holder.txtProfession = (TextView) vi.findViewById(R.id.location_desc);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        GeocodingLocation o = locations.get(position);

        if (o.getName() != null) {
            holder.txtName.setText(o.getName());
        } else {
            holder.txtName.setText("N/A");
        }

        if (o.getStreet() != null) {
            holder.txtProfession.setText(o.getStreet());
        } else {
            holder.txtProfession.setText("N/A");
        }
        return vi;
    }
}
