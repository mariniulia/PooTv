package essentials;

import essentials.genres.*;
import essentials.observerPattern.Subject;
import essentials.user.Credentials;
import essentials.user.Notification;
import essentials.user.User;
import fileio.ActionsInput;
import fileio.Filter;
import pages.Hierarchy;
import pages.Tree;

import java.util.ArrayList;

public final class Actions {

    private String type; // mandatory
    private String page;
    private String movie;
    private String feature;
    private Credentials credentials;
    private final Movie addedMovie;
    private final String deletedMovie;

    private final String subscribedGenre;
    private final String startsWith;
    private final Filter filters;
    private final Integer count;
    private final Integer rate;
    private boolean hasOutput = false;

    public static final class Builder {
        private final String type; // mandatory
        private String page;
        private String movie;
        private String feature;
        private Credentials credentials;
        private Movie addedMovie;
        private String deletedMovie;

        private String subscribedGenre;
        private String startsWith;
        private Filter filters;
        private Integer count;
        private Integer rate;
        private final boolean hasOutput = false;


        public Builder(final ActionsInput actionsInput) {
            this.type = actionsInput.getType();
        }

        /**
         * builder add
         */
        public Builder page(final ActionsInput actionsInput) {
            this.page = actionsInput.getPage();
            return this;
        }

        /**
         * builder add
         */
        public Builder movie(final ActionsInput actionsInput) {
            this.movie = actionsInput.getMovie();
            return this;
        }

        /**
         * builder add
         */
        public Builder feature(final ActionsInput actionsInput) {
            this.feature = actionsInput.getFeature();
            return this;
        }

        /**
         * builder add
         */
        public Builder credentials(final ActionsInput actionsInput) {
            if (actionsInput.getCredentials() != null) {
                this.credentials = new Credentials(actionsInput.getCredentials());
            }
            return this;
        }

        /**
         * builder add
         */
        public Builder addedMovie(final ActionsInput actionsInput) {
            if (actionsInput.getAddedMovie() != null) {
                this.addedMovie = new Movie(actionsInput.getAddedMovie());
            }
            return this;
        }

        /**
         * builder add
         */
        public Builder deletedMovie(final ActionsInput actionsInput) {
            this.deletedMovie = actionsInput.getDeletedMovie();
            return this;
        }

        /**
         * builder add
         */
        public Builder subscribedGenre(final ActionsInput actionsInput) {
            this.subscribedGenre = actionsInput.getSubscribedGenre();
            return this;
        }

        /**
         * builder add
         */
        public Builder startsWith(final ActionsInput actionsInput) {
            this.startsWith = actionsInput.getStartsWith();
            return this;
        }

        /**
         * builder add
         */
        public Builder filter(final ActionsInput actionsInput) {
            this.filters = actionsInput.getFilters();
            return this;
        }

        /**
         * builder add
         */
        public Builder count(final ActionsInput actionsInput) {
            this.count = actionsInput.getCount();
            return this;
        }

        /**
         * builder add
         */
        public Builder rate(final ActionsInput actionsInput) {
            this.rate = actionsInput.getRate();
            return this;
        }

        /**
         * build
         */
        public Actions build() {
            return new Actions(this);
        }
    }

    /**
     * private constructor using builder pattern
     */
    private Actions(final Builder builder) {
        this.type = builder.type;
        this.count = builder.count;
        this.feature = builder.feature;
        this.filters = builder.filters;
        this.rate = builder.rate;
        this.movie = builder.movie;
        this.page = builder.page;
        this.credentials = builder.credentials;
        this.startsWith = builder.startsWith;
        this.addedMovie = builder.addedMovie;
        this.deletedMovie = builder.deletedMovie;
        this.subscribedGenre = builder.subscribedGenre;
    }


    /**
     * verificam tipul actiunii si o efectuam
     */
    public void start() {
        if (this.type.compareTo("change page") == 0) {
            changePage();
        } else if (this.type.compareTo("on page") == 0) {
            onPage();
        } else if (this.type.compareTo("database") == 0) {
            database();
        } else if (this.type.compareTo("back") == 0) {
            back();
        }
    }

