package com.isunican.proyectobase.Views;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputLayout;
import com.isunican.proyectobase.Presenter.*;
import com.isunican.proyectobase.Model.*;
import com.isunican.proyectobase.R;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.os.Looper;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/*
------------------------------------------------------------------
    Vista principal

    Presenta los datos de las gasolineras en formato lista.

------------------------------------------------------------------
*/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FLECHA_ARRIBA = "flecha_arriba";
    private static final String FLECHA_ABAJO = "flecha_abajo";
    private static final String ORDEN_PRECIO = "Precio";
    private static final String ORDEN_DISTANCIA = "Distancia";
    private static final String DRAWABLE = "drawable";
    private static final String FICHERO = "datos.txt";
    public static final String CANCELAR = "Cancelar";
    public static final String FICHERO_UBICACION = "datosUbicacion.txt";

    private static final String DEBUG = "DEBUG";

    //laltitud y longitud de la ubicacion del usuario
    public double latitud;
    public double longitud;

    // El presenter
    private PresenterGasolineras presenterGasolineras;

    // Vista de lista y adaptador para cargar datos en ella
    private ListView listViewGasolineras;
    private ArrayAdapter<Gasolinera> adapter;
    // Swipe and refresh (para recargar la lista con un swipe)
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //Botones de filtro y ordenacion
    private Button buttonFiltros;
    private Button buttonOrden;
    private ImageButton config;
    private ImageView menu;
    private Button buttonConfig;
    private ImageView iconoOrden;
    Button buttonUbicacion;

    TextInputLayout textInputLatitud;
    TextInputLayout textInputLongitud;
    TextView labelLongitud;
    TextView labelLatitud;
    Button buttonCancelar;
    Button buttonEstablecer;


    //DRAWER LAYOUT
    private DrawerLayout drawerLayout;

    /*Variables para modificar filtros y ordenaciones*/
    //orden ascendente por defecto
    private String idIconoOrden = FLECHA_ARRIBA;
    private String criterioOrdenacion = ORDEN_PRECIO;
    private String tipoCombustible = "Gasóleo A"; //Por defecto
    private boolean esAsc = true; //Por defecto ascendente
    private LinkedList<String> operacionesOrdenacion = new LinkedList<>();


    // Coordenadas por defecto
    String txt_latitud = "43.350223552917";
    String txt_longitud = "-4.052258920907";
    String coordenada = txt_latitud + " " + txt_longitud;
    String newCoordenada;



    Activity ac = this;

    //API's que se utilizan para conocer la ubicacion del usuario
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    //API que se utiliza para guardar la ultima ubicacion conocidad del usuario
    private SharedPreferences sharedpreferences;

    /**
     * onCreate
     * <p>
     * Crea los elementos que conforman la actividad
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se solicitan los permison de ubicacion al usuario
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                99);

        //Localizacion
        //Se cargan la latitud y longitud por defecto, o las ultimas conocidad de la ultima vez que
        //que el usuario utilizo la aplicacion
        sharedpreferences = this.getPreferences(Context.MODE_PRIVATE);
        latitud = Double.parseDouble(sharedpreferences.getString("latitud", "0"));
        longitud = Double.parseDouble(sharedpreferences.getString("longitud", "0"));

        //Se iniciliazan los servicios de localizacion para obtener la ubicacion del usuario
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Se actualizara la ubicacion del usuario cada 10 segundos
        locationRequest.setInterval(10000);

        //Listener de los cambios de ubicacion
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();
                    }
                }
            }
        };

        //Se inicializa el presenter de las gasolineras
        this.presenterGasolineras = new PresenterGasolineras();

        try {
            //Lectura inicial del tipo de combustible por defecto
            tipoCombustible = presenterGasolineras.lecturaCombustiblePorDefecto(this, FICHERO);

        } catch (Exception e) {
            e.toString();
            try {
                presenterGasolineras.escrituraCombustiblePorDefecto("Gasóleo A", this, FICHERO);
            } catch (FileNotFoundException ex) {
                ex.toString();
            } catch (IOException exc) {
                exc.toString();

            } catch (PresenterGasolineras.CombustibleNoExistente combustibleNoExistente) {
                combustibleNoExistente.toString();
            }
        }

        try {
            tipoCombustible = presenterGasolineras.lecturaCombustiblePorDefecto(this, FICHERO);
        } catch (IOException e) {
            e.toString();
        }


        try {
            //Lectura inicial de las coordenadas por defecto
            coordenada = presenterGasolineras.lecturaCoordenadaPorDefecto(this, FICHERO);

        } catch(Exception e) {
            try {
                presenterGasolineras.escrituraCoordenadaPorDefecto("43.350223552917 -4.052258920907", this, FICHERO);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }catch (IOException exc){
                exc.printStackTrace();
            } catch (PresenterGasolineras.CoordenadaNoExistente coordenadaNoExistente) {
                coordenadaNoExistente.printStackTrace();
            }
        }

        try {
            coordenada = presenterGasolineras.lecturaCoordenadaPorDefecto(this, FICHERO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        newCoordenada = coordenada;

        drawerLayout = findViewById(R.id.drawer_layout);

        // Muestra el logo en el actionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.por_defecto_mod);
        getSupportActionBar().hide();

        // Swipe and refresh
        // Al hacer swipe en la lista, lanza la tarea asíncrona de carga de datos
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> new CargaDatosGasolinerasTask(MainActivity.this).execute());

        // Al terminar de inicializar todas las variables
        // se lanza una tarea para cargar los datos de las gasolineras
        // Esto se ha de hacer en segundo plano definiendo una tarea asíncrona
        new CargaDatosGasolinerasTask(this).execute();

        //Añadir los listener a los botones

        buttonFiltros = findViewById(R.id.buttonFiltros);
        buttonOrden = findViewById(R.id.buttonOrden);
        config = findViewById(R.id.info);
        menu = findViewById(R.id.menuNav);
        buttonConfig = findViewById(R.id.btnConfiguracion);
        iconoOrden = findViewById(R.id.iconoOrden);
        buttonUbicacion = findViewById(R.id.btnUbicacion);


        buttonFiltros.setOnClickListener(this);
        buttonOrden.setOnClickListener(this);
        config.setOnClickListener(this);
        menu.setOnClickListener(this);
        buttonConfig.setOnClickListener(this);
        iconoOrden.setOnClickListener(this);
        buttonUbicacion.setOnClickListener(this);

        //Valores por defecto cuando inicia la aplicacion
        iconoOrden.setImageResource(getResources().getIdentifier(idIconoOrden,
                DRAWABLE, getPackageName()));
        buttonOrden.setText(getResources().getString(R.string.precio));


        //Se cargan las opciones de ordenacion en la linked list que inyectaremos al spinner correspondiente
        Collections.addAll(operacionesOrdenacion, getResources().getStringArray(R.array.opcionesOrden));


    }


    public void clickMenu() {
        openDrawer(drawerLayout);
    }

    public void clickConfiguracion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title
        builder.setTitle("Configuración");
        // Specify the list array, the items to be selected by default (null for none),

        // Vista escondida del nuevo layout para los diferentes spinners a implementar para los filtros
        View mView = getLayoutInflater().inflate(R.layout.combustible_por_defecto_layout, null);

        final Spinner mSpinner = (Spinner) mView.findViewById(R.id.combustible_por_defecto);// New spinner object
        final TextView comb = mView.findViewById(R.id.porDefecto);
        try {
            comb.setText("Combustible actual: " + presenterGasolineras.lecturaCombustiblePorDefecto(ac, FICHERO));
        } catch (IOException e) {
            e.toString();
        }
        // El spinner creado contiene todos los items del array de Strings "operacionesArray"
        final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.operacionesArray)) {
            @Override
            public boolean isEnabled(int position) {
                boolean habilitado;
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    habilitado = false;
                } else {
                    habilitado = true;
                }
                return habilitado;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        // Al abrir el spinner la lista se abre hacia abajo
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapterSpinner);

        // Set the action buttons
        builder.setPositiveButton("Aplicar", (dialog, id) -> {
            // User clicked Aceptar, save the item selected in the spinner
            // If the user does not select nothing, don't do anything
            if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Combustible")) {
                tipoCombustible = mSpinner.getSelectedItem().toString();
                try {
                    presenterGasolineras.escrituraCombustiblePorDefecto(mSpinner.getSelectedItem().toString(), ac, FICHERO);
                } catch (FileNotFoundException e) {
                    e.toString();
                } catch (IOException ex) {
                    ex.toString();
                } catch (PresenterGasolineras.CombustibleNoExistente combustibleNoExistente) {
                    combustibleNoExistente.toString();
                }
                try {
                    tipoCombustible = presenterGasolineras.lecturaCombustiblePorDefecto(ac, FICHERO);
                } catch (IOException e) {
                    e.toString();
                }
            }


            closeDrawer(drawerLayout);
            refresca();
        });

        builder.setNegativeButton(CANCELAR, (dialog, id) -> dialog.dismiss());

        builder.setView(mView);
        builder.create();
        builder.show();
    }

    /**
     * Segunda opcion en la barra superior de la izquierda para poder anhadir una
     * ubicacion como punto de partida habitual
     */
    public void clickUbicacion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ubicación");

        // Vista escondida del nuevo layout para las diferentes celdas a implementar para los filtros
        View mView = getLayoutInflater().inflate(R.layout.anhadir_ubicacion_punto_partida_layout, null);
        builder.setView(mView);
        AlertDialog dialog = builder.create();


        // Campos necesarios para comprobar todos los casos de error existentes
        textInputLatitud = mView.findViewById(R.id.layout_latitud);
        textInputLongitud = mView.findViewById(R.id.layout_longitud);
        labelLatitud = mView.findViewById(R.id.labelLatitud);
        labelLongitud = mView.findViewById(R.id.labelLongitud);

        buttonCancelar = mView.findViewById(R.id.btn_cancelar);
        buttonEstablecer = mView.findViewById(R.id.btn_establecer);

        final TextView comb = mView.findViewById(R.id.ubicacionPorDefecto);

        try {
            comb.setText("Ubicación actual: " + presenterGasolineras.lecturaCoordenadaPorDefecto(ac, FICHERO_UBICACION));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Llamada al metodo para detectar errores en tiempo de ejecucion.
         */
        detectaErroresTiempoDeEjecucion(textInputLatitud, labelLatitud);
        detectaErroresTiempoDeEjecucion(textInputLongitud, labelLongitud);

        /**
         * Botones
         */
        // Boton Establecer
        buttonEstablecer.setOnClickListener(v -> {
            if ((!validateLatitudLongitud(textInputLatitud,labelLatitud) || !validateLatitudLongitud(textInputLongitud,labelLongitud))) {
                return;
            } else {

                txt_latitud = textInputLatitud.getEditText().getText().toString().trim();
                txt_longitud = textInputLongitud.getEditText().getText().toString().trim();

                newCoordenada = txt_latitud + " " + txt_longitud;


                try {
                    presenterGasolineras.escrituraCoordenadaPorDefecto(newCoordenada, ac, FICHERO_UBICACION);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }catch (IOException ex){
                    ex.printStackTrace();

                } catch (PresenterGasolineras.CoordenadaNoExistente coordenadaNoExistente) {
                coordenadaNoExistente.printStackTrace();
                }

                try {
                    newCoordenada = presenterGasolineras.lecturaCoordenadaPorDefecto(ac, FICHERO_UBICACION);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
                closeDrawer(drawerLayout);
                refresca();
            }
        });

        // Boton Cancelar
        buttonCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    /**
     * Comprobacion en tiempo de ejecucion de los posibles casos de error
     * @param text TextInputLayout que se va a tratar en cada caso.
     * @param label label situado en la zona superior a cada campo
     */
    public void detectaErroresTiempoDeEjecucion(TextInputLayout text, TextView label ) {
        text.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se hace nada ya que no se pide corregir el campo antes de escribir
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Se comprueba si existe algun caracter erroneo
                // Solo se permiten valores entre el 0 y el 9, el punto y el guion
                Pattern p = Pattern.compile("[^0-9.-]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(s.toString());
                boolean esIncorrecto = m.find();

                // Se almacena el contenido del campo correspondiente
                String latitudLongitud = s.toString().trim();

                // Se comprueba que solo hay una ocurrencia tanto para el punto como para el guion
                int numPuntos = 0;
                int numGuion = 0;
                for(int i=0 ; i<latitudLongitud.length(); i++){
                    if(latitudLongitud.charAt(i) == '.'){
                        numPuntos++;
                    }

                    if(latitudLongitud.charAt(i) == '-'){
                        numGuion++;
                    }
                }

                if (esIncorrecto || numPuntos > 1 || numGuion > 1) {
                    text.setError("Existen caracteres erróneos");
                    label.setTextColor(Color.RED);
                    text.getEditText().setTextColor(Color.RED);
                } else if (numGuion == 1 && latitudLongitud.charAt(0) != '-') {  // Se comprueba que en caso de que haya un guion, este esta en la primera posicion
                    text.setError("El guion tiene que ser el primer caracter");
                    label.setTextColor(Color.RED);
                    text.getEditText().setTextColor(Color.RED);
                } else {

                    if (latitudLongitud.length() > 15) {
                        label.setTextColor(Color.RED);
                        text.getEditText().setTextColor(Color.RED);
                    } else if (latitudLongitud.length() > 1) {
                        text.setError(null);
                        label.setTextColor(Color.GRAY);
                        text.getEditText().setTextColor(Color.BLACK);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se hace nada ya que no se pide corregir el campo despues de escribir
            }
        });
    }

    /**
     * Comprobacion de los diferentes casos de error a la hora de introducir la latitud o la longitud
     *
     * @param text Text Input Layout que va a ser tratado, puede ser la Latitud o la Longitud
     * @param label Etiqueta encima de cada campo que se va a corresponder con el text Input layout
     *              que se este tratando, es decir, latitud o longitud
     * @return true si ambos campos son introducidos correctamente o false en caso contrario.
     */
    private boolean validateLatitudLongitud(TextInputLayout text, TextView label ) {

        // Se almacena el contenido del campo correspondiente
        String latitudLongitud = text.getEditText().getText().toString().trim();

        // Se comprueba que no este vacio
        if (latitudLongitud.isEmpty()) {
            if (text == textInputLatitud) {
                text.setError("La latitud no puede estar vacia");
            } else {
                text.setError("La longitud no puede estar vacia");
            }
            label.setTextColor(Color.RED);
            text.getEditText().setTextColor(Color.RED);
            return false;
        }

        // Se comprueba si existe algun caracter erroneo
        // Solo se permiten valores entre el 0 y el 9, el punto y el guion
        Pattern p = Pattern.compile("[^0-9.-]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(latitudLongitud);
        boolean esIncorrecto = m.find();

        if (esIncorrecto) {
            text.setError("Existen caracteres erróneos");
            label.setTextColor(Color.RED);
            text.getEditText().setTextColor(Color.RED);
            return false;
        }

        // Se comprueba que solo hay una ocurrencia tanto para el punto como para el guion
        int numPuntos = 0;
        int numGuion = 0;
        for(int i=0 ; i<latitudLongitud.length(); i++){
            if(latitudLongitud.charAt(i) == '.'){
                numPuntos++;
            }

            if(latitudLongitud.charAt(i) == '-'){
                numGuion++;
            }
        }

        // Solo se permite una ocurrencia del punto
        if (numPuntos > 1) {
            text.setError("Solo puede haber un punto");
            label.setTextColor(Color.RED);
            text.getEditText().setTextColor(Color.RED);
            return false;
        }

        // Solo se permite una ocurrencia del guion
        if (numGuion > 1) {
            text.setError("Solo puede haber un guión");
            label.setTextColor(Color.RED);
            text.getEditText().setTextColor(Color.RED);
            return false;
        }

        // Se comprueba que en caso de que haya un guion, este esta en la primera posicion
        if (numGuion == 1 && latitudLongitud.charAt(0) != '-') {
            text.setError("El guion tiene que ser el primer caracter");
            label.setTextColor(Color.RED);
            text.getEditText().setTextColor(Color.RED);
            return false;
        }

        /*
         * Una vez comprobados los casos anteriores, se puede almacenar el numero insertado (formato correcto)
         * para las posteriores comprobaciones a realizar
         */
        double numLatitudLongitud;
        if (!text.getEditText().getText().toString().equals("-")) {
            numLatitudLongitud = Double.parseDouble(text.getEditText().getText().toString());
        } else {
            numLatitudLongitud = 0;
        }

        // Se comprueba que la latitud/longitud no contenga mas de 15 caracteres
        if (latitudLongitud.length() > 15) {
            if (text == textInputLatitud) {
                text.setError("Latitud demasiado larga");
            } else {
                text.setError("Longitud demasiado larga");
            }
            label.setTextColor(Color.RED);
            text.getEditText().setTextColor(Color.RED);
            return false;
        }

        // Se comprueba que en caso de la latitud esta este comprendida entre -90 y 90
        if (text == textInputLatitud) {
            if (numLatitudLongitud < -90 || numLatitudLongitud > 90) {
                text.setError("La latitud debe ser entre -90 y 90");
                label.setTextColor(Color.RED);
                text.getEditText().setTextColor(Color.RED);
                return false;
            }
            // Se comprueba que en caso de la longitud este este comprendida entre -180 y 180
        } else {
            if (numLatitudLongitud < -180 || numLatitudLongitud > 180) {
                text.setError("La longitud debe ser entre -180 y 180");
                label.setTextColor(Color.RED);
                text.getEditText().setTextColor(Color.RED);
                return false;
            }
        }

        // Caso correcto
        text.setError(null);
        label.setTextColor(Color.GRAY);
        text.getEditText().setTextColor(Color.BLACK);
        return true;
    }

    /**
     *
     */
    private void clickFiltros() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the dialog title
        builder.setTitle("Filtros");
        // Specify the list array, the items to be selected by default (null for none),

        // Vista escondida del nuevo layout para los diferentes spinners a implementar para los filtros
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);

        final TextView txtComb = mView.findViewById(R.id.combustibleSeleccionado);
        txtComb.setText(this.tipoCombustible);
        final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);    // New spinner object
        // El spinner creado contiene todos los items del array de Strings "operacionesArray"
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.operacionesArray));
        // Al abrir el spinner la lista se abre hacia abajo
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapterSpinner);

        // Opcion "Aceptar"
        builder.setPositiveButton(getResources().getString(R.string.aceptar), (dialog, id) -> {
            // User clicked Aceptar, save the item selected in the spinner
            // If the user does not select nothing, don't do anything
            if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Tipo de Combustible")) {
                tipoCombustible = mSpinner.getSelectedItem().toString();

            }
            refresca();
        });

        //Opcion "Cancelar"
        builder.setNegativeButton(getResources().getString(R.string.cancelar), (dialog, id) -> dialog.dismiss());
        builder.setView(mView);
        builder.create();
        builder.show();
    }

    /**
     * Se ejecuta cuando se pulsa la opcion de ordenacion en la aplicacion
     */
    private void clickOrdenacion() {
        //comienzo de ordenar
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Vista escondida del nuevo layout para los diferentes spinners a implementar para los filtros
        View mView = getLayoutInflater().inflate(R.layout.ordenar_layout, null);

        builder.setTitle(getResources().getString(R.string.tipo_ordenacion));

        final Spinner mSpinner = (Spinner) mView.findViewById(R.id.tipoOrden);    // New spinner object
        // El spinner creado contiene todos los items del array de Strings "operacionesArray"
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item,
                operacionesOrdenacion);

        //variable donde se guardara la posicion del elemento seleccionado dentro del spinner
        final int[] posicionSeleccionada = {0};
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //se guarda la poscion del elemento seleccionado
                posicionSeleccionada[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No es necesario realizar nada
            }
        });

        // Al abrir el spinner la lista se abre hacia abajo
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapterSpinner);

        //Opcion "Aceptar"
        builder.setPositiveButton(getResources().getString(R.string.aceptar), (dialog, id) -> {
            criterioOrdenacion = mSpinner.getSelectedItem().toString();
            if (criterioOrdenacion.equals(ORDEN_PRECIO)) {
                buttonOrden.setText(getResources().getString(R.string.precio));
            } else if (criterioOrdenacion.equals(ORDEN_DISTANCIA)) {
                buttonOrden.setText(getResources().getString(R.string.distancia));
            }

            //Se coloca el elemento seleccionado del spinner en la primera posicion
            int posicion = posicionSeleccionada[0];
            if (posicion != 0) {
                moverPrincipioOpcionSeleccionada(posicion);
            }
            refresca();
        });

        //Opcion "Cancelar"
        builder.setNegativeButton(getResources().getString(R.string.cancelar), (dialog, id) -> dialog.dismiss());
        builder.setView(mView);
        builder.create();
        builder.show();
    }

    /**
     * Mueve el elemento en la poscion indicada de la lista operacionesOrdenacion
     * al pincipio.
     *
     * @param posicion posicion del elemento que se desea mover.
     */
    private void moverPrincipioOpcionSeleccionada(int posicion) {
        String operacionSeleccionada = operacionesOrdenacion.get(posicion);
        operacionesOrdenacion.remove(posicion);
        operacionesOrdenacion.addFirst(operacionSeleccionada);
    }


    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    /**
     * Inicializa las actualizaciones de la ubicacion del usuario
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("DEBUG", "Se ha ejecutado el onPause");
        closeDrawer(drawerLayout);

        //Cuando se pare la aplicacion se guarda la ultima ubiucacion conocida
        //del usuario
        guardarUltimaUbicacion();
        //Se paran las actualizaciones de ubicacion
        stopLocationUpdates();
    }

    /**
     * Cierra la barra lateral del drawer Layout.
     * @param drawerLayout
     */
    private static void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * Guarda la latitud y longitud actuales del usuario para que puedan
     * ser accedidas mas adelante si se necesitan.
     */
    private void guardarUltimaUbicacion() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("latitud", Double.toString(latitud));
        editor.putString("longitud", Double.toString(longitud));
        editor.commit();
    }

    /**
     * Detiene las actulkizaciones de la ubicacion del usuario.
     */
    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    /**
     * @param v
     */
    public void onClick(View v) {

        if (v.getId() == R.id.buttonFiltros) {
            clickFiltros();

        } else if (v.getId() == R.id.buttonOrden) {
            clickOrdenacion();

        } else if (v.getId() == R.id.iconoOrden) {
            String valorActualconoOrden = "";
            if (esAsc) {
                valorActualconoOrden = FLECHA_ABAJO;
            } else {
                valorActualconoOrden = FLECHA_ARRIBA;
            }
            //cambia el tipo orden en caso de que sea ascendente pasa a descendente
            //y en caso de estar en descendente pasa a ascendente
            esAsc = !esAsc;
            //se cambia el icono del orden
            iconoOrden.setImageResource(getResources().getIdentifier(valorActualconoOrden,
                    DRAWABLE, getPackageName()));
            refresca();
        } else if (v.getId() == R.id.info) {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(MainActivity.this, config);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.itemActualizar) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    new CargaDatosGasolinerasTask(MainActivity.this).execute();
                } else if (item.getItemId() == R.id.itemInfo) {
                    Intent myIntent = new Intent(MainActivity.this, InfoActivity.class);
                    MainActivity.this.startActivity(myIntent);
                } else if (item.getItemId() == R.id.itemFiltro) {
                    clickFiltros();
                }
                return true;
            });
            popup.show(); //showing popup menu

        } else if (v.getId() == R.id.menuNav) {
            this.clickMenu();

        } else if (v.getId() == R.id.btnConfiguracion) {
            this.clickConfiguracion();

        } else if (v.getId() == R.id.btnUbicacion) {
            this.clickUbicacion();
        }
    }

    private void refresca() {
        //Refrescar automáticamente la lista de gasolineras
        mSwipeRefreshLayout.setRefreshing(true);
        new CargaDatosGasolinerasTask(ac).execute();
    }


    /**
     * CargaDatosGasolinerasTask
     * <p>
     * Tarea asincrona para obtener los datos de las gasolineras
     * en segundo plano.
     * <p>
     * Redefinimos varios métodos que se ejecutan en el siguiente orden:
     * onPreExecute: activamos el dialogo de progreso
     * doInBackground: solicitamos que el presenter cargue los datos
     * onPostExecute: desactiva el dialogo de progreso,
     * muestra las gasolineras en formato lista (a partir de un adapter)
     * y define la acción al realizar al seleccionar alguna de ellas
     * <p>
     * http://www.sgoliver.net/blog/tareas-en-segundo-plano-en-android-i-thread-y-asynctask/
     */
    private class CargaDatosGasolinerasTask extends AsyncTask<Void, Void, Boolean> {

        Activity activity;

        /**
         * Constructor de la tarea asincrona
         *
         * @param activity
         */
        public CargaDatosGasolinerasTask(Activity activity) {
            this.activity = activity;
        }


        /**
         * doInBackground
         * <p>
         * Tarea ejecutada en segundo plano
         * Llama al presenter para que lance el método de carga de los datos de las gasolineras
         *
         * @param params
         * @return boolean
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            return presenterGasolineras.cargaDatosGasolineras();
        }

        /**
         * onPostExecute
         * <p>
         * Se ejecuta al finalizar doInBackground
         * Oculta el diálogo de progreso.
         * Muestra en una lista los datos de las gasolineras cargadas,
         * creando un adapter y pasándoselo a la lista.
         * Define el manejo de la selección de los elementos de la lista,
         * lanzando con una intent una actividad de detalle
         * a la que pasamos un objeto Gasolinera
         *
         * @param res
         */
        @Override
        protected void onPostExecute(Boolean res) {
            Toast toast;

            mSwipeRefreshLayout.setRefreshing(false);

            // Si se ha obtenido resultado en la tarea en segundo plano
            if (Boolean.TRUE.equals(res)) {
                //Recorrer el array adapter para que no muestre las gasolineras con precios negativos
                presenterGasolineras.eliminaGasolinerasConPrecioNegativo(tipoCombustible);
                //ordenacion
                if (criterioOrdenacion.equals(ORDEN_PRECIO)) {
                    presenterGasolineras.ordenarGasolineras(esAsc, tipoCombustible);
                } else if (criterioOrdenacion.equals(ORDEN_DISTANCIA)) {
                    presenterGasolineras.ordenarGasolinerasDistancia(latitud, longitud, esAsc);
                }

                // Definimos el array adapter
                adapter = new GasolineraArrayAdapter(activity, 0, presenterGasolineras.getGasolineras());

                // Obtenemos la vista de la lista
                listViewGasolineras = findViewById(R.id.listViewGasolineras);

                // Cargamos los datos en la lista
                if (!presenterGasolineras.getGasolineras().isEmpty()) {
                    // datos obtenidos con exito
                    listViewGasolineras.setAdapter(adapter);

                    toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.datos_exito), Toast.LENGTH_LONG);

                        if (!coordenada.equals(newCoordenada)) {

                            toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.ubicacion_establecida), Toast.LENGTH_LONG);
                            coordenada = newCoordenada;
                        }

                } else {
                    // los datos estan siendo actualizados en el servidor, por lo que no son actualmente accesibles
                    // sucede en torno a las :00 y :30 de cada hora
                    toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.datos_no_accesibles), Toast.LENGTH_LONG);
                }
            } else {
                // error en la obtencion de datos desde el servidor
                if (isNetworkConnected()) {
                    toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.datos_no_obtenidos), Toast.LENGTH_LONG);
                } else {
                    adapter.clear();
                    toast = Toast.makeText(getApplicationContext(), "No hay conexión a internet", Toast.LENGTH_LONG);
                }
            }


            // Muestra el mensaje del resultado de la operación en un toast
            if (toast != null) {

                toast.show();
            }

            /*
             * Define el manejo de los eventos de click sobre elementos de la lista
             * En este caso, al pulsar un elemento se lanzará una actividad con una vista de detalle
             * a la que le pasamos el objeto Gasolinera sobre el que se pulsó, para que en el
             * destino tenga todos los datos que necesita para mostrar.
             * Para poder pasar un objeto Gasolinera mediante una intent con putExtra / getExtra,
             * hemos tenido que hacer que el objeto Gasolinera implemente la interfaz Parcelable
             */

            try {
                listViewGasolineras.setOnItemClickListener((a, v, position, id) -> {

                    /* Obtengo el elemento directamente de su posicion,
                     * ya que es la misma que ocupa en la lista
                     */
                    Intent myIntent = new Intent(MainActivity.this, DetailActivity.class);
                    myIntent.putExtra(getResources().getString(R.string.pasoDatosGasolinera),
                            presenterGasolineras.getGasolineras().get(position));

                    myIntent.putExtra(getResources().getString(R.string.pasoTipoCombustible),
                            tipoCombustible);
                    MainActivity.this.startActivity(myIntent);

                });
            } catch (Exception e1) {
                e1.toString();
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////
        }

        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }
    }


    /*
    ------------------------------------------------------------------
        GasolineraArrayAdapter

        Adaptador para inyectar los datos de las gasolineras
        en el listview del layout principal de la aplicacion
    ------------------------------------------------------------------
    */
    class GasolineraArrayAdapter extends ArrayAdapter<Gasolinera> {

        private Context context;
        private List<Gasolinera> listaGasolineras;

        // Constructor
        public GasolineraArrayAdapter(Context context, int resource, List<Gasolinera> objects) {
            super(context, resource, objects);
            this.context = context;
            this.listaGasolineras = objects;
        }

        // Llamado al renderizar la lista
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Obtiene el elemento que se está mostrando
            Gasolinera gasolinera = listaGasolineras.get(position);

            // Indica el layout a usar en cada elemento de la lista
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_gasolinera, null);

            // Asocia las variables de dicho layout
            ImageView logo = view.findViewById(R.id.imageViewLogo);
            TextView rotulo = view.findViewById(R.id.textViewRotulo);
            TextView direccion = view.findViewById(R.id.textViewDireccion);
            TextView labelGasolina = view.findViewById(R.id.textViewTipoGasolina);
            TextView precio = view.findViewById(R.id.textViewGasoleoA);
            TextView distancia = view.findViewById(R.id.valorDistancia);
            // Y carga los datos del item
            rotulo.setText(gasolinera.getRotulo());
            direccion.setText(gasolinera.getDireccion());
            labelGasolina.setText(tipoCombustible);
            double precioCombustible = presenterGasolineras.getPrecioCombustible(tipoCombustible, gasolinera);
            precio.setText(precioCombustible + getResources().getString(R.string.moneda));

            double distanciaKm = presenterGasolineras.getDistancia(latitud, longitud, gasolinera);

            distancia.setText(String.format("%.2f", distanciaKm) + "Km");
            // Se carga el icono
            cargaIcono(gasolinera, logo);


            // Si las dimensiones de la pantalla son menores
            // reducimos el texto de las etiquetas para que se vea correctamente
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            if (displayMetrics.widthPixels < 720) {
                TextView tv = view.findViewById(R.id.textViewTipoGasolina);
                RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams) tv.getLayoutParams());
                params.setMargins(15, 0, 0, 0);
                tv.setTextSize(11);
                TextView tmp;
                tmp = view.findViewById(R.id.textViewGasoleoA);
                tmp.setTextSize(11);
            }

            return view;
        }

        private void cargaIcono(Gasolinera gasolinera, ImageView logo) {
            // carga icono

            String rotuleImageID = gasolinera.getRotulo().toLowerCase();

            // Tengo que protegerme ante el caso en el que el rotulo solo tiene digitos.
            // En ese caso getIdentifier devuelve esos digitos en vez de 0.
            int imageID = context.getResources().getIdentifier(rotuleImageID,
                    DRAWABLE, context.getPackageName());

            if (imageID == 0 || TextUtils.isDigitsOnly(rotuleImageID)) {
                imageID = context.getResources().getIdentifier(getResources().getString(R.string.pordefecto),
                        DRAWABLE, context.getPackageName());
            }
            logo.setImageResource(imageID);

        }
    }
}








