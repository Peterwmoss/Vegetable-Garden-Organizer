package dk.mifu.pmos.vegetablegardening.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.appcompat.widget.MenuPopupWindow
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.appbar.MaterialToolbar
import dk.mifu.pmos.vegetablegardening.R
import dk.mifu.pmos.vegetablegardening.database.AppDatabase
import dk.mifu.pmos.vegetablegardening.database.SeasonRepository
import dk.mifu.pmos.vegetablegardening.databinding.ActivityMainBinding
import dk.mifu.pmos.vegetablegardening.viewmodels.BedViewModel
import dk.mifu.pmos.vegetablegardening.viewmodels.SeasonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val seasonViewModel: SeasonViewModel by viewModels()

    companion object {
        private const val ALL_PERMISSIONS_RESULT = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), ALL_PERMISSIONS_RESULT)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        val drawerLayout = binding.drawerLayout
        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.gardenOverviewFragment, R.id.lexiconFragment, R.id.newSeasonFragment), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        setUpSeasonsInDrawer()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setUpSeasonsInDrawer(){
        val seasonsMenu = binding.navigationView.menu.getItem(3).subMenu
        val dao = AppDatabase.getDatabase(this).seasonDao()
        val seasons = SeasonRepository(dao).getAllSeasons()

        seasons.observe(this, { list ->
            seasonsMenu.clear()

            list.sortedByDescending { it.season }.forEach {
                val item = seasonsMenu.add(it.season.toString())
                item.icon = ContextCompat.getDrawable(this, R.drawable.bed)
                item.setOnMenuItemClickListener {
                    seasonViewModel.currentSeason.value = item.title.toString().toInt()
                    findNavController(R.id.nav_host_fragment).navigate(R.id.gardenOverviewFragment)
                    return@setOnMenuItemClickListener false
                }
            }
            binding.navigationView.invalidate()
            setUpNavigation()
        })
    }

    private fun setUpNavigation(){
        binding.navigationView.setupWithNavController(navController)
    }

}
