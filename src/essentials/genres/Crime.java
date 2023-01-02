package essentials.genres;

import essentials.observerPattern.Subject;

public final class Crime extends Subject {
    private static final Crime CRIME  = new Crime();

    private Crime() {
    }

    public static Crime getInstance() {
        return CRIME;
    }
}
