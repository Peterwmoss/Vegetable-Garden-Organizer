package dk.mifu.pmos.vegetablegardening

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dk.mifu.pmos.vegetablegardening.creategarden.CreateGardenActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { _ ->
            val createIntent = Intent(this, CreateGardenActivity::class.java)
            startActivity(createIntent)
        }
    }
}