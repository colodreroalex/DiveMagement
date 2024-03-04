package com.example.divemagement


import android.annotation.SuppressLint
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity


open class ActivityWithMenus: AppCompatActivity() {
    companion object{
        var actividadActual = 0
        var isLoggedIn = false
    }
    /* Quitar interrogation? */

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)


        if(menu != null){
            for(i in 0 until menu.size()) {
                if(i == actividadActual) menu.getItem(i).isEnabled = false
                else menu.getItem(i).isEnabled = true
            }


        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId){
            R.id.registerActivity -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                actividadActual = 0
                startActivity(intent)

                true
            }
            R.id.loginActivity -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                actividadActual = 1
                startActivity(intent)

                true
            }



            else -> super.onOptionsItemSelected(item)
        }




    }
}