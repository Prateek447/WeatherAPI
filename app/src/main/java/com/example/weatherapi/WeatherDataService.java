package com.example.weatherapi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {





    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_WEATHER_CITY_BY_ID = "https://www.metaweather.com/api/location/";


    //Callback method for get Id
    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String response);
    }


    public  void getCityById(String cityName, final Context context, final VolleyResponseListener vollyResponseListener){



        // RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = QUERY_FOR_CITY_ID +cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String id ="";
                try {
                    JSONObject jsonObject =  response.getJSONObject(0);
                    id =  jsonObject.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                vollyResponseListener.onResponse(id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 vollyResponseListener.onError("Something went wrong");
            }
        });
        // queue.add(request);
        MySIngleton.getInstance(context).addToRequestQueue(request);

       // return id;
    }



//Callback for getWeatherCityByID
    public interface GetWeatherCityData {
        void onError(String message);

        void onResponse(List<WeatherDataModel> response);
    }

    public void getWeatherCityById(String cityId, final Context context, final GetWeatherCityData getWeatherCityData){

        String url  = QUERY_WEATHER_CITY_BY_ID +cityId +"/";
        JsonObjectRequest request =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
              //  Toast.makeText(context,response.toString(),Toast.LENGTH_SHORT).show();

                List<WeatherDataModel>  list = new ArrayList<>();
                try {
                    JSONArray  consolidated_weather =  response.getJSONArray("consolidated_weather");

                    int index = 0;
                   // Log.d("Success",String.valueOf(index));
                    while (index<consolidated_weather.length()){

                        JSONObject jsonObject =  consolidated_weather.getJSONObject(index);


                        int id =  jsonObject.getInt("id");
                        String weather_state_name =  jsonObject.getString("weather_state_name");
                       String weather_state_abbr =  jsonObject.getString("weather_state_abbr");
                       String wind_direction_compass =  jsonObject.getString("wind_direction_compass");
                       String  created =  jsonObject.getString("created");
                        Log.d("Successfully","Data is Added");
                      String  applicable_date = jsonObject.getString("applicable_date");
                       float min_temp =  (float)jsonObject.getDouble("min_temp");
                      float  max_temp =  (float) jsonObject.getDouble("max_temp");
                       float the_temp =  (float) jsonObject.getDouble("the_temp");
                       float wind_speed =  (float) jsonObject.getDouble("wind_speed");
                      float  wind_direction =  (float) jsonObject.getDouble("wind_direction");
                      float  air_pressure =  (float) jsonObject.getDouble("air_pressure");
                      float  humidity =  (float)  jsonObject.getDouble("humidity");
                      float  visibility =  (float) jsonObject.getDouble("visibility");
                      float  predictability =  (float)  jsonObject.getDouble("predictability");

                        list.add(new WeatherDataModel(id,weather_state_name,weather_state_abbr,wind_direction_compass,created,applicable_date,
                                min_temp,max_temp,the_temp,wind_speed,wind_direction,air_pressure,humidity,visibility,predictability));
                        index++;


                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                getWeatherCityData.onResponse(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getWeatherCityData.onError("Something went Wrong");
            }
        });

        MySIngleton.getInstance(context).addToRequestQueue(request);

    }


    //Callback for getWeatherCityByID
    public interface GeCityByName {
        void onError(String message);

        void onResponse(List<WeatherDataModel> response);
    }

    public  void  getWeatherByName(String name , final Context context, final GeCityByName geCityByName){

        getCityById(name, context, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String responseId) {

                 getWeatherCityById(responseId, context, new GetWeatherCityData() {
                     @Override
                     public void onError(String message) {
                         geCityByName.onError(message);
                     }

                     @Override
                     public void onResponse(List<WeatherDataModel> responseData) {
                            geCityByName.onResponse(responseData);
                     }
                 });
            }
        });
    }


}
