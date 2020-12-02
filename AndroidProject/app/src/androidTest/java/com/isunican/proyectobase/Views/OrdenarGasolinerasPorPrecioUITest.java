package com.isunican.proyectobase.Views;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import com.isunican.proyectobase.Model.Gasolinera;
import com.isunican.proyectobase.R;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/*
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
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo A"))).inRoot(isPlatformPopup()).perform(click());
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
        /*
        //clickamos en la opcion de ordenar gasolineras
        onView(withId(R.id.buttonOrden)).perform(click());
        onView(withText("Cancelar")).perform(click());
        onView(withId(R.id.buttonOrden)).perform(click());
        //Comprueba que por defecto el texto del boton indica que el orden es ascendente
        onView(withId(R.id.buttonprecio)).check(matches(withText("Precio (asc)")));
        onView(withText("Cancelar")).perform(click());

        //Comprueba que la gasolinera en la primera posicion es mas barata que la que esta en la seg-unda
        //y la que esta en la segunda es mas barata que la esta en la tercera
        Gasolinera tmp1 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(1);
        assertTrue(tmp1.getGasoleoA() >= gBarata.getGasoleoA());
        Gasolinera tmp2 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(2);
        assertTrue(tmp2.getGasoleoA() >= tmp1.getGasoleoA());

        //compruebo que cuando hago un click en el boton ordernar por precio su texto
        //cambia para indicar que el orden ahora es descendente
        onView(withId(R.id.buttonOrden)).perform(click());
        onView(withId(R.id.buttonprecio)).perform(click());
        onView(withId(R.id.buttonprecio)).check(matches(withText("Precio (des)")));
        onView(withText("Aceptar")).perform(click());

        //comprueba que la gasolinera en la primera posicion es mas cara que la que esta en la segunda
        //y la que esta en la segunda es mas cara que la esta en la tercera
        tmp1 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(1);
        assertTrue(tmp1.getGasoleoA() <= gCara.getGasoleoA());
        tmp2 = ((ArrayAdapter<Gasolinera>) ltmp.getAdapter()).getItem(2);
        assertTrue(tmp2.getGasoleoA() <= tmp1.getGasoleoA());

        //comprueba que la gasolinera en la primera posicion cuando el orden es descendente es la gasolinera
        //con el precio mas caro
        onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(0).onChildView(withId(R.id.textViewGasoleoA)).check(matches(withText(gCara.getGasoleoA() + "€")));


        onView(withId(R.id.buttonOrden)).perform(click());
        onView(withId(R.id.buttonprecio)).perform(click());
        onView(withId(R.id.buttonprecio)).check(matches(withText("Precio (asc)")));
        onView(withText("Aceptar")).perform(click());

        //comprueba que la gasolinera en la primera posicion cuando el orden es ascendente es la gasolinera
        //con el precio mas barato
        onData(anything()).inAdapterView(withId(R.id.listViewGasolineras)).atPosition(0).onChildView(withId(R.id.textViewGasoleoA)).check(matches(withText(gBarata.getGasoleoA() + "€")));


        //comprueba que al darle click al boton ordernar por precio y pulsa cancelar
        //no se efectua ningun cambio
        onView(withId(R.id.buttonOrden)).perform(click());
        onView(withId(R.id.buttonprecio)).perform(click());
        onView(withText("Cancelar")).perform(click());
        onView(withId(R.id.buttonOrden)).perform(click());
        onView(withId(R.id.buttonprecio)).check(matches(withText("Precio (asc)")));
        onView(withText("Aceptar")).perform(click());


        //comprueba que
        onView(withId(R.id.buttonOrden)).perform(click());
        onView(withId(R.id.buttonprecio)).check(matches(withText("Precio (asc)")));
        onView(withText("Cancelar")).perform(click());
        //assertEquals(1, 1, 0.001);
    }

}*/
