package java.awt;

/* loaded from: rt.jar:java/awt/FocusTraversalPolicy.class */
public abstract class FocusTraversalPolicy {
    public abstract Component getComponentAfter(Container container, Component component);

    public abstract Component getComponentBefore(Container container, Component component);

    public abstract Component getFirstComponent(Container container);

    public abstract Component getLastComponent(Container container);

    public abstract Component getDefaultComponent(Container container);

    public Component getInitialComponent(Window window) {
        if (window == null) {
            throw new IllegalArgumentException("window cannot be equal to null.");
        }
        Component defaultComponent = getDefaultComponent(window);
        if (defaultComponent == null && window.isFocusableWindow()) {
            defaultComponent = window;
        }
        return defaultComponent;
    }
}
