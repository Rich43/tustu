package java.awt;

/* loaded from: rt.jar:java/awt/LayoutManager2.class */
public interface LayoutManager2 extends LayoutManager {
    void addLayoutComponent(Component component, Object obj);

    Dimension maximumLayoutSize(Container container);

    float getLayoutAlignmentX(Container container);

    float getLayoutAlignmentY(Container container);

    void invalidateLayout(Container container);
}
