package matrix.weathernow;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View myview;
    GoogleApiClient mGoogleApiClient;
    static boolean onSearch=false;
    private String location;
    public static Geocoder geocoder;
    private String la;
    private String ln;
    TextView viewUpdate;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_home, container, false);

        final Button update = (Button) myview.findViewById(R.id.getupdate);
        final Button search = (Button) myview.findViewById(R.id.button2);
        viewUpdate= (TextView)myview.findViewById(R.id.viewUpdate) ;



        mMapView = (MapView) myview.findViewById(R.id.mapx);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(new Double(la),new Double(ln),1);
//            Log.d("add", addresses.toString());
                    if (addresses.size()>0)
                        Home.this.location = addresses.get(0).getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getActivity(), UserUpload.class);
                intent.putExtra("location",location);
                intent.putExtra("latitude",la);
                intent.putExtra("longitude",ln);

                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    geoLocation();
                    onSearch=true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return myview;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
  /*      // googleMap.addMarker(new MarkerOptions().position(new LatLng(6.788071, 79.891281)));

        //  CameraPosition Liberty = CameraPosition.builder().target(new LatLng(6.788071, 79.891281)).zoom(16).bearing(0).tilt(45).build();
        //  googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        // goToLocationZoom(39.008224,-76.8984527,16);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);   */

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }


    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);

    }

    public void geoLocation() throws IOException {
        EditText et = (EditText) getActivity().findViewById(R.id.editText);
        String getlocation =(et.getText().toString());


        if(!"".equals(getlocation)) {
            String location = (getlocation+ ",Sri Lanka");
            Geocoder gc = new Geocoder(getActivity());
            List<Address> list = gc.getFromLocationName(location, 1);
            Address address;
            if (list.size() > 0)
                address = list.get(0);
            else {
                Toast.makeText(getActivity(), "Location not found!!", Toast.LENGTH_SHORT).show();
                return;
            }
            String locality = address.getLocality();
            Toast.makeText(getActivity(), locality, Toast.LENGTH_LONG).show();

            double lat = address.getLatitude();
            double lng = address.getLongitude();
            goToLocationZoom(lat, lng, 16);


            //   new requestData(getlocation,lat,lng).execute();
            new AsyncTaskrunner(getlocation, lat, lng).execute("dfg");

        }
        else {
            Toast.makeText(getActivity(),"Enter Location!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class AsyncTaskrunner extends AsyncTask<String, Void, String> {

        String location;
        double latitude;
        double longitude;

        public AsyncTaskrunner(String location, double latitude, double longitude) {
            this.location = location;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            String text = "";

            try {
                String encode="ascii";
                // String data="";
                String data = "{" + URLEncoder.encode("location", encode) + ":"
                        + URLEncoder.encode(location, encode);
                data += "," + URLEncoder.encode("latitude", encode) + ":"
                        + URLEncoder.encode(String.valueOf(latitude), encode);

                data += "," + URLEncoder.encode("longitude", encode)
                        + "=" + URLEncoder.encode(String.valueOf(longitude), encode) +"}";

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("location",location);
                jsonParam.put("latitude", latitude);
                jsonParam.put("longitude",longitude);

                HashMap<String,String> hash_param = new HashMap<>();
                hash_param.put("location",location);
                hash_param.put("latitude", Double.toString(latitude));
                hash_param.put("longitude", Double.toString(longitude));
                // JSONObject j_data = new JSONObject(data);
                Log.d("Tag", jsonParam.toString());

                // Send data

                // Defined URL where to send data
                URL url = new URL(

                        "http://matrix.projects.mrt.ac.lk:3000/weatherAndroid"
                );


                // Send POST data request
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(
                        conn.getOutputStream());

                wr.write(String.valueOf(jsonParam));
                wr.flush();


                // Get the server response
                reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                // return reader.toString();
                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line);
                }
                text = sb.toString();
                Log.d("da", text);

            } catch (Exception ex) {
                return ex.toString();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                    return ex.toString();
                }
            }

            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("",s);
            try {
                JSONObject jsonResponse = new JSONObject(s);
                Boolean success = jsonResponse.getBoolean("success");
                Log.e("=================", String.valueOf(success));
                if (success){
                    viewUpdate.setText("Temperature - " + jsonResponse.getString("temperature") +"\n"+
                            "Weather type - " + jsonResponse.getString("weather") + "\n"+
                            "Cloud type - " + jsonResponse.getString("cloud") + "\n"+
                            "Wind type - " + jsonResponse.getString("wind"));
                    onSearch=false;

                }else{
                    Toast.makeText(getActivity(),"No weather data found", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            String[] responses;
//            responses = new s.split(",");
//            viewUpdate.setText("temperature - " + jsonResponse.getString("temperature") + "\n" +
////                            "Weather type - " + jsonResponse.getString("weather") + "\n" +
////                            "Cloud type - " + jsonResponse.getString("cloud") + "\n" +
////                            "Wind type - " + jsonResponse.getString("wind"));
        }
    }

//    protected class requestData extends AsyncTask<Void, Void, Void> {
//        String location;
//        double latitude;
//        double longitude;
//        JSONObject jsonResponse = null;
//
//
//        public requestData(String location, double latitude, double longitude) {
//            this.location = location;
//            this.latitude = latitude;
//            this.longitude = longitude;
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            Response.Listener<String> responseListner = new Response.Listener<String>() {
//
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        jsonResponse = new JSONObject(response);
//                        jsonResponse.put("success", true);
//                        jsonResponse.put("temperature", 33);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                ;
//            };
//            GetDataRequest getDataRequest = new GetDataRequest(location, Double.toString(latitude), Double.toString(longitude), responseListner);
//            RequestQueue queue = Volley.newRequestQueue(getActivity());
//            queue.add(getDataRequest);
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            try {
//                Log.e("=================", "Here");
//                if (jsonResponse != null || (boolean) jsonResponse.getBoolean("success")) {
//                    viewUpdate.setText("temperature - " + jsonResponse.getString("temperature") + "\n" +
//                            "Weather type - " + jsonResponse.getString("weather") + "\n" +
//                            "Cloud type - " + jsonResponse.getString("cloud") + "\n" +
//                            "Wind type - " + jsonResponse.getString("wind"));
//                } else {
//                    Toast.makeText(getActivity(), "No weather data found", Toast.LENGTH_LONG).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000000);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);



    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {

        if (!onSearch)
            if(location==null){
                Toast.makeText(getContext(),"Can't get current location", Toast.LENGTH_LONG).show();
            }
            else {
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,16);
                mGoogleMap.animateCamera(update);
                la= Double.toString(location.getLatitude());
                ln=Double.toString(location.getLongitude());

            }



    }
/*
    private void searchWeather(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://myserveraddress";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONObject obj = new JSONObject(response);


                            Boolean success = obj.getBoolean("success");
                            Log.e("=================", String.valueOf(success));
                            if (success){
                                viewUpdate.setText("temperature - " + obj.getString("temperature") +"\n"+
                                        "Weather type - " + obj.getString("weather") + "\n"+
                                        "Cloud type - " + obj.getString("cloud") + "\n"+
                                        "Wind type - " + obj.getString("wind"));
                            }else{
                                Toast.makeText(getActivity(),"No weather data found", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "test");
                return params;
            }
        };

        queue.add(strRequest);
    }*/

}
