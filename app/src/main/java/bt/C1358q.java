package bt;

import com.efiAnalytics.ui.cQ;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: bt.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/q.class */
public class C1358q implements LayoutManager2, ContainerListener, FocusListener {

    /* renamed from: a, reason: collision with root package name */
    int f9107a;

    /* renamed from: b, reason: collision with root package name */
    int f9108b;

    /* renamed from: e, reason: collision with root package name */
    private int f9109e;

    /* renamed from: c, reason: collision with root package name */
    Container f9110c;

    /* renamed from: d, reason: collision with root package name */
    List f9111d;

    public C1358q(Container container, int i2) {
        this.f9108b = 2;
        this.f9109e = -1;
        this.f9110c = null;
        this.f9111d = new ArrayList();
        this.f9107a = i2;
        this.f9110c = container;
    }

    public C1358q(Container container) {
        this(container, 1);
    }

    public void a(cQ cQVar) {
        this.f9111d.add(cQVar);
    }

    private void b(int i2) {
        Iterator it = this.f9111d.iterator();
        while (it.hasNext()) {
            ((cQ) it.next()).a(i2);
        }
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = 0;
            int i3 = 0;
            int iA = a(container);
            int i4 = 0;
            while (i4 < componentCount) {
                Dimension preferredSize = container.getComponent(i4).getPreferredSize();
                if (i2 < preferredSize.width) {
                    i2 = preferredSize.width;
                }
                i3 = i4 == iA ? i3 + preferredSize.height : i3 + (preferredSize.height / this.f9108b);
                i4++;
            }
            dimension = new Dimension(insets.left + insets.right + i2, insets.top + insets.bottom + i3 + ((componentCount - 1) * this.f9107a));
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = 0;
            int i3 = 0;
            int iA = a(container);
            int i4 = 0;
            while (i4 < componentCount) {
                Dimension minimumSize = container.getComponent(i4).getMinimumSize();
                if (i2 < minimumSize.width) {
                    i2 = minimumSize.width;
                }
                i3 = i4 == iA ? i3 + minimumSize.height : i3 + (minimumSize.height / this.f9108b);
                i4++;
            }
            dimension = new Dimension(insets.left + insets.right + i2, insets.top + insets.bottom + i3 + ((componentCount - 1) * this.f9107a));
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            if (componentCount == 0) {
                return;
            }
            int width = container.getWidth() - (insets.left + insets.right);
            int height = (container.getHeight() - (insets.top + insets.bottom)) - (componentCount * this.f9107a);
            int i2 = height / (componentCount + this.f9108b);
            int i3 = height - (i2 * (componentCount - 1));
            int i4 = insets.top;
            int iA = a(container);
            if (iA == -1 || iA >= componentCount) {
                iA = (componentCount - 1) / 2;
            }
            for (int i5 = 0; i5 < componentCount; i5++) {
                Component component = container.getComponent(i5);
                if (i5 == iA) {
                    component.setBounds(insets.left, i4, width, 0 + i3);
                    i4 += this.f9107a + 0 + i3;
                } else {
                    component.setBounds(insets.left, i4, width, i2);
                    i4 += this.f9107a + i2;
                }
            }
        }
    }

    @Override // java.awt.LayoutManager2
    public void addLayoutComponent(Component component, Object obj) {
        component.addFocusListener(this);
        if (component instanceof Container) {
            Container container = (Container) component;
            container.addContainerListener(this);
            for (Component component2 : container.getComponents()) {
                addLayoutComponent(component2, (Object) null);
            }
        }
    }

    @Override // java.awt.LayoutManager2
    public Dimension maximumLayoutSize(Container container) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentX(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentY(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public void invalidateLayout(Container container) {
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f9110c.revalidate();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.ContainerListener
    public void componentAdded(ContainerEvent containerEvent) {
        if (containerEvent.getChild() != null) {
            containerEvent.getChild().addFocusListener(this);
            if (containerEvent.getChild() instanceof Container) {
                Container container = (Container) containerEvent.getChild();
                if (a(container.getContainerListeners())) {
                    return;
                }
                container.addContainerListener(this);
            }
        }
    }

    private boolean a(ContainerListener[] containerListenerArr) {
        for (ContainerListener containerListener : containerListenerArr) {
            if (containerListener.equals(this)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.awt.event.ContainerListener
    public void componentRemoved(ContainerEvent containerEvent) {
    }

    private int a(Container container) {
        int i2 = 0;
        while (true) {
            if (i2 >= container.getComponentCount()) {
                break;
            }
            if (container.getComponent(i2).hasFocus()) {
                a(i2);
                break;
            }
            i2++;
        }
        return this.f9109e;
    }

    public void a(int i2) {
        if (this.f9109e != i2) {
            b(i2);
            this.f9109e = i2;
        }
    }
}
