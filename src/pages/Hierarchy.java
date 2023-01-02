package pages;
import java.util.ArrayList;
/**
 SINGLETON, ne folosim de ea pentru a sti la ce pagina avem acces din fiecare pagina
 */
public final class Hierarchy {
    public Tree pages;
    public Tree.Node currentPage;
    public ArrayList<Tree.Node> history = new ArrayList<>();
    private static final Hierarchy HIERARCHY = new Hierarchy();

    public static Hierarchy getInstance() {
        return HIERARCHY;
    }

    public Hierarchy() {
        /*
        creez toate paginile existente
         */
        Page neautentificat = new Page("neautentificat");
        Page login = new Page("login", "login");
        Page register = new Page("register", "register");
        Page autentificat = new Page("autentificat");
        Page movies = new Page("movies", "Search", "Filter");
        Page seeDetails = new Page("see details", "Purchase", "Watch",
                "Like", "Rate");
        Page upgrades = new Page("upgrades");
        Page logout = new Page("logout");

        //facem legatura intre pagini
        connectPages(neautentificat, login, register, autentificat, movies, seeDetails,
                upgrades, logout);

        //ne pozitionam pe neautentificat
        currentPage = pages.root;
    }

    /**
     * conecteaza paginile existente intre ele conform schemei date
     */
    private void connectPages(final Page neautentificat, final Page login, final Page register,
                              final Page autentificat, final Page movies, final Page seeDetails,
                              final Page upgrades, final Page logout) {
        //creez arborele cu neautentificat drept root
        initTree(neautentificat, login, register);

        //conectam fiecare pagina cu paginile in care se poate merge
        connectLoginAndRegisterPagesToTree(autentificat);
        connectAuthentificatedPageToTree(movies, upgrades, logout);
        connectMoviesPageToTree(seeDetails);
        connectUpgradesPageToTree();
        connectSeeDetailsPageToTree();
    }

    /**
     * conecteaza paginile existente intre ele conform schemei date
     */
    private void connectLoginAndRegisterPagesToTree(final Page autentificat) {
        //populam login
        pages.root.children.get(0).children.add(new Tree.Node(autentificat));

        //acelasi register
        pages.root.children.get(1).children.add(pages.root.children.get(0).children.get(0));
    }

    /**
     * conecteaza paginile existente intre ele conform schemei date
     */
    private void connectSeeDetailsPageToTree() {
        //populam see details
        Tree.Node seeDetails = pages.root.children.get(0).children.get(0).children.get(0).
                children.get(1);
        seeDetails.children.add(pages.root.children.get(0).children.get(0));
        seeDetails.children.add(pages.root.children.get(0).children.get(0).children.get(0));
        seeDetails.children.add(pages.root.children.get(0).children.get(0).children.get(1));
        seeDetails.children.add(pages.root.children.get(0).children.get(0).children.get(2));
    }

    /**
     * conecteaza paginile existente intre ele conform schemei date
     */
    private void connectUpgradesPageToTree() {
        //populam upgrades
        Tree.Node upgrades = pages.root.children.get(0).children.get(0).children.get(1);
        upgrades.children.add(pages.root.children.get(0).children.get(0));
        upgrades.children.add(pages.root.children.get(0).children.get(0).children.get(0));
        upgrades.children.add(pages.root.children.get(0).children.get(0).children.get(2));
    }

    /**
     * conecteaza paginile existente intre ele conform schemei date
     */
    private void connectMoviesPageToTree(final Page seeDetails) {
        //populam movies
        Tree.Node movies = pages.root.children.get(0).children.get(0).children.get(0);
        movies.children.add(pages.root.children.get(0).children.get(0));
        movies.children.add(new Tree.Node(seeDetails));
        movies.children.add(pages.root.children.get(0).children.get(0).children.get(2));
        movies.children.add(pages.root.children.get(0).children.get(0).children.get(0));
    }

    /**
     * conecteaza paginile existente intre ele conform schemei date
     */
    private void connectAuthentificatedPageToTree(final Page movies, final Page upgrades,
                                                  final Page logout) {
        // populam autentificat
        Tree.Node auth = pages.root.children.get(0).children.get(0);
        auth.children.add(new Tree.Node(movies));
        auth.children.add(new Tree.Node(upgrades));
        auth.children.add(new Tree.Node(logout));
    }

    /**
     * initializeaza arborele ierarhiei
     */
    private void initTree(final Page neautentificat, final Page login, final Page register) {
        pages = new Tree(neautentificat);
        pages.root.children.add(new Tree.Node(login));
        pages.root.children.add(new Tree.Node(register));
    }

    /**
     * verifica daca putem accesa pagina data si ne muta pe aceasta
     */
    public boolean changeToPage(final String pageName) {
        boolean verifyChildren = false;
        for (int i = 0; i < currentPage.children.size(); i++) {
            if (currentPage.children.get(i).data.getName()
                    .compareTo(pageName) == 0) {
                verifyChildren = true;
                //ne mutam pe pagina
                currentPage = currentPage.children.get(i);
                //adaugam pagina in lista;
                history.add(currentPage);
                break;
            }
        }
        return verifyChildren;
    }

    /**
     * changes to last accesed page
     */
    public void changeBack() {
        currentPage = history.get(history.size() - 2);
        history.remove(history.size() - 1);
    }
}
