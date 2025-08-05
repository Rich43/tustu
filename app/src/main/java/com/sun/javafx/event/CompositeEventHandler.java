package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;

/* loaded from: jfxrt.jar:com/sun/javafx/event/CompositeEventHandler.class */
public final class CompositeEventHandler<T extends Event> {
    private EventProcessorRecord<T> firstRecord;
    private EventProcessorRecord<T> lastRecord;
    private EventHandler<? super T> eventHandler;

    public void setEventHandler(EventHandler<? super T> eventHandler) {
        this.eventHandler = eventHandler;
    }

    public EventHandler<? super T> getEventHandler() {
        return this.eventHandler;
    }

    public void addEventHandler(EventHandler<? super T> eventHandler) {
        if (find(eventHandler, false) == null) {
            append(this.lastRecord, createEventHandlerRecord(eventHandler));
        }
    }

    public void removeEventHandler(EventHandler<? super T> eventHandler) {
        EventProcessorRecord<T> record = find(eventHandler, false);
        if (record != null) {
            remove(record);
        }
    }

    public void addEventFilter(EventHandler<? super T> eventFilter) {
        if (find(eventFilter, true) == null) {
            append(this.lastRecord, createEventFilterRecord(eventFilter));
        }
    }

    public void removeEventFilter(EventHandler<? super T> eventFilter) {
        EventProcessorRecord<T> record = find(eventFilter, true);
        if (record != null) {
            remove(record);
        }
    }

    public void dispatchBubblingEvent(Event event) {
        EventProcessorRecord<T> eventProcessorRecord = this.firstRecord;
        while (true) {
            EventProcessorRecord<T> record = eventProcessorRecord;
            if (record == null) {
                break;
            }
            if (record.isDisconnected()) {
                remove(record);
            } else {
                record.handleBubblingEvent(event);
            }
            eventProcessorRecord = ((EventProcessorRecord) record).nextRecord;
        }
        if (this.eventHandler != null) {
            this.eventHandler.handle(event);
        }
    }

    public void dispatchCapturingEvent(Event event) {
        EventProcessorRecord<T> eventProcessorRecord = this.firstRecord;
        while (true) {
            EventProcessorRecord<T> record = eventProcessorRecord;
            if (record != null) {
                if (record.isDisconnected()) {
                    remove(record);
                } else {
                    record.handleCapturingEvent(event);
                }
                eventProcessorRecord = ((EventProcessorRecord) record).nextRecord;
            } else {
                return;
            }
        }
    }

    boolean containsHandler(EventHandler<? super T> eventHandler) {
        return find(eventHandler, false) != null;
    }

    boolean containsFilter(EventHandler<? super T> eventFilter) {
        return find(eventFilter, true) != null;
    }

    private EventProcessorRecord<T> createEventHandlerRecord(EventHandler<? super T> eventHandler) {
        return eventHandler instanceof WeakEventHandler ? new WeakEventHandlerRecord((WeakEventHandler) eventHandler) : new NormalEventHandlerRecord(eventHandler);
    }

    private EventProcessorRecord<T> createEventFilterRecord(EventHandler<? super T> eventFilter) {
        return eventFilter instanceof WeakEventHandler ? new WeakEventFilterRecord((WeakEventHandler) eventFilter) : new NormalEventFilterRecord(eventFilter);
    }

    private void remove(EventProcessorRecord<T> record) {
        EventProcessorRecord<T> prevRecord = ((EventProcessorRecord) record).prevRecord;
        EventProcessorRecord<T> nextRecord = ((EventProcessorRecord) record).nextRecord;
        if (prevRecord == null) {
            this.firstRecord = nextRecord;
        } else {
            ((EventProcessorRecord) prevRecord).nextRecord = nextRecord;
        }
        if (nextRecord == null) {
            this.lastRecord = prevRecord;
        } else {
            ((EventProcessorRecord) nextRecord).prevRecord = prevRecord;
        }
    }

    private void append(EventProcessorRecord<T> prevRecord, EventProcessorRecord<T> newRecord) {
        EventProcessorRecord<T> nextRecord;
        if (prevRecord == null) {
            nextRecord = this.firstRecord;
            this.firstRecord = newRecord;
        } else {
            nextRecord = ((EventProcessorRecord) prevRecord).nextRecord;
            ((EventProcessorRecord) prevRecord).nextRecord = newRecord;
        }
        if (nextRecord == null) {
            this.lastRecord = newRecord;
        } else {
            ((EventProcessorRecord) nextRecord).prevRecord = newRecord;
        }
        ((EventProcessorRecord) newRecord).prevRecord = prevRecord;
        ((EventProcessorRecord) newRecord).nextRecord = nextRecord;
    }

