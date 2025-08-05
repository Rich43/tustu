package sun.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/SunGraphicsCallback.class */
public abstract class SunGraphicsCallback {
    public static final int HEAVYWEIGHTS = 1;
    public static final int LIGHTWEIGHTS = 2;
    public static final int TWO_PASSES = 4;
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.SunGraphicsCallback");

    public abstract void run(Component component, Graphics graphics);

    /* JADX WARN: Multi-variable type inference failed */
    protected void constrainGraphics(Graphics graphics, Rectangle rectangle) {
        if (graphics instanceof ConstrainableGraphics) {
            ((ConstrainableGraphics) graphics).constrain(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
        } else {
            graphics.translate(rectangle.f12372x, rectangle.f12373y);
        }
        graphics.clipRect(0, 0, rectangle.width, rectangle.height);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void runOneComponent(Component component, Rectangle rectangle, Graphics graphics, Shape shape, int i2) {
        if (component == null || component.getPeer() == null || !component.isVisible()) {
            return;
        }
        boolean zIsLightweight = component.isLightweight();
        if (zIsLightweight && (i2 & 2) == 0) {
            return;
        }
        if (!zIsLightweight && (i2 & 1) == 0) {
            return;
        }
        if (rectangle == null) {
            rectangle = component.getBounds();
        }
        if (shape == null || shape.intersects(rectangle)) {
            Graphics graphicsCreate = graphics.create();
            try {
                constrainGraphics(graphicsCreate, rectangle);
                graphicsCreate.setFont(component.getFont());
                graphicsCreate.setColor(component.getForeground());
                if (graphicsCreate instanceof Graphics2D) {
                    ((Graphics2D) graphicsCreate).setBackground(component.getBackground());
                } else if (graphicsCreate instanceof Graphics2Delegate) {
                    ((Graphics2Delegate) graphicsCreate).setBackground(component.getBackground());
                }
                run(component, graphicsCreate);
                graphicsCreate.dispose();
            } catch (Throwable th) {
                graphicsCreate.dispose();
                throw th;
            }
        }
    }

    public final void runComponents(Component[] componentArr, Graphics graphics, int i2) {
        int length = componentArr.length;
        Shape clip = graphics.getClip();
        if (log.isLoggable(PlatformLogger.Level.FINER) && clip != null) {
            Rectangle bounds = clip.getBounds();
            log.finer("x = " + bounds.f12372x + ", y = " + bounds.f12373y + ", width = " + bounds.width + ", height = " + bounds.height);
        }
        if ((i2 & 4) != 0) {
            for (int i3 = length - 1; i3 >= 0; i3--) {
                runOneComponent(componentArr[i3], null, graphics, clip, 2);
            }
            for (int i4 = length - 1; i4 >= 0; i4--) {
                runOneComponent(componentArr[i4], null, graphics, clip, 1);
            }
            return;
        }
        for (int i5 = length - 1; i5 >= 0; i5--) {
            runOneComponent(componentArr[i5], null, graphics, clip, i2);
        }
    }

    /* loaded from: rt.jar:sun/awt/SunGraphicsCallback$PaintHeavyweightComponentsCallback.class */
    public static final class PaintHeavyweightComponentsCallback extends SunGraphicsCallback {
        private static PaintHeavyweightComponentsCallback instance = new PaintHeavyweightComponentsCallback();

        private PaintHeavyweightComponentsCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            if (!component.isLightweight()) {
                component.paintAll(graphics);
            } else if (component instanceof Container) {
                runComponents(((Container) component).getComponents(), graphics, 3);
            }
        }

        public static PaintHeavyweightComponentsCallback getInstance() {
            return instance;
        }
    }

    /* loaded from: rt.jar:sun/awt/SunGraphicsCallback$PrintHeavyweightComponentsCallback.class */
    public static final class PrintHeavyweightComponentsCallback extends SunGraphicsCallback {
        private static PrintHeavyweightComponentsCallback instance = new PrintHeavyweightComponentsCallback();

        private PrintHeavyweightComponentsCallback() {
        }

        @Override // sun.awt.SunGraphicsCallback
        public void run(Component component, Graphics graphics) {
            if (!component.isLightweight()) {
                component.printAll(graphics);
            } else if (component instanceof Container) {
                runComponents(((Container) component).getComponents(), graphics, 3);
            }
        }

        public static PrintHeavyweightComponentsCallback getInstance() {
            return instance;
        }
    }
}
