package javax.swing;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRelation;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ScrollPaneUI;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:javax/swing/JScrollPane.class */
public class JScrollPane extends JComponent implements ScrollPaneConstants, Accessible {
    private Border viewportBorder;
    private static final String uiClassID = "ScrollPaneUI";
    protected int verticalScrollBarPolicy;
    protected int horizontalScrollBarPolicy;
    protected JViewport viewport;
    protected JScrollBar verticalScrollBar;
    protected JScrollBar horizontalScrollBar;
    protected JViewport rowHeader;
    protected JViewport columnHeader;
    protected Component lowerLeft;
    protected Component lowerRight;
    protected Component upperLeft;
    protected Component upperRight;
    private boolean wheelScrollState;

    public JScrollPane(Component component, int i2, int i3) {
        this.verticalScrollBarPolicy = 20;
        this.horizontalScrollBarPolicy = 30;
        this.wheelScrollState = true;
        setLayout(new ScrollPaneLayout.UIResource());
        setVerticalScrollBarPolicy(i2);
        setHorizontalScrollBarPolicy(i3);
        setViewport(createViewport());
        setVerticalScrollBar(createVerticalScrollBar());
        setHorizontalScrollBar(createHorizontalScrollBar());
        if (component != null) {
            setViewportView(component);
        }
        setUIProperty("opaque", true);
        updateUI();
        if (!getComponentOrientation().isLeftToRight()) {
            this.viewport.setViewPosition(new Point(Integer.MAX_VALUE, 0));
        }
    }

    public JScrollPane(Component component) {
        this(component, 20, 30);
    }

    public JScrollPane(int i2, int i3) {
        this(null, i2, i3);
    }

    public JScrollPane() {
        this(null, 20, 30);
    }

    public ScrollPaneUI getUI() {
        return (ScrollPaneUI) this.ui;
    }

