package essentials.observerPattern;
import essentials.user.Notification;
import java.util.ArrayList;
import java.util.List;

public class Subject {

    private final List<Observer> observers = new ArrayList<Observer>();
    private Notification notification;
    /**
     *modifies subject's state and notifies all observers
     */
    public void setState(final String feature, final String movie) {
        Notification newNotification = new Notification();
        if (feature.compareTo("add") == 0) {
            newNotification.setMessage("ADD");
            newNotification.setMovieName(movie);
        }
        this.notification = newNotification;
        notifyAllObservers();
    }
    /**
     * attaches a new observer to subject
     */
    public void attach(final Observer observer) {
        observers.add(observer);
    }
    /**
     * adds each notification to observer
     */
    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
    /**
     *returns all observers for subject
     */
    public List<Observer> getObservers() {
        return observers;
    }
}
