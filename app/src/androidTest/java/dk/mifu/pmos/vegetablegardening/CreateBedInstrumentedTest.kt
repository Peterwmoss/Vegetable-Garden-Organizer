package dk.mifu.pmos.vegetablegardening

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CreateBedInstrumentedTest {
    @Test
    fun outdoors_button_clicked_startCreateGridFragment_with_outdoors() {
        Espresso.onView(ViewMatchers.withId(R.id.outdoors_button)).perform(ViewActions.click())
    }
}