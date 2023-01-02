package essentials.genres;

import essentials.observerPattern.Subject;

public final class Thriller extends Subject {
    private static final Thriller THRILLER = new Thriller();

    private Thriller() {
    }

    public static Thriller getInstance() {
        return THRILLER;
    }
}

