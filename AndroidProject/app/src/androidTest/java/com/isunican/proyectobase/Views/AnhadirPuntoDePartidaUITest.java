package com.isunican.proyectobase.Views;

import android.widget.ArrayAdapter;
import android.view.Gravity;
import android.widget.ListView;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.isunican.proyectobase.Model.Gasolinera;
import com.isunican.proyectobase.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 * Clase para realizar las pruebas de interfaz de usuario (UI Test) para la historia
 * de usuario "Añadir punto de partida".
 *
 * @author Ivan Sanchez Calderon (isc144@alumnos.unican.es)
 * @version Noviembre - 2020
 */

@RunWith(AndroidJUnit4.class)
public class AnhadirPuntoDePartidaUITest {

    private ListView ltmp;
    Gasolinera g1, g2, g3, gN1, gN2, gN3;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void establecerUbicacionComoPuntoPartida() throws InterruptedException {

        // Caso UIT.1A
        // Se pulsa sobre el icono de las tres barras de arriba a la izquierda para abrir el Drawer Layout
        onView(withId(R.id.menuNav)).perform(click());

        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.START))); // Start Drawer should be open.

        // Check that the Activity was opened.
        onView(withId(R.id.btnUbicacion)).check(matches(withText("Ubicación")));

        // Caso UIT.1B
        //Se pulsa sobre el boton Ubicacion
        onView(withId(R.id.btnUbicacion)).perform(click());

        onView(withText("Añada la ubicacióncomo punto de partida"));

        // Se comprueba que aparecen los campos para introducir latitud y longitud
        onView(withId(R.id.text_input_latitud));
        onView(withId(R.id.text_input_longitud));


        // Caso UIT.1C
        // Se comprueban que los botones Establecer y cancelar son los correctos
        onView(withId(R.id.btn_cancelar)).check(matches(withText("Cancelar")));

        onView(withId(R.id.btn_establecer)).check(matches(withText("Establecer")));

        // Caso UIT.2A
        // Se comprueba que al pulsar el boton "Establecer" y haber introducido unas coordenadas correctas
        // se guardan las coordenadas como ubicacion por defecto y la lista de gasolineras no se refresca.
        onView(withId(R.id.text_input_latitud)).perform(typeText("-4"), closeSoftKeyboard());
        onView(withId(R.id.text_input_longitud)).perform(typeText("43"), closeSoftKeyboard());

        // Se guardan las 3 primeras gasolineras de la lista
        ltmp = mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);
        g1 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(0);
        g2 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(1);
        g3 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(2);

        onView(withId(R.id.btn_establecer)).perform(click());

        // Se comprueba que despues de actualizar la ubicacion, lass gasolineras de la lista son las mismas
        ltmp = mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);
        gN1 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(0);
        gN2 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(1);
        gN3 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(2);
        //Comprobaciones gasolinera 1
        assertEquals(gN1.getDireccion(), g1.getDireccion());
        assertEquals(gN1.getRotulo(), g1.getRotulo());
        assertEquals(gN1.getLocalidad(), g1.getLocalidad());
        assertEquals(gN1.getGasoleoA(), g1.getGasoleoA(), 0.001);
        assertEquals(gN1.getGasoleoPremium(), g1.getGasoleoPremium(), 0.001);
        assertEquals(gN1.getGasolina95(), g1.getGasolina95(), 0.001);
        assertEquals(gN1.getGasolina98(), g1.getGasolina98(), 0.001);
        assertEquals(gN1.getBiodiesel(), g1.getBiodiesel(), 0.001);
        //Comprobaciones gasolinera 2
        assertEquals(gN2.getDireccion(), g2.getDireccion());
        assertEquals(gN2.getRotulo(), g2.getRotulo());
        assertEquals(gN2.getLocalidad(), g2.getLocalidad());
        assertEquals(gN2.getGasoleoA(), g2.getGasoleoA(), 0.001);
        assertEquals(gN2.getGasoleoPremium(), g2.getGasoleoPremium(), 0.001);
        assertEquals(gN2.getGasolina95(), g2.getGasolina95(), 0.001);
        assertEquals(gN2.getGasolina98(), g2.getGasolina98(), 0.001);
        assertEquals(gN2.getBiodiesel(), g2.getBiodiesel(), 0.001);
        //Comprobaciones gasolinera 3
        assertEquals(gN3.getDireccion(), g3.getDireccion());
        assertEquals(gN3.getRotulo(), g3.getRotulo());
        assertEquals(gN3.getLocalidad(), g3.getLocalidad());
        assertEquals(gN3.getGasoleoA(), g3.getGasoleoA(), 0.001);
        assertEquals(gN3.getGasoleoPremium(), g3.getGasoleoPremium(), 0.001);
        assertEquals(gN3.getGasolina95(), g3.getGasolina95(), 0.001);
        assertEquals(gN3.getGasolina98(), g3.getGasolina98(), 0.001);
        assertEquals(gN3.getBiodiesel(), g3.getBiodiesel(), 0.001);

        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnUbicacion)).perform(click());
        onView(withId(R.id.ubicacionPorDefecto)).check(matches(withText("Ubicación actual: -4.00, 43.00")));

        // Caso UIT.2B
        // Se comprueba que al pulsar el boton "Establecer" estando ordenando por distancia ascendente y haber introducido unas
        // coordenadas correctas se guardan las coordenadas como ubicacion por defecto y la lista de gasolineras se refresca.
        onView(withId(R.id.text_input_latitud)).perform(typeText("-5"), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText("44"), closeSoftKeyboard());

        onView(withId(R.id.btn_establecer)).perform(click());
        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnUbicacion)).perform(click());
        onView(withId(R.id.ubicacionPorDefecto)).check(matches(withText("Ubicación actual: -5.00, 44.00")));


        // Caso UIT.2C
        // Se comprueba que al pulsar el boton "Establecer" estando ordenando por distancia descendente y haber introducido unas
        // coordenadas correctas se guardan las coordenadas como ubicacion por defecto y la lista de gasolineras se refresca.
        onView(withId(R.id.text_input_latitud)).perform(typeText("-4"), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText("43"), closeSoftKeyboard());

        onView(withId(R.id.btn_establecer)).perform(click());
        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnUbicacion)).perform(click());
        onView(withId(R.id.ubicacionPorDefecto)).check(matches(withText("Ubicación actual: -4.00, 43.00")));


        // Caso UIT.3A
        // Ambos campos vacios
        onView(withId(R.id.text_input_latitud)).perform(typeText(""), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.btn_establecer)).perform(click());
        onView(withText("La latitud no puede estar vacia"));


        // Algun caracter erróneo
        onView(withId(R.id.text_input_latitud)).perform(typeText(","), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText(","), closeSoftKeyboard());
        onView(withId(R.id.btn_establecer)).perform(click());
        onView(withText("Existen caracteres erróneos"));
        onView(withId(R.id.text_input_latitud)).perform(clearText());
        onView(withId(R.id.text_input_longitud)).perform(clearText());


        // Se introduce mas de un punto
        onView(withId(R.id.text_input_latitud)).perform(typeText("5.6."), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText("1.2."), closeSoftKeyboard());
        onView(withId(R.id.btn_establecer)).perform(click());
        onView(withText("Existen caracteres erróneos"));
        onView(withId(R.id.text_input_latitud)).perform(clearText());
        onView(withId(R.id.text_input_longitud)).perform(clearText());

        // Se introduce mas de un guion
        onView(withId(R.id.text_input_latitud)).perform(typeText("--"), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText("--"), closeSoftKeyboard());
        onView(withId(R.id.btn_establecer)).perform(click());
        onView(withText("Existen caracteres erróneos"));
        onView(withId(R.id.text_input_latitud)).perform(clearText());
        onView(withId(R.id.text_input_longitud)).perform(clearText());


        // Valor demasiado largo
        onView(withId(R.id.text_input_latitud)).perform(typeText("12345678910111213"), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText("12345678910111213"), closeSoftKeyboard());
        onView(withId(R.id.btn_establecer)).perform(click());
        onView(withText("Latitud demasiado larga"));
        onView(withText("Longitud demasiado larga"));
        onView(withId(R.id.text_input_latitud)).perform(clearText());
        onView(withId(R.id.text_input_longitud)).perform(clearText());

        // La latitud no está entre -90 y 90.
        onView(withId(R.id.text_input_latitud)).perform(typeText("100"), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText("50"), closeSoftKeyboard());
        onView(withId(R.id.btn_establecer)).perform(click());
        onView(withText("La latitud debe ser entre -90 y 90"));
        onView(withId(R.id.text_input_latitud)).perform(clearText());
        onView(withId(R.id.text_input_longitud)).perform(clearText());

        // La longitud no está entre -180 y 180.
        onView(withId(R.id.text_input_latitud)).perform(typeText("4"), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText("200"), closeSoftKeyboard());
        onView(withId(R.id.btn_establecer)).perform(click());
        onView(withText("La longitud debe ser entre -180 y 180"));
        onView(withId(R.id.text_input_latitud)).perform(clearText());
        onView(withId(R.id.text_input_longitud)).perform(clearText());


        // Caso UIT.4A
        // Se comprueba que al pulsar el boton Cancelar no efectua ningun cambio.
        // Se pulsa sobre la opción “Cancelar” de la ventana  flotante despues de haber introducido valores en los campos de coordenadas.
        onView(withId(R.id.text_input_latitud)).perform(typeText("4"), closeSoftKeyboard());

        onView(withId(R.id.text_input_longitud)).perform(typeText("20"), closeSoftKeyboard());

        onView(withId(R.id.btn_cancelar)).perform(click());

        // Caso UIT.4B
        // Se comprueba que al pulsar el boton Cancelar no efectua ningun cambio.
        // Se pulsa sobre la opción “Cancelar” de la ventana sin haber insertado ningun valor.
        onView(withId(R.id.btnUbicacion)).perform(click());

        onView(withId(R.id.btn_cancelar)).perform(click());

    }
}
