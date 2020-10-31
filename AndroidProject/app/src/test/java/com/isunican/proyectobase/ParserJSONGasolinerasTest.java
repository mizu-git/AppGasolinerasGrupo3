package com.isunican.proyectobase;

import android.os.Build;
import android.util.JsonReader;

import com.isunican.proyectobase.Model.Gasolinera;
import com.isunican.proyectobase.Utilities.ParserJSONGasolineras;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)

public class ParserJSONGasolinerasTest {


    private JsonReader reader;
    private JsonReader readerGasolineras;

    @Before
    public void setUp() {
        //Gasolinera en formato Json
        String jsonString = "{" +
                "\"Rótulo\":\"Repsol\",\"Localidad\":\"Santander\",\"Provincia\":\"Cantabria\"," +
                "\"IDEESS\":\"0\",\"Precio Gasoleo A\":\"0,95\",\"Precio Gasolina 95 E5\":\"1,01\"," +
                "\"Precio Gasolina 98 E5\":\"1,03\",\"Precio Biodiesel\":\"1,11\",\"Precio Gasoleo Premium\":\"0,99\"," +
                "\"Dirección\":\"Calle 001\"" +
                "}";

        reader = new JsonReader(new StringReader(jsonString));

        //String con varias gasolineras en formato array Json
        String jsonGasolineras = "{ \"ListaEESSPrecio\" : [{" +
                "\"Rótulo\":\"REPSOL\",\"Localidad\":\"Torrelavega\",\"Provincia\":\"Cantabria\"," +
                "\"IDEESS\":\"0\",\"Precio Gasoleo A\":\"0,95\",\"Precio Gasolina 95 E5\":\"1,01\"," +
                "\"Precio Gasolina 98 E5\":\"1,03\",\"Precio Biodiesel\":\"1,11\",\"Precio Gasoleo Premium\":\"0,99\"," +
                "\"Dirección\":\"Calle 002\"" +
                "}, {" +
                "\"Rótulo\":\"SHELL\",\"Localidad\":\"Castro Urdiales\",\"Provincia\":\"Cantabria\"," +
                "\"IDEESS\":\"0\",\"Precio Gasoleo A\":\"0,92\",\"Precio Gasolina 95 E5\":\"1,10\"," +
                "\"Precio Gasolina 98 E5\":\"1,13\",\"Precio Biodiesel\":\"1,19\",\"Precio Gasoleo Premium\":\"0,97\"," +
                "\"Dirección\":\"Calle 003\"" +
                "}]}";

        readerGasolineras = new JsonReader(new StringReader(jsonGasolineras));
    }

    @Test
    public void readGasolineraTest() {
        Gasolinera g = null;
        try {
            g = ParserJSONGasolineras.readGasolinera(reader);
        } catch (IOException e) {
            fail();
        }

        assertEquals("Santander", g.getLocalidad());
        assertEquals("Cantabria", g.getProvincia());
        assertEquals("Calle 001", g.getDireccion());

        assertEquals(0.95, g.getGasoleoA(), 0.001);
        assertEquals(1.01, g.getGasolina95(), 0.001);
        assertEquals(1.03, g.getGasolina98(), 0.001);
        assertEquals(0.99, g.getGasoleoPremium(), 0.001);
        assertEquals(1.11, g.getBiodiesel(), 0.001);

        //caso donde el Json pasado no tiene formato correcto
        reader = new JsonReader(new StringReader(""));
        try{
            g = ParserJSONGasolineras.readGasolinera(reader);
            fail();
        }catch (IOException e ){

        }
    }

    @Test
    public void readGasolinerasTest() {
        List<Gasolinera> gasolineras = new ArrayList<>();
        try {
            gasolineras = ParserJSONGasolineras.readArrayGasolineras(readerGasolineras);
        } catch (IOException e) {
            fail();
        }

        assertEquals("Torrelavega", gasolineras.get(0).getLocalidad());
        assertEquals("Cantabria", gasolineras.get(0).getProvincia());
        assertEquals("Calle 002", gasolineras.get(0).getDireccion());

        assertEquals(0.95, gasolineras.get(0).getGasoleoA(), 0.001);
        assertEquals(1.01, gasolineras.get(0).getGasolina95(), 0.001);
        assertEquals(1.03, gasolineras.get(0).getGasolina98(), 0.001);
        assertEquals(0.99, gasolineras.get(0).getGasoleoPremium(), 0.001);
        assertEquals(1.11, gasolineras.get(0).getBiodiesel(), 0.001);

        assertEquals("Castro Urdiales", gasolineras.get(1).getLocalidad());
        assertEquals("Cantabria", gasolineras.get(1).getProvincia());
        assertEquals("Calle 003", gasolineras.get(1).getDireccion());

        assertEquals(0.92, gasolineras.get(1).getGasoleoA(), 0.001);
        assertEquals(1.10, gasolineras.get(1).getGasolina95(), 0.001);
        assertEquals(1.13, gasolineras.get(1).getGasolina98(), 0.001);
        assertEquals(0.97, gasolineras.get(1).getGasoleoPremium(), 0.001);
        assertEquals(1.19, gasolineras.get(1).getBiodiesel(), 0.001);


        //caso donde el Json pasado no tiene formato correcto
        readerGasolineras = new JsonReader(new StringReader(""));
        try{
          gasolineras = ParserJSONGasolineras.readArrayGasolineras(readerGasolineras);
          fail();
        }catch (IOException e ){

        }
    }
}

