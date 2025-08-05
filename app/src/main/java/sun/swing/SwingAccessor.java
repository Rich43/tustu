package sun.swing;

import java.awt.Point;
import javax.swing.RepaintManager;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;
import sun.misc.Unsafe;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:sun/swing/SwingAccessor.class */
public final class SwingAccessor {
    private static JTextComponentAccessor jtextComponentAccessor;
    private static JLightweightFrameAccessor jLightweightFrameAccessor;
    private static RepaintManagerAccessor repaintManagerAccessor;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static ThreadLocal<Boolean> tlObj = new ThreadLocal<>();

    /* loaded from: rt.jar:sun/swing/SwingAccessor$JLightweightFrameAccessor.class */
    public interface JLightweightFrameAccessor {
        void updateCursor(JLightweightFrame jLightweightFrame);
    }

    /* loaded from: rt.jar:sun/swing/SwingAccessor$JTextComponentAccessor.class */
    public interface JTextComponentAccessor {
        TransferHandler.DropLocation dropLocationForPoint(JTextComponent jTextComponent, Point point);

        Object setDropLocation(JTextComponent jTextComponent, TransferHandler.DropLocation dropLocation, Object obj, boolean z2);
    }

    /* loaded from: rt.jar:sun/swing/SwingAccessor$RepaintManagerAccessor.class */
    public interface RepaintManagerAccessor {
        void addRepaintListener(RepaintManager repaintManager, SwingUtilities2.RepaintListener repaintListener);

        void removeRepaintListener(RepaintManager repaintManager, SwingUtilities2.RepaintListener repaintListener);
    }

    private SwingAccessor() {
    }

    public static void setJTextComponentAccessor(JTextComponentAccessor jTextComponentAccessor) {
        jtextComponentAccessor = jTextComponentAccessor;
    }

    public static JTextComponentAccessor getJTextComponentAccessor() {
        if (jtextComponentAccessor == null) {
            unsafe.ensureClassInitialized(JTextComponent.class);
        }
        return jtextComponentAccessor;
    }

    public static void setJLightweightFrameAccessor(JLightweightFrameAccessor jLightweightFrameAccessor2) {
        jLightweightFrameAccessor = jLightweightFrameAccessor2;
    }

    public static JLightweightFrameAccessor getJLightweightFrameAccessor() {
        if (jLightweightFrameAccessor == null) {
            unsafe.ensureClassInitialized(JLightweightFrame.class);
        }
        return jLightweightFrameAccessor;
    }

    public static void setRepaintManagerAccessor(RepaintManagerAccessor repaintManagerAccessor2) {
        repaintManagerAccessor = repaintManagerAccessor2;
    }

    public static RepaintManagerAccessor getRepaintManagerAccessor() {
        if (repaintManagerAccessor == null) {
            unsafe.ensureClassInitialized(RepaintManager.class);
        }
        return repaintManagerAccessor;
    }

    public static Boolean getAllowHTMLObject() {
        Boolean bool = tlObj.get();
        if (bool == null) {
            return Boolean.TRUE;
        }
        return bool;
    }

    public static void setAllowHTMLObject(Boolean bool) {
        tlObj.set(bool);
    }
}
