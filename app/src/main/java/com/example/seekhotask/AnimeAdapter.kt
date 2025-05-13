package com.example.seekhotask

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.seekhotask.dto.Data
import com.google.android.material.chip.Chip

class AnimeAdapter(private val list: ArrayList<Data>) :
    RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.title)
        val noOfEp = view.findViewById<TextView>(R.id.epNum)
        val ratingNum = view.findViewById<TextView>(R.id.ratingNum)
        val ratingText = view.findViewById<Chip>(R.id.ratingText)
        val poster = view.findViewById<ImageView>(R.id.poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.anime_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = list[position]
        Glide.with(holder.itemView).load(list.images.jpg.large_image_url).into(holder.poster)
        holder.title.text = list.title
        holder.noOfEp.text = "Number Of Episodes : ${list.episodes}"
        holder.ratingNum.text = "${list.score}"
        holder.ratingText.text = "Rating : ${list.rating}"
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AnimeDetailsActivity::class.java)
            intent.putExtra("anime_id", list.mal_id)
            holder.itemView.context.startActivity(intent)
        }
    }
}