package javafx.scene.control;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:javafx/scene/control/ScrollToEvent.class */
public class ScrollToEvent<T> extends Event {
    public static final EventType<ScrollToEvent> ANY;
    private static final EventType<ScrollToEvent<Integer>> SCROLL_TO_TOP_INDEX;
    private static final EventType<?> SCROLL_TO_COLUMN;
    private static final long serialVersionUID = -8557345736849482516L;
    private final T scrollTarget;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ScrollToEvent.class.desiredAssertionStatus();
        ANY = new EventType<>(Event.ANY, "SCROLL_TO");
        SCROLL_TO_TOP_INDEX = new EventType<>(ANY, "SCROLL_TO_TOP_INDEX");
        SCROLL_TO_COLUMN = new EventType<>(ANY, "SCROLL_TO_COLUMN");
    }

    public static EventType<ScrollToEvent<Integer>> scrollToTopIndex() {
        return SCROLL_TO_TOP_INDEX;
    }

    public static <T extends TableColumnBase<?, ?>> EventType<ScrollToEvent<T>> scrollToColumn() {
        return (EventType<ScrollToEvent<T>>) SCROLL_TO_COLUMN;
    }

    public ScrollToEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("type") EventType<ScrollToEvent<T>> type, @NamedArg("scrollTarget") T scrollTarget) {
        super(source, target, type);
        if (!$assertionsDisabled && scrollTarget == null) {
            throw new AssertionError();
        }
        this.scrollTarget = scrollTarget;
    }

    public T getScrollTarget() {
        return this.scrollTarget;
    }
}
