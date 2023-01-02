
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import essentials.*;
import essentials.user.Credentials;
import essentials.user.Notification;
import essentials.user.User;
import fileio.Input;
import java.util.ArrayList;

public final class Starter {
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Movie> movies = new ArrayList<>();
    private final ArrayList<Actions> actions = new ArrayList<>();

    public Starter(final Input input) {
        importFromInput(input);
    }

    /**
     * starting actions
     */
    public void startBaby(final ObjectMapper objectMapper, final ArrayNode output) {
        for (Actions action : actions) {
            ObjectNode node = objectMapper.createObjectNode();

            //salvam filmul pe care il avem in current
            String currentMovie = saveMovie();

            //pornim actiunea
            action.start();

            //daca actiunea necesita output, il adaugam in nod
            if (action.getHasOutput()) {
                addOutputToNode(objectMapper, output, node);
            }

            //pregatim platforma pentru urmatoarea actiune
            prepareForNextAction(action, currentMovie);
        }
        //cand se termina actiunile punem recomandari
        giveRecommendation(objectMapper, output);

        //cand se termina actiunile curatam CurrentPlatform
        CurrentPlatform.getInstance().reset();
    }

    private static String saveMovie() {
        String currentMovie = "string";
        if (CurrentPlatform.getInstance().getCurrentMoviesList().size() != 0) {
            currentMovie = CurrentPlatform.getInstance().getCurrentMoviesList().get(0).
                    getName();
        }
        return currentMovie;
    }

    /**
     * functie calculeaza recomandarile pt user si le pune in output
     */
    private void giveRecommendation(final ObjectMapper objectMapper, final ArrayNode output) {
        User user = CurrentPlatform.getInstance().getCurrentUser();

        //daca exista user curent si e premium
        if (user != null && user.getCredentials().getAccountType().compareTo("premium") == 0) {
            ObjectNode node = objectMapper.createObjectNode();

            //adaugam filmele in vector
            user.getRecommendations().getMoviesRecommended();

            //trimitem notificare
            Notification notification = createNotification();

            //adaugam notificarea
            user.update(notification);

            //golim campurile error si currentMovie
            node.put("error", (JsonNode) null);
            node.put("currentMoviesList", (JsonNode) null);


            //add current user
            addUser(objectMapper, node);

            //add it all to output
            output.add(node);
        }
    }

    private static Notification createNotification() {
        Notification notification = new Notification();
        notification.setMessage("Recommendation");
        ArrayList<Movie> recomandations = CurrentPlatform.getInstance().getCurrentUser()
                .getRecommendations().getRecommendations();

        //daca nu avem recomandari trecem norecomandation
        if (recomandations.size() < 1) {
            notification.setMovieName("No recommendation");
        } else {
            notification.setMovieName(recomandations.get(0).getName());
        }
        return notification;
    }

    private void addUser(final ObjectMapper objectMapper, final ObjectNode node) {
        if (CurrentPlatform.getInstance().getCurrentUser() != null
                && CurrentPlatform.getInstance().getError() == null) {
            ObjectNode currentUser = objectMapper.createObjectNode();
            currentUser = userToNode(currentUser, CurrentPlatform.getInstance().getCurrentUser(),
                    objectMapper);
            node.put("currentUser", currentUser);
        } else {
            node.put("currentUser", (JsonNode) null);
        }
    }

    /**
     * functie care aduce inputul in clasele User, Movies, Action
     */
    private void importFromInput(final Input input) {
        //importing existing users
        for (int i = 0; i < input.getUsers().size(); i++) {
            users.add(new User(input.getUsers().get(i)));
        }
        CurrentPlatform.getInstance().setPlatformUsers(users);

        //importing existing movies
        for (int i = 0; i < input.getMovies().size(); i++) {
            movies.add(new Movie(input.getMovies().get(i)));
        }
        CurrentPlatform.getInstance().setPlatformMovies(movies);

        //importing existing actions
        for (int i = 0; i < input.getActions().size(); i++) {
            actions.add(new Actions.Builder(input.getActions().get(i))
                    .subscribedGenre(input.getActions().get(i))
                    .page(input.getActions().get(i))
                    .feature(input.getActions().get(i))
                    .rate(input.getActions().get(i))
                    .filter(input.getActions().get(i))
                    .credentials(input.getActions().get(i))
                    .count(input.getActions().get(i))
                    .feature(input.getActions().get(i))
                    .addedMovie(input.getActions().get(i))
                    .deletedMovie(input.getActions().get(i))
                    .movie(input.getActions().get(i))
                    .startsWith(input.getActions().get(i))
                    .subscribedGenre(input.getActions().get(i)).build());
        }
    }

    /**
     * functie care pregateste platforma pentru urmatoarea actiune
     */
    private static void prepareForNextAction(final Actions action, final String currentMovie) {
        //daca schimbam pagina si nu suntem pe movies sau see details, nu afisam current movies
        changePageNoOutput(action);

        //daca avem eroare la o actiune on page, si ne intoarcem in see details, pastram filmul
        errorInSeeDetails(action, currentMovie);

        //schimbam pagina si avem eroare -> e necesar sa ne intoarcem, daca ne intoacem pe movies
        // afiseaza current movies
        backToMovies(action);

        //resetez eroarea pe null pentru urmatoarea actiune
        CurrentPlatform.getInstance().setError(null);
    }

    private static void changePageNoOutput(final Actions action) {
        if (CurrentPlatform.getInstance().getCurrentMoviesList() != null
                && action.getType().compareTo("change page") == 0
                && action.getPage().compareTo("see details") != 0
                && action.getPage().compareTo("movies") != 0) {
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
        }
    }

