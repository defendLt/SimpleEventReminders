package com.platdmit.simpleeventreminders.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.platdmit.simpleeventreminders.R
import com.platdmit.simpleeventreminders.app.helpers.ActionButtonControl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActionButtonControl {
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBarInit()
        navigationInit()
        actionButtonInit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item)
    }

    private fun actionBarInit() {
        setSupportActionBar(toolbar)
    }

    private fun navigationInit() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.eventsListFragment, R.id.eventFragment
        ).build()
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun actionButtonInit(){
        fab.setOnClickListener{
            navController.navigate(R.id.eventFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return (NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp())
    }

    override fun actionButtonVisible(status: Boolean) {
        if(status){
            fab.show()
        } else {
            fab.hide()
        }
    }
}