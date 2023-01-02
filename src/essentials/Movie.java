package essentials;

import fileio.MovieInput;

import java.util.ArrayList;

public final class Movie {
    private String name;
    private String year;
    private Integer duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;

    private Integer numLikes = 0;
    private double rating = 0;
    private Integer numRatings = 0;

    public Movie(final MovieInput movieInput) {
        this.actors = movieInput.getActors();
        this.name = movieInput.getName();
        this.year = movieInput.getYear().toString();
        this.duration = movieInput.getDuration();
        this.genres = movieInput.getGenres();
        this.countriesBanned = movieInput.getCountriesBanned();
    }

    public Integer getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(final Integer numLikes) {
        this.numLikes = numLikes;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public Integer getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(final Integer numRatings) {
        this.numRatings = numRatings;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getCountriesBanned() {
        return countriesBanned;
    }

    public void setCountriesBanned(final ArrayList<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }
}
