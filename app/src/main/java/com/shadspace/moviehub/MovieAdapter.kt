package com.shadspace.moviehub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            // Bind your UI components here (e.g., TextViews, ImageView)
            itemView.findViewById<TextView>(R.id.titleTextView).text = movie.title
            itemView.findViewById<TextView>(R.id.overviewTextView).text = movie.overview
            itemView.findViewById<TextView>(R.id.releaseDateTextView).text = movie.release_date
            itemView.findViewById<TextView>(R.id.originalLanguageTextView).text = movie.original_language
             val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)


            // Use Glide to load the image
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w500/${movie.backdrop_path}")
                .into(posterImageView)
        }
    }
}
