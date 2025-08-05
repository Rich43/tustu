package java.awt;

/* loaded from: rt.jar:java/awt/EventFilter.class */
interface EventFilter {

    /* loaded from: rt.jar:java/awt/EventFilter$FilterAction.class */
    public enum FilterAction {
        ACCEPT,
        REJECT,
        ACCEPT_IMMEDIATELY
    }

    FilterAction acceptEvent(AWTEvent aWTEvent);
}
