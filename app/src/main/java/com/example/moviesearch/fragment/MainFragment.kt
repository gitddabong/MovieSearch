package com.example.moviesearch.fragment

import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviesearch.TAG
import com.example.moviesearch.classes.ApiClient
import com.example.moviesearch.classes.MovieAdapter
import com.example.moviesearch.classes.Movies
import com.example.moviesearch.clientId
import com.example.moviesearch.clientSecret
import com.example.moviesearch.databinding.FragmentMainBinding
import com.example.moviesearch.interfaces.ApiInterface
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. API에서 얻어온 데이터들로 영화 목록을 객체화하여 저장
        val list = ArrayList<Movies>()
        val word = "감바스알아히요"
        getResultSearch(word)

        // 2. 반복문으로 list에 영화 추가

        // 3. 리사이클러뷰에 어댑터 적용
        val adapter = MovieAdapter(list)
        binding.recyclerView.adapter = adapter

    }

    private fun getResultSearch(word: String) {
        val apiInterface: ApiInterface = ApiClient.instance!!.create(ApiInterface::class.java)
        val call: Call<String> = apiInterface.getSearchResult(clientId, clientSecret, "movie.json", word)
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    Log.e(TAG, "성공 : $result")
                } else {
                    Log.e(TAG, "실패 : " + response.body())
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e(TAG, "에러 : " + t.message)
            }
        })
    }

    private fun jsonParsing(json: String) {
        try {
            val jsonObject = JSONObject(json)
            val movieArray = jsonObject.getJSONArray("items")
            for (i in 0 until movieArray.length()) {
                val movieObject = movieArray.getJSONObject(i)
                val movie = Movie()
                movie.setTitle(movieObject.getString("title"))
                movie.setGrade(movieObject.getString("grade"))
                movie.setCategory(movieObject.getString("category"))
                movieList.add(movie)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

}