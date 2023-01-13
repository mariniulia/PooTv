package essentials;

import pages.Tree;

import java.util.ArrayList;

public interface Strategy {
    /**
     * executa actiunea in functie de tipul ei
     */
    Actions execute(Actions action);

    // Concrete strategies implement the algorithm while following
// the base strategy interface. The interface makes them
// interchangeable in the context.
    class ChangePage implements Strategy {
        public Actions execute(final Actions action) {
            if (CurrentPlatform.getInstance().getHierarchy().changeToPage(action.getPage())) {
                //pregatim paginile
                action.preparePages();
                //fac logout daca e nevoie
                action.checkLogout();
            } else {
                //pregatim eroarea
                CurrentPlatform.getInstance().setError("Error");
                //ne intoarcem in istoric unde eram
                CurrentPlatform.getInstance().getCurrentMoviesList().clear();
                action.setHasOutput(true);
            }
            return action;
        }
    }

    class OnPage implements Strategy {
        public Actions execute(final Actions action) {
            switch (action.getFeature()) {
                case "login" -> {
                    action.checkLogin();
                }
                case "register" -> {
                    action.checkRegister();
                }
                case "search" -> {
                    action.checkSearch();
                }
                case "filter" -> {
                    action.checkFilter();
                }
                case "buy tokens" -> {
                    action.checkBuy();
                }
                case "buy premium account" -> {
                    action.checkBuyPremium();
                }
                case "purchase" -> {
                    action.checkPurchase();

                }
                case "watch" -> {
                    action.checkWatch();

                }
                case "like" -> {
                    action.checkLike(CurrentPlatform.getInstance().like() == 1);

                }
                case "rate" -> {
                    action.checkLike(CurrentPlatform.getInstance().rate(action.getRate()) == 1);
                }
                case "subscribe" -> {
                    action.subscribe();
                }
                default -> {
                }
            }
            return action;
        }
    }

    /**
     * database actions
     */
    class Database implements Strategy {
        public Actions execute(final Actions action) {
            switch (action.getFeature()) {
                case "add" -> {
                    action.databaseAdd();
                }
                case "delete" -> {
                    action.databaseDelete();
                }
                default -> {
                    return action;
                }
            }
            return action;
        }
    }
    /**
     * back action
     */
    class Back implements Strategy {
        public Actions execute(final Actions action) {
            ArrayList<Tree.Node> history = CurrentPlatform.getInstance().getHierarchy().history;

            //verifica daca e conectat user
            if (CurrentPlatform.getInstance().getCurrentUser() == null) {
                action.setError();
                return action;
            }

            //verificam paginile de pe care nu putem da back
            String lastPageName = history.get(history.size() - 1).data.getName();
            if (lastPageName.compareTo("login") == 0 || lastPageName.compareTo("register") == 0
                    || lastPageName.compareTo("autentificat") == 0) {
                action.setError();
                return action;
            }

            //ne mutam inapoi
            CurrentPlatform.getInstance().getHierarchy().changeBack();

            //pregatim pagina noua
            Tree.Node currentPage = CurrentPlatform.getInstance().getHierarchy().currentPage;
            if (currentPage.data.getName().compareTo("movies") == 0) {
                CurrentPlatform.getInstance().getAvailableMovies();
                action.setHasOutput(true);
            }
            return action;
        }

    }
}

