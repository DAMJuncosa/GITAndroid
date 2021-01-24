package org.insbaixcamp.localjsonparserapi_30;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    ListView lvLlista;
    ArrayList<HashMap<String, String>> llistaAndroid;
    private final int MENU_OP1 = 1;
    private final String ARXIU_JSON = "android_versions.json";
    private final String URL_JSON = "https://www.vidalibarraquer.net/android/androidVersions/versions.json";
    static protected final String CATEGORIA = new String("android");
    static protected final String SUBCATEGORIA1 = new String("ver");
    static protected final String SUBCATEGORIA2 = new String("name");
    static protected final String SUBCATEGORIA3 = new String("api");
    static protected final String NOM = "Adrià Juncosa Garica";
    static protected final String NOM_EMPRESA = "Jewish Programin Company S.L";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton bGet = findViewById(R.id.bGet);
        lvLlista = findViewById(R.id.lvLlista);
        bGet.setOnClickListener(this);
        lvLlista.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu;

        // Forma 1: utilitzant el menu_main.xml
        // Imflem el menú afegint items dins del codi xml.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // L'Objecte "menu" no permet mostrar icones,
        // per decisió de la guia d'estils d'Android.
        // Sintaxi:
        // add (int groupId, int itemId, int order, CharSequence title)

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = new Intent(this, MostraImatge.class);
        intent.putExtra(SUBCATEGORIA1, NOM);
        intent.putExtra(SUBCATEGORIA2, "logo");
        intent.putExtra(SUBCATEGORIA3, NOM_EMPRESA);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        //Snackbar.make(v, "ReOplace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        if (!(hiHaConnexio())) {
            Snackbar.make(v, R.string.no_conexio, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            //Toast.makeText(this, R.string.no_conexio, Toast.LENGTH_LONG);
        } else {
            // Agafem l'array que volem desde l'arxiu JSON
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_JSON, null, this, this);
            //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://insbaixcamp.org/android/versions.json", null, this, null);
            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        //Snackbar.make (v, "Funciona!", Snackbar.LENGTH_LONG).setAction(null, null).show();
        JSONObject jsonObject;
        JSONArray jsonArrayAndroidVersions;
        String versio;
        String nom;
        String api;
        HashMap<String, String> hashMap;
        llistaAndroid = new ArrayList<HashMap<String, String>>();
        ListAdapter adapter;

        try {
            jsonObject = new JSONObject(carregaJsonLocal());
            jsonArrayAndroidVersions = jsonObject.getJSONArray(CATEGORIA);
            for (int i = 0; i < jsonArrayAndroidVersions.length(); i++) {
                JSONObject jsonObectLine = jsonArrayAndroidVersions.getJSONObject(i);
                // Desem l'ítem JSON en una variable
                versio = jsonObectLine.getString(SUBCATEGORIA1);
                nom = jsonObectLine.getString(SUBCATEGORIA2);
                api = jsonObectLine.getString(SUBCATEGORIA3);
                // Afegim la clau-valor a un objecte HashMap
                hashMap = new HashMap<String, String>();
                hashMap.put(SUBCATEGORIA1, versio);
                hashMap.put(SUBCATEGORIA2, nom);
                hashMap.put(SUBCATEGORIA3, api);
                llistaAndroid.add(hashMap);
                adapter = new SimpleAdapter(MainActivity.this,
                        llistaAndroid,
                        R.layout.llista,
                        new String[]{SUBCATEGORIA1, SUBCATEGORIA2, SUBCATEGORIA3},
                        new int[]{R.id.tvVersio, R.id.tvNom, R.id.tvApi}
                );
                lvLlista.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String carregaJsonLocal() {
        InputStream inputStream;
        byte[] buffer;
        String stringJson = null;

        try {
            inputStream = getResources().getAssets().open(ARXIU_JSON);
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            stringJson = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringJson;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, "Has clicat" + llistaAndroid.get(position).get("name"), Toast.LENGTH_SHORT).show();
        // Fer que aquí surti algun valor de la ListView
        Intent intent = new Intent(this, MostraImatge.class);
        intent.putExtra(SUBCATEGORIA1, llistaAndroid.get(position).get(SUBCATEGORIA1));
        intent.putExtra(SUBCATEGORIA2, llistaAndroid.get(position).get(SUBCATEGORIA2));
        intent.putExtra(SUBCATEGORIA3, llistaAndroid.get(position).get(SUBCATEGORIA3));
        startActivity(intent);

    }

    private boolean hiHaConnexio() {
        boolean resultat = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Comprovem la versió del dispositiu Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    resultat = true;
                }
            }
        } else {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                resultat = true;
            } else {
                resultat = false;
            }
        }

        return resultat;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }
}