package com.gd05.brickr.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.gd05.brickr.R
import com.gd05.brickr.databinding.ActivityHomeBinding

/** HomeActivity is a class that define the Activity where we are going to deploy different fragments */
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    /** We define the navController val in charge of handle everything related to navigation
     * we assign the nav_host_fragment we define in activity_home and returns as NavHostFragment*/
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setUpUI()
        setUpListeners()
    }

    fun setUpUI() {
        /**We link the bottonNavigation with the NavController*/
        binding.bottomNavigation.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.FavoriteFragment,
                R.id.SearchFragment,
                R.id.InventoryFragment
            )
        )
        /**We link the toolbar(our instance) to the ActionBar(Class from androidX)-->*/
        setSupportActionBar(binding.toolbar)
        /**We link the ActionBar(from androidX) to the navController(define in this app from androidX)*/
        setupActionBarWithNavController(navController, appBarConfiguration)
        /**The text for the ToolBar will be the label of the botton_bar_nav_graph.xml(Define in strings.xml)*/

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    /**This method is in charge of inflate the menu*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_home, menu)

        val searchItem = menu?.findItem(R.id.action_search) /**We find the search item*/
        val searchView = searchItem?.actionView as SearchView /**We cast the searchItem to SearchView*/

        // Configure the search info and add any event listeners.

        return super.onCreateOptionsMenu(menu)
    }

    /**This method is in charge of handle the click on the menu*/
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chooses the "Settings" item. Show the app settings UI.
            Toast.makeText(this, "Settings option", Toast.LENGTH_SHORT).show()
            true
        }
        else -> {
            // The user's action isn't recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


    fun setUpListeners() {
        //nothing to do
    }

}