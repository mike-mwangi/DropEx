package com.example.dropex.Adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dropex.Common.ColorWheel;
import com.example.dropex.FetchGeocodingConfig;
import com.example.dropex.FetchGeocodingTask;
import com.example.dropex.FetchGeocodingTaskCallbackInterface;
import com.example.dropex.Model.CustomService;
import com.example.dropex.Model.ItemSize;
import com.example.dropex.R;
import com.example.dropex.ui.shipments.main.AutoCompleteAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.graphhopper.directions.api.client.model.Address;
import com.graphhopper.directions.api.client.model.GeocodingLocation;
import com.graphhopper.directions.api.client.model.Stop;
import com.mapbox.geojson.Point;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShipmentItemAdapter  extends RecyclerView.Adapter<ShipmentItemAdapter.ItemViewHolder>  {

    private List<CustomService> services=new ArrayList<>();
    private CustomService model;
    public shipmentItemOnClickListener onClickListener;
    public AutoCompleteAdapter autoCompleteAdapter1;
    public Context context;
    public GeocodingLocation initialLocation;


    private ItemViewHolder viewHolder;
    private Address initialAddress;

    public shipmentItemOnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(shipmentItemOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public List<CustomService> getServices() {
        return services;
    }




    public interface shipmentItemOnClickListener{
        public void itemOnClickListener(String action,List<CustomService> services);

    }

    public void setServices(List<CustomService> services) {
        this.services = services;
    }


    @NonNull
    @NotNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shipping_list_item,
                parent, false);
        context=parent.getContext();
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ShipmentItemAdapter.ItemViewHolder holder, int position) {
        model=services.get(position);
        viewHolder=holder;
        holder.userName.setText(model.getCustomerName());
        holder.packageNote.setText(model.getDeliveryNote());
        holder.locationInput.setText(model.getAddress().getName());
        holder.customerNameTextInput.getEditText().setText(model.getCustomerName());
        holder.customerPhoneNumberInput.setText(model.getCustomerPhoneNumber());
        initialAddress = model.getAddress();
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements FetchGeocodingTaskCallbackInterface,AutoCompleteAdapter.OnItemClickListener{
        public ShapeableImageView receiverImage;
        public MaterialTextView userName;
        public MaterialTextView packageNote;
        public ImageButton expandLayoutButton;
        public ConstraintLayout editShipmentLayout;
        private GeocodingLocation deliveryLocation;
        public AutoCompleteTextView locationInput;
        private ArrayList<GeocodingLocation> locations;
        private GeocodingLocation pickuplocation;
        private TextInputLayout customerNameTextInput;
        private ChipGroup sizeChips;
        private TextInputEditText customerPhoneNumberInput;
        public String sizeOfItemString;
        public TextInputEditText itemNote;
        public MaterialButton saveButton;
        public MaterialButton deleteButton;
        public RecyclerView recyclerView;
        private ArrayAdapter<String> locationArrayAdapter;
        private ArrayList<GeocodingLocation> deliveryLocations;
        private ArrayList<String> typeNames;


        public ItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            GradientDrawable gd2 = new GradientDrawable(
                    GradientDrawable.Orientation.BR_TL,
                    ColorWheel.getGcolors());
            gd2.setCornerRadius(20f);

            ((MaterialCardView)itemView).setBackground(gd2);




            receiverImage=itemView.findViewById(R.id.receiver_image);
            userName=itemView.findViewById(R.id.name_of_reciever);
            packageNote=itemView.findViewById(R.id.package_note);
            expandLayoutButton=itemView.findViewById(R.id.expand_button);
            editShipmentLayout=itemView.findViewById(R.id.edit_shipment_layout);

            itemNote=editShipmentLayout.findViewById(R.id.item_note_input);
            sizeChips=editShipmentLayout.findViewById(R.id.size_input_chips);
            locationInput=editShipmentLayout.findViewById(R.id.location_input);
            customerNameTextInput=editShipmentLayout.findViewById(R.id.receiver_name_input_layout);
            customerPhoneNumberInput=editShipmentLayout.findViewById(R.id.receiver_phone_number_input);
            saveButton=editShipmentLayout.findViewById(R.id.save_shipment);
            deleteButton=editShipmentLayout.findViewById(R.id.delete_shipment);
            recyclerView=editShipmentLayout.findViewById(R.id.location_recycler_view);

            locations= new ArrayList<>();
            ImageButton searchButton=itemView.findViewById(R.id.search_btn);
            autoCompleteAdapter1= new AutoCompleteAdapter();
            /*
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setHasFixedSize(true);


            recyclerView.setAdapter(autoCompleteAdapter1);
 autoCompleteAdapter1= new AutoCompleteAdapter();
            autoCompleteAdapter1.setOnItemClickListener(this);

            locationInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else{
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                }
            });

             */
            typeNames = new ArrayList<>();

            locationArrayAdapter = new ArrayAdapter<String>(itemView.getContext(),R.layout.spinner_list_item, typeNames);
            locationInput.setAdapter(locationArrayAdapter);
            locationArrayAdapter.notifyDataSetChanged();
            locationInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    deliveryLocation=deliveryLocations.get(position);
                }
            });
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String address=locationInput.getText().toString();
                    new FetchGeocodingTask(ItemViewHolder.this,itemView.getContext().getString(R.string.gh_key)).execute(new FetchGeocodingConfig(address, "en", 5, false, "-1.2778285,36.8486", "default"));
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomService service=getService();
                    services.set(getAbsoluteAdapterPosition(),service);
                    onClickListener.itemOnClickListener("save",services);
                    notifyDataSetChanged();
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    services.remove(getAbsoluteAdapterPosition());
                    onClickListener.itemOnClickListener("delete",services);

                    notifyDataSetChanged();

                }
            });

            expandLayoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int visibility=(editShipmentLayout.getVisibility()==View.VISIBLE) ? View.GONE:View.VISIBLE;
                    editShipmentLayout.setVisibility(visibility);
                }
            });


        }
        @Override
        public void onError(int message) {

        }

        @Override
        public void onPostExecuteGeocodingSearch(List<GeocodingLocation> points) {


            autoCompleteAdapter1.setLocations((ArrayList<GeocodingLocation>) points);
            deliveryLocations= (ArrayList<GeocodingLocation>) points;
            typeNames=new ArrayList<>();
            int i=0;
            for(GeocodingLocation location:points) {

                final GeocodingLocation location1 = points.get(i);

                typeNames.add(location1.getCountry()+","+location1.getCity()+","+location1.getName());
            i++;
            }
            locationInput.setAdapter(new ArrayAdapter<String>(itemView.getContext(), R.layout.spinner_list_item,typeNames));
            locationInput.showDropDown();

            //deliveryLocation=points.get(0);
            //pickuplocation=points.get(2);
            // Log.e("GEOCODE",points.toString());
        }

        @Override
        public void onItemClick(GeocodingLocation location) {
            deliveryLocation=location;
            locationInput.setText(location.getCountry()+","+location.getCity()+","+location.getName());
        }
        public CustomService getService() {
            String customerName=customerNameTextInput.getEditText().getText().toString();
            String deliveryNote=itemNote.getText().toString();
            String customerPhoneNumber=customerPhoneNumberInput.getText().toString();
            int sizeOfItem= ItemSize.getSizeOfItem(sizeOfItemString);

            CustomService service=new CustomService();
            service.setCustomerName(customerName);
            service.setDeliveryNote(deliveryNote);
            service.setCustomerPhoneNumber(customerPhoneNumber);
            service.setCustomSize(sizeOfItem);

            Stop stop=new Stop();
            Address address=new Address();
            address.setLat(deliveryLocation.getPoint().getLat());
            address.setLon(deliveryLocation.getPoint().getLng());
            address.setName(deliveryLocation.getName());
            stop.setAddress(address);
            service.setAddress(address);

            String shimentId=customerName+customerPhoneNumber;
            service.setId(shimentId);
            service.setName("pickup and deliver to me");
            long maxT=9222036;
            service.setMaxTimeInVehicle(maxT);

            if(service.getCustomerName()==""){
                service=null;
            }
            else if(service.getCustomerPhoneNumber()=="")
            {
                service=null;
            }
            else if(service.getDeliveryNote()=="")
            {
                service=null;
            } else if(deliveryLocation==null)
            {
                service=null;
            }


            return service;
        }

    }
}
