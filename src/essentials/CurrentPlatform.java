package essentials;
import essentials.user.Credentials;
import essentials.user.User;
import fileio.Filter;
import pages.Hierarchy;
import java.util.ArrayList;
import java.util.Comparator;

//SINGLETON current state
public final class CurrentPlatform {
    private String error = null;
    private ArrayList<Movie> currentMoviesList = new ArrayList<>();
    private User currentUser;
    //baza de date
    private ArrayList<Movie> platformMovies;
    private ArrayList<User> platformUsers;

    public boolean resetMovies = false;
    private final Hierarchy hierarchy = new Hierarchy();

    private static final CurrentPlatform currentPlatform = new CurrentPlatform();

    private CurrentPlatform() {
    }

    public static CurrentPlatform getInstance() {
        return currentPlatform;
    }


    /**
     * cutatam platforma
     */
    public void reset() {
        this.currentUser = null;
        this.currentMoviesList = new ArrayList<>();
        this.error = null;
        this.platformMovies = new ArrayList<>();
        this.platformUsers = new ArrayList<>();
        getHierarchy().currentPage = getHierarchy().pages.root;
        //ierarhia nu trebuie curatata deoarece e mereu aceeasi
    }

    /**
     * pune in current movies, filemele disponibile pentru user
     */
    public void getAvailableMovies() {
        currentMoviesList.clear();
        for (Movie platformMovie : platformMovies) {
            //daca tara userului nu e bannata
            if (!platformMovie.getCountriesBanned().contains(currentUser.getCredentials().
                    getCountry())) {
                currentMoviesList.add(platformMovie);
            }
        }
    }

    /**
     * pune in current movies, filmul dorit
     */
    public int getMovie(final String movie) {
        for (int i = 0; i < currentMoviesList.size(); i++) {
            if (currentMoviesList.get(i).getName().compareTo(movie) != 0) {
                currentMoviesList.remove(currentMoviesList.get(i));
                i = i - 1;
            }
        }
        if (currentMoviesList.size() == 0) {
            error = "Error";
            return 1;
        }
        return 0;
    }

    /**
     * login
     */
    public int login(final Credentials credentials) {
        for (User platformUser : platformUsers) {
            if (platformUser.getCredentials().getName().compareTo(credentials.getName()) == 0) {
                if (platformUser.getCredentials().getPassword().
                        compareTo(credentials.getPassword()) == 0) {
                    error = null;
                    currentUser = platformUser;
                    return 0;
                } else {
                    error = "Error";
                    return 1;
                }
            }
        }

        return -1;
    }

    /**
     * register
     */
    public void register(final Credentials credentials) {
        currentUser = new User(credentials);
        platformUsers.add(new User(credentials));
    }

    /**
     * search
     */
    public void search(final String startsWith) {
        for (int i = 0; i < currentMoviesList.size(); i++) {
            if (!currentMoviesList.get(i).getName().startsWith(startsWith)) {
                currentMoviesList.remove(i);
                i = i - 1;
            }
        }
    }

    /**
     * filter
     */
    public void filter(final Filter filter) {
        //pastram doar filmele care contin ce trebuie
        if (filter.getContains() != null) {
            //verificam toti actorii si scoatem filmele care nu ii contin
            removeIfActorNotFound(filter);
            removeIfGenreNotFound(filter);
        }
        //apoi sortam
        if (filter.getSort() != null) {
            sortDuration(filter);
            sortRating(filter);
        }
    }

    private void sortRating(final Filter filter) {
        if (filter.getSort().getDuration() == null && filter.getSort().getRating().
                compareTo("increasing") == 0) {
            currentMoviesList.sort(Comparator.comparingDouble(Movie::getRating));
        }

        if (filter.getSort().getDuration() == null && filter.getSort().getRating().
                compareTo("decreasing") == 0) {
            sortByRating(filter);
        }
    }

    private void sortDuration(final Filter filter) {
        if (filter.getSort().getDuration() != null && filter.getSort().getDuration().
                compareTo("increasing") == 0) {
            sortByDuration(filter);
        }

        if (filter.getSort().getDuration() != null && filter.getSort().getDuration().
                compareTo("decreasing") == 0) {
            sortByDuration(filter);
        }
    }

    private void removeIfGenreNotFound(final Filter filter) {
        if (filter.getContains().getGenre() != null) {
            //verificam toate genurile si scoatem filmele care nu le contin
            for (int i = 0; i < filter.getContains().getGenre().size(); i++) {
                for (int j = 0; j < currentMoviesList.size(); j++) {
                    if (!currentMoviesList.get(j).getGenres().contains(
                            filter.getContains().getGenre().get(i))) {
                        currentMoviesList.remove(j);
                        j = j - 1;
                    }
                }
            }
        }
    }

    private void removeIfActorNotFound(final Filter filter) {
        if (filter.getContains().getActors() != null) {
            for (int i = 0; i < filter.getContains().getActors().size(); i++) {
                for (int j = 0; j < currentMoviesList.size(); j++) {
                    if (!currentMoviesList.get(j).getActors().contains(
                            filter.getContains().getActors().get(i))) {
                        currentMoviesList.remove(j);
                        j = j - 1;
                    }
                }
            }

        }
    }

    /**
     * sortarea filmelor din current list dupa durata si specificator
     */
    private void sortByDuration(final Filter filter) {
        currentMoviesList.sort((o1, o2) -> {
            if (o1.getDuration() > o2.getDuration()) {
                return -1;
            }
            if (o1.getDuration() < o2.getDuration()) {
                return 1;
            }
            return sort(filter, o1, o2);
        });
    }

