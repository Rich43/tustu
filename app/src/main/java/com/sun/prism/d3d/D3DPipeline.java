package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessController;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DPipeline.class */
public final class D3DPipeline extends GraphicsPipeline {
    private static final boolean d3dEnabled = ((Boolean) AccessController.doPrivileged(() -> {
        if (PrismSettings.verbose) {
            System.out.println("Loading D3D native library ...");
        }
        NativeLibLoader.loadLibrary("prism_d3d");
        if (PrismSettings.verbose) {
            System.out.println("\tsucceeded.");
        }
        return Boolean.valueOf(nInit(PrismSettings.class, true));
    })).booleanValue();
    private static final Thread creator;
    private static D3DPipeline theInstance;
    private static D3DResourceFactory[] factories;
    private static boolean d3dInitialized;
    D3DResourceFactory _default;
    private int maxSamples = -1;

    private static native boolean nInit(Class cls, boolean z2);

    private static native String nGetErrorMessage();

    private static native void nDispose(boolean z2);

    private static native int nGetAdapterOrdinal(long j2);

    private static native int nGetAdapterCount();

    private static native D3DDriverInformation nGetDriverInformation(int i2, D3DDriverInformation d3DDriverInformation);

    private static native int nGetMaxSampleSupport(int i2);

    static {
        if (PrismSettings.verbose) {
            System.out.println("Direct3D initialization " + (d3dEnabled ? "succeeded" : "failed"));
        }
        boolean printD3DError = PrismSettings.verbose || !PrismSettings.disableBadDriverWarning;
        if (!d3dEnabled && printD3DError) {
            if (PrismSettings.verbose) {
                System.out.println(nGetErrorMessage());
            }
            printDriverWarnings();
        }
        creator = Thread.currentThread();
        if (d3dEnabled) {
            d3dInitialized = true;
            theInstance = new D3DPipeline();
            factories = new D3DResourceFactory[nGetAdapterCount()];
        }
    }

    public static D3DPipeline getInstance() {
        return theInstance;
    }

    private static boolean isDriverWarning(String warningMessage) {
        return warningMessage.contains("driver version");
    }

    private static void printDriverWarning(D3DDriverInformation di) {
        if (di != null && di.warningMessage != null) {
            if (PrismSettings.verbose || isDriverWarning(di.warningMessage)) {
                System.out.println("Device \"" + di.deviceDescription + "\" (" + di.deviceName + ") initialization failed : ");
                System.out.println(di.warningMessage);
            }
        }
    }

    private static void printDriverWarning(int adapter) {
        printDriverWarning(nGetDriverInformation(adapter, new D3DDriverInformation()));
    }

    private static void printDriverInformation(int adapter) {
        D3DDriverInformation di = nGetDriverInformation(adapter, new D3DDriverInformation());
        if (di != null) {
            System.out.println("OS Information:");
            System.out.println("\t" + di.getOsVersion() + " build " + di.osBuildNumber);
            System.out.println("D3D Driver Information:");
            System.out.println("\t" + di.deviceDescription);
            System.out.println("\t" + di.deviceName);
            System.out.println("\tDriver " + di.driverName + ", version " + di.getDriverVersion());
            System.out.println("\tPixel Shader version " + di.psVersionMajor + "." + di.psVersionMinor);
            System.out.println("\tDevice : " + di.getDeviceID());
            System.out.println("\tMax Multisamples supported: " + di.maxSamples);
            if (di.warningMessage != null) {
                System.out.println("\t *** " + di.warningMessage);
            }
        }
    }

    private static void printDriverWarnings() {
        int adapter = 0;
        while (true) {
            D3DDriverInformation di = nGetDriverInformation(adapter, new D3DDriverInformation());
            if (di != null) {
                printDriverWarning(di);
                adapter++;
            } else {
                return;
            }
        }
    }

