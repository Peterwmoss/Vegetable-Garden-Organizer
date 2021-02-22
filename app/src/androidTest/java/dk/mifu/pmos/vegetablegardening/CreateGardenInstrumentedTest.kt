package dk.mifu.pmos.vegetablegardening

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
<<<<<<< HEAD:app/src/androidTest/java/dk/mifu/pmos/vegetablegardening/CreateBedInstrumentedTest.kt
class CreateBedInstrumentedTest {
=======
class CreateGardenInstrumentedTest {
>>>>>>> master:app/src/androidTest/java/dk/mifu/pmos/vegetablegardening/CreateGardenInstrumentedTest.kt
    @Test
    fun outdoors_button_clicked_startCreateGridFragment_with_outdoors() {
        Espresso.onView(ViewMatchers.withId(R.id.outdoors_button)).perform(ViewActions.click())
    }
}