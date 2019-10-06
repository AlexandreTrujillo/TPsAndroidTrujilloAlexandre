package com.example.tp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG:String = "MainActivity"
    private val numeros = arrayOf("1","2","3","4","5","6","7","8","9")
    private val operations = arrayOf("+","-","x","/")
    private var nbParenthese = 0
    private enum class Operators(val sign: Char) {
        MINUS('-'),
        PLUS('+'),
        MULTIPLY('*'),
        DIVISION('/'),
    }
    private var calculFini = false
    private var histo = ""
    private var peutMettreUneVirgule = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadHisto()
    }

    fun onClick(view: View) {
        if(view is Button) { // les différents boutons du clavier
            when (view.text){
                in numeros -> majAffichage(view.text)
                "0" -> zero(view.text)
                "," -> virgule(view.text)
                in operations -> majAffichageOperation(view.text)
                "C" -> effaceTout()
                "()" -> parentheses()
                "%" -> pourcent()
                "=" -> effectueOperation()
                "+ / -" -> inverse()
            }
        } else if (view is ImageButton){ // Image < x ]
            effaceDernier()
        } else { // Historique
            // MainActivity envoie l'histo à AffichageHistorique
            val intent = Intent(this, AffichageHistorique::class.java)
            intent.putExtra("histo",histo)
            startActivity(intent)
        }
    }

    // MainActivity reçoit l'histo de AffichageHistorique
    private fun loadHisto(){
        if (intent.getStringExtra("histo") != null) {
            histo = intent.getStringExtra("histo")
        }
    }

    // Met à jour l'affichage de la calculatrice
    private fun majAffichage(text:CharSequence){
        if (calculFini) {
            effaceTout()
            calculFini = false
        }
        affichage.text.append(text)
    }

    // Efface le dernier charactère saisi, prend en compte les parenthèses et les virgules
    private fun effaceDernier(){
        val taille = affichage.text.length
        if (affichage.text.isNotEmpty()) {
            if (affichage.text.toString().last().toString() == "(") {
                nbParenthese--
            } else if (affichage.text.toString().last().toString() == ")") {
                nbParenthese++
            } else if (affichage.text.toString().last().toString() == ".") {
                peutMettreUneVirgule = true
            }
            affichage.text.delete(taille - 1, taille)
        }
    }

    // Ne peut pas mettre plus d'un zéro au début
    private fun zero(text:CharSequence){
        if (!(affichage.text.toString() == "0")) {
            majAffichage(text)
        }
    }

    // Ajoute une virgule dans un nombre qui n'en possède pas déjà une
    private fun virgule(text:CharSequence) {
        if ((peutMettreUneVirgule)
            && affichage.text.isNotEmpty()
            && !(affichage.text.last().toString() in operations)) {

            majAffichage(text)
            peutMettreUneVirgule = false
        }
    }

    // Efface tout le texte présent sur l'écran et réinitialise certaines variables
    private fun effaceTout(){
        affichage.text.clear()
        nbParenthese = 0
        peutMettreUneVirgule = true
    }

    // Met à jour l'affichage du calcul
    private fun majAffichageOperation(text:CharSequence){
        if(affichage.text.isNotEmpty()
            && (affichage.text.toString().last().toString() != ",")
            && (affichage.text.toString().last().toString() != "(")
            && !(affichage.text.last().toString() in operations)){
            majAffichage(text)
            peutMettreUneVirgule = true
        }
    }

    // Fonction qui ajoute des parenthèses et s'assure qu'elles sont correctement placées
    private fun parentheses() {
        if (affichage.text.isNotEmpty()
            && (affichage.text.last().toString() != "(")
            && ((affichage.text.last().toString() in numeros) || (affichage.text.last().toString() == ")") || (affichage.text.last().toString() == "%"))
            && nbParenthese > 0) {
            majAffichage(")")
            nbParenthese--
        }
        else if (affichage.text.isEmpty()
            || (affichage.text.last().toString() == "(")
            || (affichage.text.last().toString() in operations)) {
            majAffichage("(")
            nbParenthese++
        }
    }

    // Pas eu le temps d'implémenter
    private fun pourcent() {
        Toast.makeText(this, "Pas encore implémenté !", Toast.LENGTH_LONG).show()
    }

    // Affiche l'inverse du dernier nombre saisi (marche dans les deux sens)
    private fun inverse() {
        if (affichage.text.isEmpty()) {
            majAffichage("(-")
            nbParenthese++
        }
        else {
            for (index in affichage.text.length-1 downTo 0) {
                if(affichage.text.get(index).toString() in operations) {
                    if (affichage.text.get(index).toString() == "-"
                        && affichage.text.get(index-1).toString() == "(") {
                        affichage.text.delete(index-1,index+1)
                        nbParenthese--
                        break
                    } else {
                        affichage.text.insert(index+1,"(-")
                        nbParenthese++
                        break
                    }
                } else if (index == 0) {
                    affichage.text.insert(index,"(-")
                    nbParenthese++
                    break
                }
            }
        }
    }

    // Valide le calcul puis le soumet à l'évaluation
    private fun effectueOperation() {
        val dernierCharactere = affichage.text.last().toString()
        val calcul = affichage.text.toString()

        if (calcul != ""
            && !(dernierCharactere in operations)
            && (dernierCharactere != "(")
            && (dernierCharactere != ")")
            && (dernierCharactere != ".")
            && (nbParenthese == 0)) {

            val vraiCalcul = calcul.replace('x','*')

            val resultat = evaluate(vraiCalcul).toString()
            effaceTout()
            affichage.append(resultat)
            calculFini = true

            histo = histo.plus(calcul + " = " + resultat + ",")
        }

    }


    // Code pris sur internet
    private fun evaluate(expression: String): Double? {
        for (operator in Operators.values()) {
            var position = expression.reversed().lastIndexOf(operator.sign)
            if(position>0){
                val partialExpressions = expression.split(position)
                val left = partialExpressions[0]
                val right = partialExpressions[1]

                val value0 = evaluate(left)
                val value1 = evaluate(right)

                val res = when (operator) {
                    Operators.PLUS -> value0!! + value1!!
                    Operators.MINUS -> value0!! - value1!!
                    Operators.DIVISION -> {
                        if (value1 == 0.0)
                            throw ArithmeticException("Divide By Zero")
                        value0!! / value1!!
                    }
                    Operators.MULTIPLY -> value0!! * value1!!
                }
                if (expression.startsWith('(') && expression.endsWith(')')) {
                    return evaluate(expression.substring(1,expression.lastIndex))
                }
                return res
            }
        }
        if (expression.startsWith('(') && expression.endsWith(')')) {
            return evaluate(expression.substring(1,expression.lastIndex))
        }
        return expression.toDoubleOrNull()
    }

    private fun String.lastIndexOf(char: Char): Int {
        var bOpen = 0
        var bClose = 0
        for (i in this.indices) {
            val currChar = this[i]

            when {
                currChar == char && bOpen == bClose ->
                    return this.length - i - 1
                currChar == '(' -> bOpen++
                currChar == ')' -> bClose++
            }
        }
        return -1
    }

    private fun String.split(position: Int) = listOf(this.substring(0, position), this.substring(position + 1, this.length))

}
