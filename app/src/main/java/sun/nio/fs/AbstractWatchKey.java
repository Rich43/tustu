package sun.nio.fs;

import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* loaded from: rt.jar:sun/nio/fs/AbstractWatchKey.class */
abstract class AbstractWatchKey implements WatchKey {
    static final int MAX_EVENT_LIST_SIZE = 512;
    static final Event<Object> OVERFLOW_EVENT;
    private final AbstractWatchService watcher;
    private final Path dir;
    private State state = State.READY;
    private List<WatchEvent<?>> events = new ArrayList();
    private Map<Object, WatchEvent<?>> lastModifyEvents = new HashMap();
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:sun/nio/fs/AbstractWatchKey$State.class */
    private enum State {
        READY,
        SIGNALLED
    }

    static {
        $assertionsDisabled = !AbstractWatchKey.class.desiredAssertionStatus();
        OVERFLOW_EVENT = new Event<>(StandardWatchEventKinds.OVERFLOW, null);
    }

    protected AbstractWatchKey(Path path, AbstractWatchService abstractWatchService) {
        this.watcher = abstractWatchService;
        this.dir = path;
    }

    final AbstractWatchService watcher() {
        return this.watcher;
    }

    @Override // java.nio.file.WatchKey
    public Path watchable() {
        return this.dir;
    }

    final void signal() {
        synchronized (this) {
            if (this.state == State.READY) {
                this.state = State.SIGNALLED;
                this.watcher.enqueueKey(this);
            }
        }
    }

    final void signalEvent(WatchEvent.Kind<?> kind, Object obj) {
        boolean z2 = kind == StandardWatchEventKinds.ENTRY_MODIFY;
        synchronized (this) {
            int size = this.events.size();
            if (size > 0) {
                WatchEvent<?> watchEvent = this.events.get(size - 1);
                if (watchEvent.kind() == StandardWatchEventKinds.OVERFLOW || (kind == watchEvent.kind() && Objects.equals(obj, watchEvent.context()))) {
                    ((Event) watchEvent).increment();
                    return;
                }
                if (!this.lastModifyEvents.isEmpty()) {
                    if (z2) {
                        WatchEvent<?> watchEvent2 = this.lastModifyEvents.get(obj);
                        if (watchEvent2 != null) {
                            if (!$assertionsDisabled && watchEvent2.kind() != StandardWatchEventKinds.ENTRY_MODIFY) {
                                throw new AssertionError();
                            }
                            ((Event) watchEvent2).increment();
                            return;
                        }
                    } else {
                        this.lastModifyEvents.remove(obj);
                    }
                }
                if (size >= 512) {
                    kind = StandardWatchEventKinds.OVERFLOW;
                    z2 = false;
                    obj = null;
                }
            }
            Event event = new Event(kind, obj);
            if (z2) {
                this.lastModifyEvents.put(obj, event);
            } else if (kind == StandardWatchEventKinds.OVERFLOW) {
                this.events.clear();
                this.lastModifyEvents.clear();
            }
            this.events.add(event);
            signal();
        }
    }

    @Override // java.nio.file.WatchKey
    public final List<WatchEvent<?>> pollEvents() {
        List<WatchEvent<?>> list;
        synchronized (this) {
            list = this.events;
            this.events = new ArrayList();
            this.lastModifyEvents.clear();
        }
        return list;
    }

    @Override // java.nio.file.WatchKey
    public final boolean reset() {
        boolean zIsValid;
        synchronized (this) {
            if (this.state == State.SIGNALLED && isValid()) {
                if (this.events.isEmpty()) {
                    this.state = State.READY;
                } else {
                    this.watcher.enqueueKey(this);
                }
            }
            zIsValid = isValid();
        }
        return zIsValid;
    }

    /* loaded from: rt.jar:sun/nio/fs/AbstractWatchKey$Event.class */
    private static class Event<T> implements WatchEvent<T> {
        private final WatchEvent.Kind<T> kind;
        private final T context;
        private int count = 1;

        Event(WatchEvent.Kind<T> kind, T t2) {
            this.kind = kind;
            this.context = t2;
        }

        @Override // java.nio.file.WatchEvent
        public WatchEvent.Kind<T> kind() {
            return this.kind;
        }

        @Override // java.nio.file.WatchEvent
        public T context() {
            return this.context;
        }

        @Override // java.nio.file.WatchEvent
        public int count() {
            return this.count;
        }

        void increment() {
            this.count++;
        }
    }
}
