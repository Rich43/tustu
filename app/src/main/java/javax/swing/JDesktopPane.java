package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DesktopPaneUI;

/* loaded from: rt.jar:javax/swing/JDesktopPane.class */
public class JDesktopPane extends JLayeredPane implements Accessible {
    private static final String uiClassID = "DesktopPaneUI";
    transient DesktopManager desktopManager;
    public static final int LIVE_DRAG_MODE = 0;
    public static final int OUTLINE_DRAG_MODE = 1;
    private transient List<JInternalFrame> framesCache;
    private transient JInternalFrame selectedFrame = null;
    private int dragMode = 0;
    private boolean dragModeSet = false;
    private boolean componentOrderCheckingEnabled = true;
    private boolean componentOrderChanged = false;

    public JDesktopPane() {
        setUIProperty("opaque", Boolean.TRUE);
        setFocusCycleRoot(true);
        setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() { // from class: javax.swing.JDesktopPane.1
            @Override // javax.swing.SortingFocusTraversalPolicy, java.awt.FocusTraversalPolicy
            public Component getDefaultComponent(Container container) {
                Component defaultComponent = null;
                for (Container container2 : JDesktopPane.this.getAllFrames()) {
                    defaultComponent = container2.getFocusTraversalPolicy().getDefaultComponent(container2);
                    if (defaultComponent != null) {
                        break;
                    }
                }
                return defaultComponent;
            }
        });
        updateUI();
    }

    public DesktopPaneUI getUI() {
        return (DesktopPaneUI) this.ui;
    }

    public void setUI(DesktopPaneUI desktopPaneUI) {
        super.setUI((ComponentUI) desktopPaneUI);
    }

    public void setDragMode(int i2) {
        int i3 = this.dragMode;
        this.dragMode = i2;
        firePropertyChange("dragMode", i3, this.dragMode);
        this.dragModeSet = true;
    }

    public int getDragMode() {
        return this.dragMode;
    }

    public DesktopManager getDesktopManager() {
        return this.desktopManager;
    }

    public void setDesktopManager(DesktopManager desktopManager) {
        DesktopManager desktopManager2 = this.desktopManager;
        this.desktopManager = desktopManager;
        firePropertyChange("desktopManager", desktopManager2, this.desktopManager);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((DesktopPaneUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public JInternalFrame[] getAllFrames() {
        return (JInternalFrame[]) getAllFrames(this).toArray(new JInternalFrame[0]);
    }

    private static Collection<JInternalFrame> getAllFrames(Container container) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        int componentCount = container.getComponentCount();
        for (int i2 = 0; i2 < componentCount; i2++) {
            Component component = container.getComponent(i2);
            if (component instanceof JInternalFrame) {
                linkedHashSet.add((JInternalFrame) component);
            } else if (component instanceof JInternalFrame.JDesktopIcon) {
                JInternalFrame internalFrame = ((JInternalFrame.JDesktopIcon) component).getInternalFrame();
                if (internalFrame != null) {
                    linkedHashSet.add(internalFrame);
                }
            } else if (component instanceof Container) {
                linkedHashSet.addAll(getAllFrames((Container) component));
            }
        }
        return linkedHashSet;
    }

    public JInternalFrame getSelectedFrame() {
        return this.selectedFrame;
    }

    public void setSelectedFrame(JInternalFrame jInternalFrame) {
        this.selectedFrame = jInternalFrame;
    }

    public JInternalFrame[] getAllFramesInLayer(int i2) {
        Collection<JInternalFrame> allFrames = getAllFrames(this);
        Iterator<JInternalFrame> it = allFrames.iterator();
        while (it.hasNext()) {
            if (it.next().getLayer() != i2) {
                it.remove();
            }
        }
        return (JInternalFrame[]) allFrames.toArray(new JInternalFrame[0]);
    }

    private List<JInternalFrame> getFrames() {
        TreeSet treeSet = new TreeSet();
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            Component component = getComponent(i2);
            if (component instanceof JInternalFrame) {
                treeSet.add(new ComponentPosition((JInternalFrame) component, getLayer(component), i2));
            } else if (component instanceof JInternalFrame.JDesktopIcon) {
                JInternalFrame internalFrame = ((JInternalFrame.JDesktopIcon) component).getInternalFrame();
                treeSet.add(new ComponentPosition(internalFrame, getLayer((Component) internalFrame), i2));
            }
        }
        ArrayList arrayList = new ArrayList(treeSet.size());
        Iterator<E> it = treeSet.iterator();
        while (it.hasNext()) {
            arrayList.add(((ComponentPosition) it.next()).component);
        }
        return arrayList;
    }

    /* loaded from: rt.jar:javax/swing/JDesktopPane$ComponentPosition.class */
    private static class ComponentPosition implements Comparable<ComponentPosition> {
        private final JInternalFrame component;
        private final int layer;
        private final int zOrder;

        ComponentPosition(JInternalFrame jInternalFrame, int i2, int i3) {
            this.component = jInternalFrame;
            this.layer = i2;
            this.zOrder = i3;
        }

        @Override // java.lang.Comparable
        public int compareTo(ComponentPosition componentPosition) {
            int i2 = componentPosition.layer - this.layer;
            if (i2 == 0) {
                return this.zOrder - componentPosition.zOrder;
            }
            return i2;
        }
    }

    private JInternalFrame getNextFrame(JInternalFrame jInternalFrame, boolean z2) {
        int size;
        verifyFramesCache();
        if (jInternalFrame == null) {
            return getTopInternalFrame();
        }
        int iIndexOf = this.framesCache.indexOf(jInternalFrame);
        if (iIndexOf == -1 || this.framesCache.size() == 1) {
            return null;
        }
        if (z2) {
            size = iIndexOf + 1;
            if (size == this.framesCache.size()) {
                size = 0;
            }
        } else {
            size = iIndexOf - 1;
            if (size == -1) {
                size = this.framesCache.size() - 1;
            }
        }
        return this.framesCache.get(size);
    }

    JInternalFrame getNextFrame(JInternalFrame jInternalFrame) {
        return getNextFrame(jInternalFrame, true);
    }

    private JInternalFrame getTopInternalFrame() {
        if (this.framesCache.size() == 0) {
            return null;
        }
        return this.framesCache.get(0);
    }

    private void updateFramesCache() {
        this.framesCache = getFrames();
    }

    private void verifyFramesCache() {
        if (this.componentOrderChanged) {
            this.componentOrderChanged = false;
            updateFramesCache();
        }
    }

    @Override // java.awt.Container
    public void remove(Component component) {
        super.remove(component);
        updateFramesCache();
    }

    public JInternalFrame selectFrame(boolean z2) {
        JInternalFrame selectedFrame = getSelectedFrame();
        JInternalFrame nextFrame = getNextFrame(selectedFrame, z2);
        if (nextFrame == null) {
            return null;
        }
        setComponentOrderCheckingEnabled(false);
        if (z2 && selectedFrame != null) {
            selectedFrame.moveToBack();
        }
        try {
            nextFrame.setSelected(true);
        } catch (PropertyVetoException e2) {
        }
        setComponentOrderCheckingEnabled(true);
        return nextFrame;
    }

    void setComponentOrderCheckingEnabled(boolean z2) {
        this.componentOrderCheckingEnabled = z2;
    }

    @Override // javax.swing.JLayeredPane, java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        super.addImpl(component, obj, i2);
        if (this.componentOrderCheckingEnabled) {
            if ((component instanceof JInternalFrame) || (component instanceof JInternalFrame.JDesktopIcon)) {
                this.componentOrderChanged = true;
            }
        }
    }

    @Override // javax.swing.JLayeredPane, java.awt.Container
    public void remove(int i2) {
        if (this.componentOrderCheckingEnabled) {
            Component component = getComponent(i2);
            if ((component instanceof JInternalFrame) || (component instanceof JInternalFrame.JDesktopIcon)) {
                this.componentOrderChanged = true;
            }
        }
        super.remove(i2);
    }

    @Override // javax.swing.JLayeredPane, java.awt.Container
    public void removeAll() {
        if (this.componentOrderCheckingEnabled) {
            int componentCount = getComponentCount();
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component component = getComponent(i2);
                if ((component instanceof JInternalFrame) || (component instanceof JInternalFrame.JDesktopIcon)) {
                    this.componentOrderChanged = true;
                    break;
                }
            }
        }
        super.removeAll();
    }

    @Override // java.awt.Container
    public void setComponentZOrder(Component component, int i2) {
        super.setComponentZOrder(component, i2);
        if (this.componentOrderCheckingEnabled) {
            if ((component instanceof JInternalFrame) || (component instanceof JInternalFrame.JDesktopIcon)) {
                this.componentOrderChanged = true;
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.JComponent
    void setUIProperty(String str, Object obj) {
        if (str == "dragMode") {
            if (!this.dragModeSet) {
                setDragMode(((Integer) obj).intValue());
                this.dragModeSet = false;
                return;
            }
            return;
        }
        super.setUIProperty(str, obj);
    }

    @Override // javax.swing.JLayeredPane, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",desktopManager=" + (this.desktopManager != null ? this.desktopManager.toString() : "");
    }

    @Override // javax.swing.JLayeredPane, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJDesktopPane();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JDesktopPane$AccessibleJDesktopPane.class */
    protected class AccessibleJDesktopPane extends JComponent.AccessibleJComponent {
        protected AccessibleJDesktopPane() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.DESKTOP_PANE;
        }
    }
}
