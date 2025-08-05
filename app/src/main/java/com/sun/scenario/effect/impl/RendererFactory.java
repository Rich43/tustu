package com.sun.scenario.effect.impl;

import com.sun.javafx.PlatformUtil;
import com.sun.scenario.effect.FilterContext;
import java.lang.reflect.Method;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/RendererFactory.class */
class RendererFactory {
    private static String rootPkg = Renderer.rootPkg;
    private static boolean tryRSL;
    private static boolean trySIMD;
    private static boolean tryJOGL;
    private static boolean tryPrism;

    RendererFactory() {
    }

    static {
        tryRSL = true;
        trySIMD = false;
        tryJOGL = PlatformUtil.isMac();
        tryPrism = true;
        try {
            if ("false".equals(System.getProperty("decora.rsl"))) {
                tryRSL = false;
            }
            if ("false".equals(System.getProperty("decora.simd"))) {
                trySIMD = false;
            }
            String tryJOGLProp = System.getProperty("decora.jogl");
            if (tryJOGLProp != null) {
                tryJOGL = Boolean.parseBoolean(tryJOGLProp);
            }
            if ("false".equals(System.getProperty("decora.prism"))) {
                tryPrism = false;
            }
        } catch (SecurityException e2) {
        }
    }

    private static boolean isRSLFriendly(Class klass) {
        if (klass.getName().equals("sun.java2d.pipe.hw.AccelGraphicsConfig")) {
            return true;
        }
        boolean rsl = false;
        Class[] interfaces = klass.getInterfaces();
        int length = interfaces.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            Class iface = interfaces[i2];
            if (!isRSLFriendly(iface)) {
                i2++;
            } else {
                rsl = true;
                break;
            }
        }
        return rsl;
    }

    private static boolean isRSLAvailable(FilterContext fctx) {
        return isRSLFriendly(fctx.getReferent().getClass());
    }

    private static Renderer createRSLRenderer(FilterContext fctx) {
        try {
            Class klass = Class.forName(rootPkg + ".impl.j2d.rsl.RSLRenderer");
            Method m2 = klass.getMethod("createRenderer", FilterContext.class);
            return (Renderer) m2.invoke(null, fctx);
        } catch (Throwable th) {
            return null;
        }
    }

    private static Renderer createJOGLRenderer(FilterContext fctx) {
        if (tryJOGL) {
            try {
                Class klass = Class.forName(rootPkg + ".impl.j2d.jogl.JOGLRenderer");
                Method m2 = klass.getMethod("createRenderer", FilterContext.class);
                return (Renderer) m2.invoke(null, fctx);
            } catch (Throwable th) {
                return null;
            }
        }
        return null;
    }

    private static Renderer createPrismRenderer(FilterContext fctx) {
        if (tryPrism) {
            try {
                Class klass = Class.forName(rootPkg + ".impl.prism.PrRenderer");
                Method m2 = klass.getMethod("createRenderer", FilterContext.class);
                return (Renderer) m2.invoke(null, fctx);
            } catch (Throwable e2) {
                e2.printStackTrace();
                return null;
            }
        }
        return null;
    }

    private static Renderer getSSERenderer() {
        if (trySIMD) {
            try {
                Class klass = Class.forName(rootPkg + ".impl.j2d.J2DSWRenderer");
                Method m2 = klass.getMethod("getSSEInstance", (Class[]) null);
                Renderer sseRenderer = (Renderer) m2.invoke(null, (Object[]) null);
                if (sseRenderer != null) {
                    return sseRenderer;
                }
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
            trySIMD = false;
            return null;
        }
        return null;
    }

    private static Renderer getJavaRenderer() {
        try {
            Class klass = Class.forName(rootPkg + ".impl.prism.sw.PSWRenderer");
            Class screenClass = Class.forName("com.sun.glass.ui.Screen");
            Method m2 = klass.getMethod("createJSWInstance", screenClass);
            Renderer jswRenderer = (Renderer) m2.invoke(null, null);
            if (jswRenderer != null) {
                return jswRenderer;
            }
            return null;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static Renderer getJavaRenderer(FilterContext fctx) {
        try {
            Class klass = Class.forName(rootPkg + ".impl.prism.sw.PSWRenderer");
            Method m2 = klass.getMethod("createJSWInstance", FilterContext.class);
            Renderer jswRenderer = (Renderer) m2.invoke(null, fctx);
            if (jswRenderer != null) {
                return jswRenderer;
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    static Renderer getSoftwareRenderer() {
        Renderer r2 = getSSERenderer();
        if (r2 == null) {
            r2 = getJavaRenderer();
        }
        return r2;
    }

    static Renderer createRenderer(FilterContext fctx) {
        return (Renderer) AccessController.doPrivileged(() -> {
            Renderer r2 = null;
            String klassName = fctx.getClass().getName();
            String simpleName = klassName.substring(klassName.lastIndexOf(".") + 1);
            if (simpleName.equals("PrFilterContext") && tryPrism) {
                r2 = createPrismRenderer(fctx);
            }
            if (r2 == null && tryRSL && isRSLAvailable(fctx)) {
                r2 = createRSLRenderer(fctx);
            }
            if (r2 == null && tryJOGL) {
                r2 = createJOGLRenderer(fctx);
            }
            if (r2 == null && trySIMD) {
                r2 = getSSERenderer();
            }
            if (r2 == null) {
                r2 = getJavaRenderer(fctx);
            }
            return r2;
        });
    }
}
