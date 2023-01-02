package essentials.genres;

import essentials.observerPattern.Subject;

public final class Action extends Subject {
    private static final Action ACTION = new Action();

    private Action() {
    }

    public static Action getInstance() {
        return ACTION;
    }
}
