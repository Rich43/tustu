package sun.awt;

import java.awt.Adjustable;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.event.MouseWheelEvent;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/ScrollPaneWheelScroller.class */
public abstract class ScrollPaneWheelScroller {
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.ScrollPaneWheelScroller");

    private ScrollPaneWheelScroller() {
    }

    public static void handleWheelScrolling(ScrollPane scrollPane, MouseWheelEvent mouseWheelEvent) {
        Adjustable adjustableToScroll;
        if (log.isLoggable(PlatformLogger.Level.FINER)) {
            log.finer("x = " + mouseWheelEvent.getX() + ", y = " + mouseWheelEvent.getY() + ", src is " + mouseWheelEvent.getSource());
        }
        if (scrollPane != null && mouseWheelEvent.getScrollAmount() != 0 && (adjustableToScroll = getAdjustableToScroll(scrollPane)) != null) {
            int incrementFromAdjustable = getIncrementFromAdjustable(adjustableToScroll, mouseWheelEvent);
            if (log.isLoggable(PlatformLogger.Level.FINER)) {
                log.finer("increment from adjustable(" + ((Object) adjustableToScroll.getClass()) + ") : " + incrementFromAdjustable);
            }
            scrollAdjustable(adjustableToScroll, incrementFromAdjustable);
        }
    }

    public static Adjustable getAdjustableToScroll(ScrollPane scrollPane) {
        int scrollbarDisplayPolicy = scrollPane.getScrollbarDisplayPolicy();
        if (scrollbarDisplayPolicy == 1 || scrollbarDisplayPolicy == 2) {
            if (log.isLoggable(PlatformLogger.Level.FINER)) {
                log.finer("using vertical scrolling due to scrollbar policy");
            }
            return scrollPane.getVAdjustable();
        }
        Insets insets = scrollPane.getInsets();
        int vScrollbarWidth = scrollPane.getVScrollbarWidth();
        if (log.isLoggable(PlatformLogger.Level.FINER)) {
            log.finer("insets: l = " + insets.left + ", r = " + insets.right + ", t = " + insets.top + ", b = " + insets.bottom);
            log.finer("vertScrollWidth = " + vScrollbarWidth);
        }
        if (insets.right >= vScrollbarWidth) {
            if (log.isLoggable(PlatformLogger.Level.FINER)) {
                log.finer("using vertical scrolling because scrollbar is present");
            }
            return scrollPane.getVAdjustable();
        }
        if (insets.bottom >= scrollPane.getHScrollbarHeight()) {
            if (log.isLoggable(PlatformLogger.Level.FINER)) {
                log.finer("using horiz scrolling because scrollbar is present");
            }
            return scrollPane.getHAdjustable();
        }
        if (log.isLoggable(PlatformLogger.Level.FINER)) {
            log.finer("using NO scrollbar becsause neither is present");
            return null;
        }
        return null;
    }

    public static int getIncrementFromAdjustable(Adjustable adjustable, MouseWheelEvent mouseWheelEvent) {
        if (log.isLoggable(PlatformLogger.Level.FINE) && adjustable == null) {
            log.fine("Assertion (adj != null) failed");
        }
        int blockIncrement = 0;
        if (mouseWheelEvent.getScrollType() == 0) {
            blockIncrement = mouseWheelEvent.getUnitsToScroll() * adjustable.getUnitIncrement();
        } else if (mouseWheelEvent.getScrollType() == 1) {
            blockIncrement = adjustable.getBlockIncrement() * mouseWheelEvent.getWheelRotation();
        }
        return blockIncrement;
    }

    public static void scrollAdjustable(Adjustable adjustable, int i2) {
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            if (adjustable == null) {
                log.fine("Assertion (adj != null) failed");
            }
            if (i2 == 0) {
                log.fine("Assertion (amount != 0) failed");
            }
        }
        int value = adjustable.getValue();
        int maximum = adjustable.getMaximum() - adjustable.getVisibleAmount();
        if (log.isLoggable(PlatformLogger.Level.FINER)) {
            log.finer("doScrolling by " + i2);
        }
        if (i2 > 0 && value < maximum) {
            if (value + i2 < maximum) {
                adjustable.setValue(value + i2);
                return;
            } else {
                adjustable.setValue(maximum);
                return;
            }
        }
        if (i2 < 0 && value > adjustable.getMinimum()) {
            if (value + i2 > adjustable.getMinimum()) {
                adjustable.setValue(value + i2);
            } else {
                adjustable.setValue(adjustable.getMinimum());
            }
        }
    }
}
