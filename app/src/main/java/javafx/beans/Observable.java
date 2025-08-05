package javafx.beans;

/* loaded from: jfxrt.jar:javafx/beans/Observable.class */
public interface Observable {
    void addListener(InvalidationListener invalidationListener);

    void removeListener(InvalidationListener invalidationListener);
}
