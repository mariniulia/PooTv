package essentials.genres;

import essentials.observerPattern.Subject;

public final class Comedy extends Subject {
    private static final Comedy COMEDY = new Comedy();

    private Comedy() {
    }

    public static Comedy getInstance() {
        return COMEDY;
    }
}
