package com.example.workingonvolley.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.workingonvolley.R;
import com.example.workingonvolley.singletonclasses.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{
    private static String FEED_URL = "http://services.hanselandpetal.com/feeds/flowers.json";
    private Context context;

    private LruCache<Integer,Bitmap> imageCache;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        imageView = (ImageView) findViewById(R.id.image);
        setImageCache();

//        RequestQueue requestQueue = Volley.newRequestQueue(this); // Not Recommended

        RequestQueue requestQueue = VolleySingleton.getVolleySingletonInstance().getRequestQueue();
        StringRequest stringRequest = getStringRequest();
        JsonArrayRequest jsonArrayRequest = getJsonArrayRequest();
        JsonObjectRequest jsonObjectRequest = getJsonObjectRequest();
        ImageRequest imageRequest = getImageRequest();



        requestQueue.add(stringRequest);
//        requestQueue.add(jsonArrayRequest);
//        requestQueue.add(jsonObjectRequest);
//        requestQueue.add(imageRequest);

    }

    private void setImageCache()
    {
        final int maximum_memory = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cache_size = maximum_memory/8;
        imageCache = new LruCache<>(cache_size);
    }

    private StringRequest getStringRequest()
    {
        //  @params   REQUEST_METHOD,REQUEST_URL,RESPONSE,ERROR_RESPONSE
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                FEED_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        handleVolleyError(error);
                    }
                });
        return stringRequest;
    }

    private JsonArrayRequest getJsonArrayRequest()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                FEED_URL,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        handleVolleyError(error);
                    }
                });
        return jsonArrayRequest;
    }

    private JsonObjectRequest getJsonObjectRequest()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://45.79.180.7/api/v1/categories",
                null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        handleVolleyError(error);
                    }
                });

        return jsonObjectRequest;
    }

    private ImageRequest getImageRequest()
    {
        String image_url = "http://photy.me/api/v1/image/224e0947440ce00f639798c332fd6c271480424092032.jpeg";

        ImageRequest imageRequest = new ImageRequest(
                image_url,
                new Response.Listener<Bitmap>()
                {
                    @Override
                    public void onResponse(Bitmap response)
                    {
                        imageView.setImageBitmap(response);
                    }
                },
                100, 100,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        handleVolleyError(error);
                    }
                });
        return imageRequest;
    }

    private void handleVolleyError(VolleyError error)
    {
        if (error instanceof AuthFailureError || error instanceof TimeoutError)
        {
            Toast.makeText(context,"AuthFailureError/TimeoutError",Toast.LENGTH_LONG).show();
        }
        else if (error instanceof NoConnectionError)
        {
            Toast.makeText(context,"NoConnectionError",Toast.LENGTH_LONG).show();
        }
        else if (error instanceof NetworkError)
        {
            Toast.makeText(context,"NetworkError",Toast.LENGTH_LONG).show();
        }
        else if (error instanceof ServerError)
        {
            Toast.makeText(context,"ServerError",Toast.LENGTH_LONG).show();
        }
        else if (error instanceof ParseError)
        {
            Toast.makeText(context,"ParseError",Toast.LENGTH_LONG).show();
        }
    }

}
