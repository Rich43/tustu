package com.sun.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.prism.impl.PrismSettings;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: jfxrt.jar:com/sun/prism/GraphicsPipeline.class */
public abstract class GraphicsPipeline {
    private FontFactory fontFactory;
    private final Set<Runnable> disposeHooks = new HashSet();
    protected Map deviceDetails = null;
    private static GraphicsPipeline installedPipeline;

    /* loaded from: jfxrt.jar:com/sun/prism/GraphicsPipeline$ShaderModel.class */
    public enum ShaderModel {
        SM3
    }

    /* loaded from: jfxrt.jar:com/sun/prism/GraphicsPipeline$ShaderType.class */
    public enum ShaderType {
        HLSL,
        GLSL
    }

    public abstract boolean init();

    public abstract int getAdapterOrdinal(Screen screen);

    public abstract ResourceFactory getResourceFactory(Screen screen);

    public abstract ResourceFactory getDefaultResourceFactory(List<Screen> list);

    public abstract boolean is3DSupported();

    public abstract boolean isVsyncSupported();

    public abstract boolean supportsShaderType(ShaderType shaderType);

    public abstract boolean supportsShaderModel(ShaderModel shaderModel);

    public void dispose() {
        notifyDisposeHooks();
        installedPipeline = null;
    }

    public void addDisposeHook(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        synchronized (this.disposeHooks) {
            this.disposeHooks.add(runnable);
        }
    }

    private void notifyDisposeHooks() {
        List<Runnable> hooks;
        synchronized (this.disposeHooks) {
            hooks = new ArrayList<>(this.disposeHooks);
            this.disposeHooks.clear();
        }
        for (Runnable hook : hooks) {
            hook.run();
        }
    }

    public boolean isMSAASupported() {
        return false;
    }

    public boolean supportsShader(ShaderType type, ShaderModel model) {
        return supportsShaderType(type) && supportsShaderModel(model);
    }

    public static ResourceFactory getDefaultResourceFactory() {
        List<Screen> screens = Screen.getScreens();
        return getPipeline().getDefaultResourceFactory(screens);
    }

    public FontFactory getFontFactory() {
        if (this.fontFactory == null) {
            this.fontFactory = PrismFontFactory.getFontFactory();
        }
        return this.fontFactory;
    }

    public Map getDeviceDetails() {
        return this.deviceDetails;
    }

    protected void setDeviceDetails(Map details) {
        this.deviceDetails = details;
    }

    public static GraphicsPipeline createPipeline() {
        Class klass;
        GraphicsPipeline newPipeline;
        if (PrismSettings.tryOrder.isEmpty()) {
            if (PrismSettings.verbose) {
                System.out.println("No Prism pipelines specified");
                return null;
            }
            return null;
        }
        if (installedPipeline != null) {
            throw new IllegalStateException("pipeline already created:" + ((Object) installedPipeline));
        }
        for (String prefix : PrismSettings.tryOrder) {
            if ("j2d".equals(prefix)) {
                System.err.println("WARNING: The prism-j2d pipeline should not be used as the software");
                System.err.println("fallback pipeline. It is no longer tested nor intended to be used for");
                System.err.println("on-screen rendering. Please use the prism-sw pipeline instead by setting");
                System.err.println("the \"prism.order\" system property to \"sw\" rather than \"j2d\".");
            }
            if (PrismSettings.verbose && ("j2d".equals(prefix) || "sw".equals(prefix))) {
                System.err.println("*** Fallback to Prism SW pipeline");
            }
            String className = "com.sun.prism." + prefix + "." + prefix.toUpperCase() + "Pipeline";
            try {
                if (PrismSettings.verbose) {
                    System.out.println("Prism pipeline name = " + className);
                }
                klass = Class.forName(className);
                if (PrismSettings.verbose) {
                    System.out.println("(X) Got class = " + ((Object) klass));
                }
                Method m2 = klass.getMethod("getInstance", (Class[]) null);
                newPipeline = (GraphicsPipeline) m2.invoke(null, (Object[]) null);
            } catch (Throwable t2) {
                if (PrismSettings.verbose) {
                    System.err.println("GraphicsPipeline.createPipeline failed for " + className);
                    t2.printStackTrace();
                }
            }
            if (newPipeline != null && newPipeline.init()) {
                if (PrismSettings.verbose) {
                    System.out.println("Initialized prism pipeline: " + klass.getName());
                }
                installedPipeline = newPipeline;
                return installedPipeline;
            }
            if (newPipeline != null) {
                newPipeline.dispose();
            }
            if (PrismSettings.verbose) {
                System.err.println("GraphicsPipeline.createPipeline: error initializing pipeline " + className);
            }
        }
        StringBuffer sBuf = new StringBuffer("Graphics Device initialization failed for :  ");
        Iterator<String> orderIterator = PrismSettings.tryOrder.iterator();
        if (orderIterator.hasNext()) {
            sBuf.append(orderIterator.next());
            while (orderIterator.hasNext()) {
                sBuf.append(", ");
                sBuf.append(orderIterator.next());
            }
        }
        System.err.println(sBuf);
        return null;
    }

    public static GraphicsPipeline getPipeline() {
        return installedPipeline;
    }

    public boolean isEffectSupported() {
        return true;
    }

    public boolean isUploading() {
        return PrismSettings.forceUploadingPainter;
    }
}
