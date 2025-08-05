package java.awt;

import java.awt.Component;
import java.awt.image.BufferStrategy;
import java.awt.peer.CanvasPeer;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;

/* loaded from: rt.jar:java/awt/Canvas.class */
public class Canvas extends Component implements Accessible {
    private static final String base = "canvas";
    private static int nameCounter = 0;
    private static final long serialVersionUID = -2284879212465893870L;

    public Canvas() {
    }

    public Canvas(GraphicsConfiguration graphicsConfiguration) {
        this();
        setGraphicsConfiguration(graphicsConfiguration);
    }

    @Override // java.awt.Component
    void setGraphicsConfiguration(GraphicsConfiguration graphicsConfiguration) {
        synchronized (getTreeLock()) {
            CanvasPeer canvasPeer = (CanvasPeer) getPeer();
            if (canvasPeer != null) {
                graphicsConfiguration = canvasPeer.getAppropriateGraphicsConfiguration(graphicsConfiguration);
            }
            super.setGraphicsConfiguration(graphicsConfiguration);
        }
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Canvas.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    @Override // java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                this.peer = getToolkit().createCanvas(this);
            }
            super.addNotify();
        }
    }

    @Override // java.awt.Component
    public void paint(Graphics graphics) {
        graphics.clearRect(0, 0, this.width, this.height);
    }

    @Override // java.awt.Component
    public void update(Graphics graphics) {
        graphics.clearRect(0, 0, this.width, this.height);
        paint(graphics);
    }

    @Override // java.awt.Component
    boolean postsOldMouseEvents() {
        return true;
    }

    @Override // java.awt.Component
    public void createBufferStrategy(int i2) {
        super.createBufferStrategy(i2);
    }

    @Override // java.awt.Component
    public void createBufferStrategy(int i2, BufferCapabilities bufferCapabilities) throws AWTException {
        super.createBufferStrategy(i2, bufferCapabilities);
    }

    @Override // java.awt.Component
    public BufferStrategy getBufferStrategy() {
        return super.getBufferStrategy();
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTCanvas();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/Canvas$AccessibleAWTCanvas.class */
    protected class AccessibleAWTCanvas extends Component.AccessibleAWTComponent {
        private static final long serialVersionUID = -6325592262103146699L;

        protected AccessibleAWTCanvas() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.CANVAS;
        }
    }
}
