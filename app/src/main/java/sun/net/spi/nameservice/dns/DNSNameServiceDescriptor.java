package sun.net.spi.nameservice.dns;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

/* loaded from: dnsns.jar:sun/net/spi/nameservice/dns/DNSNameServiceDescriptor.class */
public final class DNSNameServiceDescriptor implements NameServiceDescriptor {
    @Override // sun.net.spi.nameservice.NameServiceDescriptor
    public NameService createNameService() throws Exception {
        return new DNSNameService();
    }

    @Override // sun.net.spi.nameservice.NameServiceDescriptor
    public String getProviderName() {
        return "sun";
    }

    @Override // sun.net.spi.nameservice.NameServiceDescriptor
    public String getType() {
        return "dns";
    }
}
