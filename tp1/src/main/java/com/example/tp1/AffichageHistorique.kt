package com.example.tp1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.historique.*

class AffichageHistorique : AppCompatActivity() {

    private var histo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historique)

        loadHisto()

        affichageHisto()
    }

    // L'historique affiche en premier les calculs les plus récents
    private fun affichageHisto() {
        var histoTab = histo.split(",").reversed()
        var histoMisEnForme = ""
        for (element in histoTab) {
            histoMisEnForme = histoMisEnForme.plus(element).plus("\n")
        }
        historiqueDetails.text = histoMisEnForme
    }

    // AffichageHistorique envoie l'histo à MainActivity
    fun onClick(view: View) {
        if(view is TextView) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("histo",histo)
            startActivity(intent)
        }
    }

    // AffichageHistorique reçoit l'histo de MainActivity
    private fun loadHisto(){
        if (intent.getStringExtra("histo") != null) {
            histo = intent.getStringExtra("histo")
        }
    }
}