package com.example.seekhotask

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.seekhotask.databinding.ActivityAnimeDetailsBinding
import com.example.seekhotask.dto.Data
import com.example.seekhotask.dto.Detail
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AnimeDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityAnimeDetailsBinding? = null
    private val binding: ActivityAnimeDetailsBinding
        get() {
            return _binding ?: ActivityAnimeDetailsBinding.inflate(layoutInflater)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        val id = intent?.getIntExtra("anime_id", 0)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lifecycle.addObserver(binding.youTubePlayerView)
        getAnimeDetails(id ?: 0)
    }

    private fun getAnimeDetails(id: Int) {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
        val listCall: Call<Detail>? =
            ApiClient.apiInterface?.getAnimeDetails(id)
        listCall?.enqueue(object : Callback<Detail> {
            override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@AnimeDetailsActivity,
                        "${response.code()} Error. Please try again Later",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (response.body()?.data != null) {
                    Log.d("MyTag", response.body()?.data?.title.toString())
                    setUI(response.body()?.data!!)
                }
            }

            override fun onFailure(p0: Call<Detail>, p1: Throwable) {
                Log.d("MyTag", p1.message.toString())
                Toast.makeText(
                    this@AnimeDetailsActivity,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setUI(body: Data) {
        if (body.trailer.youtube_id != null) {
            binding.youTubePlayerView.visibility = View.VISIBLE
            binding.poster.visibility = View.GONE
            binding.youTubePlayerView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = body.trailer.youtube_id
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
            binding.guide.setGuidelinePercent(.35f)
        } else {
            binding.youTubePlayerView.visibility = View.GONE
            binding.poster.visibility = View.VISIBLE
            Glide.with(this).load(body.images.jpg.large_image_url).into(binding.poster)
            binding.guide.setGuidelinePercent(.45f)
        }
        binding.title.text = "Title : ${body.title}"
        binding.epNum.text = "Number Of Episodes : ${body.episodes}"
        binding.ratingNum.text = "${body.score}"
        binding.ratingText.text = "Rating : ${body.rating}"
        binding.plotSub.text = body.synopsis
        binding.genreSub.text = "Genre(s) : ${body.genres.joinToString(", ") { it.name }}"
        binding.producers.text = "Producers : ${body.producers.joinToString(", ") { it.name }}"
    }
}