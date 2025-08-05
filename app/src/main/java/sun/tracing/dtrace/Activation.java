package sun.tracing.dtrace;

/* loaded from: rt.jar:sun/tracing/dtrace/Activation.class */
class Activation {
    private SystemResource resource;
    private int referenceCount;

    Activation(String str, DTraceProvider[] dTraceProviderArr) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("com.sun.tracing.dtrace.createProvider"));
        }
        this.referenceCount = dTraceProviderArr.length;
        for (DTraceProvider dTraceProvider : dTraceProviderArr) {
            dTraceProvider.setActivation(this);
        }
        this.resource = new SystemResource(this, JVM.activate(str, dTraceProviderArr));
    }

    void disposeProvider(DTraceProvider dTraceProvider) {
        int i2 = this.referenceCount - 1;
        this.referenceCount = i2;
        if (i2 == 0) {
            this.resource.dispose();
        }
    }
}
