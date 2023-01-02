package essentials.genres;

import essentials.observerPattern.Subject;

public final class Drama extends Subject {
    private static final Drama DRAMA = new Drama();

    private Drama() {
    }

    public static Drama getInstance() {
        return DRAMA;
    }
}
