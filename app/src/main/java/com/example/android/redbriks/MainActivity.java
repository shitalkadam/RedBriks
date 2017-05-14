package com.example.android.redbriks;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.android.smartstreet.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public int[] houseImageArray = {
            R.drawable.house1,
            R.drawable.house2,
            R.drawable.house3,
            R.drawable.house4,
            R.drawable.house5,
            R.drawable.house6,
            R.drawable.house7,
            R.drawable.house8,
            R.drawable.house9,
            R.drawable.house10,
            R.drawable.house11,
            R.drawable.house12,
            R.drawable.house13,
            R.drawable.house14,
            R.drawable.house15
    };

    LocationManager locationManager;
    Location location;
    UserProfile user;
    String name = "user";
    String email = "abc@example.com";
    String state,currentCity;
    ImageButton searchButton;
    TextView cityText;
    TextView stateText;
    double lat;
    double lang;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    SearchPreference searchPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = getIntent().getStringExtra("firstname");
        email = getIntent().getStringExtra("email");
//        user.setFirstName(name);


 //       user.setEmail(email);

        customActionBar();

        cityText = (TextView) findViewById(R.id.city_text);
        stateText = (TextView) findViewById(R.id.state_text);

        searchButton = (ImageButton) findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchHouse();
            }
        });

        searchPreference = new SearchPreference();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Criteria criteria = new Criteria();
        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if(location!= null) {
            lat = location.getLatitude();
            lang = location.getLongitude();
            try {
                currentCity = getCityName(lat, lang);
                cityText.setText(currentCity);
                stateText.setText(state);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            currentCity = "San Jose";
            cityText.setText(currentCity);
            stateText.setText("California");
        }

        searchPreference.setAgeRangePreference("Young");
        searchPreference.setCityPreference(currentCity);
        searchPreference.setFamilySizePreference("Medium");
        searchPreference.setRentSizePreference("Low");


//        currentCity = "San Jose";
//        cityText.setText(currentCity);

        String trimmedCityName = getTrimmedCity(currentCity);

        String url = "http://104.198.212.96/project277/index.php/welcome/getHouseByLocation?city=" + trimmedCityName;
        Log.d("URL", url);
        new RetrieveFeedTask().execute(url);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //  layoutManager = new LinearLayoutManager(this);
        layoutManager = new GridLayoutManager(recyclerView.getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }

    public String getTrimmedCity(String cityName){

        String name = cityName.replace(" ","+");
        return name;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {
        ArrayList<House> list = new ArrayList<House>();
        private Exception exception;
        protected String doInBackground(String... urls) {

            String url = urls[0];

            try {

                HttpClient client = new DefaultHttpClient();
                Log.d("Msg:", "Object Created");
                HttpGet request = new HttpGet(url);
                // replace with your url
                Log.d("Msg:", "Get executed");
                HttpResponse response = null;
                try {

                    response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    Log.d("Response of GET request", sb.toString());

                    JSONArray ja = new JSONArray(sb.toString());
                    Log.d("city",ja.toString());
                    for (int i = 0;  i < ja.length(); i++) {

                        JSONObject item = ja.getJSONObject(i);
                        House h = new House();
                        h.setCity(item.getString("city"));
                        h.setHouse_address(item.getString("address"));
                        h.setBuild_year(item.getInt("buildYear"));
                        h.setHouse_id(item.getInt("house_id"));
                        h.setNo_of_bedroom(item.getInt("bedroom"));
                        h.setPrice(item.getInt("price"));
                        h.setProperty_area(item.getInt("area"));
                        h.setProperty_area(item.getInt("area"));

                        Random r = new Random();
                        int i1 = (r.nextInt(14)+0);

                        h.setImage_id(houseImageArray[i1]);
                        list.add(h);
                    }

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return response.toString();
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String str) {

            adapter = new HouseAdapter(list, getApplicationContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

   /* class SendFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        StatusLine statusLine;
        HttpResponse response;
        byte[] result = null;
        String str;

        private ArrayList<String> mData = null;

        public void AsyncHttpPost() {

        }

        //JSONArray arr_strJson = new JSONArray(obj_list));
        protected String doInBackground(String... urls) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://146.148.40.179/project277/index.php/welcome/dbtest277");

                try {
                    // Add your data

                    JSONObject json=new JSONObject();
                    json.put("city",searchPreference.getCityPreference());
                    json.put("ageType",searchPreference.getAgeRangePreference());
                    json.put("familySize",searchPreference.getFamilySizePreference());
                    json.put("rentRange",searchPreference.getRentSizePreference());

                    Log.d("JSON data is displayed",json.toString());
                    List nameValuePairs = new ArrayList(1);
                    nameValuePairs.add(new BasicNameValuePair("json", json.toString()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    response = httpclient.execute(httppost);
                    StatusLine statusLine = response.getStatusLine();
                    String responseBody = EntityUtils.toString(response.getEntity());
                    Log.d("JSON","RESPONSE is displayed : " + responseBody);

                } catch (ClientProtocolException e) {
                    Log.e("JSON",e.getMessage());
                } catch (IOException e) {
                    Log.e("JSON", e.getMessage());
                } catch (JSONException e) {
                    Log.e("JSON", e.getMessage());
                }
                if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                    result = EntityUtils.toByteArray(response.getEntity());
                    String str = new String(result, "UTF-8");
                }
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
            return str;
        }

        protected void onPostExecute(Object[] obj) {
        }
    }*/

    /**
     * This function creates the custom toolbar for the application
     * Including the application icon and application name and a refresh button
     */
    public void customActionBar() {
        android.support.v7.app.ActionBar myActionBar = getSupportActionBar();
        if (myActionBar != null) {
            myActionBar.setDisplayShowHomeEnabled(false);
        }
        if (myActionBar != null) {
            myActionBar.setDisplayShowTitleEnabled(false);
        }
        //layout inflater will inflate the custom_actionbar layout in actionbar
        LayoutInflater MyLayout = LayoutInflater.from(this);
        View View = MyLayout.inflate(R.layout.custom_actionbar, null);

        //Toast will appear on clicking logobutton
        if (myActionBar != null) {
            myActionBar.setCustomView(View);
        }
        if (myActionBar != null) {
            myActionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                //Do Some Task
                return true;
            case R.id.action_settings:
                //Do Some Task
                return true;
            case R.id.action_logout:
                //Do Some Task
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //launches the register activity
    public void register(View view) {
        Intent register = new Intent(this, Registration.class);
        startActivity(register);
        this.finish();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /*Started code for Redbriks*/

    public void doNothing(View v) {

    }
    public void displayComments(View view) {
        Intent intent = new Intent(this, ReviewListActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void shareOnFacebook(View v) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "http://www.google.com");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public String getCityName(double lat1, double lang1) throws IOException {

        String cityName = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat1, lang1, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        cityName = addresses.get(0).getLocality();
        state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        return cityName;
    }


    private void searchHouse() {

        searchPreference = new SearchPreference();
        DialogFragment df = new DialogFragment() {
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.user_preference, container);
                //   getDialog().setTitle("Add a Review and Rating");
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

                final Button confirmButton = (Button) view.findViewById(R.id.preference_ok);
                final Button cancelButton = (Button) view.findViewById(R.id.preference_cancel);


                final Spinner citySpinner = (Spinner) view.findViewById(R.id.location_spinner);
                final Spinner ageRangeSpinner = (Spinner) view.findViewById(R.id.agerange_spinner);
                final Spinner priceSpinner = (Spinner) view.findViewById(R.id.price_spinner);

                ArrayAdapter cityAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                        R.array.city_range, android.R.layout.simple_spinner_item);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(cityAdapter);


                ArrayAdapter ageAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                        R.array.age_range, android.R.layout.simple_spinner_item);
                ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ageRangeSpinner.setAdapter(ageAdapter);


                ArrayAdapter priceAdapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.rent_range, android.R.layout.simple_spinner_item);
                priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                priceSpinner.setAdapter(priceAdapter);

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchPreference.setCityPreference(citySpinner.getSelectedItem().toString());
                        int ageType = ageRangeSpinner.getSelectedItemPosition();

                        switch (ageType) {
                            case 0:
                                searchPreference.setAgeRangePreference("Young");
                                break;
                            case 1:
                                searchPreference.setAgeRangePreference("Middle");
                                break;
                            case 2:
                                searchPreference.setAgeRangePreference("Old");
                                break;
                            case (Spinner.INVALID_POSITION):
                                searchPreference.setAgeRangePreference("Young");
                                break;
                            default:
                                searchPreference.setAgeRangePreference("Young");
                                break;
                        }


                        int rentRange = priceSpinner.getSelectedItemPosition();
                        switch (rentRange) {
                            case 0:
                                searchPreference.setRentSizePreference("Low");
                                break;
                            case 1:
                                searchPreference.setRentSizePreference("Medium");
                                break;
                            case 2:
                                searchPreference.setRentSizePreference("High");
                                break;
                            case (Spinner.INVALID_POSITION):
                                searchPreference.setRentSizePreference("Low");
                                break;
                            default:
                                searchPreference.setRentSizePreference("Low");
                                break;
                        }

                        String preferedCity= getTrimmedCity(searchPreference.getCityPreference());

                        StringBuilder urlString = new StringBuilder("http://104.198.212.96/project277/index.php/welcome/getSearchedHouses?");
                        urlString.append("city=").append(preferedCity).append("&rentRange=").append(searchPreference.getRentSizePreference());

                        new RetrieveFeedTask().execute(urlString.toString());

                        dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                return view;
            }

            @Override
            public void onAttach(Context context) {
                super.onAttach(context);
            }
        };
        df.show(getFragmentManager(), "");
    }
}
