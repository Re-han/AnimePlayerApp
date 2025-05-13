package com.example.seekhotask

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seekhotask.databinding.ActivityMainBinding
import com.example.seekhotask.dto.AnimeDTO
import com.example.seekhotask.dto.Data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() {
            return _binding ?: ActivityMainBinding.inflate(layoutInflater)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAnimeList()
    }

    private fun getAnimeList() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
        val listCall: Call<AnimeDTO>? =
            ApiClient.apiInterface?.getTopAnimeList()
        listCall?.enqueue(object : Callback<AnimeDTO> {
            override fun onResponse(call: Call<AnimeDTO>, response: Response<AnimeDTO>) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "${response.code()} Error. Please try again Later",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (response.body()?.data?.isNotEmpty() == true) {
                    initRv(response.body()?.data!!)
                }
            }

            override fun onFailure(p0: Call<AnimeDTO>, p1: Throwable) {
                Log.d("MyTag", p1.message.toString())
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRv(data: List<Data>) {
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = AnimeAdapter(data as ArrayList)
    }
}