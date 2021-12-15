package com.example.rssstackoverflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rssstackoverflow.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var questionRecyclerView: RecyclerView
    lateinit var questionAdapter: QuestionAdapter
    private var questions = ArrayList<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        parseRSS()
    }

    private fun setupRecyclerView() {
        questionRecyclerView = binding.rvQuestion
        questionAdapter = QuestionAdapter(questions, this)
        questionRecyclerView.adapter = questionAdapter
        questionRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun parseRSS(){
        CoroutineScope(IO).launch {
            val data = async {
                val parser = XMLParser()
                parser.parse()
            }.await()
            try{
                withContext(Main){
                    Log.d("MAIN", "data\n ${data[0].title}\n${data[0].category}")
                    questionAdapter.update(data)
                }
            }catch(e: java.lang.Exception){
                Log.d("MAIN", "Unable to get data")
            }
        }
    }
}