package essentials.genres;

import essentials.observerPattern.Subject;

public final class Fantasy extends Subject {
    private static final Fantasy FANTASY = new Fantasy();

    private Fantasy() {
    }

    public static Fantasy getInstance() {
        return FANTASY;
    }
}

