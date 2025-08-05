package sun.util.locale.provider;

import java.lang.reflect.InvocationTargetException;
import java.util.spi.LocaleServiceProvider;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/HostLocaleProviderAdapter.class */
public class HostLocaleProviderAdapter extends AuxLocaleProviderAdapter {
    @Override // sun.util.locale.provider.LocaleProviderAdapter
    public LocaleProviderAdapter.Type getAdapterType() {
        return LocaleProviderAdapter.Type.HOST;
    }

    @Override // sun.util.locale.provider.AuxLocaleProviderAdapter
    protected <P extends LocaleServiceProvider> P findInstalledProvider(Class<P> cls) {
        try {
            return (P) HostLocaleProviderAdapterImpl.class.getMethod("get" + cls.getSimpleName(), (Class[]) null).invoke(null, (Object[]) null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e2) {
            LocaleServiceProviderPool.config(HostLocaleProviderAdapter.class, e2.toString());
            return null;
        }
    }
}
