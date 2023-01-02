package essentials.genres;

import essentials.observerPattern.Subject;

public final class Mystery extends Subject {
    private static final Mystery MYSTERY = new Mystery();

    private Mystery() {
    }

    public static Mystery getInstance() {
        return MYSTERY;
    }
}
