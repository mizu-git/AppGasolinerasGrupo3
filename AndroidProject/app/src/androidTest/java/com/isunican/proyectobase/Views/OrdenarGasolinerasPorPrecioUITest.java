package com.isunican.proyectobase.Views;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import com.isunican.proyectobase.Model.Gasolinera;
import com.isunican.proyectobase.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class OrdenarGasolinerasPorPrecioUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private ListView ltmp;
    private Gasolinera gBarata;
    private Gasolinera gCara;

    @Before
    public void setUp() {
        // Se va a estabblecer como tipo de combutiuble el gasoleo A
        //para hacer esta prueba
        onView(ViewMatchers.withId(R.id.buttonFiltros)).perform(click());
        onView((withId(R.id.spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Gasóleo A")))
                .inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Gasóleo A"))));
        onView(withText("Aceptar")).perform(click());

        // Context of the app under test.
        ltmp = mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);
        gBarata = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(0);
        int lng = ltmp.getAdapter().getCount();
        gCara = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(lng - 1);
    }

    @Test
    public void ordenPorPrecio() {
        //clickamos en la opcion de ordenar gasolineras
        //para comprobar que el boton existe
        onView(withId(R.id.buttonOrden)).perform(click());
        onView(withText("Cancelar")).perform(click());

        //se comprueba que la imagen del iconoOrdenar es la flecha hacia arriba por defecto
        onView(withTagValue(is(R.drawable.flecha_arriba))).check(matches(isDisplayed()));

        // se comprueba que las gasolineras estan correctamente ordenadas comparando
        // el precio de todas estas y comprobando que el precio va subiendo segun
        // avanzamos en la lista
        for (int i = 0; i < ltmp.getAdapter().getCount() - 1; i++) {
            Gasolinera tmp1 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(i);
            Gasolinera tmp2 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(i+1);
            assertTrue(tmp2.getGasoleoA() >= tmp1.getGasoleoA());
        }

        //se cambia el orden a descendente
        onView(withId(R.id.iconoOrden)).perform(click());
        //se comprueba que la imagen del iconoOrdenar es la flecha hacia arriba
        onView(withTagValue(is(R.drawable.flecha_abajo))).check(matches(isDisplayed()));

        // se comprueba que las gasolineras estan correctamente ordenadas comparando
        // el precio de todas estas y comprobando que el precio va bajando segun
        // avanzamos en la lista
        for (int i = 0; i < ltmp.getAdapter().getCount() - 1; i++) {
            Gasolinera tmp1 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(i);
            Gasolinera tmp2 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(i+1);
            assertTrue(tmp2.getGasoleoA() <= tmp1.getGasoleoA());
        }

        //comprueba que la gasolinera en la primera posicion cuando el orden es descendente es la gasolinera
        //con el precio mas caro
        onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(0)
                .onChildView(withId(R.id.textViewGasoleoA)).check(matches(withText(gCara.getGasoleoA() + "€")));

        onView(withId(R.id.buttonOrden)).perform(click());
        onView(withText("Aceptar")).perform(click());

        //se cambia el orden a ascendente
        onView(withId(R.id.iconoOrden)).perform(click());
        //se comprueba que la imagen del iconoOrdenar es la flecha hacia arriba
        onView(withTagValue(is(R.drawable.flecha_arriba))).check(matches(isDisplayed()));

        //comprueba que la gasolinera en la primera posicion cuando el orden es ascendente es la gasolinera
        //con el precio mas barato
        onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(0)
                .onChildView(withId(R.id.textViewGasoleoA)).check(matches(withText(gBarata.getGasoleoA() + "€")));
    }
}