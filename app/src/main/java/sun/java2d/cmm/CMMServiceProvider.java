package sun.java2d.cmm;

/* loaded from: rt.jar:sun/java2d/cmm/CMMServiceProvider.class */
public abstract class CMMServiceProvider {
    protected abstract PCMM getModule();

    public final PCMM getColorManagementModule() {
        if (CMSManager.canCreateModule()) {
            return getModule();
        }
        return null;
    }
}
