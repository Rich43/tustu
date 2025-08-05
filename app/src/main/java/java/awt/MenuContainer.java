package java.awt;

/* loaded from: rt.jar:java/awt/MenuContainer.class */
public interface MenuContainer {
    Font getFont();

    void remove(MenuComponent menuComponent);

    @Deprecated
    boolean postEvent(Event event);
}
