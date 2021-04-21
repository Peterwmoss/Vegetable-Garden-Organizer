package dk.mifu.pmos.vegetablegardening.helpers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText

class KeyboardHelper {
    companion object {
        fun showKeyboard(context: Context?, editText: EditText) {
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            editText.postDelayed({
                editText.requestFocus()
                inputMethodManager.showSoftInput(editText, SHOW_IMPLICIT)
            }, 100)
        }

        fun hideKeyBoard(context: Context?, view: View) {
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}