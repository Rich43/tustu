package com.sun.scenario.effect.impl.prism;

import com.sun.glass.ui.Screen;
import com.sun.scenario.effect.FilterContext;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrFilterContext.class */
public class PrFilterContext extends FilterContext {
    private static Screen defaultScreen;
    private static final Map<Screen, PrFilterContext> ctxMap = new WeakHashMap();
    private static PrFilterContext printerFilterContext = null;
    private PrFilterContext swinstance;
    private boolean forceSW;

    public static PrFilterContext getPrinterContext(Object resourceFactory) {
        if (printerFilterContext == null) {
            printerFilterContext = new PrFilterContext(resourceFactory);
        }
        return printerFilterContext;
    }

    private PrFilterContext(Object screen) {
        super(screen);
    }

    public static PrFilterContext getInstance(Screen screen) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen must be non-null");
        }
        PrFilterContext fctx = ctxMap.get(screen);
        if (fctx == null) {
            fctx = new PrFilterContext(screen);
            ctxMap.put(screen, fctx);
        }
        return fctx;
    }

    public static PrFilterContext getDefaultInstance() {
        if (defaultScreen == null) {
            defaultScreen = Screen.getMainScreen();
        }
        return getInstance(defaultScreen);
    }

    public PrFilterContext getSoftwareInstance() {
        if (this.swinstance == null) {
            if (this.forceSW) {
                this.swinstance = this;
            } else {
                this.swinstance = new PrFilterContext(getReferent());
                this.swinstance.forceSW = true;
            }
        }
        return this.swinstance;
    }

    public boolean isForceSoftware() {
        return this.forceSW;
    }

    private static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    @Override // com.sun.scenario.effect.FilterContext
    public int hashCode() {
        return getReferent().hashCode() ^ hashCode(this.forceSW);
    }

    @Override // com.sun.scenario.effect.FilterContext
    public boolean equals(Object o2) {
        if (!(o2 instanceof PrFilterContext)) {
            return false;
        }
        PrFilterContext pfctx = (PrFilterContext) o2;
        return getReferent().equals(pfctx.getReferent()) && this.forceSW == pfctx.forceSW;
    }
}
