package com.isunican.proyectobase.Utilities;

import android.os.Build;

import androidx.core.view.ScaleGestureDetectorCompat;

import com.isunican.proyectobase.Model.Gasolinera;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class CalculaDistanciaTest {
    @Before
    public  void setUp(){

       }
    @Test
    public void distanciaEntreDosCoordenadasTest(){

        //UT.2A
        assertEquals(62.6574, CalculaDistancia.distanciaEntreDosCoordenadas(43.353139713292,-4.062254446255398,43.352619991145394,-4.062554545912644), 0.001);
        //UT.2B
        assertEquals(0, CalculaDistancia.distanciaEntreDosCoordenadas(43,-4,43,-4), 0.001);
        //UT.2C
        try {
            CalculaDistancia.distanciaEntreDosCoordenadas(195,49,52,25);
            fail();
        }catch (Exception e){

        }
        //UT.2D
        try {
            CalculaDistancia.distanciaEntreDosCoordenadas(-25,-280,5,2);
            fail();
        }catch (Exception e){

        }
        //UT.2E
        try {
            CalculaDistancia.distanciaEntreDosCoordenadas(5,80,95,2);
            fail();
        }catch (Exception e){

        }
        //UT.2F
        try {
            CalculaDistancia.distanciaEntreDosCoordenadas(44,-6,43,190);
            fail();
        }catch (Exception e){

        }



    }
}
