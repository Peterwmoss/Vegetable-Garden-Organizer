package dk.mifu.pmos.vegetablegardening.helpers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class KeyboardHelper {
    companion object {
        fun showKeyboard(context: Context?, view: View) {
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 0)
        }

        fun hideKeyboard(context: Context?, editText: EditText) {
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }
}