package org.icepdf.core.pobjects.graphics;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/PaintTimer.class */
public class PaintTimer {
    protected static final Logger logger = Logger.getLogger(PaintTimer.class.toString());
    protected static int paintDelay;
    private long lastPaintTime;

    static {
        try {
            paintDelay = Defs.intProperty("org.icepdf.core.views.refreshfrequency", 250);
        } catch (NumberFormatException e2) {
            logger.log(Level.FINE, "Error reading buffered scale factor");
        }
    }

    public boolean shouldTriggerRepaint() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastPaintTime > paintDelay) {
            this.lastPaintTime = currentTime;
            return true;
        }
        return false;
    }
}
