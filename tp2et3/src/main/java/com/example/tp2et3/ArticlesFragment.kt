package com.example.tp2et3


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2et3.network.ArticleRepository
import com.example.tp2et3.network.ArticleService
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticlesFragment : Fragment() {

    lateinit var sujets:Array<String>
    lateinit var spinner:Spinner
    lateinit var adapter:SpinnerAdapter
    lateinit var recyclerView:RecyclerView
    lateinit var adapterRecycler:ArticleAdapter
    val artRepo = ArticleRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.articles_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sujets = resources.getStringArray(R.array.sujets)
        spinner = view.findViewById(R.id.spinner)
        adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, sujets)
        spinner.adapter = adapter

        recyclerView = view.findViewById(R.id.reycler_view)

        // créer une liste d'articles
        val articles = listOf<Article>()

        // créer une instance de l'adapteur
        adapterRecycler = ArticleAdapter(articles)

        // définir l'orientation des élements (vertical)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // associer l'adapter à la recyclerview
        recyclerView.adapter = adapterRecycler

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(context, "Vous n'avez rien selectionné", Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(context, "Vous avez selectionné le sujet \"${sujets[position]}\"", Toast.LENGTH_LONG).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        GlobalScope.launch {
            getData()
        }
    }

    //S'execute dans un thread secondeaire
    private suspend fun getData() {
        withContext(Dispatchers.IO) {
            val resultList = artRepo.bitcoins()
            bindData(resultList)
        }
    }

    //S'execute sur le thread principal
    private suspend fun bindData(resultList: List<Article>) {
        withContext(Dispatchers.Main) {

            //afficher les données dans le recycler
            for (result in resultList) {
                println(result.title)
                println(result.author)
                println(result.description)
                recyclerView.article_title.text = result.title
                recyclerView.article_author.text = result.author
                recyclerView.article_description.text = result.description

            }

        }
    }



}
