package com.example.napat.glide

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_movie_laout.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var Movie_List = ArrayList<Movie>()
    private lateinit var movieAdapter : MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieAdapter = MovieAdapter(this,Movie_List)

        test.adapter = movieAdapter
        var retrofit : Retrofit = Retrofit.Builder()
                .baseUrl("https://workshopup.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val apimovie = retrofit.create(API ::class.java)

        apimovie.getMovie()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    movieAdapter.setMovies(it.results)},
                        {Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()
                        })


    }
    inner class MovieAdapter : BaseAdapter {
        var Movielist = ArrayList<Movie>()
        var context: Context? = null

        fun setMovies(data: List<Movie>) {
            Movielist.addAll(data)
            notifyDataSetChanged()
        }

        constructor(context: Context, foodsList: ArrayList<Movie>) : super() {
            this.context = context
            this.Movielist = foodsList
        }

        override fun getCount(): Int {
            return Movielist.size
        }

        override fun getItem(position: Int): Any {
            return Movielist[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val movie = this.Movielist[position]

            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var MovieView = inflator.inflate(R.layout.item_movie_laout, null)
            Glide.with(this@MainActivity).load(movie.image_url).into(MovieView.imgMovie)
            MovieView.tvTitle.text = movie.title!!

            return MovieView
        }
    }


}
