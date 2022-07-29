package com.example.moviesearch.fragment

import android.os.Bundle
import android.util.JsonReader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviesearch.classes.MovieAdapter
import com.example.moviesearch.classes.Movies
import com.example.moviesearch.databinding.FragmentMainBinding
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


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


        // 2. 반복문으로 list에 영화 추가

        // 3. 리사이클러뷰에 어댑터 적용
        val adapter = MovieAdapter(list)
        binding.recyclerView.adapter = adapter

    }

//    private fun getDatas(){
//        var imgurl = ""
//        var title = ""
//        var year = 0
//        var star = ""
//
//        val endpoint = URL("https://openapi.naver.com/v1/search/movie.json")
//        val myConnection = endpoint.openConnection() as HttpsURLConnection
//        myConnection.setRequestProperty("H8nXnpefveSI2x5pOyjd", "hNslSZqruK")
//        if (myConnection.responseCode == 200) {
//            // Success
//            // Further Processing here
//            val responseBody = myConnection.inputStream
//            val responseBodyReader = InputStreamReader(responseBody, "UTF-8")
//            val jsonReader = JsonReader(responseBodyReader)
//            jsonReader.beginObject() // Start processing the JSON object
//
//            while (jsonReader.hasNext()) { // Loop through all keys
//                val key: String = jsonReader.nextName() // Fetch the next key
//                if (key == "organization_url") { // Check if desired key
//                    // Fetch the value as a String
//                    val value: String = jsonReader.nextString()
//
//                    // Do something with the value
//                    // ...
//                    break // Break out of the loop
//                } else {
//                    jsonReader.skipValue() // Skip values of other keys
//                }
//            }
//        } else {
//            // error handling code goes here
//        }
//
//
//        var item = Movies(imgurl, title, year, star)
//
//        return
//    }
}