    private D3DPipeline() {
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean init() {
        return d3dEnabled;
    }

    private void reset(boolean unload) {
        if (!d3dInitialized) {
            return;
        }
        if (creator != Thread.currentThread()) {
            throw new IllegalStateException("This operation is not permitted on the current thread [" + Thread.currentThread().getName() + "]");
        }
        for (int i2 = 0; i2 != factories.length; i2++) {
            if (factories[i2] != null) {
                factories[i2].dispose();
            }
            factories[i2] = null;
        }
        factories = null;
        this._default = null;
        d3dInitialized = false;
        nDispose(unload);
    }

    void reinitialize() {
        if (PrismSettings.verbose) {
            System.err.println("D3DPipeline: reinitialize after device was removed");
        }
        reset(false);
        boolean success = nInit(PrismSettings.class, false);
        if (!success) {
            nDispose(false);
        } else {
            d3dInitialized = true;
            factories = new D3DResourceFactory[nGetAdapterCount()];
        }
    }

    @Override // com.sun.prism.GraphicsPipeline
    public void dispose() {
        reset(true);
        theInstance = null;
        super.dispose();
    }

    private static D3DResourceFactory createResourceFactory(int adapterOrdinal, Screen screen) {
        long pContext = D3DResourceFactory.nGetContext(adapterOrdinal);
        if (pContext != 0) {
            return new D3DResourceFactory(pContext, screen);
        }
        return null;
    }

    private static D3DResourceFactory getD3DResourceFactory(int adapterOrdinal, Screen screen) {
        D3DResourceFactory factory = factories[adapterOrdinal];
        if (factory == null && screen != null) {
            factory = createResourceFactory(adapterOrdinal, screen);
            factories[adapterOrdinal] = factory;
        }
        return factory;
    }

    private static Screen getScreenForAdapter(List<Screen> screens, int adapterOrdinal) {
        for (Screen screen : screens) {
            if (screen.getAdapterOrdinal() == adapterOrdinal) {
                return screen;
            }
        }
        return Screen.getMainScreen();
    }

    @Override // com.sun.prism.GraphicsPipeline
    public int getAdapterOrdinal(Screen screen) {
        return nGetAdapterOrdinal(screen.getNativeScreen());
    }

    private static D3DResourceFactory findDefaultResourceFactory(List<Screen> screens) {
        if (!d3dInitialized) {
            getInstance().reinitialize();
            if (!d3dInitialized) {
                return null;
            }
        }
        int n2 = nGetAdapterCount();
        for (int adapter = 0; adapter != n2; adapter++) {
            D3DResourceFactory rf = getD3DResourceFactory(adapter, getScreenForAdapter(screens, adapter));
            if (rf != null) {
                if (PrismSettings.verbose) {
                    printDriverInformation(adapter);
                }
                return rf;
            }
            if (!PrismSettings.disableBadDriverWarning) {
                printDriverWarning(adapter);
            }
        }
        return null;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public ResourceFactory getDefaultResourceFactory(List<Screen> screens) {
        if (this._default == null) {
            this._default = findDefaultResourceFactory(screens);
        }
        return this._default;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public ResourceFactory getResourceFactory(Screen screen) {
        return getD3DResourceFactory(screen.getAdapterOrdinal(), screen);
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean is3DSupported() {
        return true;
    }

    int getMaxSamples() {
        if (this.maxSamples < 0) {
            isMSAASupported();
        }
        return this.maxSamples;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean isMSAASupported() {
        if (this.maxSamples < 0) {
            this.maxSamples = nGetMaxSampleSupport(0);
        }
        return this.maxSamples > 0;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean isVsyncSupported() {
        return true;
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean supportsShaderType(GraphicsPipeline.ShaderType type) {
        switch (type) {
            case HLSL:
                return true;
            default:
                return false;
        }
    }

    @Override // com.sun.prism.GraphicsPipeline
    public boolean supportsShaderModel(GraphicsPipeline.ShaderModel model) {
        switch (model) {
            case SM3:
                return true;
            default:
                return false;
        }
    }
}
