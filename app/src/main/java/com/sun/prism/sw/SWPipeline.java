package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import java.security.AccessController;
import java.util.HashMap;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWPipeline.class */
public final class SWPipeline extends GraphicsPipeline {
    private static SWPipeline theInstance;
    private final HashMap<Integer, SWResourceFactory> factories = new HashMap<>(1);

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("prism_sw");
            return null;
        });
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean init() {
        return true;
    }

    private SWPipeline() {
    }

    public static SWPipeline getInstance() {
        if (theInstance == null) {
            theInstance = new SWPipeline();
        }
        return theInstance;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public int getAdapterOrdinal(Screen screen) {
        return Screen.getScreens().indexOf(screen);
    }

    @Override // com.sun.prism.GraphicsPipeline
    public ResourceFactory getResourceFactory(Screen screen) {
        Integer index = new Integer(screen.getAdapterOrdinal());
        SWResourceFactory factory = this.factories.get(index);
        if (factory == null) {
            factory = new SWResourceFactory(screen);
            this.factories.put(index, factory);
        }
        return factory;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public ResourceFactory getDefaultResourceFactory(List<Screen> screens) {
        return getResourceFactory(Screen.getMainScreen());
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean is3DSupported() {
        return false;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean isVsyncSupported() {
        return false;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean supportsShaderType(GraphicsPipeline.ShaderType type) {
        return false;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean supportsShaderModel(GraphicsPipeline.ShaderModel model) {
        return false;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public void dispose() {
        super.dispose();
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean isUploading() {
        return true;
    }
}
