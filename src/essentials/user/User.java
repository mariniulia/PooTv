package essentials.user;
import essentials.Movie;
import essentials.observerPattern.Observer;
import essentials.observerPattern.Subject;
import essentials.genres.utils.Recommendation;
import fileio.UserInput;

import java.util.ArrayList;

public final class User extends Observer {
    private Credentials credentials;
    private Integer tokensCount = 0;
    private Integer numFreePremiumMovies = 15;
    private ArrayList<Movie> purchasedMovies = new ArrayList<>();
    private ArrayList<Movie> watchedMovies = new ArrayList<>();
    private ArrayList<Movie> likedMovies = new ArrayList<>();
    private ArrayList<Movie> ratedMovies = new ArrayList<>();
    private ArrayList<Notification> notifications = new ArrayList<>();

    private final Recommendation recommendations = new Recommendation();

    public User() {
    }

    public User(final Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * user subscribes to a genre
     */
    public void userFollows(final Subject subject) {
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update(final Notification notification) {
        //daca se sterge un film din database, compensam utilizatrul
        if (notification.getMessage().compareTo("delete") == 0) {

            //daca cumparase filmul
            if (this.getPurchasedMovies().stream()
                    .anyMatch(e -> e.getName().compareTo(notification.getMovieName()) == 0)) {

                //daca e utilizator premium
                if (this.getCredentials().getAccountType().compareTo("premium") == 0) {
                    numFreePremiumMovies++;
                }

                //daca e utilizator normal
                if (this.getCredentials().getAccountType().compareTo("standard") == 0) {
                    tokensCount += 2;
                }

                //si scoatem filmul din toate listele acestuia
                deleteFromListMovieWithName(purchasedMovies, notification.getMovieName());
                deleteFromListMovieWithName(likedMovies, notification.getMovieName());
                deleteFromListMovieWithName(ratedMovies, notification.getMovieName());

                //il notificam doar daca a cumparat
                notifications.add(notification);
                return;
            }
        }

        //daca se adauga, il notificam oricum
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getMovieName().compareTo(notification.getMovieName()) == 0
                    && notifications.get(i).getMessage()
                    .compareTo(notification.getMessage()) == 0) {
                return;
            }
        }
        notifications.add(notification);
    }

    private void deleteFromListMovieWithName(final ArrayList<Movie> movieList,
                                             final String movieName) {
        for (int i = 0; i < movieList.size(); i++) {
            if (movieList.get(i).getName().compareTo(movieName) == 0) {
                movieList.remove(i);
                return;
            }
        }
    }

    public User(final UserInput user) {
        this.credentials = new Credentials(user.getCredentials());
        this.credentials.setName(user.getCredentials().getName());
        this.credentials.setPassword(user.getCredentials().getPassword());
        this.credentials.setAccountType(user.getCredentials().getAccountType());
        this.credentials.setCountry(user.getCredentials().getCountry());
        this.credentials.setBalance(user.getCredentials().getBalance());
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }

    public Integer getTokensCount() {
        return tokensCount;
    }

    public void setTokensCount(final Integer tokensCount) {
        this.tokensCount = tokensCount;
    }

    public Integer getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public void setNumFreePremiumMovies(final Integer numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public ArrayList<Movie> getPurchasedMovies() {
        return purchasedMovies;
    }

    public void setPurchasedMovies(final ArrayList<Movie> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public ArrayList<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(final ArrayList<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public ArrayList<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(final ArrayList<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setRatedMovies(final ArrayList<Movie> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    public Recommendation getRecommendations() {
        return recommendations;
    }
}
