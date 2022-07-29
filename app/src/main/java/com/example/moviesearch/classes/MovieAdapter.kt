package com.example.moviesearch.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        val listener = View.OnClickListener {
            if(position!= RecyclerView.NO_POSITION) {
                listener?.onItemClick(item,position)
            }
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieAdapter.ViewHolder(inflatedView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var image: ImageView
        lateinit var title: TextView
        lateinit var year: TextView
        lateinit var star: TextView

        init {
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            year = itemView.findViewById(R.id.year)
            star = itemView.findViewById(R.id.star)
        }

        fun bind(listener:View.OnClickListener, item: Movies) {
//            image.setImageResource()
            title.text = item.title
            year.text = item.year.toString()
            star.text = item.star
            itemView.setOnClickListener(listener)
        }

    }

}
