package java.awt;

import java.awt.peer.LightweightPeer;
import sun.awt.SunGraphicsCallback;

/* loaded from: rt.jar:java/awt/GraphicsCallback.class */
abstract class GraphicsCallback extends SunGraphicsCallback {
    GraphicsCallback() {
    }

    /* loaded from: rt.jar:java/awt/GraphicsCallback$PaintCallback.class */
    static final class PaintCallback extends GraphicsCallback {
        private static PaintCallback instance = new PaintCallback();

        private PaintCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            component.paint(graphics);
        }

        static PaintCallback getInstance() {
            return instance;
        }
    }

    /* loaded from: rt.jar:java/awt/GraphicsCallback$PrintCallback.class */
    static final class PrintCallback extends GraphicsCallback {
        private static PrintCallback instance = new PrintCallback();

        private PrintCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            component.print(graphics);
        }

        static PrintCallback getInstance() {
            return instance;
        }
    }

    /* loaded from: rt.jar:java/awt/GraphicsCallback$PaintAllCallback.class */
    static final class PaintAllCallback extends GraphicsCallback {
        private static PaintAllCallback instance = new PaintAllCallback();

        private PaintAllCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            component.paintAll(graphics);
        }

        static PaintAllCallback getInstance() {
            return instance;
        }
    }

    /* loaded from: rt.jar:java/awt/GraphicsCallback$PrintAllCallback.class */
    static final class PrintAllCallback extends GraphicsCallback {
        private static PrintAllCallback instance = new PrintAllCallback();

        private PrintAllCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            component.printAll(graphics);
        }

        static PrintAllCallback getInstance() {
            return instance;
        }
    }

    /* loaded from: rt.jar:java/awt/GraphicsCallback$PeerPaintCallback.class */
    static final class PeerPaintCallback extends GraphicsCallback {
        private static PeerPaintCallback instance = new PeerPaintCallback();

        private PeerPaintCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            component.validate();
            if (component.peer instanceof LightweightPeer) {
                component.lightweightPaint(graphics);
            } else {
                component.peer.paint(graphics);
            }
        }

        static PeerPaintCallback getInstance() {
            return instance;
        }
    }

    /* loaded from: rt.jar:java/awt/GraphicsCallback$PeerPrintCallback.class */
    static final class PeerPrintCallback extends GraphicsCallback {
        private static PeerPrintCallback instance = new PeerPrintCallback();

        private PeerPrintCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            component.validate();
            if (component.peer instanceof LightweightPeer) {
                component.lightweightPrint(graphics);
            } else {
                component.peer.print(graphics);
            }
        }

        static PeerPrintCallback getInstance() {
            return instance;
        }
    }

    /* loaded from: rt.jar:java/awt/GraphicsCallback$PaintHeavyweightComponentsCallback.class */
    static final class PaintHeavyweightComponentsCallback extends GraphicsCallback {
        private static PaintHeavyweightComponentsCallback instance = new PaintHeavyweightComponentsCallback();

        private PaintHeavyweightComponentsCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            if (component.peer instanceof LightweightPeer) {
                component.paintHeavyweightComponents(graphics);
            } else {
                component.paintAll(graphics);
            }
        }

        static PaintHeavyweightComponentsCallback getInstance() {
            return instance;
        }
    }

    /* loaded from: rt.jar:java/awt/GraphicsCallback$PrintHeavyweightComponentsCallback.class */
    static final class PrintHeavyweightComponentsCallback extends GraphicsCallback {
        private static PrintHeavyweightComponentsCallback instance = new PrintHeavyweightComponentsCallback();

        private PrintHeavyweightComponentsCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            if (component.peer instanceof LightweightPeer) {
                component.printHeavyweightComponents(graphics);
            } else {
                component.printAll(graphics);
            }
        }

        static PrintHeavyweightComponentsCallback getInstance() {
            return instance;
        }
    }
}
