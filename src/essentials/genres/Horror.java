package essentials.genres;

import essentials.observerPattern.Subject;

public final class Horror extends Subject {
    private static final Horror HORROR = new Horror();

    private Horror() {
    }

    public static Horror getInstance() {
        return HORROR;
    }
}