    private EventProcessorRecord<T> find(EventHandler<? super T> eventProcessor, boolean isFilter) {
        EventProcessorRecord<T> eventProcessorRecord = this.firstRecord;
        while (true) {
            EventProcessorRecord<T> record = eventProcessorRecord;
            if (record != null) {
                if (record.isDisconnected()) {
                    remove(record);
                } else if (record.stores(eventProcessor, isFilter)) {
                    return record;
                }
                eventProcessorRecord = ((EventProcessorRecord) record).nextRecord;
            } else {
                return null;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/event/CompositeEventHandler$EventProcessorRecord.class */
    private static abstract class EventProcessorRecord<T extends Event> {
        private EventProcessorRecord<T> nextRecord;
        private EventProcessorRecord<T> prevRecord;

        public abstract boolean stores(EventHandler<? super T> eventHandler, boolean z2);

        public abstract void handleBubblingEvent(T t2);

        public abstract void handleCapturingEvent(T t2);

        public abstract boolean isDisconnected();

        private EventProcessorRecord() {
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/event/CompositeEventHandler$NormalEventHandlerRecord.class */
    private static final class NormalEventHandlerRecord<T extends Event> extends EventProcessorRecord<T> {
        private final EventHandler<? super T> eventHandler;

        public NormalEventHandlerRecord(EventHandler<? super T> eventHandler) {
            super();
            this.eventHandler = eventHandler;
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public boolean stores(EventHandler<? super T> eventProcessor, boolean isFilter) {
            return !isFilter && this.eventHandler == eventProcessor;
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public void handleBubblingEvent(T event) {
            this.eventHandler.handle(event);
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public void handleCapturingEvent(T event) {
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public boolean isDisconnected() {
            return false;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/event/CompositeEventHandler$WeakEventHandlerRecord.class */
    private static final class WeakEventHandlerRecord<T extends Event> extends EventProcessorRecord<T> {
        private final WeakEventHandler<? super T> weakEventHandler;

        public WeakEventHandlerRecord(WeakEventHandler<? super T> weakEventHandler) {
            super();
            this.weakEventHandler = weakEventHandler;
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public boolean stores(EventHandler<? super T> eventProcessor, boolean isFilter) {
            return !isFilter && this.weakEventHandler == eventProcessor;
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public void handleBubblingEvent(T event) {
            this.weakEventHandler.handle(event);
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public void handleCapturingEvent(T event) {
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public boolean isDisconnected() {
            return this.weakEventHandler.wasGarbageCollected();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/event/CompositeEventHandler$NormalEventFilterRecord.class */
    private static final class NormalEventFilterRecord<T extends Event> extends EventProcessorRecord<T> {
        private final EventHandler<? super T> eventFilter;

        public NormalEventFilterRecord(EventHandler<? super T> eventFilter) {
            super();
            this.eventFilter = eventFilter;
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public boolean stores(EventHandler<? super T> eventProcessor, boolean isFilter) {
            return isFilter && this.eventFilter == eventProcessor;
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public void handleBubblingEvent(T event) {
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public void handleCapturingEvent(T event) {
            this.eventFilter.handle(event);
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public boolean isDisconnected() {
            return false;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/event/CompositeEventHandler$WeakEventFilterRecord.class */
    private static final class WeakEventFilterRecord<T extends Event> extends EventProcessorRecord<T> {
        private final WeakEventHandler<? super T> weakEventFilter;

        public WeakEventFilterRecord(WeakEventHandler<? super T> weakEventFilter) {
            super();
            this.weakEventFilter = weakEventFilter;
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public boolean stores(EventHandler<? super T> eventProcessor, boolean isFilter) {
            return isFilter && this.weakEventFilter == eventProcessor;
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public void handleBubblingEvent(T event) {
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public void handleCapturingEvent(T event) {
            this.weakEventFilter.handle(event);
        }

        @Override // com.sun.javafx.event.CompositeEventHandler.EventProcessorRecord
        public boolean isDisconnected() {
            return this.weakEventFilter.wasGarbageCollected();
        }
    }
}
