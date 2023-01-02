package essentials.observerPattern;
import essentials.user.Notification;

public abstract class Observer {
    protected Subject subject;

    /**
     * notifies observer of subject's changed state
     */
    public abstract void update(Notification notification);
}
