package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontFactory;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import java.util.HashMap;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DPipeline.class */
public class J2DPipeline extends GraphicsPipeline {
    private static J2DPipeline theInstance;
    private final HashMap<Integer, J2DResourceFactory> factories = new HashMap<>(1);
    private FontFactory j2DFontFactory;

    @Override // com.sun.prism.GraphicsPipeline
    public boolean init() {
        return true;
    }

    private J2DPipeline() {
    }

    public static J2DPipeline getInstance() {
        if (theInstance == null) {
            theInstance = new J2DPipeline();
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
        J2DResourceFactory factory = this.factories.get(index);
        if (factory == null) {
            factory = new J2DResourceFactory(screen);
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
    public FontFactory getFontFactory() {
        if (this.j2DFontFactory == null) {
            FontFactory fontFactory = super.getFontFactory();
            this.j2DFontFactory = new J2DFontFactory(fontFactory);
        }
        return this.j2DFontFactory;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean isUploading() {
        return true;
    }
}
