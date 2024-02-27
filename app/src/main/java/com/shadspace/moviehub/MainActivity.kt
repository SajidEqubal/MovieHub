package com.shadspace.moviehub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var filterSpinner: Spinner
    val movieApi: MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        filterSpinner = findViewById(R.id.filterSpinner)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set default endpoint
        fetchMovies()

        // set up filtration
        setupSpinner()

        // Set default filter
        fetchMovies("top_rated")


        // Implement dropdown to change filters
        // You can use a Spinner or other UI components for this
        // On selection change, call fetchMovies with the selected filter
    }

    private fun setupSpinner() {
        val filters = arrayOf("Top Rated", "Popular")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filters)
        filterSpinner.adapter = adapter

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFilter = if (position == 0) "top_rated" else "popular"
                fetchMovies(selectedFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun fetchMovies(selectedFilter: String = "top_rated") {
        // Make network request using Retrofit
        lifecycleScope.launch {
            try {
                val response = movieApi.getMovies(
                    apiKey = "13befb0c6409e8c61c5e9ec4265a1d1c", // Provide your actual API key here
                    sortBy = selectedFilter
                )
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    movieAdapter = MovieAdapter(movies)
                    recyclerView.adapter = movieAdapter
                } else {
                    // Handle error
                    val errorBody = response.errorBody()?.string()
                    Log.e("MovieFetchError", "Error fetching movies: $errorBody")
                    Toast.makeText(this@MainActivity, "Error fetching movies: $errorBody", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Handle exception
                Toast.makeText(this@MainActivity, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
