package javafx.concurrent;

import com.sun.javafx.event.EventHandlerManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:javafx/concurrent/EventHelper.class */
class EventHelper {
    private final EventTarget target;
    private final ObjectProperty<EventHandler<WorkerStateEvent>> onReady;
    private final ObjectProperty<EventHandler<WorkerStateEvent>> onScheduled;
    private final ObjectProperty<EventHandler<WorkerStateEvent>> onRunning;
    private final ObjectProperty<EventHandler<WorkerStateEvent>> onSucceeded;
    private final ObjectProperty<EventHandler<WorkerStateEvent>> onCancelled;
    private final ObjectProperty<EventHandler<WorkerStateEvent>> onFailed;
    private EventHandlerManager internalEventDispatcher;

    final ObjectProperty<EventHandler<WorkerStateEvent>> onReadyProperty() {
        return this.onReady;
    }

    final EventHandler<WorkerStateEvent> getOnReady() {
        return this.onReady.get();
    }

    final void setOnReady(EventHandler<WorkerStateEvent> value) {
        this.onReady.set(value);
    }

    final ObjectProperty<EventHandler<WorkerStateEvent>> onScheduledProperty() {
        return this.onScheduled;
    }

    final EventHandler<WorkerStateEvent> getOnScheduled() {
        return this.onScheduled.get();
    }

    final void setOnScheduled(EventHandler<WorkerStateEvent> value) {
        this.onScheduled.set(value);
    }

    final ObjectProperty<EventHandler<WorkerStateEvent>> onRunningProperty() {
        return this.onRunning;
    }

    final EventHandler<WorkerStateEvent> getOnRunning() {
        return this.onRunning.get();
    }

    final void setOnRunning(EventHandler<WorkerStateEvent> value) {
        this.onRunning.set(value);
    }

    final ObjectProperty<EventHandler<WorkerStateEvent>> onSucceededProperty() {
        return this.onSucceeded;
    }

    final EventHandler<WorkerStateEvent> getOnSucceeded() {
        return this.onSucceeded.get();
    }

    final void setOnSucceeded(EventHandler<WorkerStateEvent> value) {
        this.onSucceeded.set(value);
    }

    final ObjectProperty<EventHandler<WorkerStateEvent>> onCancelledProperty() {
        return this.onCancelled;
    }

    final EventHandler<WorkerStateEvent> getOnCancelled() {
        return this.onCancelled.get();
    }

    final void setOnCancelled(EventHandler<WorkerStateEvent> value) {
        this.onCancelled.set(value);
    }

    final ObjectProperty<EventHandler<WorkerStateEvent>> onFailedProperty() {
        return this.onFailed;
    }

    final EventHandler<WorkerStateEvent> getOnFailed() {
        return this.onFailed.get();
    }

    final void setOnFailed(EventHandler<WorkerStateEvent> value) {
        this.onFailed.set(value);
    }

    EventHelper(EventTarget bean) {
        this.target = bean;
        this.onReady = new SimpleObjectProperty<EventHandler<WorkerStateEvent>>(bean, "onReady") { // from class: javafx.concurrent.EventHelper.1
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                EventHandler<WorkerStateEvent> handler = get();
                EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_READY, handler);
            }
        };
        this.onScheduled = new SimpleObjectProperty<EventHandler<WorkerStateEvent>>(bean, "onScheduled") { // from class: javafx.concurrent.EventHelper.2
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                EventHandler<WorkerStateEvent> handler = get();
                EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_SCHEDULED, handler);
            }
        };
        this.onRunning = new SimpleObjectProperty<EventHandler<WorkerStateEvent>>(bean, "onRunning") { // from class: javafx.concurrent.EventHelper.3
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                EventHandler<WorkerStateEvent> handler = get();
                EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_RUNNING, handler);
            }
        };
        this.onSucceeded = new SimpleObjectProperty<EventHandler<WorkerStateEvent>>(bean, "onSucceeded") { // from class: javafx.concurrent.EventHelper.4
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                EventHandler<WorkerStateEvent> handler = get();
                EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, handler);
            }
        };
        this.onCancelled = new SimpleObjectProperty<EventHandler<WorkerStateEvent>>(bean, "onCancelled") { // from class: javafx.concurrent.EventHelper.5
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                EventHandler<WorkerStateEvent> handler = get();
                EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, handler);
            }
        };
        this.onFailed = new SimpleObjectProperty<EventHandler<WorkerStateEvent>>(bean, "onFailed") { // from class: javafx.concurrent.EventHelper.6
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                EventHandler<WorkerStateEvent> handler = get();
                EventHelper.this.setEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, handler);
            }
        };
    }

    final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().addEventHandler(eventType, eventHandler);
    }

    final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().removeEventHandler(eventType, eventHandler);
    }

    final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().addEventFilter(eventType, eventFilter);
    }

    final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        getInternalEventDispatcher().removeEventFilter(eventType, eventFilter);
    }

    final <T extends Event> void setEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        getInternalEventDispatcher().setEventHandler(eventType, eventHandler);
    }

    private EventHandlerManager getInternalEventDispatcher() {
        if (this.internalEventDispatcher == null) {
            this.internalEventDispatcher = new EventHandlerManager(this.target);
        }
        return this.internalEventDispatcher;
    }

    final void fireEvent(Event event) {
        Event.fireEvent(this.target, event);
    }

    EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return this.internalEventDispatcher == null ? tail : tail.append(getInternalEventDispatcher());
    }
}
