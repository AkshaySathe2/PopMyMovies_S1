package com.udacity.akkisathe2.popmymovies_s1.controller;

import com.udacity.akkisathe2.popmymovies_s1.loader.Dataloader;
import com.udacity.akkisathe2.popmymovies_s1.utility.UrlBuilder;

/**
 * Created by 836158 on 01-04-2016.
 */
public class MovieController {

    public String fetchMovieList(String sortBy, String page)
    {
        String url= UrlBuilder.buildMovieListUrl(sortBy, page);
        Dataloader dataloader=new Dataloader();
        return dataloader.fetchData(url);
    }

    public String fetchMovie(String movieId)
    {
        String url= UrlBuilder.buildMovieDetailUrl(movieId);
        Dataloader dataloader=new Dataloader();
        return dataloader.fetchData(url);
    }

}
