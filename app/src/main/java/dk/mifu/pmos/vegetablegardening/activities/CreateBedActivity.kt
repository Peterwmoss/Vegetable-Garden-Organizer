package dk.mifu.pmos.vegetablegardening.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.databinding.ActivityCreateGardenBinding

class CreateBedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateGardenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateGardenBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbarCreate)
    }
}