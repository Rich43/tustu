package com.sun.corba.se.spi.resolver;

import com.sun.corba.se.impl.resolver.BootstrapResolverImpl;
import com.sun.corba.se.impl.resolver.CompositeResolverImpl;
import com.sun.corba.se.impl.resolver.FileResolverImpl;
import com.sun.corba.se.impl.resolver.INSURLOperationImpl;
import com.sun.corba.se.impl.resolver.LocalResolverImpl;
import com.sun.corba.se.impl.resolver.ORBDefaultInitRefResolverImpl;
import com.sun.corba.se.impl.resolver.ORBInitRefResolverImpl;
import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orb.StringPair;
import java.io.File;

/* loaded from: rt.jar:com/sun/corba/se/spi/resolver/ResolverDefault.class */
public class ResolverDefault {
    public static LocalResolver makeLocalResolver() {
        return new LocalResolverImpl();
    }

    public static Resolver makeORBInitRefResolver(Operation operation, StringPair[] stringPairArr) {
        return new ORBInitRefResolverImpl(operation, stringPairArr);
    }

    public static Resolver makeORBDefaultInitRefResolver(Operation operation, String str) {
        return new ORBDefaultInitRefResolverImpl(operation, str);
    }

    public static Resolver makeBootstrapResolver(ORB orb, String str, int i2) {
        return new BootstrapResolverImpl(orb, str, i2);
    }

    public static Resolver makeCompositeResolver(Resolver resolver, Resolver resolver2) {
        return new CompositeResolverImpl(resolver, resolver2);
    }

    public static Operation makeINSURLOperation(ORB orb, Resolver resolver) {
        return new INSURLOperationImpl(orb, resolver);
    }

    public static LocalResolver makeSplitLocalResolver(Resolver resolver, LocalResolver localResolver) {
        return new SplitLocalResolverImpl(resolver, localResolver);
    }

    public static Resolver makeFileResolver(ORB orb, File file) {
        return new FileResolverImpl(orb, file);
    }
}
