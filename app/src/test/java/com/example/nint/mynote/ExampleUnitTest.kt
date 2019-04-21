package com.example.nint.mynote

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun dayDate(){
        assertEquals(MyAppliction.dayDate(1555140755821), 13)
        assertEquals(MyAppliction.dayDate(1556610270089), 30)
        assertEquals(MyAppliction.dayDate(1556696705109), 1)
        assertEquals(MyAppliction.dayDate(951814083298), 29)
    }

    @Test
    fun monthDate(){
        assertEquals(MyAppliction.monthDate(1555140755821), 4)
        assertEquals(MyAppliction.monthDate(1556696705109), 5)
        assertEquals(MyAppliction.monthDate(951814083298), 2)
    }

}
