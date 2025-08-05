package com.sun.scenario.effect.impl.prism;

import com.sun.glass.ui.Screen;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrRenderer.class */
public abstract class PrRenderer extends Renderer {
    private static final Set<String> intrinsicPeerNames = new HashSet(4);

    public abstract PrDrawable createDrawable(RTTexture rTTexture);

    static {
        intrinsicPeerNames.add("Crop");
        intrinsicPeerNames.add("Flood");
        intrinsicPeerNames.add("Merge");
        intrinsicPeerNames.add("Reflection");
    }

    protected PrRenderer() {
    }

    public static Renderer createRenderer(FilterContext fctx) {
        boolean isHW;
        Object ref = fctx.getReferent();
        if (!(ref instanceof Screen)) {
            return null;
        }
        if (((PrFilterContext) fctx).isForceSoftware()) {
            isHW = false;
        } else {
            GraphicsPipeline pipe = GraphicsPipeline.getPipeline();
            if (pipe == null) {
                return null;
            }
            isHW = pipe.supportsShaderModel(GraphicsPipeline.ShaderModel.SM3);
        }
        return createRenderer(fctx, isHW);
    }

    private static PrRenderer createRenderer(FilterContext fctx, boolean isHW) {
        String klassName = isHW ? "com.sun.scenario.effect.impl.prism.ps.PPSRenderer" : "com.sun.scenario.effect.impl.prism.sw.PSWRenderer";
        try {
            Class klass = Class.forName(klassName);
            Method m2 = klass.getMethod("createRenderer", FilterContext.class);
            return (PrRenderer) m2.invoke(null, fctx);
        } catch (Throwable th) {
            return null;
        }
    }

    public static boolean isIntrinsicPeer(String name) {
        return intrinsicPeerNames.contains(name);
    }
}
