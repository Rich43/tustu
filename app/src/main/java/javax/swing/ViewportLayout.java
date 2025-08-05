package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/ViewportLayout.class */
public class ViewportLayout implements LayoutManager, Serializable {
    static ViewportLayout SHARED_INSTANCE = new ViewportLayout();

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        Component view = ((JViewport) container).getView();
        if (view == 0) {
            return new Dimension(0, 0);
        }
        if (view instanceof Scrollable) {
            return ((Scrollable) view).getPreferredScrollableViewportSize();
        }
        return view.getPreferredSize();
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return new Dimension(4, 4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        JViewport jViewport = (JViewport) container;
        Component view = jViewport.getView();
        Scrollable scrollable = null;
        if (view == 0) {
            return;
        }
        if (view instanceof Scrollable) {
            scrollable = (Scrollable) view;
        }
        jViewport.getInsets();
        Dimension preferredSize = view.getPreferredSize();
        Dimension size = jViewport.getSize();
        Dimension viewCoordinates = jViewport.toViewCoordinates(size);
        Dimension dimension = new Dimension(preferredSize);
        if (scrollable != null) {
            if (scrollable.getScrollableTracksViewportWidth()) {
                dimension.width = size.width;
            }
            if (scrollable.getScrollableTracksViewportHeight()) {
                dimension.height = size.height;
            }
        }
        Point viewPosition = jViewport.getViewPosition();
        if (scrollable == null || jViewport.getParent() == null || jViewport.getParent().getComponentOrientation().isLeftToRight()) {
            if (viewPosition.f12370x + viewCoordinates.width > dimension.width) {
                viewPosition.f12370x = Math.max(0, dimension.width - viewCoordinates.width);
            }
        } else if (viewCoordinates.width > dimension.width) {
            viewPosition.f12370x = dimension.width - viewCoordinates.width;
        } else {
            viewPosition.f12370x = Math.max(0, Math.min(dimension.width - viewCoordinates.width, viewPosition.f12370x));
        }
        if (viewPosition.f12371y + viewCoordinates.height > dimension.height) {
            viewPosition.f12371y = Math.max(0, dimension.height - viewCoordinates.height);
        }
        if (scrollable == null) {
            if (viewPosition.f12370x == 0 && size.width > preferredSize.width) {
                dimension.width = size.width;
            }
            if (viewPosition.f12371y == 0 && size.height > preferredSize.height) {
                dimension.height = size.height;
            }
        }
        jViewport.setViewPosition(viewPosition);
        jViewport.setViewSize(dimension);
    }
}
