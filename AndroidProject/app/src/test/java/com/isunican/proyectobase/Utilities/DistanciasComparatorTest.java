package com.isunican.proyectobase.Utilities;


import android.os.Build;

import com.isunican.proyectobase.Model.Gasolinera;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

//Ricardo Armando Blanco Lopez
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class DistanciasComparatorTest {
    DistanciasComparator sut;
    Gasolinera g1, g2, g4, g3, g5;

    @Before
    public void setUp() {
        g1 = new Gasolinera(1, "", "0", "", 3.2, 3, 3, 3, 3, "", 50, 15);
        g2 = new Gasolinera(2, "", "0", "", 3.2, 3, 3, 3, 3, "", 43, -4);

        g3 = new Gasolinera(1, "", "0", "", 3.2, 3, 3, 3, 3, "", 10, 25);

        g4 = new Gasolinera(1, "", "0", "", 3.2, 3, 3, 3, 3, "", 15, 45);
        g5 = new Gasolinera(1, "", "0", "", 3.2, 3, 3, 3, 3, "", 45, -2);

        sut = new DistanciasComparator(0, 0, true);

    }


    @Test
    public void compareTest() {

//ut.1a
        assertEquals(1, sut.compare(g1, g2), 0.001);
//ut.1b
        sut.setAsc(false);
        assertEquals(1, sut.compare(g3, g2), 0.001);
//ut.1c
        try {
            sut.compare(g3, null);
            fail();
        } catch (Exception e) {

        }

//ut.1d
        try {
            sut.compare(null,g4);
            fail();
        } catch (Exception e) {

        }
        //ut.1e
sut.setAsc(true);
        assertEquals(-1, sut.compare(g5, g5), 0.001);
//ut.1f
        sut.setAsc(false);
        assertEquals(1, sut.compare(g5, g5), 0.001);



    }
}
