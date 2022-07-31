package com.example.moviesearch.fragment

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.moviesearch.MainActivity
import com.example.moviesearch.R
import com.example.moviesearch.TAG
import com.example.moviesearch.classes.DBHelper
import com.example.moviesearch.databinding.FragmentRecentWordBinding
import com.example.moviesearch.tableName

class RecentWordFragment : Fragment() {
    private lateinit var binding: FragmentRecentWordBinding
    lateinit var navController: NavController

    lateinit var dbHelper: DBHelper
    lateinit var database: SQLiteDatabase

    lateinit var thisView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentWordBinding.inflate(inflater, container, false)

        dbHelper = DBHelper(activity?.applicationContext, "moviesearch.db", null, 1)
        database = dbHelper.writableDatabase

        val query = "SELECT * FROM $tableName order by date desc limit 10;"
        val cursor = database.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val word: String = cursor.getString(cursor.getColumnIndex("word"))
            val date: String = cursor.getString(cursor.getColumnIndex("date"))
            Log.d(TAG, date)
            createDynamicButton(word)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisView = view
    }

    private fun createDynamicButton(word: String) {
        val dynamicButton = Button(activity!!.applicationContext).apply {
            text = word
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                val bundle = Bundle()
                bundle.putString("searchWord", word)
                navController = Navigation.findNavController(thisView)
                navController.navigate(R.id.action_recentWordFragment_to_mainFragment, bundle)
            }
        }
        binding.linearLayout.addView(dynamicButton)
    }
}