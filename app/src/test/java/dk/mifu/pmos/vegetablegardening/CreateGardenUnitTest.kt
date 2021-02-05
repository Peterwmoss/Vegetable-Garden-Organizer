package dk.mifu.pmos.vegetablegardening

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test

class CreateGardenUnitTest {
    @Test
    fun outdoors_button_clicked_startCreateGridFragment_with_outdoors() {
        onView(withId(R.id.outdoors_button)).perform(click())
    }
}