package com.isunican.proyectobase.Views;

import android.Manifest;
import android.view.Gravity;
import android.widget.ListView;


import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.isunican.proyectobase.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
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
 * de usuario "Establecer Tipo De Combustible".
 *
 * @author Juan David Corrales Gil (jcg700@alumnos.unican.es)
 * @version Noviembre - 2020
 */
@RunWith(AndroidJUnit4.class)
public class EstablecerTipoDeCombustibleUITest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    
    private ListView ltmp;
    @Before
    public void setUp(){
        // Se va a estabblecer como tipo de combustible el gasoleo A
        onView(ViewMatchers.withId(R.id.buttonFiltros)).perform(click());
        onView((withId(R.id.spinner))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo A"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Gasóleo A"))));
        onView(withText("Aceptar")).perform(click());
        // Variable para tener la lista de gasolineras
        ltmp = mActivityTestRule.getActivity().findViewById(R.id.listViewGasolineras);
    }

    @Test
    public void establecerTipoCombustibleTest() throws InterruptedException {

        // Caso UIT.1A
        // Se pulsa sobre el icono de las tres barras de arriba a la izquierda para abrir el Drawer Layout
        onView(withId(R.id.menuNav)).perform(click());

        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.START))); // Start Drawer should be open.

        // Check that the Activity was opened.
        onView(withId(R.id.btnConfiguracion)).check(matches(withText("Configuración")));

        // Caso UIT.1B
        onView(withId(R.id.btnConfiguracion)).perform(click());

        onView(withId(R.id.defecto)).check(matches(withText("Escoja su combustible de preferencia")));

        // Se comprueba seleccionando cada elemento existente dentro del spinner
        // y se comprueba su correspondiente nombre si es el correcto
        onView((withId(R.id.combustible_por_defecto))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo A"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.combustible_por_defecto)).check(matches(withSpinnerText(containsString("Gasóleo A"))));

        onView((withId(R.id.combustible_por_defecto))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Gasolina 95"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.combustible_por_defecto)).check(matches(withSpinnerText(containsString("Gasolina 95"))));

        onView((withId(R.id.combustible_por_defecto))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Gasolina 98"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.combustible_por_defecto)).check(matches(withSpinnerText(containsString("Gasolina 98"))));

        onView((withId(R.id.combustible_por_defecto))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Biodiésel"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.combustible_por_defecto)).check(matches(withSpinnerText(containsString("Biodiésel"))));

        onView((withId(R.id.combustible_por_defecto))).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo Premium"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.combustible_por_defecto)).check(matches(withSpinnerText(containsString("Gasóleo Premium"))));

        // Caso UIT.1C
        // Se comprueban que los botones aplicar y cancelar son los correctos
        onView(withText("Cancelar")).check(matches(withText("Cancelar")));

        onView(withText("Aplicar")).check(matches(withText("Aplicar")));

        // Caso UIT.1G
        // Se comprueba que al pulsar el boton Cancelar no efectua ningun cambio.
        // Se pulsa sobre la opción “Cancelar” de la ventana flotante luego de haber seleccionado un tipo de combustible del desplegable.
        onView(withText("Cancelar")).perform(click());

        // Comprobamos que el combustible establecido es el mismo que al principio, es decir, Gasoleo A
        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(0)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Gasóleo A")));


        // Caso UIT.1F
        // Se comprueba que al pulsar el boton Cancelar no efectua ningun cambio.
        // Se pulsa sobre la opción “Cancelar” de la ventana flotante sin haber seleccionado ningún combustible del desplegable.
        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnConfiguracion)).perform(click());

        onView(withText("Cancelar")).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(10)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Gasóleo A")));


        // Caso UIT.1E
        // Se comprueba que al pulsar el boton Aplicar no efectua ningun cambio.
        // Se pulsa sobre la opción “Aplicar” de la ventana flotante sin haber seleccionado ningún combustible del desplegable.
        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnConfiguracion)).perform(click());

        onView(withText("Aplicar")).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(15)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Gasóleo A")));


        /*
         * Se comprueba establecer cada tipo de combustible y probar su correcto funcionamiento
         * pulsando sobre una gasolineras de la lista despues de que se haya refrescado correctamente.
         *
         * Excepto para el tipo de combustible Gasolina 98 en el cual se va a comporbar que todas las gasolineras
         * de la lista son del tipo de combustible establecido, es decir, gasolina 98.
         * Casos UIT.1D
         */
        /** gasoleoA_Test() **/

        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnConfiguracion)).perform(click());
        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.combustible_por_defecto))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Gasoleo A
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo A"))).inRoot(isPlatformPopup()).perform(click());

        // Se pulsa el botón Aceptar dentro del menú desplegable
        onView(withText("Aplicar")).perform(click());

        // Se comprueba que el tipo de combustible se ha cambiado correctamente accediendo
        // a cada una de las gasolineras de la lista actual y comprobando su textViewTipoGasolina
        for (int i=0; i<ltmp.getCount()-1; i++) {
            onData(anything())
                    .inAdapterView(withId(R.id.listViewGasolineras))
                    .atPosition(i)
                    .onChildView(withId(R.id.textViewTipoGasolina))
                    .check(matches(withText("Gasóleo A")));
        }

        /** gasolina95_Test() **/
        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnConfiguracion)).perform(click());
        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.combustible_por_defecto))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Gasolina 95
        onData(allOf(is(instanceOf(String.class)),
                is("Gasolina 95"))).inRoot(isPlatformPopup()).perform(click());

        onView(withText("Aplicar")).perform(click());

        // Se accede a la primera gasolinera de la lista
        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(0)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Gasolina 95")));

        /** gasolina98_Test() **/
        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnConfiguracion)).perform(click());
        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.combustible_por_defecto))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Gasolina 98
        onData(allOf(is(instanceOf(String.class)),
                is("Gasolina 98"))).inRoot(isPlatformPopup()).perform(click());

        onView(withText("Aplicar")).perform(click());

        // Se accede a cada una de las gasolineras de la lista refrescada
        for (int i=0; i<ltmp.getCount()-1; i++) {
            onData(anything())
                    .inAdapterView(withId(R.id.listViewGasolineras))
                    .atPosition(i)
                    .onChildView(withId(R.id.textViewTipoGasolina))
                    .check(matches(withText("Gasolina 98")));
        }


        /** biodiésel_Test() **/
        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnConfiguracion)).perform(click());
        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.combustible_por_defecto))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Biodiésel
        onData(allOf(is(instanceOf(String.class)),
                is("Biodiésel"))).inRoot(isPlatformPopup()).perform(click());

        onView(withText("Aplicar")).perform(click());

        // Se accede a cada una de las gasolineras de la lista actualizada
        for (int i=0; i<ltmp.getCount()-1; i++) {
            onData(anything())
                    .inAdapterView(withId(R.id.listViewGasolineras))
                    .atPosition(i)
                    .onChildView(withId(R.id.textViewTipoGasolina))
                    .check(matches(withText("Biodiésel")));
        }

        /**gasóleoPremium_Test() **/
        onView(withId(R.id.menuNav)).perform(click());
        onView(withId(R.id.btnConfiguracion)).perform(click());
        // Antes de cada test se pulsa el spinner para ver su contenido
        onView((withId(R.id.combustible_por_defecto))).perform(click());

        // Se selecciona el segundo tipo de combustible que se corresponde con Gasóleo Premium
        onData(allOf(is(instanceOf(String.class)),
                is("Gasóleo Premium"))).inRoot(isPlatformPopup()).perform(click());

        onView(withText("Aplicar")).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listViewGasolineras))
                .atPosition(5)
                .onChildView(withId(R.id.textViewTipoGasolina))
                .check(matches(withText("Gasóleo Premium")));

    }
}