    public void setUI(ScrollPaneUI scrollPaneUI) {
        super.setUI((ComponentUI) scrollPaneUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((ScrollPaneUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // java.awt.Container
    public void setLayout(LayoutManager layoutManager) {
        if (layoutManager instanceof ScrollPaneLayout) {
            super.setLayout(layoutManager);
            ((ScrollPaneLayout) layoutManager).syncWithScrollPane(this);
        } else {
            if (layoutManager == null) {
                super.setLayout(layoutManager);
                return;
            }
            throw new ClassCastException("layout of JScrollPane must be a ScrollPaneLayout");
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public boolean isValidateRoot() {
        return true;
    }

    public int getVerticalScrollBarPolicy() {
        return this.verticalScrollBarPolicy;
    }

    public void setVerticalScrollBarPolicy(int i2) {
        switch (i2) {
            case 20:
            case 21:
            case 22:
                int i3 = this.verticalScrollBarPolicy;
                this.verticalScrollBarPolicy = i2;
                firePropertyChange("verticalScrollBarPolicy", i3, i2);
                revalidate();
                repaint();
                return;
            default:
                throw new IllegalArgumentException("invalid verticalScrollBarPolicy");
        }
    }

    public int getHorizontalScrollBarPolicy() {
        return this.horizontalScrollBarPolicy;
    }

    public void setHorizontalScrollBarPolicy(int i2) {
        switch (i2) {
            case 30:
            case 31:
            case 32:
                int i3 = this.horizontalScrollBarPolicy;
                this.horizontalScrollBarPolicy = i2;
                firePropertyChange("horizontalScrollBarPolicy", i3, i2);
                revalidate();
                repaint();
                return;
            default:
                throw new IllegalArgumentException("invalid horizontalScrollBarPolicy");
        }
    }

    public Border getViewportBorder() {
        return this.viewportBorder;
    }

    public void setViewportBorder(Border border) {
        Border border2 = this.viewportBorder;
        this.viewportBorder = border;
        firePropertyChange("viewportBorder", border2, border);
    }

    public Rectangle getViewportBorderBounds() {
        Rectangle rectangle = new Rectangle(getSize());
        Insets insets = getInsets();
        rectangle.f12372x = insets.left;
        rectangle.f12373y = insets.top;
        rectangle.width -= insets.left + insets.right;
        rectangle.height -= insets.top + insets.bottom;
        boolean zIsLeftToRight = SwingUtilities.isLeftToRight(this);
        JViewport columnHeader = getColumnHeader();
        if (columnHeader != null && columnHeader.isVisible()) {
            int height = columnHeader.getHeight();
            rectangle.f12373y += height;
            rectangle.height -= height;
        }
        JViewport rowHeader = getRowHeader();
        if (rowHeader != null && rowHeader.isVisible()) {
            int width = rowHeader.getWidth();
            if (zIsLeftToRight) {
                rectangle.f12372x += width;
            }
            rectangle.width -= width;
        }
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        if (verticalScrollBar != null && verticalScrollBar.isVisible()) {
            int width2 = verticalScrollBar.getWidth();
            if (!zIsLeftToRight) {
                rectangle.f12372x += width2;
            }
            rectangle.width -= width2;
        }
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        if (horizontalScrollBar != null && horizontalScrollBar.isVisible()) {
            rectangle.height -= horizontalScrollBar.getHeight();
        }
        return rectangle;
    }

    /* loaded from: rt.jar:javax/swing/JScrollPane$ScrollBar.class */
    protected class ScrollBar extends JScrollBar implements UIResource {
        private boolean unitIncrementSet;
        private boolean blockIncrementSet;

        public ScrollBar(int i2) {
            super(i2);
            putClientProperty("JScrollBar.fastWheelScrolling", Boolean.TRUE);
        }

        @Override // javax.swing.JScrollBar, java.awt.Adjustable
        public void setUnitIncrement(int i2) {
            this.unitIncrementSet = true;
            putClientProperty("JScrollBar.fastWheelScrolling", null);
            super.setUnitIncrement(i2);
        }

        @Override // javax.swing.JScrollBar
        public int getUnitIncrement(int i2) {
            JViewport viewport = JScrollPane.this.getViewport();
            if (!this.unitIncrementSet && viewport != null && (viewport.getView() instanceof Scrollable)) {
                return ((Scrollable) viewport.getView()).getScrollableUnitIncrement(viewport.getViewRect(), getOrientation(), i2);
            }
            return super.getUnitIncrement(i2);
        }

        @Override // javax.swing.JScrollBar, java.awt.Adjustable
        public void setBlockIncrement(int i2) {
            this.blockIncrementSet = true;
            putClientProperty("JScrollBar.fastWheelScrolling", null);
            super.setBlockIncrement(i2);
        }

        @Override // javax.swing.JScrollBar
        public int getBlockIncrement(int i2) {
            JViewport viewport = JScrollPane.this.getViewport();
            if (this.blockIncrementSet || viewport == null) {
                return super.getBlockIncrement(i2);
            }
            if (viewport.getView() instanceof Scrollable) {
                return ((Scrollable) viewport.getView()).getScrollableBlockIncrement(viewport.getViewRect(), getOrientation(), i2);
            }
            if (getOrientation() == 1) {
                return viewport.getExtentSize().height;
            }
            return viewport.getExtentSize().width;
        }
    }

    public JScrollBar createHorizontalScrollBar() {
        return new ScrollBar(0);
    }

    @Transient
    public JScrollBar getHorizontalScrollBar() {
        return this.horizontalScrollBar;
    }

    public void setHorizontalScrollBar(JScrollBar jScrollBar) {
        JScrollBar horizontalScrollBar = getHorizontalScrollBar();
        this.horizontalScrollBar = jScrollBar;
        if (jScrollBar != null) {
            add(jScrollBar, ScrollPaneConstants.HORIZONTAL_SCROLLBAR);
        } else if (horizontalScrollBar != null) {
            remove(horizontalScrollBar);
        }
        firePropertyChange("horizontalScrollBar", horizontalScrollBar, jScrollBar);
        revalidate();
        repaint();
    }

    public JScrollBar createVerticalScrollBar() {
        return new ScrollBar(1);
    }

    @Transient
    public JScrollBar getVerticalScrollBar() {
        return this.verticalScrollBar;
    }

    public void setVerticalScrollBar(JScrollBar jScrollBar) {
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        this.verticalScrollBar = jScrollBar;
        add(jScrollBar, ScrollPaneConstants.VERTICAL_SCROLLBAR);
        firePropertyChange("verticalScrollBar", verticalScrollBar, jScrollBar);
        revalidate();
        repaint();
    }

    protected JViewport createViewport() {
        return new JViewport();
    }

    public JViewport getViewport() {
        return this.viewport;
    }

    public void setViewport(JViewport jViewport) {
        JViewport viewport = getViewport();
        this.viewport = jViewport;
        if (jViewport != null) {
            add(jViewport, ScrollPaneConstants.VIEWPORT);
        } else if (viewport != null) {
            remove(viewport);
        }
        firePropertyChange("viewport", viewport, jViewport);
        if (this.accessibleContext != null) {
            ((AccessibleJScrollPane) this.accessibleContext).resetViewPort();
        }
        revalidate();
        repaint();
    }

    public void setViewportView(Component component) {
        if (getViewport() == null) {
            setViewport(createViewport());
        }
        getViewport().setView(component);
    }

    @Transient
    public JViewport getRowHeader() {
        return this.rowHeader;
    }

    public void setRowHeader(JViewport jViewport) {
        JViewport rowHeader = getRowHeader();
        this.rowHeader = jViewport;
        if (jViewport != null) {
            add(jViewport, ScrollPaneConstants.ROW_HEADER);
        } else if (rowHeader != null) {
            remove(rowHeader);
        }
        firePropertyChange("rowHeader", rowHeader, jViewport);
        revalidate();
        repaint();
    }

    public void setRowHeaderView(Component component) {
        if (getRowHeader() == null) {
            setRowHeader(createViewport());
        }
        getRowHeader().setView(component);
    }

    @Transient
    public JViewport getColumnHeader() {
        return this.columnHeader;
    }

    public void setColumnHeader(JViewport jViewport) {
        JViewport columnHeader = getColumnHeader();
        this.columnHeader = jViewport;
        if (jViewport != null) {
            add(jViewport, ScrollPaneConstants.COLUMN_HEADER);
        } else if (columnHeader != null) {
            remove(columnHeader);
        }
        firePropertyChange("columnHeader", columnHeader, jViewport);
        revalidate();
        repaint();
    }

    public void setColumnHeaderView(Component component) {
        if (getColumnHeader() == null) {
            setColumnHeader(createViewport());
        }
        getColumnHeader().setView(component);
    }

    public Component getCorner(String str) {
        boolean zIsLeftToRight = getComponentOrientation().isLeftToRight();
        if (str.equals(ScrollPaneConstants.LOWER_LEADING_CORNER)) {
            str = zIsLeftToRight ? ScrollPaneConstants.LOWER_LEFT_CORNER : ScrollPaneConstants.LOWER_RIGHT_CORNER;
        } else if (str.equals(ScrollPaneConstants.LOWER_TRAILING_CORNER)) {
            str = zIsLeftToRight ? ScrollPaneConstants.LOWER_RIGHT_CORNER : ScrollPaneConstants.LOWER_LEFT_CORNER;
        } else if (str.equals(ScrollPaneConstants.UPPER_LEADING_CORNER)) {
            str = zIsLeftToRight ? ScrollPaneConstants.UPPER_LEFT_CORNER : ScrollPaneConstants.UPPER_RIGHT_CORNER;
        } else if (str.equals(ScrollPaneConstants.UPPER_TRAILING_CORNER)) {
            str = zIsLeftToRight ? ScrollPaneConstants.UPPER_RIGHT_CORNER : ScrollPaneConstants.UPPER_LEFT_CORNER;
        }
        if (str.equals(ScrollPaneConstants.LOWER_LEFT_CORNER)) {
            return this.lowerLeft;
        }
        if (str.equals(ScrollPaneConstants.LOWER_RIGHT_CORNER)) {
            return this.lowerRight;
        }
        if (str.equals(ScrollPaneConstants.UPPER_LEFT_CORNER)) {
            return this.upperLeft;
        }
        if (str.equals(ScrollPaneConstants.UPPER_RIGHT_CORNER)) {
            return this.upperRight;
        }
        return null;
    }

    public void setCorner(String str, Component component) {
        Component component2;
        boolean zIsLeftToRight = getComponentOrientation().isLeftToRight();
        if (str.equals(ScrollPaneConstants.LOWER_LEADING_CORNER)) {
            str = zIsLeftToRight ? ScrollPaneConstants.LOWER_LEFT_CORNER : ScrollPaneConstants.LOWER_RIGHT_CORNER;
        } else if (str.equals(ScrollPaneConstants.LOWER_TRAILING_CORNER)) {
            str = zIsLeftToRight ? ScrollPaneConstants.LOWER_RIGHT_CORNER : ScrollPaneConstants.LOWER_LEFT_CORNER;
        } else if (str.equals(ScrollPaneConstants.UPPER_LEADING_CORNER)) {
            str = zIsLeftToRight ? ScrollPaneConstants.UPPER_LEFT_CORNER : ScrollPaneConstants.UPPER_RIGHT_CORNER;
        } else if (str.equals(ScrollPaneConstants.UPPER_TRAILING_CORNER)) {
            str = zIsLeftToRight ? ScrollPaneConstants.UPPER_RIGHT_CORNER : ScrollPaneConstants.UPPER_LEFT_CORNER;
        }
        if (str.equals(ScrollPaneConstants.LOWER_LEFT_CORNER)) {
            component2 = this.lowerLeft;
            this.lowerLeft = component;
        } else if (str.equals(ScrollPaneConstants.LOWER_RIGHT_CORNER)) {
            component2 = this.lowerRight;
            this.lowerRight = component;
        } else if (str.equals(ScrollPaneConstants.UPPER_LEFT_CORNER)) {
            component2 = this.upperLeft;
            this.upperLeft = component;
        } else if (str.equals(ScrollPaneConstants.UPPER_RIGHT_CORNER)) {
            component2 = this.upperRight;
            this.upperRight = component;
        } else {
            throw new IllegalArgumentException("invalid corner key");
        }
        if (component2 != null) {
            remove(component2);
        }
        if (component != null) {
            add(component, str);
        }
        firePropertyChange(str, component2, component);
        revalidate();
        repaint();
    }

    @Override // java.awt.Component
    public void setComponentOrientation(ComponentOrientation componentOrientation) {
        super.setComponentOrientation(componentOrientation);
        if (this.verticalScrollBar != null) {
            this.verticalScrollBar.setComponentOrientation(componentOrientation);
        }
        if (this.horizontalScrollBar != null) {
            this.horizontalScrollBar.setComponentOrientation(componentOrientation);
        }
    }

    public boolean isWheelScrollingEnabled() {
        return this.wheelScrollState;
    }

    public void setWheelScrollingEnabled(boolean z2) {
        boolean z3 = this.wheelScrollState;
        this.wheelScrollState = z2;
        firePropertyChange("wheelScrollingEnabled", z3, z2);
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

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str;
        String str2;
        String string = this.viewportBorder != null ? this.viewportBorder.toString() : "";
        String string2 = this.viewport != null ? this.viewport.toString() : "";
        if (this.verticalScrollBarPolicy == 20) {
            str = "VERTICAL_SCROLLBAR_AS_NEEDED";
        } else if (this.verticalScrollBarPolicy == 21) {
            str = "VERTICAL_SCROLLBAR_NEVER";
        } else if (this.verticalScrollBarPolicy == 22) {
            str = "VERTICAL_SCROLLBAR_ALWAYS";
        } else {
            str = "";
        }
        if (this.horizontalScrollBarPolicy == 30) {
            str2 = "HORIZONTAL_SCROLLBAR_AS_NEEDED";
        } else if (this.horizontalScrollBarPolicy == 31) {
            str2 = "HORIZONTAL_SCROLLBAR_NEVER";
        } else if (this.horizontalScrollBarPolicy == 32) {
            str2 = "HORIZONTAL_SCROLLBAR_ALWAYS";
        } else {
            str2 = "";
        }
        String string3 = this.horizontalScrollBar != null ? this.horizontalScrollBar.toString() : "";
        String string4 = this.verticalScrollBar != null ? this.verticalScrollBar.toString() : "";
        String string5 = this.columnHeader != null ? this.columnHeader.toString() : "";
        String string6 = this.rowHeader != null ? this.rowHeader.toString() : "";
        return super.paramString() + ",columnHeader=" + string5 + ",horizontalScrollBar=" + string3 + ",horizontalScrollBarPolicy=" + str2 + ",lowerLeft=" + (this.lowerLeft != null ? this.lowerLeft.toString() : "") + ",lowerRight=" + (this.lowerRight != null ? this.lowerRight.toString() : "") + ",rowHeader=" + string6 + ",upperLeft=" + (this.upperLeft != null ? this.upperLeft.toString() : "") + ",upperRight=" + (this.upperRight != null ? this.upperRight.toString() : "") + ",verticalScrollBar=" + string4 + ",verticalScrollBarPolicy=" + str + ",viewport=" + string2 + ",viewportBorder=" + string;
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJScrollPane();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JScrollPane$AccessibleJScrollPane.class */
    protected class AccessibleJScrollPane extends JComponent.AccessibleJComponent implements ChangeListener, PropertyChangeListener {
        protected JViewport viewPort;

        public void resetViewPort() {
            if (this.viewPort != null) {
                this.viewPort.removeChangeListener(this);
                this.viewPort.removePropertyChangeListener(this);
            }
            this.viewPort = JScrollPane.this.getViewport();
            if (this.viewPort != null) {
                this.viewPort.addChangeListener(this);
                this.viewPort.addPropertyChangeListener(this);
            }
        }

        public AccessibleJScrollPane() {
            super();
            this.viewPort = null;
            resetViewPort();
            JScrollBar horizontalScrollBar = JScrollPane.this.getHorizontalScrollBar();
            if (horizontalScrollBar != null) {
                setScrollBarRelations(horizontalScrollBar);
            }
            JScrollBar verticalScrollBar = JScrollPane.this.getVerticalScrollBar();
            if (verticalScrollBar != null) {
                setScrollBarRelations(verticalScrollBar);
            }
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SCROLL_PANE;
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            if (changeEvent == null) {
                throw new NullPointerException();
            }
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, false, true);
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if ((propertyName == "horizontalScrollBar" || propertyName == "verticalScrollBar") && (propertyChangeEvent.getNewValue() instanceof JScrollBar)) {
                setScrollBarRelations((JScrollBar) propertyChangeEvent.getNewValue());
            }
        }

        void setScrollBarRelations(JScrollBar jScrollBar) {
            AccessibleRelation accessibleRelation = new AccessibleRelation(AccessibleRelation.CONTROLLED_BY, jScrollBar);
            jScrollBar.getAccessibleContext().getAccessibleRelationSet().add(new AccessibleRelation(AccessibleRelation.CONTROLLER_FOR, JScrollPane.this));
            getAccessibleRelationSet().add(accessibleRelation);
        }
    }
}
