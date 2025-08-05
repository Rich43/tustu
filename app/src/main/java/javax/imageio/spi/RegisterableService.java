package javax.imageio.spi;

/* loaded from: rt.jar:javax/imageio/spi/RegisterableService.class */
public interface RegisterableService {
    void onRegistration(ServiceRegistry serviceRegistry, Class<?> cls);

    void onDeregistration(ServiceRegistry serviceRegistry, Class<?> cls);
}
