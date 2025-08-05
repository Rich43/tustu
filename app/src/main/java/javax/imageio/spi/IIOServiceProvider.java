package javax.imageio.spi;

import java.util.Locale;

/* loaded from: rt.jar:javax/imageio/spi/IIOServiceProvider.class */
public abstract class IIOServiceProvider implements RegisterableService {
    protected String vendorName;
    protected String version;

    public abstract String getDescription(Locale locale);

    public IIOServiceProvider(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("vendorName == null!");
        }
        if (str2 == null) {
            throw new IllegalArgumentException("version == null!");
        }
        this.vendorName = str;
        this.version = str2;
    }

    public IIOServiceProvider() {
    }

    @Override // javax.imageio.spi.RegisterableService
    public void onRegistration(ServiceRegistry serviceRegistry, Class<?> cls) {
    }

    @Override // javax.imageio.spi.RegisterableService
    public void onDeregistration(ServiceRegistry serviceRegistry, Class<?> cls) {
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public String getVersion() {
        return this.version;
    }
}
