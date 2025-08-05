package javafx.concurrent;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;

/* loaded from: jfxrt.jar:javafx/concurrent/Worker.class */
public interface Worker<V> {

    /* loaded from: jfxrt.jar:javafx/concurrent/Worker$State.class */
    public enum State {
        READY,
        SCHEDULED,
        RUNNING,
        SUCCEEDED,
        CANCELLED,
        FAILED
    }

    State getState();

    ReadOnlyObjectProperty<State> stateProperty();

    V getValue();

    ReadOnlyObjectProperty<V> valueProperty();

    Throwable getException();

    ReadOnlyObjectProperty<Throwable> exceptionProperty();

    double getWorkDone();

    ReadOnlyDoubleProperty workDoneProperty();

    double getTotalWork();

    ReadOnlyDoubleProperty totalWorkProperty();

    double getProgress();

    ReadOnlyDoubleProperty progressProperty();

    boolean isRunning();

    ReadOnlyBooleanProperty runningProperty();

    String getMessage();

    ReadOnlyStringProperty messageProperty();

    String getTitle();

    ReadOnlyStringProperty titleProperty();

    boolean cancel();
}