    /**
     * back action
     */
    private void back() {
        ArrayList<Tree.Node> history = CurrentPlatform.getInstance().getHierarchy().history;

        //verifica daca e conectat user
        if (CurrentPlatform.getInstance().getCurrentUser() == null) {
            setError();
            return;
        }

        //verificam paginile de pe care nu putem da back
        String lastPageName = history.get(history.size() - 1).data.getName();
        if (lastPageName.compareTo("login") == 0 || lastPageName.compareTo("register") == 0
                || lastPageName.compareTo("autentificat") == 0) {
            setError();
            return;
        }

        //verificam stiva sa nu fie goala
        if (history.size() == 0) {
            setError();
            return;
        }

        //ne mutam inapoi
        CurrentPlatform.getInstance().getHierarchy().changeBack();

        //pregatim pagina noua
        Tree.Node currentPage = CurrentPlatform.getInstance().getHierarchy().currentPage;
        if (currentPage.data.getName().compareTo("movies") == 0) {
            CurrentPlatform.getInstance().getAvailableMovies();
            hasOutput = true;
        }
    }

    /**
     * subscribe action
     */
    private void subscribe() {
        User user = CurrentPlatform.getInstance().getCurrentUser();
        ArrayList<Movie> movies = CurrentPlatform.getInstance().getCurrentMoviesList();
        Tree.Node currentPage = CurrentPlatform.getInstance().getHierarchy().currentPage;

        //verifica sa fii in see details
        if (!(currentPage.data.getName().compareTo("see details") == 0)) {
            setError();
            return;
        }

        //verifica sa fie unul dintre genurile filmului disponibil
        if (movies.get(0) == null || !movies.get(0).getGenres().contains(subscribedGenre)) {
            setError();
            return;
        }

        //verifica daca nu a dat deja subscribe
        if (giveInstanceFor(subscribedGenre).getObservers().contains(user)) {
            setError();
            return;
        }
        //subscribe
        user.userFollows(giveInstanceFor(subscribedGenre));
    }

    /**
     * database actions
     */
    private void database() {
        switch (feature) {
            case "add" -> {
                databaseAdd();
            }
            case "delete" -> {
                databaseDelete();
            }
            default -> {
                return;
            }
        }
    }

    /**
     * deletes movie from database
     */
    private void databaseDelete() {
        //verificam daca exista filmul cautat
        if (CurrentPlatform.getInstance().getPlatformMovies().stream().
                noneMatch(e -> e.getName().compareTo(deletedMovie) == 0)) {
            setError();
            return;
        }

        //creem notificarea
        Notification newNotification = new Notification();
        newNotification.setMessage("DELETE");
        newNotification.setMovieName(deletedMovie);

        //notificam userii care cumparasera filmul
        notifyUsers(newNotification);
    }

    /**
     * adds movie in database
     */
    private void databaseAdd() {
        //verificam daca exista deja film cu acelasi nume
        if (CurrentPlatform.getInstance().getPlatformMovies().stream()
                .anyMatch(e -> e.getName().compareTo(addedMovie.getName()) == 0)) {
            setError();
            return;
        }
        //adaugam filmul in platforma
        CurrentPlatform.getInstance().getPlatformMovies().add(addedMovie);

        //notificam utilizatorii abonati la genurile filmului
        for (int i = 0; i < addedMovie.getGenres().size(); i++) {
            giveInstanceFor(addedMovie.getGenres().get(i)).setState("add",
                    addedMovie.getName());
        }
    }

    /**
     * notifies all users of update
     */
    private void notifyUsers(final Notification newNotification) {
        ArrayList<User> users = CurrentPlatform.getInstance().getPlatformUsers();
        //cautam in fiecare utilizator
        for (User user : users) {
            //cautam in filmele cumparate de fiecare utilizator
            for (int j = 0; j < user.getPurchasedMovies().size(); j++) {
                if (user.getPurchasedMovies().get(j).getName().compareTo(deletedMovie) == 0) {
                    user.update(newNotification);
                }
            }
        }
    }

