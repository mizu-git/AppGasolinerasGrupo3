package com.isunican.proyectobase.Utilities;

import android.location.Location;

/**
 * Clase para calcular la distancia entre dos coordenadas dadas en forma de latitud y longitud.
 *
 * @author Hamza Hamda
 */
public class CalculaDistancia {

    private CalculaDistancia() { throw new IllegalStateException("Utility class"); }


    /**
     * Devuelve la distancia entre las coordenadas indicadas
     * @param latitudX  latitud de un punto x.
     * @param longitudX longitud de un punto x.
     * @param latitudY  latitud de un punto y.
     * @param longitudY longitud de un punto y.
     * @return la distancia entre las coordenadas indicadas
     */
    public static double distanciaEntreDosCoordenadas(double latitudX, double longitudX,
                                                      double latitudY, double longitudY){
        double resul;
        Location locX = new Location("");
        locX.setLatitude(latitudX);
        locX.setLongitude(longitudX);

        Location locY = new Location("");
        locY.setLatitude(latitudY);
        locY.setLongitude(longitudY);

        resul = locX.distanceTo(locY);

        return resul;
    }
}
