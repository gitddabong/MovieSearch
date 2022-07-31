package com.example.moviesearch.classes

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesearch.R


class MovieAdapter(private val items: ArrayList<Movies>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: Movies, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    var listener: OnItemClickListener? = null

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            bind(item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieAdapter.ViewHolder(inflatedView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var imageView: ImageView
        lateinit var titleView: TextView
        lateinit var pubDateView: TextView
        lateinit var userRatingView: TextView

        init {
            imageView = itemView.findViewById(R.id.image)
            titleView = itemView.findViewById(R.id.title)
            pubDateView = itemView.findViewById(R.id.pubDate)
            userRatingView = itemView.findViewById(R.id.userRating)
        }

        fun bind(item: Movies) {
            Glide.with(itemView).load(item.image).into(imageView)
            titleView.text = item.title
            pubDateView.text = item.pubDate
            userRatingView.text = item.userRating
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                itemView.context.startActivity(intent)
            }
        }
    }

}
