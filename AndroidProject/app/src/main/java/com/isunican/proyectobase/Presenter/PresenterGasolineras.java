package com.isunican.proyectobase.Presenter;

import android.app.Activity;
import android.util.Log;

import com.isunican.proyectobase.Model.*;
import com.isunican.proyectobase.Utilities.ParserJSONGasolineras;
import com.isunican.proyectobase.Utilities.RemoteFetch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/*
------------------------------------------------------------------
    Clase presenter con la logica de gasolineras
    Mantiene un objeto ListaGasolineras que es el que mantendrá
    los datos de las gasolineras cargadas en nuestra aplicación
    Incluye métodos para gestionar la lista de gasolineras y
    cargar datos en ella.
------------------------------------------------------------------
*/
public class PresenterGasolineras {

    private List<Gasolinera> gasolineras;

    //URLs para obtener datos de las gasolineras
    //https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/help
    public static final String URL_GASOLINERAS_SPAIN = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/";
    public static final String URL_GASOLINERAS_CANTABRIA = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroCCAA/06";
    public static final String URL_GASOLINERAS_SANTANDER = "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/FiltroMunicipio/5819";
    public static final String SANTANDER = "Santander";
    public static final String GASOLEO = "Gasóleo A";
    public static final String GASOLINA95 = "Gasolina 95";
    public static final String GASOLINA98 = "Gasolina 98";
    public static final String BIODIESEL = "Biodiésel";
    public static final String GASOLEOPREMIUM = "Gasóleo Premium";

    // Coordenada Inicial
    public static final String COORDENADAPREDETERMINADA = "43.350223552917 -4.052258920907";


    /**
     * Constructor, getters y setters
     */
    public PresenterGasolineras() {
        gasolineras = new ArrayList<>();
    }

    public List<Gasolinera> getGasolineras() {
        return gasolineras;
    }

    public void setGasolineras(List<Gasolinera> l) {
        this.gasolineras = l;
    }


    /**
     * cargaDatosGasolineras
     * <p>
     * Carga los datos de las gasolineras en la lista de gasolineras de la clase.
     * Para ello llama a métodos de carga de datos internos de la clase ListaGasolineras.
     * En este caso realiza una carga de datos remotos dada una URL
     * <p>
     * Habría que mejorar el método para que permita pasar un parámetro
     * con los datos a cargar (id de la ciudad, comunidad autónoma, etc.)
     *
     * @param
     * @return boolean Devuelve true si se han podido cargar los datos
     */
    public boolean cargaDatosGasolineras() {
        return cargaDatosRemotos(URL_GASOLINERAS_CANTABRIA);
    }

    /**
     * cargaDatosDummy
     * <p>
     * Carga en la lista de gasolineras varias gasolineras definidas a "mano"
     * para hacer pruebas de funcionamiento
     *
     * @param
     * @return boolean
     */

    public boolean cargaDatosDummy(){
        this.gasolineras.add(new Gasolinera(1000,SANTANDER,SANTANDER, "Av Valdecilla", 1.299,1.359, 1.3,1.2,2,"AVIA", 0, 0));
        this.gasolineras.add(new Gasolinera(1053,SANTANDER,SANTANDER, "Plaza Matias Montero", 1.270,1.349,1.22,1.11,1.21,"CAMPSA", 0, 0));
        this.gasolineras.add(new Gasolinera(420,SANTANDER,SANTANDER, "Area Arrabal Puerto de Raos", 1.249,1.279,1.3,1.2,1.5,"E.E.S.S. MAS, S.L.", 0, 0));
        this.gasolineras.add(new Gasolinera(9564,SANTANDER,SANTANDER, "Av Parayas", 1.189,1.269,1.11,1.28,1.25,"EASYGAS", 0, 0));
        this.gasolineras.add(new Gasolinera(1025,SANTANDER,SANTANDER, "Calle el Empalme", 1.259,1.319,1.65,1.27,1.01,"CARREFOUR", 0, 0));
        return true;
    }

    /**
     * cargaDatosLocales
     * <p>
     * A partir de la dirección de un fichero JSON pasado como parámetro:
     * Parsea la información para obtener una lista de gasolineras.
     * Finalmente, dicha lista queda almacenada en la clase.
     *
     * @param fichero
     * @return boolean Devuelve true si se han podido cargar los datos
     */
    public boolean cargaDatosLocales(String fichero) {
        return (fichero != null);
    }

