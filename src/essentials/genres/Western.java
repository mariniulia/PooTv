package essentials.genres;

import essentials.observerPattern.Subject;

public final class Western extends Subject {
    private static final Western WESTERN = new Western();

    private Western() {
    }

    public static Western getInstance() {
        return WESTERN;
    }
}
