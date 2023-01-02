package essentials.genres.utils;
import java.util.ArrayList;
/**
 * Clasa care stocheaza likeurile si topul genurilor pentru fiecare user
*/
public final class GenreDatabase {
    private ArrayList<GenreStatus> top = new ArrayList();

    //initialize all genre
    private GenreStatus action = new GenreStatus("Action");
    private GenreStatus comedy = new GenreStatus("Comedy");
    private GenreStatus crimes = new GenreStatus("Crimes");
    private GenreStatus drama = new GenreStatus("Drama");
    private GenreStatus fantasy = new GenreStatus("Fantasy");
    private GenreStatus mystery = new GenreStatus("Mystery");
    private GenreStatus romance = new GenreStatus("Romance");
    private GenreStatus thriller = new GenreStatus("Thriller");
    private GenreStatus western = new GenreStatus("Western");
    private GenreStatus horror = new GenreStatus("Horror");


    public GenreDatabase() {
        //add all to top
        top.add(action);
        top.add(comedy);
        top.add(crimes);
        top.add(drama);
        top.add(fantasy);
        top.add(mystery);
        top.add(romance);
        top.add(thriller);
        top.add(western);
        top.add(horror);
    }
    /**
     * returns up-to-date top of genres for user
     */
    public ArrayList<GenreStatus> getTop() {
        for (int i = 0; i < top.size(); i++) {
            //daca nu au nici macar un like, out
            if (top.get(i).getNumLikes() < 1) {
                top.remove(i);
                i--;
            }
        }

        //sortare dupa nr likeuri apoi lexicografic
        top.sort((o1, o2) -> {
            if (o1.getNumLikes() > o2.getNumLikes()) {
                return -1;
            }
            if (o1.getNumLikes() < o2.getNumLikes()) {
                return 1;
            }
            if (o1.getGenreName().compareTo(o2.getGenreName()) > 0) {
                return -1;
            }
            return 0;
        });

        return top;
    }

    /**
     * add like to given genre
     */
    public void addLike(final String genreName) {
        for (GenreStatus genreStatus : top) {
            if (genreStatus.getGenreName().compareTo(genreName) == 0) {
                genreStatus.setNumLikes(genreStatus.getNumLikes() + 1);
            }
        }
    }

    public void setTop(final ArrayList<GenreStatus> top) {
        this.top = top;
    }
}
