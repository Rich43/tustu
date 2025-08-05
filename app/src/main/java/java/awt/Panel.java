package java.awt;

import java.awt.Container;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;

/* loaded from: rt.jar:java/awt/Panel.class */
public class Panel extends Container implements Accessible {
    private static final String base = "panel";
    private static int nameCounter = 0;
    private static final long serialVersionUID = -2728009084054400034L;

    public Panel() {
        this(new FlowLayout());
    }

    public Panel(LayoutManager layoutManager) {
        setLayout(layoutManager);
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Panel.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    @Override // java.awt.Container, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                this.peer = getToolkit().createPanel(this);
            }
            super.addNotify();
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTPanel();
        }
        return this.accessibleContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:java/awt/Panel$AccessibleAWTPanel.class */
    public class AccessibleAWTPanel extends Container.AccessibleAWTContainer {
        private static final long serialVersionUID = -6409552226660031050L;

        protected AccessibleAWTPanel() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PANEL;
        }
    }
}
