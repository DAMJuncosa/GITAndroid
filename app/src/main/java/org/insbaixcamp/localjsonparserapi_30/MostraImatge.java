package org.insbaixcamp.localjsonparserapi_30;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class MostraImatge extends AppCompatActivity implements Response.Listener<Bitmap>, Response.ErrorListener {
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_imatge);
        String ver;
        String name;
        String api;
        TextView tvVercio;
        TextView tvApi;
        ImageRequest imageRequest;

        //Recuperar l extra de les dades anteriors.
        Intent intent = getIntent();
        ver = intent.getStringExtra(MainActivity.SUBCATEGORIA1);
        name = intent.getStringExtra(MainActivity.SUBCATEGORIA2).toLowerCase();
        api = intent.getStringExtra(MainActivity.SUBCATEGORIA3);
        tvVercio = findViewById(R.id.tvVersio);
        tvApi = findViewById(R.id.tvApi);
        ivLogo = findViewById(R.id.ivLogo);
        tvVercio.setText(ver);
        tvApi.setText(api);
        if (name.equals("logo")) {
            ivLogo.setImageResource(R.drawable.ic_jewish);
        } else {
            //https://www.vidalibarraquer.net/android/androidVersions/kitkat.jpg
            String link = new String("https://www.vidalibarraquer.net/android/androidVersions/" + name + ".jpg");
            //Toast.makeText(this, link, Toast.LENGTH_LONG).show();
            imageRequest = new ImageRequest(
                    link,
                    this,
                    0,
                    0,
                    ImageView.ScaleType.CENTER_INSIDE,
                    null,
                    null

            );
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(imageRequest);
        }
    }
    @Override
    public void onResponse(Bitmap response) {
        ivLogo.setImageBitmap(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(MostraImatge.this, error.toString(), Toast.LENGTH_LONG);
    }
}