package com.isunican.proyectobase.Presenter;

import com.isunican.proyectobase.Model.Gasolinera;
import com.isunican.proyectobase.Presenter.PresenterGasolineras;
import com.isunican.proyectobase.Views.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Clase de prueba donde se realizan las pruebas unitarias correspondiente a la clase
 * PresenterGasolineras.
 *
 * @author Corocotta
 */
public class PresenterGasolinerasTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    MainActivity ac;
    @Mock
    MainActivity ac2;


    // Objeto de la clase Presentergasolineras para implementar los siguientes metodos Test
    private PresenterGasolineras pr;
    // Gasolineras para utilizarlas posteriormente
    private ArrayList<Gasolinera> gasolineras;
    private String ruta = "datos_test.txt";
    private String ruta2 = "datos_coord.txt";
    private FileInputStream fis;
    private FileInputStream fis2;
    private FileOutputStream fos;
    private FileOutputStream fos2;
    private File archivo;
    private File archivo2;

    @Before
    public void setUp() {
        archivo = new File(ruta);
        try {
            archivo.createNewFile();
        } catch (IOException e) {
            fail("" + e.getStackTrace());
        }
        try {
            fis = new FileInputStream(ruta);
            when(ac.openFileInput(ruta)).thenReturn(fis);
        } catch (FileNotFoundException e) {
            fail("" + e.getStackTrace());
        }

        archivo2 = new File(ruta2);
        try {
            archivo2.createNewFile();
        } catch (IOException e) {
            fail("" + e.getStackTrace());
        }
        try {
            fis2 = new FileInputStream(ruta2);
            when(ac2.openFileInput(ruta2)).thenReturn(fis2);
        } catch (FileNotFoundException e) {
            fail("" + e.getStackTrace());
        }

        pr = new PresenterGasolineras();//se inicializa el presenter
        gasolineras = new ArrayList<>();//se crea lista de de tipo Gasolina para probar
        /*Se anhaden varias gasolineras a la lista para pasarselas al presenter y utilizarlas en la pruebas*/
        gasolineras.add(new Gasolinera(0, "Castro", "Cantabria", "Calle 1",
                0.91, 1.06, 1.11, 1.31, 1.01, "CEPSA", 40, -3));
        gasolineras.add(new Gasolinera(0, "Viesgo", "Cantabria", "Calle 2",
                1.01, 1.09, 1.11, 1.26, 1.16, "Repsol", 40, -3));
        gasolineras.add(new Gasolinera(0, "Puente San Miguel", "Cantabria", "Calle 1",
                0.92, 1.12, 1.09, 1.26, 0.97, "Shell", 40, -3));
        gasolineras.add(new Gasolinera(0, "Tanos", "Cantabria", "Calle 3",
                1.02, 1.21, 1.08, 1.35, 0.93, "Repsol", 40, -3));
        gasolineras.add(new Gasolinera(0, "Ganzo", "Cantabria", "Calle 4",
                0.96, 0.99, 1.07, 1.15, 0.94, "CEPSA", 40, -3));
        gasolineras.add(new Gasolinera(0, "Torrelavega", "Cantabria", "Calle 5",
                0.97, 1.11, 1.07, 1.10, 0.92, "Shell", 40, -3));
        gasolineras.add(new Gasolinera(0, "Santander", "Cantabria", "Calle 6",
                0.90, 1.03, 1.07, 1.14, 0.97, "CEPSA", 40, -3));
        pr.setGasolineras(gasolineras);

    }//setUp

    /*
     * Test para el metodo getPrecioGasolinera
     * Se comprobara si retorna correctamente el precio de cada tipo de combustible
     * de las gasolineras creadas anteriormente.
     */
    @Test
    public void getPrecioGasolinera() {
        try {
            assertEquals(0.0, pr.getPrecioCombustible(null, null), 0.000001);
            fail();
        } catch (NullPointerException e) {

        }

        assertEquals(0.91, pr.getPrecioCombustible("Gasóleo A", pr.getGasolineras().get(0)), 0.000001);

        assertEquals(1.06, pr.getPrecioCombustible("Gasolina 95", pr.getGasolineras().get(0)), 0.000001);

        assertEquals(1.11, pr.getPrecioCombustible("Gasolina 98", pr.getGasolineras().get(0)), 0.000001);

        assertEquals(1.31, pr.getPrecioCombustible("Biodiésel", pr.getGasolineras().get(0)), 0.000001);

        assertEquals(1.01, pr.getPrecioCombustible("Gasóleo Premium", pr.getGasolineras().get(0)), 0.000001);
    }

    @Test
    public void eliminarGasolinerasConPrecioNegativoTest() {
        //caso donde la cadena pasada esta vacia
        pr.eliminaGasolinerasConPrecioNegativo("");
        boolean noCambiaLista = gasolineras.size() == 7;
        //se comprueba que el metodo no realiza ningun cambio en la lista
        assertTrue(noCambiaLista);

        //caso donde una gasolinera tiene el gasoleao con precio negativo
        Gasolinera gasolineraConPrecioGasoleoNegativo = new Gasolinera(0, "Torrelavega", "Cantabria", "Calle 1",
                -0.3, 1.06, 1.07, 1.26, 0.97, "CEPSA", 40, -3);
        pr.eliminaGasolinerasConPrecioNegativo("Gasóleo A");
        //se comprueba que despues de ejecutar el metodo la gasoliner con precio negativo ya no esta en la lista
        assertFalse(gasolineras.contains(gasolineraConPrecioGasoleoNegativo));

        //caso donde una gasolinera tiene el gasolina 95 con precio negativo
        Gasolinera gasolineraConPrecioGasolina95Negativo = new Gasolinera(0, "Torrelavega", "Cantabria", "Calle 1",
                0.94, -0.91, 1.07, 1.26, 0.97, "CEPSA", 40, -3);
        pr.eliminaGasolinerasConPrecioNegativo("Gasolina 95");
        //se comprueba que despues de ejecutar el metodo la gasoliner con precio negativo ya no esta en la lista
        assertFalse(gasolineras.contains(gasolineraConPrecioGasolina95Negativo));

        //caso donde una gasolinera tiene la gasolina 98 con precio negativo
        Gasolinera gasolineraConPrecioGasolina98Negativo = new Gasolinera(0, "Torrelavega", "Cantabria", "Calle 1",
                0.94, -2.01, 1.07, 1.26, 0.97, "CEPSA", 40, -3);
        pr.eliminaGasolinerasConPrecioNegativo("Gasolina 98");
        //se comprueba que despues de ejecutar el metodo la gasoliner con precio negativo ya no esta en la lista
        assertFalse(gasolineras.contains(gasolineraConPrecioGasolina98Negativo));

        //caso donde una gasolinera tiene el biodiesel con precio negativo
        Gasolinera gasolineraConPrecioBiodieselNegativo = new Gasolinera(0, "Torrelavega", "Cantabria", "Calle 1",
                0.94, -4, 1.07, 1.26, 0.97, "CEPSA", 40, -3);
        pr.eliminaGasolinerasConPrecioNegativo("Biodiésel");
        //se comprueba que despues de ejecutar el metodo la gasoliner con precio negativo ya no esta en la lista
        assertFalse(gasolineras.contains(gasolineraConPrecioBiodieselNegativo));

        //caso donde una gasolinera tiene el gasoleao premium con precio negativo
        Gasolinera gasolineraConPrecioGasoleoPremiumNegativo = new Gasolinera(0, "Torrelavega", "Cantabria", "Calle 1",
                0.94, -0.0001, 1.07, 1.26, 0.97, "CEPSA", 40, -3);
        pr.eliminaGasolinerasConPrecioNegativo("Gasóleo Premium");
        //se comprueba que despues de ejecutar el metodo la gasoliner con precio negativo ya no esta en la lista
        assertFalse(gasolineras.contains(gasolineraConPrecioGasoleoPremiumNegativo));

        //caso donde el parametro pasado es nulo
        String parametro = null;
        //se comprueba que se lanza la excepcion NullPointerException
        try {
            pr.eliminaGasolinerasConPrecioNegativo(parametro);
            fail();
        } catch (NullPointerException e) {
        }
        //se comprueba que el metodo no realiza ningun cambio en la lista
        assertTrue(noCambiaLista);

    }//eliminarGasolinerasConPrecionNegativoTest


    /*
     * Test para el metodo ordenarGasolineras
     * Se comprobara si funciona correctamente ascender y descender dependiendo
     * del precio de las gasolineras creadas anteriormente.
     */
    @Test
    public void ordenarGasolineras() {


        try {
            pr.ordenarGasolineras(true, null);
            fail();
        } catch (NullPointerException e) {

        }

        pr.ordenarGasolineras(true, "Gasóleo A");
        assertEquals(0.90, pr.getGasolineras().get(0).getGasoleoA(), 0.00001);

        pr.ordenarGasolineras(false, "Gasóleo A");
        assertEquals(1.02, pr.getGasolineras().get(0).getGasoleoA(), 0.00001);

        pr.ordenarGasolineras(true, "Gasolina 95");
        assertEquals(0.99, pr.getGasolineras().get(0).getGasolina95(), 0.00001);

        pr.ordenarGasolineras(false, "Gasolina 95");
        assertEquals(1.21, pr.getGasolineras().get(0).getGasolina95(), 0.00001);

        pr.ordenarGasolineras(true, "Gasolina 98");
        assertEquals(1.07, pr.getGasolineras().get(0).getGasolina98(), 0.00001);

        pr.ordenarGasolineras(false, "Gasolina 98");
        assertEquals(1.11, pr.getGasolineras().get(0).getGasolina98(), 0.00001);

        pr.ordenarGasolineras(true, "Biodiésel");
        assertEquals(1.10, pr.getGasolineras().get(0).getBiodiesel(), 0.00001);

        pr.ordenarGasolineras(false, "Biodiésel");
        assertEquals(1.35, pr.getGasolineras().get(0).getBiodiesel(), 0.00001);

        pr.ordenarGasolineras(true, "Gasóleo Premium");
        assertEquals(0.92, pr.getGasolineras().get(0).getGasoleoPremium(), 0.00001);

        pr.ordenarGasolineras(false, "Gasóleo Premium");
        assertEquals(1.16, pr.getGasolineras().get(0).getGasoleoPremium(), 0.00001);
    }

    /**
     * Prueba unitaria del metodo escrituraCombustiblePorDefecto de la clase PresenterGasolinera
     * @author Hamza Hamda
     */
    @Test
    public void lecturaCombustiblePorDefectoTest() {
        FileInputStream fisVacio;
        FileInputStream fisCombustibleErroneo;
        File archivoVacio = new File("archivoVacio.txt");
        FileWriter myWriter;
        File archivoCombErr = new File("archivoCombErr.txt");;
        FileWriter myWriterErr = null;


        try {
            //se establece por defecto el tipo de combustible como gasolina 98
            myWriter = new FileWriter(archivo);
            myWriter.write("Gasolina 98");
            myWriter.close();

            //creamos un segundo archivo de prueba que utilizaremos como archivo vacio
            archivoVacio.createNewFile();
            fisVacio = new FileInputStream(archivoVacio);

            archivoCombErr.createNewFile();
            fisCombustibleErroneo = new FileInputStream(archivoCombErr);
            myWriterErr = new FileWriter(archivoCombErr);


            //se mockea el comportamiento de la activity
            when(ac.openFileInput("err.txt")).thenThrow(new FileNotFoundException());
            when(ac.openFileInput("archivoVacio.txt")).thenReturn(fisVacio);
            when(ac.openFileInput("archivoCombErr.txt")).thenReturn(fisCombustibleErroneo);
        } catch (IOException e) {
            fail("" + e.getStackTrace());
        }


        //UT.1a
        String combustible = null;
        try {
            combustible = pr.lecturaCombustiblePorDefecto(ac, ruta);
        } catch (IOException e) {
            fail("" + e.getStackTrace());
        }
        assertEquals("Gasolina 98", combustible);

        //UT.1b
        try {
            pr.lecturaCombustiblePorDefecto(ac, "err.txt");
            fail();
        } catch (IOException e) {
        }

        //UT.1c
        try {
            combustible = pr.lecturaCombustiblePorDefecto(ac, "archivoVacio.txt");
        } catch (IOException e) {
            fail("" + e.getStackTrace());
        }
        assertEquals("Gasóleo A", combustible);

        //UT.1d

        try {
            myWriterErr.write("Gas");
            myWriterErr.close();
            combustible = pr.lecturaCombustiblePorDefecto(ac, "archivoCombErr.txt");
        } catch (IOException e) {
            e.getMessage();
            fail("" + e.getStackTrace());

        }
        assertEquals("Gasóleo A", combustible);

        archivo.delete();
        archivoVacio.delete();
        archivoCombErr.delete();
    }

    /**
     * Prueba unitaria del metodo escrituraCombustiblePorDefecto de la clase PresenterGasolinera
     * @author Hamza Hamda
     */
    @Test
    public void escrituraCombustiblePorDefectoTest() {

        try {
            fos = new FileOutputStream(ruta);
            when(ac.openFileOutput(ruta, android.content.Context.MODE_PRIVATE)).thenReturn(fos);
            when(ac.openFileOutput("err.txt", android.content.Context.MODE_PRIVATE)).thenThrow(new FileNotFoundException());
        } catch (IOException e) {
        }

        //UT.2a
        try {
            pr.escrituraCombustiblePorDefecto("Gasóleo A",ac,ruta);
        }catch (IOException e){
            fail("" + e.getStackTrace());
        }catch (PresenterGasolineras.CombustibleNoExistente e){
            fail("" + e.getStackTrace());
        }

        String combusitble = null;
        try {
            combusitble = pr.lecturaCombustiblePorDefecto(ac, ruta);
        } catch (IOException e) {
            fail();
        }
        assertEquals("Gasóleo A", combusitble);

        //UT.2b
        try {
            pr.escrituraCombustiblePorDefecto("Gasóleo A", ac, "err.txt");
            fail();
        }catch (IOException e){
        } catch (PresenterGasolineras.CombustibleNoExistente e) {
            fail("" + e.getStackTrace());
        }

        //UT.2c
        try {
            pr.escrituraCombustiblePorDefecto("Gas", ac, ruta);
            fail();
        }catch (PresenterGasolineras.CombustibleNoExistente e){
        }catch (IOException e){
            fail("" + e.getStackTrace());
        }
        archivo.delete();
    }

    /**
     * Prueba unitaria del metodo lecturaCoordenadaPorDefecto de la clase PresenterGasolinera
     * @author Iván Sánchez
     */
    @Test
    public void lecturaCoordenadaPorDefectoTest() {

        FileInputStream fisVacio;
        FileInputStream fisCoordErroneas;
        File archivoVacio = new File("archivoVacio.txt");
        FileWriter myWriter2;
        File archivoCombErr = new File("archivoCombErr.txt");;
        FileWriter myWriterErr = null;


        try {
            //se establece por defecto unas coordenadas
            myWriter2 = new FileWriter(archivo2);
            myWriter2.write("44.212 -5.899");
            myWriter2.close();

            //creamos un segundo archivo de prueba que utilizaremos como archivo vacio
            archivoVacio.createNewFile();
            fisVacio = new FileInputStream(archivoVacio);

            archivoCombErr.createNewFile();
            fisCoordErroneas = new FileInputStream(archivoCombErr);
            myWriterErr = new FileWriter(archivoCombErr);


            //se mockea el comportamiento de la activity
            when(ac2.openFileInput("err.txt")).thenThrow(new FileNotFoundException());
            when(ac2.openFileInput("archivoVacio.txt")).thenReturn(fisVacio);
            when(ac2.openFileInput("archivoCombErr.txt")).thenReturn(fisCoordErroneas);
        } catch (IOException e) {
            fail("" + e.getStackTrace());
        }


        //UT.1a
        String coordenada = null;
        try {
            coordenada = pr.lecturaCoordenadaPorDefecto(ac2, ruta2);
        } catch (IOException e) {
            fail("" + e.getStackTrace());
        }
        assertEquals("44.212 -5.899\n", coordenada);

        //UT.1b
        try {
            pr.lecturaCoordenadaPorDefecto(ac2, "err.txt");
            fail();
        } catch (IOException e) {
        }

        //UT.1c
        try {
            coordenada = pr.lecturaCoordenadaPorDefecto(ac2, "");
        } catch (IOException e) {
            fail("" + e.getStackTrace());
        }
        assertEquals("43.350223552917 -4.052258920907", coordenada);

        //UT.1d

        try {
            myWriterErr.write("43.350223552917 -4.052258920907");
            myWriterErr.close();
            coordenada = pr.lecturaCoordenadaPorDefecto(ac2, "archivoCombErr.txt");
        } catch (IOException e) {
            e.getMessage();
            fail("" + e.getStackTrace());

        }
        assertEquals("43.350223552917 -4.052258920907\n", coordenada);

        archivo2.delete();
        archivoVacio.delete();
        archivoCombErr.delete();

    }

    /**
     * Prueba unitaria del metodo escrituraCoordenadaPorDefecto de la clase PresenterGasolinera
     * @author Iván Sánchez
     */
    @Test
    public void escrituraCoordenadaPorDefectoTest() {

        try {
            fos2 = new FileOutputStream(ruta2);
            when(ac2.openFileOutput(ruta2, android.content.Context.MODE_PRIVATE)).thenReturn(fos2);
            when(ac2.openFileOutput("err.txt", android.content.Context.MODE_PRIVATE)).thenThrow(new FileNotFoundException());
        } catch (IOException e) {
        }

        //UT.2a
        try {
            pr.escrituraCoordenadaPorDefecto("43.212 -5.899",ac2,ruta2);
        }catch (IOException e){
            fail("" + e.getStackTrace());
        } catch (PresenterGasolineras.CoordenadaNoExistente coordenadaNoExistente) {
            coordenadaNoExistente.printStackTrace();
        }

        String coordenada = null;
        try {
            coordenada = pr.lecturaCoordenadaPorDefecto(ac2, ruta2);
        } catch (Exception e) {
            fail();
        }
        assertEquals("43.212 -5.899\n", coordenada);

        //UT.2b
        try {
            pr.escrituraCoordenadaPorDefecto("43.212 -5.899", ac2, "err.txt");
            fail();
        }catch (IOException e){
        } catch (PresenterGasolineras.CoordenadaNoExistente coordenadaNoExistente) {
            coordenadaNoExistente.printStackTrace();
        }

        //UT.2c
        try {
            pr.escrituraCoordenadaPorDefecto("100 200", ac2, ruta2);
            fail();
        }catch (IOException e){
            fail("" + e.getStackTrace());
        } catch (PresenterGasolineras.CoordenadaNoExistente coordenadaNoExistente) {
            coordenadaNoExistente.printStackTrace();
        }
        archivo2.delete();

    }

}//class
