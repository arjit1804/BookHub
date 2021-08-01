package com.example.bookhub

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.model.Book
import com.example.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var recyclerDashboard : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{book1, book2 ->
        if (book1.bookRating.compareTo(book2.bookRating, true)== 0){
            //sort according to name if rating is same
            book1.bookName.compareTo(book2.bookName, true)
        }else{
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }



    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)


        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE






        layoutManager = LinearLayoutManager(activity)



        val queue = Volley.newRequestQueue(activity as Context)

        val  url = "http://13.235.250.119/v1/book/fetch_books/"


//first checking the connection is it established or not if it is then the if part will executed otherwise else to open up the connection
        if (ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object : JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener {
                        //here we will handle the response
                        try {

                            progressLayout.visibility = View.GONE
                            val success = it.getBoolean("success")

                            if (success) {
                                val data = it.getJSONArray("data")
                                for (i in 0 until data.length()) {
                                    val bookJsonObject = data.getJSONObject(i)
                                    val bookObject = Book(
                                            bookJsonObject.getString("book_id"),
                                            bookJsonObject.getString("name"),
                                            bookJsonObject.getString("author"),
                                            bookJsonObject.getString("rating"),
                                            bookJsonObject.getString("price"),
                                            bookJsonObject.getString("image"),
                                    )
                                    bookInfoList.add(bookObject)

                                    recyclerAdapter =
                                            DashboardRecyclerAdapter(activity as Context, bookInfoList)

                                    recyclerDashboard.adapter = recyclerAdapter
                                    recyclerDashboard.layoutManager = layoutManager



                                }
                            } else {
                                Toast.makeText(activity, "some error occurred !!!", Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: JSONException) {
                            Toast.makeText(activity, "Some unexpected error occurred !!!", Toast.LENGTH_SHORT).show()
                        }

                    },
                    Response.ErrorListener {
                        //here we will handle the errors
                        if (activity!=null){
                            //checking
                            Toast.makeText(activity, "volley error occurred !!!", Toast.LENGTH_SHORT).show()

                        }
                    }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String ,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "a503a953d714c2"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        }else{
            //Internet is not available
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Open Settings"){text , listener ->
                //opening settings to make the connection available
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Exit"){text,listener ->
                //exiting the app
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }


        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard, menu)
        //in fragment file we have to specify that the menu items have to display and it is done within onCreateView method
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if (id == R.id.action_sort){
            Collections.sort(bookInfoList , ratingComparator)
            bookInfoList.reverse()
        }

        //we always have to notify the adapter when we are making the changes
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DashboardFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}