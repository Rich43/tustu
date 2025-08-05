package sun.net.spi.nameservice;

/* loaded from: rt.jar:sun/net/spi/nameservice/NameServiceDescriptor.class */
public interface NameServiceDescriptor {
    NameService createNameService() throws Exception;

    String getProviderName();

    String getType();
}
