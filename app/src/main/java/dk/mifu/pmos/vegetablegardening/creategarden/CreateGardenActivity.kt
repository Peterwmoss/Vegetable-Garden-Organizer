package dk.mifu.pmos.vegetablegardening.creategarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.data.PlantViewModel

class CreateGardenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_garden)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val model: PlantViewModel by viewModels()

        if(fragment == null){
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment_container,
                   ChoosePlantFragment(model)
                )
                .commit()
        }
    }
}