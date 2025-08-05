package sun.java2d.cmm.lcms;

import sun.java2d.cmm.CMMServiceProvider;
import sun.java2d.cmm.PCMM;

/* loaded from: rt.jar:sun/java2d/cmm/lcms/LcmsServiceProvider.class */
public final class LcmsServiceProvider extends CMMServiceProvider {
    @Override // sun.java2d.cmm.CMMServiceProvider
    protected PCMM getModule() {
        return LCMS.getModule();
    }
}
