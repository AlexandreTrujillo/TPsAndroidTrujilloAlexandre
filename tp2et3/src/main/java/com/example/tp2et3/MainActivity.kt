package com.example.tp2et3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // créer une instance du fragment
        val fragment = ArticlesFragment()

        // créer un transaction sur le fragment manager
        supportFragmentManager.beginTransaction().apply {

            // replacer le précédent fragment, s'il existe
            replace(R.id.container, fragment)

            // ajouter la transaction dans la stack
            addToBackStack(null)

        }.commit() // finalement, on valide la transaction

    }

}
