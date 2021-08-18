package com.example.dropex;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.dropex.Common.BottomSheetHandler;
import com.example.dropex.Common.Common;
import com.example.dropex.Model.CustomerModel;
import com.example.dropex.Model.JobSolution;
import com.example.dropex.ui.home.HomeActivity;
import com.example.dropex.ui.job.JobsActivity;
import com.example.dropex.ui.main.SplashScreenActivity;
import com.example.dropex.ui.profile.ProfileActivity;
import com.example.dropex.ui.shipments.main.CallToActionActivity;
import com.example.dropex.utils.AppExecutor;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.graphhopper.directions.api.client.model.GeocodingLocation;
import com.graphhopper.directions.api.client.model.GeocodingPoint;
import com.graphhopper.directions.api.client.model.RoutePoint;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.exceptions.InvalidLatLngBoundsException;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Telemetry;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.ui.v5.route.OnRouteSelectionChangeListener;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.utils.LocaleUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.dropex.Common.Common.currentCustomer;

public class NavigationLauncherActivity extends AppCompatActivity implements OnMapReadyCallback,
        MapboxMap.OnMapLongClickListener, OnRouteSelectionChangeListener,
        FetchSolutionCallBackInterfaceButWithJobSolution,
        PermissionsListener {

    private static final int CAMERA_ANIMATION_DURATION = 1000;
    private static final int DEFAULT_CAMERA_ZOOM = 16;
    private static final int CHANGE_SETTING_REQUEST_CODE = 1;
    // If you change the first start dialog (help message), increase this number, all users will be shown the message again
    private static final int FIRST_START_DIALOG_VERSION = 1;
    private static final int FIRST_NAVIGATION_DIALOG_VERSION = 1;

    private LocationLayerPlugin locationLayer;
    private NavigationMapRoute mapRoute;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private static AppExecutor appExecutor=new AppExecutor();



    MapView mapView;
    @BindView(R.id.loading)
    ProgressBar loading;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private ImageView img_avatar;
    private ActivityOptions options;
    private MaterialToolbar toolbar;

    private Marker currentMarker;
    private List<Marker> markers;
    private List<Point> waypoints = new ArrayList<>();
    private DirectionsRoute route;
    private LocaleUtils localeUtils;

    private final int[] padding = new int[]{50, 50, 50, 50};

    private String currentJobId = "";
    private String currentVehicleId = "";
    private String currentGeocodingInput = "";
    private TextView bikePriceTextView;
    private TextView truckPriceTextView;
    private TextView carPriceTextView;

    private TextView capacityBikeTextView;
    private TextView capacityTruckTextView;
    private TextView capacityCarTextView;
    private ConstraintLayout bottomSheet;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_launcher);
        Mapbox.getInstance(this.getApplicationContext(), getString(R.string.dropexpress1));
        inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);


        Telemetry.disableOnUserRequest();
       // ButterKnife.bind(this);

        bottomSheet = findViewById(R.id.bottom_sheet_behavior_id);
        bikePriceTextView = bottomSheet.findViewById(R.id.number_of_bikes);
        carPriceTextView = bottomSheet.findViewById(R.id.number_of_cars);
        truckPriceTextView = bottomSheet.findViewById(R.id.number_of_trucks);
        capacityBikeTextView= bottomSheet.findViewById(R.id.explain_why_bikes);
        capacityCarTextView= bottomSheet.findViewById(R.id.explain_why_cars);
        capacityTruckTextView= bottomSheet.findViewById(R.id.explain_why_trucks);


        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        mapView=findViewById(R.id.mapView);
        loading=findViewById(R.id.loading);
      //  mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        localeUtils=new LocaleUtils();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.menu.activity_main_drawer)
                .setOpenableLayout(drawer)
                .build();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.open();
            }
        });


   /*     NavHostFragment fragment= (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_home);
        NavController navController = fragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    */

        init();
        showFirstStartIfNecessary();
    }
    private void setVehiclePrices(Integer cost){
        int bikePrice=cost*2;
        bikePriceTextView.setText(String.valueOf(bikePrice)+" Kshs");
        carPriceTextView.setText(String.valueOf(cost*2.7)+" Kshs");
        truckPriceTextView.setText(String.valueOf(cost*3)+" Kshs");
        capacityBikeTextView.setText("capacity < 5kg");
        capacityCarTextView.setText("capacity < 200kg");
        capacityTruckTextView.setText("capacity < 1000kg");
    }

    private void init() {
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_sign_out) {
                /*
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(NavigationLauncherActivity.this);
                builder.setTitle("Sign out")
                        .setMessage("Are you sure you want to sign out?")
                        .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setPositiveButton("SIGN OUT", (dialogInterface, i) -> {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(NavigationLauncherActivity.this, SplashScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }).setCancelable(false);
                androidx.appcompat.app.AlertDialog dialog = builder.create();

                dialog.show();

                 */
                launchNavigationWithRoute();
            }
            else if(item.getItemId() == R.id.nav_home){
               startActivity(new Intent(this, JobsActivity.class));

            }

            return true;
        });

        //Populate user data
        View headerView = navigationView.getHeaderView(0);
        TextView text_name = (TextView)headerView.findViewById(R.id.text_name);

        CustomerModel customerModel=Common.getCurrentCustomer();

        img_avatar = (ImageView)headerView.findViewById(R.id.user_avatar);
        Glide
                .with(this)
                .load(customerModel .getUserImageUrl())
                .into(img_avatar);


        text_name.setText(customerModel.getFirstName()+" "+customerModel.getLastName());


    }

  /*  @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

   */

    public void showProfileAFragment(View view) {
       startActivity(new Intent(NavigationLauncherActivity.this, ProfileActivity.class));
       overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

    }





    private void showSettings() {
        startActivityForResult(new Intent(this, NavigationViewSettingsActivity.class), CHANGE_SETTING_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_SETTING_REQUEST_CODE && resultCode == RESULT_OK) {
            boolean shouldRefetch = data.getBooleanExtra(NavigationViewSettingsActivity.UNIT_TYPE_CHANGED, false)
                    || data.getBooleanExtra(NavigationViewSettingsActivity.LANGUAGE_CHANGED, false)
                    || data.getBooleanExtra(NavigationViewSettingsActivity.PROFILE_CHANGED, false);
            if (waypoints.size() > 0 && shouldRefetch) {
                fetchRoute();
            }
        }
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        if (locationLayer != null) {
            locationLayer.onStart();
        }
    }



    private void handleIntent(Intent intent) {
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null && "graphhopper.com".equals(data.getHost())) {
                if (data.getPath() != null) {
                    if (this.mapboxMap == null) {
                        //this happens when onResume is called at the initial start and we will call this method again in onMapReady
                        return;
                    }
                    if (data.getPath().contains("maps")) {
                        clearRoute();
                        //Open Map Url
                        setRouteProfileToSharedPreferences(data.getQueryParameter("vehicle"));

                        List<String> points = data.getQueryParameters("point");
                        for (String point : points) {
                            String[] pointArr = point.split(",");
                            addPointToRoute(Double.parseDouble(pointArr[0]), Double.parseDouble(pointArr[1]));
                        }

                        setStartFromLocationToSharedPreferences(false);
                        updateRouteAfterWaypointChange();
                    }
                    // https://graphhopper.com/api/1/vrp/solution/e7fb8a9b-e441-4ec2-a487-20788e591bb3?vehicle_id=1&key=[KEY]
                    if (data.getPath().contains("api/1/vrp/solution")) {
                        clearRoute();
                        //Open Vrp Url
                        List<String> pathSegments = data.getPathSegments();
                        Log.e("job id in navlauncher",pathSegments.get(pathSegments.size()-1));
                        fetchVrpSolution(pathSegments.get(pathSegments.size() - 1), data.getQueryParameter("vehicle_id"));
                    }
                }

            }
        }
    }

    private void showFirstStartIfNecessary() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getInt(getString(R.string.first_start_dialog_key), -1) < FIRST_START_DIALOG_VERSION) {
            showHelpDialog();
        }
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help_message_title);
        builder.setMessage(Html.fromHtml("1. Please note, this is a demo and not a full featured application. The purpose of this application is to provide an easy starting point for developers to create a navigation application with GraphHopper<br/>2. You should enable your GPS/location<br/>3.You can either search for a location using the magnifier icon or by long pressing on the map<br/>4. Start the navigation by tapping the arrow button<br/><br/>This project is 100% open source, contributions are welcome.<br/><br/>Please drive carefully and always abide local law and signs. Roads might be impassible due to construction projects, traffic, weather, or other events."));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        builder.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(getString(R.string.first_start_dialog_key), FIRST_START_DIALOG_VERSION);
                editor.apply();
            }
        });
        builder.setNeutralButton(R.string.github, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/graphhopper/graphhopper-navigation-example")));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNavigationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.legal_title);
        builder.setMessage(Html.fromHtml("You are required to comply with applicable laws.<br/>When using mapping data, directions or other data from GraphHopper / OpenStreetMap, it is possible that the results differ from the actual situation. You should therefore act at your own discretion. Use of GraphHopper / OpenStreetMap is at your own risk. You are responsible for your own behavior and consequences at all times."));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        builder.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(getString(R.string.first_navigation_dialog_key), FIRST_NAVIGATION_DIALOG_VERSION);
                editor.apply();
                launchNavigationWithRoute();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }





    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void clearRoute() {
        waypoints.clear();
        mapRoute.removeRoute();
        route = null;
        if (currentMarker != null) {
            mapboxMap.removeMarker(currentMarker);
            currentMarker = null;
        }
    }

    private void clearGeocodingResults() {
        if (markers != null) {
            for (Marker marker : markers) {
                this.mapboxMap.removeMarker(marker);
            }
            markers.clear();
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.mapboxMap.getUiSettings().setAttributionDialogManager(new GHAttributionDialogManager(this.mapView.getContext(), this.mapboxMap));
        this.mapboxMap.addOnMapLongClickListener(this);
        this.mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                long id=marker.getId();
                final String title = marker.getTitle();
                View childLayout = inflater.inflate(R.layout.fragment_shipping_item, bottomSheet);
                BottomSheetHandler.showShipment(title,bottomSheet);
                return false;
            }
        });
        initMapRoute();

        this.mapboxMap.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
            @Override
            public boolean onInfoWindowClick(@NonNull Marker marker) {
                for (Marker geocodingMarker : markers) {
                    if (geocodingMarker.getId() == marker.getId()) {
                        LatLng position = geocodingMarker.getPosition();
                        addPointToRoute(position.getLatitude(), position.getLongitude());
                        updateRouteAfterWaypointChange();
                        marker.hideInfoWindow();
                        return true;
                    }
                }
                return true;
            }
        });

        // Check for location permission
        permissionsManager = new PermissionsManager(this);
        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
            permissionsManager.requestLocationPermissions(this);
        } else {
            initLocationLayer();
        }

        handleIntent(getIntent());
    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        addPointToRoute(point.getLatitude(), point.getLongitude());
        updateRouteAfterWaypointChange();

    }

    private void addPointToRoute(double lat, double lng) {
        waypoints.add(Point.fromLngLat(lng, lat));
    }

    @Override
    public void onNewPrimaryRouteSelected(DirectionsRoute directionsRoute) {
        route = directionsRoute;
    }

    @SuppressWarnings({"MissingPermission"})
    private void initLocationLayer() {
        locationLayer = new LocationLayerPlugin(mapView, mapboxMap);
        locationLayer.setRenderMode(RenderMode.COMPASS);
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null) {
            // TODO we could think about zoom to the user location later on as well
            animateCamera(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));

        }
    }

    private void initMapRoute() {
        mapRoute = new NavigationMapRoute(mapView, mapboxMap);
        mapRoute.setOnRouteSelectionChangeListener(this);
    }

    private void fetchRoute() {
        NavigationRoute.Builder builder = NavigationRoute.builder(this)
                .accessToken("pk." + getString(R.string.gh_key))
                .baseUrl(getString(R.string.base_url))
                .user("gh")
                .alternatives(true);

        boolean startFromLocation = getStartFromLocationFromSharedPreferences();

        if (!startFromLocation && waypoints.size() < 2 || startFromLocation && waypoints.size() < 1) {
            onError(R.string.error_not_enough_waypoints);
            return;
        }

        if (startFromLocation) {
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation == null) {
                onError(R.string.error_location_not_found);
                return;
            } else {
                Point location = Point.fromLngLat(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude());
                if (lastKnownLocation.hasBearing())
                    // 90 seems to be the default tolerance of the SDK
                    builder.origin(location, (double) lastKnownLocation.getBearing(), 90.0);
                else
                    builder.origin(location);
            }
        }


        for (int i = 0; i < waypoints.size(); i++) {

            Point p = waypoints.get(i);
            if (i == 0 && !startFromLocation) {
                builder.origin(p);
            } else if (i < waypoints.size() - 1) {
                builder.addWaypoint(p);
            } else {
                builder.destination(p);
            }
        }

        showLoading();

        setFieldsFromSharedPreferences(builder);
        builder.build().getRoute(new SimplifiedCallback() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (validRouteResponse(response)) {
                    route = response.body().routes().get(0);
                    mapRoute.addRoutes(response.body().routes());

                    boundCameraToRoute();
                } else {
                    Snackbar.make(mapView, R.string.error_calculating_route, Snackbar.LENGTH_LONG).show();
                }
                hideLoading();
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                super.onFailure(call, throwable);
                Snackbar.make(mapView, R.string.error_calculating_route, Snackbar.LENGTH_LONG).show();
                hideLoading();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation() {
        if (locationLayer != null) {
            LocationManager lm = (LocationManager)this.getSystemService(this.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            if(!gps_enabled && !network_enabled) {
                // notify user
                new AlertDialog.Builder(this)
                        .setMessage("turn on location service on your device")
                        .setPositiveButton("enable", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("cancel",null)
                        .show();
            }

            return locationLayer.getLastKnownLocation();
        }

        return null;
    }

    private void updateRouteAfterWaypointChange() {
        if (this.waypoints.isEmpty()) {
            hideLoading();
        } else {
            Point lastPoint = this.waypoints.get(this.waypoints.size() - 1);
            LatLng latLng = new LatLng(lastPoint.latitude(), lastPoint.longitude());
            setCurrentMarkerPosition(latLng);
            if (this.waypoints.size() > 0) {
                fetchRoute();
            } else {
                hideLoading();
            }
        }
    }

    private void setFieldsFromSharedPreferences(NavigationRoute.Builder builder) {
        builder
                .language(getLanguageFromSharedPreferences())
                .voiceUnits(getUnitTypeFromSharedPreferences())
                .profile(getRouteProfileFromSharedPreferences());
    }

    private String getUnitTypeFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultUnitType = getString(R.string.default_unit_type);
        String unitType = sharedPreferences.getString(getString(R.string.unit_type_key), defaultUnitType);
        if (unitType.equals(defaultUnitType)) {
            unitType = localeUtils.getUnitTypeForDeviceLocale(this);
        }

        return unitType;
    }

    private Locale getLanguageFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultLanguage = getString(R.string.default_locale);
        String language = sharedPreferences.getString(getString(R.string.language_key), defaultLanguage);
        if (language.equals(defaultLanguage)) {
            return localeUtils.inferDeviceLocale(this);
        } else {
            return new Locale(language);
        }
    }

    private boolean getShouldSimulateRouteFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(getString(R.string.simulate_route_key), false);
    }

    private boolean getStartFromLocationFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(getString(R.string.start_from_location_key), true);
    }

    private void setStartFromLocationToSharedPreferences(boolean setStartFromLocation) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.start_from_location_key), setStartFromLocation);
        editor.apply();
    }

    private void setRouteProfileToSharedPreferences(String ghVehicle) {
        if (ghVehicle == null)
            return;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String routeProfile;
        switch (ghVehicle) {
            case "foot":
            case "hike":
                routeProfile = "walking";
                break;
            case "bike":
            case "mtb":
            case "racingbike":
                routeProfile = "cycling";
                break;
            default:
                routeProfile = "driving";
        }
        editor.putString(getString(R.string.route_profile_key), routeProfile);
        editor.apply();
    }

    private String getRouteProfileFromSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(
                getString(R.string.route_profile_key), DirectionsCriteria.PROFILE_DRIVING
        );
    }


    private void launchNavigationWithRoute() {
        if (route == null) {
            Toast.makeText(this, R.string.error_route_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getInt(getString(R.string.first_navigation_dialog_key), -1) < FIRST_NAVIGATION_DIALOG_VERSION) {
            showNavigationDialog();
            return;
        }

        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null && waypoints.size() > 1) {
            float[] distance = new float[1];
            Location.distanceBetween(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), waypoints.get(0).latitude(), waypoints.get(0).longitude(), distance);

            //Ask the user if he would like to recalculate the route from his current positions
            if (distance[0] > 100) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.error_too_far_from_start_title);
                builder.setMessage(R.string.error_too_far_from_start_message);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        waypoints.set(0, Point.fromLngLat(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude()));
                        fetchRoute();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        _launchNavigationWithRoute();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                _launchNavigationWithRoute();
            }
        } else {
            _launchNavigationWithRoute();
        }

    }



    private void _launchNavigationWithRoute() {
        NavigationLauncherOptions.Builder optionsBuilder = NavigationLauncherOptions.builder()
                .shouldSimulateRoute(true)
                .directionsProfile(DirectionsCriteria.PROFILE_DRIVING)
                .waynameChipEnabled(false);

        optionsBuilder.directionsRoute(route);

        NavigationLauncher.startNavigation(this, optionsBuilder.build());
    }

    private boolean validRouteResponse(Response<DirectionsResponse> response) {
        return response.body() != null && !response.body().routes().isEmpty();
    }

    private void hideLoading() {
        if (loading.getVisibility() == View.VISIBLE) {
            loading.setVisibility(View.INVISIBLE);
        }
    }

    private void showLoading() {
        if (loading.getVisibility() == View.INVISIBLE) {
            loading.setVisibility(View.VISIBLE);
        }
    }

    private void boundCameraToRoute() {
        if (route != null) {
            List<Point> routeCoords = LineString.fromPolyline(route.geometry(),
                    Constants.PRECISION_6).coordinates();

            List<LatLng> bboxPoints = new ArrayList<>();
            for (Point point : routeCoords) {
                bboxPoints.add(new LatLng(point.latitude(), point.longitude()));
            }
            if (bboxPoints.size() > 1) {
                try {
                    LatLngBounds bounds = new LatLngBounds.Builder().
                            includes(bboxPoints).build();
                    // left, top, right, bottom
                    animateCameraBbox(bounds, CAMERA_ANIMATION_DURATION, padding);
                } catch (InvalidLatLngBoundsException exception) {
                    Toast.makeText(this, R.string.error_valid_route_not_found, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void animateCameraBbox(LatLngBounds bounds, int animationTime, int[] padding) {
        CameraPosition position = mapboxMap.getCameraForLatLngBounds(bounds, padding);
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), animationTime);
    }

    private void animateCamera(LatLng point) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, DEFAULT_CAMERA_ZOOM), CAMERA_ANIMATION_DURATION);
    }

    private void setCurrentMarkerPosition(LatLng position) {
        if (position != null) {
            if (currentMarker == null) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position);
                currentMarker = mapboxMap.addMarker(markerOptions);
            } else {
                currentMarker.setPosition(position);
            }
        }
    }

    private void updateWaypoints(List<Point> points) {
        if (points.size() > 24) {
            onError(R.string.error_too_many_waypoints);
            return;
        }
        clearRoute();
        this.waypoints = points;
        updateRouteAfterWaypointChange();
    }

    private void fetchVrpSolution(String jobId, String vehicleId) {
        currentJobId = jobId;
        currentVehicleId = vehicleId;

        //   showLoading();
        new FetchSolutionTaskButReturnJobSolution(this, getString(R.string.gh_key)).execute(new FetchSolutionConfig(currentJobId, currentVehicleId));
    }



    @Override
    public void onError(int message) {
        Snackbar.make(mapView, message, Snackbar.LENGTH_LONG).show();
    }
    /*



    @Override
    public void onPostExecuteGeocodingSearch(List<GeocodingLocation> locations) {
        clearGeocodingResults();
        markers = new ArrayList<>(locations.size());

        if (locations.isEmpty()) {
            onError(R.string.error_geocoding_no_location);
            return;
        }

        List<LatLng> bounds = new ArrayList<>();
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null)
            bounds.add(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));

        for (GeocodingLocation location : locations) {
            GeocodingPoint point = location.getPoint();
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(point.getLat(), point.getLng());
            markerOptions.position(latLng);
            bounds.add(latLng);
            markerOptions.title(location.getName());
            String snippet = "";
            if (location.getStreet() != null) {
                snippet += location.getStreet();
                if (location.getHousenumber() != null)
                    snippet += " " + location.getHousenumber();
                snippet += "\n";
            }
            if (location.getCity() != null) {
                if (location.getPostcode() != null)
                    snippet += location.getPostcode() + " ";
                snippet += location.getCity() + "\n";
            }
            if (location.getCountry() != null)
                snippet += location.getCountry() + "\n";
            if (location.getOsmId() != null) {
                snippet += "OSM-Id: " + location.getOsmId() + "\n";
                if (location.getOsmKey() != null)
                    snippet += "OSM-Key: " + location.getOsmKey() + "\n";
                if (location.getOsmType() != null)
                    snippet += "OSM-Type: " + location.getOsmType() + "\n";
            }
            snippet += "\n\n Tap on info window\n to add point to route";
            if (!snippet.isEmpty())
                markerOptions.snippet(snippet);
            markerOptions.icon(IconFactory.getInstance(this.getApplicationContext()).fromResource(R.drawable.ic_map_marker));
            final Marker marker = mapboxMap.addMarker(markerOptions);
            markers.add(mapboxMap.addMarker(markerOptions));
        }

        // For bounds we need at least 2 entries
        if (bounds.size() >= 2) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boundsBuilder.includes(bounds);
            animateCameraBbox(boundsBuilder.build(), CAMERA_ANIMATION_DURATION, padding);
        } else if (bounds.size() == 1) {
            // If there is only 1 result (=>current location unknown), we just zoom to that result
            animateCamera(bounds.get(0));
        }
        hideLoading();
    }


     */
    @Override
    public void onPostExecute(JobSolution jobSolution) {
        List<Point> points=jobSolution.getPoints();
        if (getStartFromLocationFromSharedPreferences() && !points.isEmpty()) {
            // Remove the first point if we want to start from the current location

        }
        setVehiclePrices(jobSolution.getCosts());
        updateWaypoints(points);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "This app needs location permissions to work properly.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            initLocationLayer();
        } else {
            Toast.makeText(this, "You didn't grant location permissions.",
                    Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationLayer != null) {
            locationLayer.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);
    }
}
