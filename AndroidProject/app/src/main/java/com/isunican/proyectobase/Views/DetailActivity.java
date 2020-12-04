package com.isunican.proyectobase.Views;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.isunican.proyectobase.Presenter.PresenterGasolineras;
import com.isunican.proyectobase.R;
import com.isunican.proyectobase.Model.*;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


/*
------------------------------------------------------------------
    Vista de detalle

    Presenta datos de detalle de una Gasolinera concreta.
    La gasolinera a mostrar se le pasa directamente a la actividad
    en la llamada por intent (usando putExtra / getExtra)
    Para ello Gasolinera implementa la interfaz Parcelable
------------------------------------------------------------------
*/
public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView imgG;
    TextView txtNomG;
    TextView txtTipoGasolina;
    TextView txtPrecioGasolina;
    TextView txtDir;
    MapView mapDir;
    Gasolinera g;

    PresenterGasolineras presenter;

    /**
     * onCreate
     * <p>
     * Crea los elementos que conforman la actividad
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.presenter = new PresenterGasolineras();
        // muestra el logo en el actionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.por_defecto_mod);



        mapDir = (MapView) findViewById(R.id.mapGasolinera);
        mapDir.onCreate(savedInstanceState);
        mapDir.getMapAsync(this);

        // captura el TextView
        // obtiene el objeto Gasolinera a mostrar
        // y lo introduce en el TextView convertido a cadena de texto
        imgG = findViewById(R.id.imgGasolinera);
        txtNomG = findViewById(R.id.txtNomGasolinera);
        txtTipoGasolina = findViewById(R.id.txtTipoGasolina);
        txtPrecioGasolina = findViewById(R.id.txtPrecioGasolina);
        txtDir = findViewById(R.id.txtDireccion);
        g = getIntent().getExtras().getParcelable(getResources().getString(R.string.pasoDatosGasolinera));
        //tipo de combustible seleccionado
        String tipoCombustible = getIntent().getExtras().getString(getResources().getString(R.string.pasoTipoCombustible));

        txtNomG.setText(g.getRotulo());
        txtTipoGasolina.setText(tipoCombustible);

        //precio  del combustible seleccionado
        Double precioCombustible = presenter.getPrecioCombustible(tipoCombustible, g);
        txtPrecioGasolina.setText(precioCombustible + "€");
        txtDir.setText(g.getDireccion());

        String rotuleImageID = g.getRotulo().toLowerCase();

        // Tengo que protegerme ante el caso en el que el rotulo solo tiene digitos.
        // En ese caso getIdentifier devuelve esos digitos en vez de 0.
        int imageID = this.getResources().getIdentifier(rotuleImageID,
                "drawable", this.getPackageName());

        if (imageID == 0 || TextUtils.isDigitsOnly(rotuleImageID)) {
            imageID = this.getResources().getIdentifier(getResources().getString(R.string.pordefecto),
                    "drawable", this.getPackageName());
        }
        imgG.setImageResource(imageID);

    }

    @Override
    public void onResume() {
        super.onResume();
        mapDir.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapDir.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapDir.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }

        Log.v("DEBUG", "Permisos solicitados");
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        Log.v("DEBUG", String.valueOf(map));
        //Si las coordenadas de la gasolinera son erroneas
        //se establece una vision de la provincia de cantabria
        //por defecto
        if(g.getLatitud() == -1 || g.getLongitud() == -1) {
            LatLng cantabria = new LatLng(43.2, -4.03333);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cantabria, 8.55f));
        }else {
            LatLng latLng =new LatLng(g.getLatitud(), g.getLongitud());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            map.addMarker(new MarkerOptions().position(latLng).title(g.getRotulo())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }


    }

    @Override
    public void onPause() {
        mapDir.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapDir.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapDir.onLowMemory();
    }

}

