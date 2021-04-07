package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button getCityId;
    Button getWeatherCityByid;
    Button getWeatherByName;
    EditText editText;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCityId  = findViewById(R.id.btn_getCityId);
        getWeatherCityByid = findViewById(R.id.btn_getWeatherCityById);
        getWeatherByName = findViewById(R.id.btn_getWeatherCityByName);
        editText = findViewById(R.id.edit_text);
        listView = findViewById(R.id.list_view);
        final WeatherDataService weatherDataService = new WeatherDataService();

        getCityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 weatherDataService.getCityById(editText.getText().toString(), MainActivity.this, new WeatherDataService.VolleyResponseListener() {
                   @Override
                   public void onError(String message) {
                       Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onResponse(String response) {
                       Toast.makeText(MainActivity.this,"City Id = "+response,Toast.LENGTH_SHORT).show();
                   }
               });


            }
        });

        getWeatherCityByid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDataService.getWeatherCityById(editText.getText().toString(), MainActivity.this, new WeatherDataService.GetWeatherCityData() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherDataModel> response) {
                        ArrayAdapter adapter =  new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,response);
                        listView.setAdapter(adapter);
                    }
                });
            }
        });

        getWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weatherDataService.getWeatherByName(editText.getText().toString(), MainActivity.this, new WeatherDataService.GeCityByName() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherDataModel> response) {
                        ArrayAdapter adapter =  new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,response);
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