    private void sortByRating(final Filter filter) {
        currentMoviesList.sort((o1, o2) -> sort(filter, o1, o2));
    }

    private int sort(final Filter filter, final Movie o1, final Movie o2) {
        if (filter.getSort().getRating().compareTo("decreasing") == 0) {
            return Double.compare(o2.getRating(), o1.getRating());
        }
        if (filter.getSort().getRating().compareTo("increasing") == 0) {
            return Double.compare(o1.getRating(), o2.getRating());
        }
        return 0;
    }

    /**
     * buy tokens
     */
    public void buyTokens(final Integer count) {
        currentUser.setTokensCount(currentUser.getTokensCount() + count);
        currentUser.getCredentials().setBalance(currentUser.getCredentials().getBalance() - count);
    }

    /**
     * buy premium account
     */
    public void buyPremiumAccount() {
        currentUser.getCredentials().setAccountType("premium");
        currentUser.setTokensCount(currentUser.getTokensCount() - 10);
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    /**
     * cumpara un film
     */
    public int purchase() {
        if (currentUser.getCredentials().getAccountType().compareTo("premium") == 0) {
            if (currentUser.getNumFreePremiumMovies() > 0) {
                return userUsesFreePremiumMovies();
            } else {
                return canUserAffordMovie();
            }
        }
        if (currentUser.getCredentials().getAccountType().compareTo("premium") != 0) {
            return canUserAffordMovie();
        }
        error = "Error";
        return 1;
    }

    /**
     * foloseste un film din cele gratuite
     */
    private int userUsesFreePremiumMovies() {
        if (currentMoviesList.size() == 1) {
            currentUser.getPurchasedMovies().add(currentMoviesList.get(0));
            currentUser.setNumFreePremiumMovies(currentUser.getNumFreePremiumMovies() - 1);
            return 0;
        } else {
            error = "Error";
            return 1;
        }
    }

    /**
     * verifica daca un user poate vizualiza un film(material vorbind)
     */
    private int canUserAffordMovie() {
        if (currentMoviesList.size() == 1 && currentUser.getTokensCount() >= 2) {

            currentUser.getPurchasedMovies().add(currentMoviesList.get(0));
            currentUser.setTokensCount(currentUser.getTokensCount() - 2);
            return 0;
        } else {
            error = "Error";
            return 1;
        }
    }

    /**
     * watch
     */
    public int watch() {
        //testez intai sa fie cumparat filmul
        if (currentMoviesList.size() != 1) {
            error = "Error";
            return 1;
        }
        if (currentUser.getPurchasedMovies().contains(currentMoviesList.get(0))) {
            //daca am vazut filmul nu il mai adaug
            if (currentUser.getWatchedMovies().contains(currentMoviesList.get(0))) {
                return 2;
            }
            currentUser.getWatchedMovies().add(currentMoviesList.get(0));
            return 0;

        } else {
            error = "Error";
            return 1;
        }
    }

    /**
     * like
     */
    public int like() {
        //testez intai sa fie vazut filmul
        if (currentMoviesList.size() != 1) {
            error = "Error";
            return 1;
        }
        if (currentUser.getWatchedMovies().contains(currentMoviesList.get(0))) {
            currentUser.getLikedMovies().add(currentMoviesList.get(0));
            currentMoviesList.get(0).setNumLikes(currentMoviesList.get(0).getNumLikes() + 1);

            //actualizam topul userului
            //pentru fiecare gen, adaugam un like genului
            for (int i = 0; i < currentMoviesList.get(0).getGenres().size(); i++) {
                currentUser.getRecommendations().getDatabase().addLike(currentMoviesList
                        .get(0).getGenres().get(i));
            }
            return 0;

        } else {
            error = "Error";
            return 1;
        }
    }

    /**
     * rate
     */
    public double rate(final double rate) {
        //testez intai sa fie vazut filmul
        if (currentMoviesList.size() != 1 || rate > 5) {
            error = "Error";
            return 1;
        }
        if (currentUser.getWatchedMovies().contains(currentMoviesList.get(0))) {
            if (currentUser.getRatedMovies().contains(currentMoviesList.get(0))) {
                //calculez noul rating
                currentMoviesList.get(0).setRating((currentMoviesList.get(0).getNumRatings()
                        * currentMoviesList.get(0).getRating() + rate)
                        / (currentMoviesList.get(0).getNumRatings() + 1));

                return 0;
            } else {
                //il adaug in lista rated
                currentUser.getRatedMovies().add(currentMoviesList.get(0));

                //calculez noul rating
                currentMoviesList.get(0).setRating((currentMoviesList.get(0).getNumRatings()
                        * currentMoviesList.get(0).getRating() + rate)
                        / (currentMoviesList.get(0).getNumRatings() + 1));

                //actualizez numarul de ratinguri
                currentMoviesList.get(0).setNumRatings(currentMoviesList.get(0)
                        .getNumRatings() + 1);
                return 0;
            }
        } else {
            error = "Error";
            return 1;
        }
    }

    public void setPlatformMovies(final ArrayList<Movie> platformMovies) {
        this.platformMovies = platformMovies;
    }

    public void setPlatformUsers(final ArrayList<User> platformUsers) {
        this.platformUsers = platformUsers;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public ArrayList<Movie> getCurrentMoviesList() {
        return currentMoviesList;
    }

    public void setCurrentMoviesList(final ArrayList<Movie> currentMoviesList) {
        this.currentMoviesList = currentMoviesList;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<Movie> getPlatformMovies() {
        return platformMovies;
    }

    public ArrayList<User> getPlatformUsers() {
        return platformUsers;
    }

    public void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
    }
}