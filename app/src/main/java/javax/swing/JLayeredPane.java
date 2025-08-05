package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/JLayeredPane.class */
public class JLayeredPane extends JComponent implements Accessible {
    public static final Integer DEFAULT_LAYER = new Integer(0);
    public static final Integer PALETTE_LAYER = new Integer(100);
    public static final Integer MODAL_LAYER = new Integer(200);
    public static final Integer POPUP_LAYER = new Integer(300);
    public static final Integer DRAG_LAYER = new Integer(400);
    public static final Integer FRAME_CONTENT_LAYER = new Integer(-30000);
    public static final String LAYER_PROPERTY = "layeredContainerLayer";
    private Hashtable<Component, Integer> componentToLayer;
    private boolean optimizedDrawingPossible = true;

    public JLayeredPane() {
        setLayout(null);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x004b A[PHI: r6
  0x004b: PHI (r6v1 java.lang.Integer) = (r6v0 java.lang.Integer), (r6v2 java.lang.Integer) binds: [B:8:0x002e, B:12:0x0048] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void validateOptimizedDrawing() {
        /*
            r3 = this;
            r0 = 0
            r4 = r0
            r0 = r3
            java.lang.Object r0 = r0.getTreeLock()
            r1 = r0
            r5 = r1
            monitor-enter(r0)
            r0 = r3
            java.awt.Component[] r0 = r0.getComponents()     // Catch: java.lang.Throwable -> L6c
            r7 = r0
            r0 = r7
            int r0 = r0.length     // Catch: java.lang.Throwable -> L6c
            r8 = r0
            r0 = 0
            r9 = r0
        L17:
            r0 = r9
            r1 = r8
            if (r0 >= r1) goto L67
            r0 = r7
            r1 = r9
            r0 = r0[r1]     // Catch: java.lang.Throwable -> L6c
            r10 = r0
            r0 = 0
            r6 = r0
            r0 = r10
            java.lang.String r1 = "javax.swing.JInternalFrame"
            boolean r0 = sun.awt.SunToolkit.isInstanceOf(r0, r1)     // Catch: java.lang.Throwable -> L6c
            if (r0 != 0) goto L4b
            r0 = r10
            boolean r0 = r0 instanceof javax.swing.JComponent     // Catch: java.lang.Throwable -> L6c
            if (r0 == 0) goto L61
            r0 = r10
            javax.swing.JComponent r0 = (javax.swing.JComponent) r0     // Catch: java.lang.Throwable -> L6c
            java.lang.String r1 = "layeredContainerLayer"
            java.lang.Object r0 = r0.getClientProperty(r1)     // Catch: java.lang.Throwable -> L6c
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch: java.lang.Throwable -> L6c
            r1 = r0
            r6 = r1
            if (r0 == 0) goto L61
        L4b:
            r0 = r6
            if (r0 == 0) goto L5c
            r0 = r6
            java.lang.Integer r1 = javax.swing.JLayeredPane.FRAME_CONTENT_LAYER     // Catch: java.lang.Throwable -> L6c
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L6c
            if (r0 == 0) goto L5c
            goto L61
        L5c:
            r0 = 1
            r4 = r0
            goto L67
        L61:
            int r9 = r9 + 1
            goto L17
        L67:
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L6c
            goto L73
        L6c:
            r11 = move-exception
            r0 = r5
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L6c
            r0 = r11
            throw r0
        L73:
            r0 = r4
            if (r0 == 0) goto L7f
            r0 = r3
            r1 = 0
            r0.optimizedDrawingPossible = r1
            goto L84
        L7f:
            r0 = r3
            r1 = 1
            r0.optimizedDrawingPossible = r1
        L84:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.JLayeredPane.validateOptimizedDrawing():void");
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        int layer;
        if (obj instanceof Integer) {
            layer = ((Integer) obj).intValue();
            setLayer(component, layer);
        } else {
            layer = getLayer(component);
        }
        super.addImpl(component, obj, insertIndexForLayer(layer, i2));
        component.validate();
        component.repaint();
        validateOptimizedDrawing();
    }

    @Override // java.awt.Container
    public void remove(int i2) {
        Component component = getComponent(i2);
        super.remove(i2);
        if (component != null && !(component instanceof JComponent)) {
            getComponentToLayer().remove(component);
        }
        validateOptimizedDrawing();
    }

    @Override // java.awt.Container
    public void removeAll() {
        Component[] components = getComponents();
        Hashtable<Component, Integer> componentToLayer = getComponentToLayer();
        for (int length = components.length - 1; length >= 0; length--) {
            Component component = components[length];
            if (component != null && !(component instanceof JComponent)) {
                componentToLayer.remove(component);
            }
        }
        super.removeAll();
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return this.optimizedDrawingPossible;
    }

    public static void putLayer(JComponent jComponent, int i2) {
        jComponent.putClientProperty(LAYER_PROPERTY, new Integer(i2));
    }

    public static int getLayer(JComponent jComponent) {
        Integer num = (Integer) jComponent.getClientProperty(LAYER_PROPERTY);
        if (num != null) {
            return num.intValue();
        }
        return DEFAULT_LAYER.intValue();
    }

    public static JLayeredPane getLayeredPaneAbove(Component component) {
        Container container;
        if (component == null) {
            return null;
        }
        Container parent = component.getParent();
        while (true) {
            container = parent;
            if (container == null || (container instanceof JLayeredPane)) {
                break;
            }
            parent = container.getParent();
        }
        return (JLayeredPane) container;
    }

    public void setLayer(Component component, int i2) {
        setLayer(component, i2, -1);
    }

    public void setLayer(Component component, int i2, int i3) {
        Integer objectForLayer = getObjectForLayer(i2);
        if (i2 == getLayer(component) && i3 == getPosition(component)) {
            repaint(component.getBounds());
            return;
        }
        if (component instanceof JComponent) {
            ((JComponent) component).putClientProperty(LAYER_PROPERTY, objectForLayer);
        } else {
            getComponentToLayer().put(component, objectForLayer);
        }
        if (component.getParent() == null || component.getParent() != this) {
            repaint(component.getBounds());
        } else {
            setComponentZOrder(component, insertIndexForLayer(component, i2, i3));
            repaint(component.getBounds());
        }
    }

    public int getLayer(Component component) {
        Integer num;
        if (component instanceof JComponent) {
            num = (Integer) ((JComponent) component).getClientProperty(LAYER_PROPERTY);
        } else {
            num = getComponentToLayer().get(component);
        }
        if (num == null) {
            return DEFAULT_LAYER.intValue();
        }
        return num.intValue();
    }

    public int getIndexOf(Component component) {
        int componentCount = getComponentCount();
        for (int i2 = 0; i2 < componentCount; i2++) {
            if (component == getComponent(i2)) {
                return i2;
            }
        }
        return -1;
    }

    public void moveToFront(Component component) {
        setPosition(component, 0);
    }

    public void moveToBack(Component component) {
        setPosition(component, -1);
    }

    public void setPosition(Component component, int i2) {
        setLayer(component, getLayer(component), i2);
    }

    public int getPosition(Component component) {
        int i2 = 0;
        getComponentCount();
        int indexOf = getIndexOf(component);
        if (indexOf == -1) {
            return -1;
        }
        int layer = getLayer(component);
        for (int i3 = indexOf - 1; i3 >= 0; i3--) {
            if (getLayer(getComponent(i3)) == layer) {
                i2++;
            } else {
                return i2;
            }
        }
        return i2;
    }

    public int highestLayer() {
        if (getComponentCount() > 0) {
            return getLayer(getComponent(0));
        }
        return 0;
    }

    public int lowestLayer() {
        int componentCount = getComponentCount();
        if (componentCount > 0) {
            return getLayer(getComponent(componentCount - 1));
        }
        return 0;
    }

    public int getComponentCountInLayer(int i2) {
        int i3 = 0;
        int componentCount = getComponentCount();
        for (int i4 = 0; i4 < componentCount; i4++) {
            int layer = getLayer(getComponent(i4));
            if (layer == i2) {
                i3++;
            } else if (i3 > 0 || layer < i2) {
                break;
            }
        }
        return i3;
    }

    public Component[] getComponentsInLayer(int i2) {
        int i3 = 0;
        Component[] componentArr = new Component[getComponentCountInLayer(i2)];
        int componentCount = getComponentCount();
        for (int i4 = 0; i4 < componentCount; i4++) {
            int layer = getLayer(getComponent(i4));
            if (layer == i2) {
                int i5 = i3;
                i3++;
                componentArr[i5] = getComponent(i4);
            } else if (i3 > 0 || layer < i2) {
                break;
            }
        }
        return componentArr;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (isOpaque()) {
            Rectangle clipBounds = graphics.getClipBounds();
            Color background = getBackground();
            if (background == null) {
                background = Color.lightGray;
            }
            graphics.setColor(background);
            if (clipBounds != null) {
                graphics.fillRect(clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height);
            } else {
                graphics.fillRect(0, 0, getWidth(), getHeight());
            }
        }
        super.paint(graphics);
    }

    protected Hashtable<Component, Integer> getComponentToLayer() {
        if (this.componentToLayer == null) {
            this.componentToLayer = new Hashtable<>(4);
        }
        return this.componentToLayer;
    }

    protected Integer getObjectForLayer(int i2) {
        Integer num;
        switch (i2) {
            case 0:
                num = DEFAULT_LAYER;
                break;
            case 100:
                num = PALETTE_LAYER;
                break;
            case 200:
                num = MODAL_LAYER;
                break;
            case 300:
                num = POPUP_LAYER;
                break;
            case 400:
                num = DRAG_LAYER;
                break;
            default:
                num = new Integer(i2);
                break;
        }
        return num;
    }

    protected int insertIndexForLayer(int i2, int i3) {
        return insertIndexForLayer(null, i2, i3);
    }

    private int insertIndexForLayer(Component component, int i2, int i3) {
        int i4 = -1;
        int i5 = -1;
        int componentCount = getComponentCount();
        ArrayList arrayList = new ArrayList(componentCount);
        for (int i6 = 0; i6 < componentCount; i6++) {
            if (getComponent(i6) != component) {
                arrayList.add(getComponent(i6));
            }
        }
        int size = arrayList.size();
        int i7 = 0;
        while (true) {
            if (i7 >= size) {
                break;
            }
            int layer = getLayer((Component) arrayList.get(i7));
            if (i4 == -1 && layer == i2) {
                i4 = i7;
            }
            if (layer >= i2) {
                i7++;
            } else if (i7 == 0) {
                i4 = 0;
                i5 = 0;
            } else {
                i5 = i7;
            }
        }
        if (i4 == -1 && i5 == -1) {
            return size;
        }
        if (i4 != -1 && i5 == -1) {
            i5 = size;
        }
        if (i5 != -1 && i4 == -1) {
            i4 = i5;
        }
        if (i3 == -1) {
            return i5;
        }
        if (i3 > -1 && i4 + i3 <= i5) {
            return i4 + i3;
        }
        return i5;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",optimizedDrawingPossible=" + (this.optimizedDrawingPossible ? "true" : "false");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJLayeredPane();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JLayeredPane$AccessibleJLayeredPane.class */
    protected class AccessibleJLayeredPane extends JComponent.AccessibleJComponent {
        protected AccessibleJLayeredPane() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.LAYERED_PANE;
        }
    }
}
