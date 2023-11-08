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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }






    fun setUpListeners() {
        //nothing to do
    }

}