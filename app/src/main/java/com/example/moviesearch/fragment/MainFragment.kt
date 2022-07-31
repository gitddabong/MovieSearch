package com.example.moviesearch.fragment

import android.content.ContentValues
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.moviesearch.*
import com.example.moviesearch.classes.ApiClient
import com.example.moviesearch.classes.DBHelper
import com.example.moviesearch.classes.MovieAdapter
import com.example.moviesearch.classes.Movies
import com.example.moviesearch.databinding.FragmentMainBinding
import com.example.moviesearch.interfaces.ApiInterface
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainFragment : Fragment() {

    lateinit var navController: NavController
    private lateinit var binding: FragmentMainBinding
    private var searchList: ArrayList<Movies> = ArrayList()

    lateinit var dbHelper: DBHelper
    lateinit var database: SQLiteDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        // database (SQlite)
        dbHelper = DBHelper(activity?.applicationContext, "moviesearch.db", null, 1)
        database = dbHelper.writableDatabase

        val gainWord: String? = arguments?.getString("searchWord")
        if (gainWord != null) {
            getResultSearch(gainWord)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener {
            searchList.clear()

            // 1. API에서 얻어온 데이터들로 영화 목록을 객체화하여 저장
            val word = binding.searchEdittext.text.toString()
            Log.d(TAG, "순서 1")
            getResultSearch(word)
            insertRecentWord(word)
        }

        binding.recentWords.setOnClickListener{
            navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_mainFragment_to_recentWordFragment)
        }
    }

    private fun getResultSearch(word: String) {
        val apiInterface: ApiInterface = ApiClient.instance!!.create(ApiInterface::class.java)
        val call: Call<String> = apiInterface.getSearchResult(clientId, clientSecret, "movie.json", word, 100)
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
//                    Log.e(TAG, "성공 : $result")
                    if (result != null) {
                        jsonParsing(result)
                    }
                } else {
                    Log.e(TAG, "실패 : " + response.body())
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e(TAG, "에러 : " + t.message)
            }
        })
    }

    // 2. 반복문으로 list에 영화 추가
    private fun jsonParsing(json: String) {
        try {
            Log.d(TAG, "순서 2")
            val jsonObject = JSONObject(json)
            val total = jsonObject.getString("total")
            Log.d(TAG, "total : $total")
            val movieArray = jsonObject.getJSONArray("items")
            for (i in 0 until movieArray.length()) {
                val movieObject = movieArray.getJSONObject(i)
                val title = movieObject.getString("title")
                val image = movieObject.getString("image")
                val pubDate = movieObject.getString("pubDate")
                val userRating = movieObject.getString("userRating")
                val link = movieObject.getString("link")

                val movie = Movies(image, title, pubDate, userRating, link)
//                Log.d(TAG, "$movie")
                searchList.add(movie)
//                Log.d(TAG, "title : $title ${title::class.simpleName}")
//                Log.d(TAG, "image : $image ${image::class.simpleName}")
//                Log.d(TAG, "pubDate : $pubDate ${pubDate::class.simpleName}")
//                Log.d(TAG, "userRating : $userRating ${userRating::class.simpleName}")
//                Log.d(TAG, "link : $link ${link::class.simpleName}")
            }
            setRecyclerviewAdapter()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun hideSoftKeybord() {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    private fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.attach(fragment).commit()
    }

    private fun setRecyclerviewAdapter() {
        // 3. 리사이클러뷰에 어댑터 적용
        Log.d(TAG, "searchList 크기 : ${searchList.size}")
        Log.d(TAG, "순서 3")

        val adapter = MovieAdapter(searchList)
        binding.recyclerView.adapter = adapter
        hideSoftKeybord()
        refreshFragment(this, activity!!.supportFragmentManager)
    }

    private fun insertRecentWord(word: String) {
        val values = ContentValues()
        values.put("word", word)
        values.put("date", getCurrentTime())
        database.replace(tableName, null, values)
    }

    private fun getCurrentTime(): String {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyyMMdd HH:mm:ss")
        return sdf.format(date)
    }
}