    /**
     * sets error for output
     */
    private void setError() {
        CurrentPlatform.getInstance().setError("Error");
        CurrentPlatform.getInstance().getCurrentMoviesList().clear();
        hasOutput = true;
    }

    /**
     * return instance class of genre for a given string name
     */
    private Subject giveInstanceFor(final String genreName) {
        switch (genreName) {
            case "Action" -> {
                return Action.getInstance();
            }
            case "Comedy" -> {
                return Comedy.getInstance();
            }
            case "Drama" -> {
                return Drama.getInstance();
            }
            case "Thriller" -> {
                return Thriller.getInstance();
            }
            case "Crime" -> {
                return Crime.getInstance();
            }
            case "Western" -> {
                return Western.getInstance();
            }
            case "Mystery" -> {
                return Mystery.getInstance();
            }
            case "Fantasy" -> {
                return Fantasy.getInstance();
            }
            case "Romance" -> {
                return Romance.getInstance();
            }
            case "Horror" -> {
                return Horror.getInstance();
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * efectuam feature-ul dat
     */
    private void onPage() {
        switch (feature) {
            case "login" -> {
                checkLogin();
            }
            case "register" -> {
                checkRegister();
            }
            case "search" -> {
                checkSearch();
            }
            case "filter" -> {
                checkFilter();
            }
            case "buy tokens" -> {
                checkBuy();
            }
            case "buy premium account" -> {
                checkBuyPremium();
            }
            case "purchase" -> {
                checkPurchase();

            }
            case "watch" -> {
                checkWatch();

            }
            case "like" -> {
                checkLike(CurrentPlatform.getInstance().like() == 1);

            }
            case "rate" -> {
                checkLike(CurrentPlatform.getInstance().rate(rate) == 1);
            }
            case "subscribe" -> {
                subscribe();
            }
            default -> {
            }
        }
    }

    private void checkLike(final boolean x) {
        //verificam ca suntem pe pagina corespunzatoare
        if (cantDoThisHere("see details")) {
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
            return;
        }

        //like
        if (x) {
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
        }
        hasOutput = true;
    }

    private void checkWatch() {
        //verificam ca suntem pe pagina corespunzatoare
        if (cantDoThisHere("see details")) {
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
            return;
        }

        //watch
        hasOutput = true;
        int test = CurrentPlatform.getInstance().watch();
        if (test == 1) {
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
        }
    }

    private void checkPurchase() {
        //verificam ca suntem pe pagina corespunzatoare
        if (cantDoThisHere("see details")) {
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
            return;
        }
        //verificam ca nu e deja cumparat
        if (CurrentPlatform.getInstance().getCurrentUser().getPurchasedMovies()
                .contains(CurrentPlatform.getInstance().getCurrentMoviesList().get(0))) {
            setError();
            return;
        }
        if (CurrentPlatform.getInstance().purchase() == 1) {
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
        }
        hasOutput = true;
    }

    private void checkBuyPremium() {
        //verificam ca suntem pe pagina corespunzatoare
        if (cantDoThisHere("upgrades")) {
            return;
        }
        CurrentPlatform.getInstance().buyPremiumAccount();
    }

    private void checkBuy() {
        //verificam ca suntem pe pagina corespunzatoare
        if (cantDoThisHere("upgrades")) {
            return;
        }
        CurrentPlatform.getInstance().buyTokens(count);
    }

    private void checkFilter() {
        //verificam ca suntem pe pagina corespunzatoare
        if (cantDoThisHere("movies")) {
            return;
        }
        CurrentPlatform.getInstance().getAvailableMovies();
        CurrentPlatform.getInstance().filter(filters);
        hasOutput = true;
    }

    private void checkSearch() {
        //verificam ca suntem pe pagina corespunzatoare
        if (cantDoThisHere("movies")) {
            return;
        }
        CurrentPlatform.getInstance().search(startsWith);
        hasOutput = true;
    }

    private void checkRegister() {
        //verificam ca suntem pe pagina corespunzatoare
        if (cantDoThisHere("register")) {
            return;
        }
        register();
    }

    private void checkLogin() {
        //verificam ca suntem pe pagina corespunzatoare
        if (cantDoThisHere("login")) {
            return;
        }
        login();
    }

    /**
     * register
     */
    private void register() {
        Hierarchy hierarchy = CurrentPlatform.getInstance().getHierarchy();

        //ne inregistram in platforma
        CurrentPlatform.getInstance().register(credentials);

        //ne mutam pe autentificat
        hierarchy.history.add(hierarchy.currentPage.children.get(0));
        hierarchy.currentPage = hierarchy.currentPage.children.get(0);
        hasOutput = true;
    }

    /**
     * login
     */
    private void login() {
        //daca nu sunt corecte, ma mut pe pe neautentificat
        Hierarchy hierarchy = CurrentPlatform.getInstance().getHierarchy();
        if (CurrentPlatform.getInstance().login(credentials) == 1) {
            hierarchy.currentPage = hierarchy.pages.root;
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
        } else {
            //daca credidentialele sunt corecte, ma mut pe autentificat
            hierarchy.history.add(hierarchy.currentPage.children.get(0));
            hierarchy.currentPage = hierarchy.currentPage.children.get(0);

        }
        //setam sa afiseze output
        hasOutput = true;
    }

    /**
     * schimbam pagina
     */
    private void changePage() {
        if (CurrentPlatform.getInstance().getHierarchy().changeToPage(this.page)) {
            //pregatim paginile
            preparePages();
            //fac logout daca e nevoie
            checkLogout();
        } else {
            //pregatim eroarea
            CurrentPlatform.getInstance().setError("Error");
            //ne intoarcem in istoric unde eram
            CurrentPlatform.getInstance().getCurrentMoviesList().clear();
            hasOutput = true;
        }
    }

    /**
     * preagatim platfoma pentru movies si seeDetails
     */
    private void preparePages() {
        if (page.compareTo("movies") == 0) {
            prepareMoviesPage();
        } else if (page.compareTo("see details") == 0) {
            prepareSeeDetailsPage();
        }
    }

    /**
     * pregatim movies
     */
    private void prepareMoviesPage() {
        CurrentPlatform.getInstance().getAvailableMovies();
        hasOutput = true;
    }

    /**
     * pregatim seeDetails
     */
    private void prepareSeeDetailsPage() {
        if (CurrentPlatform.getInstance().getMovie(movie) != 0) {
            //daca pica see details, ma duc pe movies
            CurrentPlatform.getInstance().getHierarchy().currentPage
                    = CurrentPlatform.getInstance().getHierarchy().
                    pages.root.children.get(0).children.get(0).children.get(0);
            CurrentPlatform.getInstance().resetMovies = true;
            //actualizez stiva cu istoric
            CurrentPlatform.getInstance().getHierarchy().changeBack();
        }
        hasOutput = true;
    }

    /**
     * facem logout daca e necesar
     */
    private static void checkLogout() {
        if (CurrentPlatform.getInstance().getHierarchy().currentPage.data
                .getName().compareTo("logout") == 0) {
            CurrentPlatform.getInstance().getHierarchy().currentPage
                    = CurrentPlatform.getInstance().getHierarchy().pages.root;
            CurrentPlatform.getInstance().setCurrentUser(null);
            //golim stiva de back
            CurrentPlatform.getInstance().getHierarchy().history.clear();

        }
    }

    /**
     * setam eroare daca nu putem efectua actiunea pe pagina curenta
     */
    private boolean cantDoThisHere(final String upgrades) {
        if (CurrentPlatform.getInstance().getHierarchy().currentPage.data.
                getName().compareTo(upgrades) != 0) {
            CurrentPlatform.getInstance().setError("Error");
            hasOutput = true;
            return true;
        }
        return false;
    }

    public boolean getHasOutput() {
        return hasOutput;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(final String page) {
        this.page = page;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(final String movie) {
        this.movie = movie;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(final String feature) {
        this.feature = feature;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(final Credentials credentials) {
        this.credentials = credentials;
    }
}
