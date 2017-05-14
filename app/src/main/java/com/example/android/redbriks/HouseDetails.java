package com.example.android.redbriks;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Random;

public class HouseDetails extends AppCompatActivity {
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

//    public String[] cityArray = {"Fremont", "San Jose", "Santa Clara", "Sunnyvale", "Palo alto","Fremont"};
//    public int[] houseIdArray = {100, 200, 300, 400, 500,600};
//    public String[] houseAddressArray = {"3750 Tamayo st Fremont CA",
//            "1073 Burntwood Avenue Sunnyvale CA",
//            "610 Park View Drive, Santa Clara, CA",
//            "3750 Tamayo st Fremont CA",
//            "610 Park View Drive, Santa Clara, CA",
//            "610 Park View Drive, Santa Clara, CA"};
//    public int[] builtYearArray = {1980, 1999, 2010, 2004, 2008,2000};
//    public int[] noOfBedroomArray = {1, 3, 4, 3, 2,3};
//    public int[] areaArray = {1500, 2487, 3050, 2398, 2065,3209};
//    public int[] rentArray = {2200, 3400, 4000, 3100, 2500,3000};


    ImageView imageView;
    TextView tx_house_id, tx_rent, tx_address, tx_built_year,tx_bedroom,tx_property_area;
    String dAddress;
    int houseID;
    String phoneNumber = "14087977662";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<House> list = new ArrayList<House>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        imageView = (ImageView) findViewById(R.id.d_house_image);
        tx_house_id = (TextView) findViewById(R.id.d_house_id);
        tx_address = (TextView) findViewById(R.id.d_address_id);
        tx_built_year = (TextView) findViewById(R.id.d_built_year_id);
        tx_bedroom = (TextView) findViewById(R.id.d_type_id);
        tx_property_area =  (TextView) findViewById(R.id.d_area_id);
        tx_rent = (TextView) findViewById(R.id.d_rent_id);

        dAddress = tx_address.getText().toString();

        Intent i = getIntent();

        // Get house details which is clicked
        House currentHouse = (House) i.getParcelableExtra("House");

        //displaying house details on the view
        imageView.setImageResource(currentHouse.getImage_id());
        houseID = currentHouse.getHouse_id();
        tx_house_id.setText(String.valueOf(houseID));
        tx_address.setText(currentHouse.getHouse_address());
        tx_built_year.setText(String.valueOf(currentHouse.getBuild_year()));
        tx_bedroom.setText(String.valueOf(currentHouse.getNo_of_bedroom()));
        tx_property_area.setText(String.valueOf(currentHouse.getProperty_area()));
        tx_rent.setText(String.valueOf(currentHouse.getPrice()));

        String url = "http://104.198.212.96/project277/index.php/welcome/getRecommendedHouses";
        Log.d("URL", url);
        new RetrieveFeedTask().execute(url);

        recyclerView = (RecyclerView) findViewById(R.id.d_recycler_view);

      //  layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager = new GridLayoutManager(recyclerView.getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        adapter = new HouseAdapter(list, this);
//        recyclerView.setAdapter(adapter);

    }

    public String getTrimmedCity(String cityName){

        String name = cityName.replace(" ","+");
        return name;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;
        protected String doInBackground(String... urls) {

            String url = urls[0];

            try {

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);
                // replace with your url
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
        }
    }


    public void getDirectionToHouse(View v) {
        dAddress = tx_address.getText().toString();
        String houseAddress = "http://maps.google.co.in/maps?q=" + dAddress;
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(houseAddress));
        startActivity(mapIntent);
    }

    public void virtualTour(View v) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=Q1x_wnHR-nM")));

    }

    public void callUS(View v) {
        try {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Activity not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public void emailUS(View v) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"kadam.shital@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "House inquiry about House ID: " + houseID);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hello Redbricks");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public void openAugu(View view) {

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.Sitepoint.RSAR");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }
}