    /**
     * cargaDatosRemotos
     * <p>
     * A partir de la dirección pasada como parámetro:
     * Utiliza RemoteFetch para cargar el fichero JSON ubicado en dicha URL
     * en un stream de datos.
     * Luego utiliza ParserJSONGasolineras para parsear dicho stream
     * y extraer una lista de gasolineras.
     * Finalmente, dicha lista queda almacenada en la clase.
     *
     * @param direccion URL del JSON con los datos
     * @return boolean Devuelve true si se han podido cargar los datos
     */
    public boolean cargaDatosRemotos(String direccion) {
        try {
            BufferedInputStream buffer = RemoteFetch.cargaBufferDesdeURL(direccion);
            gasolineras = ParserJSONGasolineras.parseaArrayGasolineras(buffer);
            Log.d("ENTRA", "Obten gasolineras:" + gasolineras.size());
            return true;
        } catch (Exception e) {
            Log.e("ERROR", "Error en la obtención de gasolineras: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina las gasolinera si el precio el combustible indicado es negativo
     *
     * @param combustible combustible por el cual se esta filtrando
     */
    public void eliminaGasolinerasConPrecioNegativo(String combustible) {
        int i = 0;
        while (i < gasolineras.size()) {
            Gasolinera g = gasolineras.get(i);
            //se calcula el precio del combustible en cuetion
            double precio = getPrecioCombustible(combustible, g);
            //si el precio del combustible es negativo la gasolinera se borra de la lista
            if (precio < 0) {
                gasolineras.remove(g);
                i--;
            }
            i++;
        }
    }

    /**
     * Ordena las gasolineras en funcion del precio del combustible indicado de manera ascendente si
     * asc es verdadero o de manera descendete si no lo es.
     *
     * @param asc         manera de odernar la lista, si asc es verdadero se orderna de forma ascendente, en
     *                    caso contrario de forma descendente.
     * @param combustible combustible por el que se desea filtar, se utiliza el precio de este combutible
     *                    para ordenar.
     */
    public void ordernarGasolineras(boolean asc, String combustible) {
        for (int i = 0; i < gasolineras.size(); i++) {
            for (int j = 0; j < gasolineras.size() - 1; j++) {
                Gasolinera tmp = gasolineras.get(j + 1);
                if (asc) {
                    if (getPrecioCombustible(combustible, gasolineras.get(j)) > getPrecioCombustible(combustible, tmp)) {
                        gasolineras.remove(tmp);
                        gasolineras.add(j, tmp);
                    }
                } else {
                    if (getPrecioCombustible(combustible, gasolineras.get(j)) < getPrecioCombustible(combustible, tmp)) {
                        gasolineras.remove(tmp);
                        gasolineras.add(j, tmp);
                    }
                }
            }
        }
    }

    /**
     * Develve el precio del combustible de la gasolinera indicada segun el combustible indicado
     *
     * @param combustible combustible del que se desao conocer el precio
     * @param g           gasolinera de la que se desea conocer el precio
     * @return el precio del combustible de la gasolinera indicada segun el combustible indicado
     */
    public double getPrecioCombustible(String combustible, Gasolinera g) {
        double precio = 0.0;
        switch (combustible) {
            case GASOLEO:
                precio = g.getGasoleoA();
                break;
            case GASOLINA95:
                precio = g.getGasolina95();
                break;
            case GASOLINA98:
                precio = g.getGasolina98();
                break;
            case BIODIESEL:
                precio = g.getBiodiesel();
                break;
            case GASOLEOPREMIUM:
                precio = g.getGasoleoPremium();
                break;
            default:
                break;
        }
        return precio;
    }

    public String lecturaCombustiblePorDefecto(Activity a, String fichero)
            throws IOException {
        FileInputStream fis = null;
        String resultado = "";
        String combustible = "";

        if (fichero.equals("")) {
            combustible = GASOLEO;
        } else {
            fis = a.openFileInput(fichero);
            InputStreamReader isr = new InputStreamReader(fis);
            StringBuilder sb;
            try (BufferedReader br = new BufferedReader(isr)) {
                sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }
            }
            resultado = sb.toString();
            if (resultado.contains(GASOLEO)) {
                combustible = GASOLEO;
            } else if (resultado.contains(GASOLINA95)) {
                combustible = GASOLINA95;
            } else if (resultado.contains(GASOLINA98)) {
                combustible = GASOLINA98;
            } else if (resultado.contains(BIODIESEL)) {
                combustible = BIODIESEL;
            } else if (resultado.contains(GASOLEOPREMIUM)) {
                combustible = GASOLEOPREMIUM;
            } else {
                combustible = GASOLEO;
            }

            fis.close();
        }
        return combustible;
    }


    public void escrituraCombustiblePorDefecto(String combustible, Activity a, String fichero)
            throws IOException, CombustibleNoExistente {
        if (combustible.equals(GASOLEO) || combustible.equals(GASOLINA95) || combustible.equals(GASOLINA98) || combustible.equals(BIODIESEL) || combustible.equals(GASOLEOPREMIUM)) {
            FileOutputStream fos = null;
            try {
                fos = a.openFileOutput(fichero, android.content.Context.MODE_PRIVATE);
                fos.write(combustible.getBytes());

            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        } else {
            throw new CombustibleNoExistente();
        }

    }

    public class CombustibleNoExistente extends Exception {
    }


    /**
     *
     * @param a
     * @param fichero
     * @return
     * @throws IOException
     */
    public String lecturaCoordenadaPorDefecto(Activity a, String fichero)
            throws IOException {
        FileInputStream fis = null;
        String resultado = "";
        String coordenada = "";

        if (fichero.equals("")) {
            coordenada = COORDENADAPREDETERMINADA;
        } else {
            fis = a.openFileInput(fichero);
            InputStreamReader isr = new InputStreamReader(fis);
            StringBuilder sb;
            try (BufferedReader br = new BufferedReader(isr)) {
                sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }
            }

            resultado = sb.toString();

            coordenada = resultado;

            fis.close();
        }

        return coordenada;
    }


    public void escrituraCoordenadaPorDefecto(String coordenada, Activity a, String fichero)
            throws IOException, CoordenadaNoExistente {

        int posicion = coordenada.indexOf(" ");
        String latitud = coordenada.substring(0, posicion);
        String longitud = coordenada.substring(posicion+1);

        if (Double.parseDouble(latitud) >= -90 && Double.parseDouble(latitud) <= 90 || Double.parseDouble(longitud) >= -180 && Double.parseDouble(longitud) <= 180) {
            FileOutputStream fos = null;
            try {
                fos = a.openFileOutput(fichero, android.content.Context.MODE_PRIVATE);
                fos.write(coordenada.getBytes());

            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        } else {
            throw new CoordenadaNoExistente();
        }

    }

    public class CoordenadaNoExistente extends Exception {
    }

}
