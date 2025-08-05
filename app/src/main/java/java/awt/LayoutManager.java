package java.awt;

/* loaded from: rt.jar:java/awt/LayoutManager.class */
public interface LayoutManager {
    void addLayoutComponent(String str, Component component);

    void removeLayoutComponent(Component component);

    Dimension preferredLayoutSize(Container container);

    Dimension minimumLayoutSize(Container container);

    void layoutContainer(Container container);
}