    private static void backToMovies(final Actions action) {
        if (action.getType().compareTo("change page") == 0
                && CurrentPlatform.getInstance().getError() != null && action.getPage() != null
                && CurrentPlatform.getInstance().getHierarchy().currentPage.data.getName().
                compareTo("movies") == 0) {
            CurrentPlatform.getInstance().getAvailableMovies();
        }
    }

    private static void errorInSeeDetails(final Actions action, final String currentMovie) {
        if (action.getType().compareTo("on page") == 0
                && CurrentPlatform.getInstance().getError() != null
                && CurrentPlatform.getInstance().getHierarchy().currentPage.data.getName()
                .compareTo("see details") == 0) {
            CurrentPlatform.getInstance().getAvailableMovies();
            CurrentPlatform.getInstance().getMovie(currentMovie);
        }
    }

    /**
     * functie care pune outputul corespunzator
     */
    private void addOutputToNode(final ObjectMapper objectMapper, final ArrayNode output,
                                 final ObjectNode node) {
        //add error
        node.put("error", CurrentPlatform.getInstance().getError());

        //add current movies
        ArrayNode currentMovieList = objectMapper.createArrayNode();
        if (CurrentPlatform.getInstance().getCurrentMoviesList() != null) {
            for (int i = 0; i < CurrentPlatform.getInstance().getCurrentMoviesList().size(); i++) {
                ObjectNode movie = objectMapper.createObjectNode();
                movie = movieToNode(movie, CurrentPlatform.getInstance().getCurrentMoviesList()
                        .get(i));
                currentMovieList.add(movie);
            }
        }

        //add current movies
        if (CurrentPlatform.getInstance().getError() != null) {
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
            node.put("currentMoviesList", currentMovieList);
        } else {
            node.put("currentMoviesList", currentMovieList);
        }


        //add current user
        addUser(objectMapper, node);

        //add it all to output
        output.add(node);
    }

    /**
     * functie care transforma un user in formatul corespunzator de output
     */
    public ObjectNode userToNode(final ObjectNode node, final User user,
                                 final ObjectMapper objectMapper) {

        ObjectNode credentials = objectMapper.createObjectNode();
        credentials = credentialsToNode(credentials, user.getCredentials());
        node.put("credentials", credentials);

        node.put("tokensCount", user.getTokensCount());
        node.put("numFreePremiumMovies", user.getNumFreePremiumMovies());

        ArrayNode purchasedMovies = objectMapper.createArrayNode();
        for (int i = 0; i < user.getPurchasedMovies().size(); i++) {
            ObjectNode movie = objectMapper.createObjectNode();
            movie = movieToNode(movie, user.getPurchasedMovies().get(i));
            purchasedMovies.add(movie);
        }
        node.put("purchasedMovies", purchasedMovies);

        ArrayNode watchedMovies = objectMapper.createArrayNode();
        for (int i = 0; i < user.getWatchedMovies().size(); i++) {
            ObjectNode movie = objectMapper.createObjectNode();
            movie = movieToNode(movie, user.getWatchedMovies().get(i));
            watchedMovies.add(movie);
        }
        node.put("watchedMovies", watchedMovies);

        ArrayNode likedMovies = objectMapper.createArrayNode();
        for (int i = 0; i < user.getLikedMovies().size(); i++) {
            ObjectNode movie = objectMapper.createObjectNode();
            movie = movieToNode(movie, user.getLikedMovies().get(i));
            likedMovies.add(movie);
        }
        node.put("likedMovies", likedMovies);


        ArrayNode ratedMovies = objectMapper.createArrayNode();
        for (int i = 0; i < user.getRatedMovies().size(); i++) {
            ObjectNode movie = objectMapper.createObjectNode();
            movie = movieToNode(movie, user.getRatedMovies().get(i));
            ratedMovies.add(movie);
        }
        node.put("ratedMovies", ratedMovies);

        ArrayNode notifications = objectMapper.createArrayNode();
        for (int i = 0; i < user.getNotifications().size(); i++) {
            ObjectNode notification = objectMapper.createObjectNode();
            notification = notificationToNode(notification, user.getNotifications().get(i));
            notifications.add(notification);
        }
        node.put("notifications", notifications);

        return node;
    }

    private ObjectNode notificationToNode(final ObjectNode node, final Notification notification) {
        node.put("movieName", notification.getMovieName());
        node.put("message", notification.getMessage());
        return node;
    }

    /**
     * functie care transforma credeentiale in formatul corespunzator de output
     */
    public ObjectNode credentialsToNode(final ObjectNode node, final Credentials credentials) {
        node.put("name", credentials.getName());
        node.put("password", credentials.getPassword());
        node.put("accountType", credentials.getAccountType());
        node.put("country", credentials.getCountry());
        node.put("balance", String.valueOf(credentials.getBalance()));
        return node;
    }

    /**
     * functie care transforma un film in formatul corespunzator de output
     */
    public ObjectNode movieToNode(final ObjectNode node, final Movie movie) {
        node.put("name", movie.getName());
        node.put("year", movie.getYear());
        node.put("duration", movie.getDuration());
        ArrayNode genres = node.putArray("genres");
        for (String genre : movie.getGenres()) {
            genres.add(genre);
        }
        ArrayNode actors = node.putArray("actors");
        for (String actor : movie.getActors()) {
            actors.add(actor);
        }
        ArrayNode countriesBanned = node.putArray("countriesBanned");
        for (String country : movie.getCountriesBanned()) {
            countriesBanned.add(country);
        }
        node.put("numLikes", movie.getNumLikes());
        node.put("rating", movie.getRating());
        node.put("numRatings", movie.getNumRatings());

        return node;

    }
}
