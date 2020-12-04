package com.isunican.proyectobase.Views;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;
import com.isunican.proyectobase.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/*
 * Clase para realizar las pruebas de interfaz de usuario (UI Test) para la historia
 * de usuario "Buscar gasolineras por un tipo de combustible".
 *
 * @author Juan David Corrales Gil (jcg700@alumnos.unican.es)
 * @version Octubre - 2020
 */
@RunWith(AndroidJUnit4.class)
public class BuscarGasolinerasPorUnTipoDeCombustibleUITest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void tiposCombustible_Test() {

        // Se pulsa el boton para acceder a los Filtros.
        onView(ViewMatchers.withId(R.id.buttonFiltros)).perform(click());

        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.spinner))).perform(click());

        // Se comprueba seleccionando cada elemento existente dentro del spinner
        // y se comprueba su correspondiente nombre si es el correcto
        onData(allOf(is(instanceOf(String.class)),
                is("Combustible"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Combustible"))));

        onView((withId(R.id.spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo A"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Gasóleo A"))));

        onView((withId(R.id.spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Gasolina 95"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Gasolina 95"))));

        onView((withId(R.id.spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Gasolina 98"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Gasolina 98"))));

        onView((withId(R.id.spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Biodiésel"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Biodiésel"))));

        onView((withId(R.id.spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo Premium"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Gasóleo Premium"))));

        // Se pulsa el botón Cancelar dentro del menú desplegable
        onView(withText("Cancelar")).perform(click());

        /**gasoleoA_Test()*/

        // Se pulsa el boton para acceder a los Filtros.
        onView(withId(R.id.buttonFiltros)).perform(click());

        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.spinner))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Gasoleo A
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo A"))).inRoot(isPlatformPopup()).perform(click());

        // Se pulsa el botón Aceptar dentro del menú desplegable
        onView(withText("Aceptar")).perform(click());

        // Se comprueba que el tipo de combustible se ha cambiado correctamente accediendo
        // a la primera gasolinera y comprobando su textViewTipoGasolina
        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(0)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Gasóleo A")));

        /**gasolina95_Test()*/

        // Se pulsa el boton para acceder a los Filtros.
        onView(withId(R.id.buttonFiltros)).perform(click());

        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.spinner))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Gasolina 95
        onData(allOf(is(instanceOf(String.class)),
                is("Gasolina 95"))).inRoot(isPlatformPopup()).perform(click());

        onView(withText("Aceptar")).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(0)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Gasolina 95")));

        /**gasolina98_Test()*/

        // Se pulsa el boton para acceder a los Filtros.
        onView(withId(R.id.buttonFiltros)).perform(click());

        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.spinner))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Gasolina 98
        onData(allOf(is(instanceOf(String.class)),
                is("Gasolina 98"))).inRoot(isPlatformPopup()).perform(click());

        onView(withText("Aceptar")).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(0)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Gasolina 98")));

        /**biodiésel_Test()*/

        // Se pulsa el boton para acceder a los Filtros.
        onView(withId(R.id.buttonFiltros)).perform(click());

        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.spinner))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Biodiésel
        onData(allOf(is(instanceOf(String.class)),
                is("Biodiésel"))).inRoot(isPlatformPopup()).perform(click());

        onView(withText("Aceptar")).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(0)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Biodiésel")));

        /**gasóleoPremium_Test()*/

        // Se pulsa el boton para acceder a los Filtros.
        onView(withId(R.id.buttonFiltros)).perform(click());

        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.spinner))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Gasóleo Premium
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo Premium"))).inRoot(isPlatformPopup()).perform(click());

        onView(withText("Aceptar")).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(0)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Gasóleo Premium")));

    }

}
