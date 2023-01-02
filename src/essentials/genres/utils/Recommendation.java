package essentials.genres.utils;
import essentials.CurrentPlatform;
import essentials.Movie;
import essentials.user.User;

import java.util.ArrayList;

public final class Recommendation {
    private ArrayList<Movie> recommendations = new ArrayList();
    private GenreDatabase database = new GenreDatabase();

    public Recommendation() {
    }
    /**
     * puts in recommendations movies
     */
    public void getMoviesRecommended() {
        ArrayList<Movie> movies = CurrentPlatform.getInstance().getCurrentMoviesList();
        User user = CurrentPlatform.getInstance().getCurrentUser();
        //pregatim topul genurilor
        database.getTop();

        //punem in currentMovies toate filmele disponibile
        CurrentPlatform.getInstance().getAvailableMovies();
        for (int i = 0; i < movies.size(); i++) {
            i = removeIfSeen(movies, user, i);
        }

        //le sortam dupa numarul de likeuri
        movies.sort((o1, o2) -> o2.getNumLikes().compareTo(o1.getNumLikes()));

        //le punem in lista de recomndari in ordinea topului genurilor
        //pentru fiecare gen top, punem filmele in ordinea likeurilor
        for (int i = 0; i < database.getTop().size(); i++) {
            setMoviesInOrder(movies, i);
        }
    }

    private void setMoviesInOrder(final ArrayList<Movie> movies, final int i) {
        for (Movie movie : movies) {
            if (movie.getGenres().contains(database.getTop().get(i).getGenreName())) {
                recommendations.add(movie);
            }
        }
    }

    private static int removeIfSeen(final ArrayList<Movie> movies, final User user, final int i) {
        //le scoatem pe cele pe care le a vazut
        int k = i;
        if (user.getWatchedMovies().contains(movies.get(k))) {
            movies.remove(k);
            k--;
        }
        return k;
    }

    public ArrayList<Movie> getRecommendations() {
        return recommendations;
    }

    public GenreDatabase getDatabase() {
        return database;
    }
}
