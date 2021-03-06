package com.udacity.akkisathe2.popmymovies_s1.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.udacity.akkisathe2.popmymovies_s1.R;
import com.udacity.akkisathe2.popmymovies_s1.controller.MovieController;
import com.udacity.akkisathe2.popmymovies_s1.model.Movie;
import com.udacity.akkisathe2.popmymovies_s1.utility.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailFragment extends Fragment {
    static Movie movie;
    static String movieId;
    Context mContext;
    View view;
    TextView title;
    TextView overview;
    ImageView poster;
    TextView movieYear;
    TextView voteAverage;
    ImageView adultIcon;
    private TextView tagline;
    LinearLayout genre;
    GridView gridGenre;

    public static MovieDetailFragment newInstance(Movie m) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        movie = m;
        return fragment;
    }

    public static MovieDetailFragment newInstance(String id) {
        MovieDetailFragment fragment=new MovieDetailFragment();
        movieId=id;
        return fragment;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(false);
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        title = (TextView) view.findViewById(R.id.txt_movie_title);
        tagline=(TextView)view.findViewById(R.id.txt_movie_tagline);
        overview = (TextView) view.findViewById(R.id.txt_movie_overview);
        LinearLayout moviePosterDetails = (LinearLayout) view.findViewById(R.id.ll_movie_poster_details);
        poster = (ImageView) moviePosterDetails.findViewById(R.id.img_movie_poster);
        movieYear = (TextView) moviePosterDetails.findViewById(R.id.txt_movie_year);
        voteAverage = (TextView) moviePosterDetails.findViewById(R.id.txt_movie_rating);
        adultIcon=(ImageView)moviePosterDetails.findViewById(R.id.img_adult);
        genre=(LinearLayout) view.findViewById(R.id.ll_genre_tags);

        /*gridGenre=(GridView)view.findViewById(R.id.grid_genre_tags);*/

        FetchMovieData data=new FetchMovieData();
        data.execute(movieId);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //setHasOptionsMenu(false);
    }


    public class FetchMovieData extends AsyncTask<String,Void,String[]>
    {

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(getContext());
            dialog.setMessage("Loading data Please wait...");
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            try {
                MovieController controller=new MovieController();
                String jsonString=controller.fetchMovie(params[0]);
                if(!(jsonString != null && jsonString.trim().equals(""))) {
                    movie = new Gson().fromJson(jsonString, Movie.class);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            dialog.dismiss();
            updateUI();
            super.onPostExecute(strings);
        }
    }

    public void updateUI()
    {
        title.setText(movie.getTitle());
        tagline.setText(movie.getTagline());
        overview.setText(movie.getOverview());
        Picasso.with(mContext).load(UrlBuilder.buildPosterUrl("w185") + movie.getPosterPath()).into(poster);
        movieYear.setText(movie.getReleaseDate());
        voteAverage.setText(getString(R.string.total_rating, movie.getVoteAverage()));
        if(Boolean.valueOf(movie.getAdult()))
        {
            adultIcon.setVisibility(View.VISIBLE);
        }
        /*String[] genres=movie.getGenreNames();*/
        TextView txtGenre = new TextView(mContext);
        txtGenre.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        txtGenre.setText("Genres : ");
        genre.addView(txtGenre);
        Movie.Genres[] genres=movie.getGenres();
        for (Movie.Genres genre1 : genres) {
            TextView textView1 = new TextView(mContext);
            textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textView1.setText(genre1.getName());
            textView1.setBackgroundResource(R.drawable.genre_tag);
            genre.addView(textView1);

        }

        //New Approach
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                R.layout.item_genre, genres);
        gridGenre.setAdapter(adapter);*/
        genre.setVisibility(View.VISIBLE);

    }



}
