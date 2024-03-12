package com.example.divemagement
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.divemagement.ActivitysInmersiones.Anadir_inmersion
import com.example.divemagement.ActivitysInmersiones.DeleteActivity
import com.example.divemagement.ActivitysInmersiones.UpdateInmersion


open class ActivitysWithMenuLista: AppCompatActivity() {



    companion object{
        //var actividadActual = 0
    }


    //@SuppressLint("SuspiciousIndentation")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_lista, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId){
            R.id.añadir -> {
                val intent = Intent(this, Anadir_inmersion::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent)
                true
            }
            R.id.eliminar -> {
                val intent = Intent(this, DeleteActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent)
                true
            }
            R.id.actualizar -> {
                val intent = Intent(this, UpdateInmersion::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent)
                true
            }
            R.id.id_salir -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }




    }
}
