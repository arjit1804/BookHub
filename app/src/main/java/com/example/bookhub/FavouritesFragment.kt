package com.example.bookhub

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavouritesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerFavourite : RecyclerView
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter

    var dbBookList = listOf<BookEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        //fragment can have the activity as context not as ApplicationContext
        layoutManager = GridLayoutManager(activity as Context,2)

        dbBookList = RetrieveFavourites(activity as Context).execute().get()

        if(activity!=null){
            progressLayout.visibility = View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, dbBookList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }



        return view
    }

    class RetrieveFavourites(val context: Context): AsyncTask<Void,Void,List<BookEntity>>(){

        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()
            return db.bookDao().getAllBooks()

        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}