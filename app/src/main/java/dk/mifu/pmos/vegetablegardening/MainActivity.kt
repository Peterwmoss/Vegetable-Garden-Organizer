package dk.mifu.pmos.vegetablegardening

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.mifu.pmos.vegetablegardening.creategarden.CreateGardenActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        new_location_btn.setOnClickListener {
            val createIntent = Intent(this, CreateGardenActivity::class.java)
            startActivity(createIntent)
        }
    }
}