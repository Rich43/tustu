package com.sun.corba.se.impl.resolver;

import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.resolver.Resolver;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/resolver/ORBDefaultInitRefResolverImpl.class */
public class ORBDefaultInitRefResolverImpl implements Resolver {
    Operation urlHandler;
    String orbDefaultInitRef;

    public ORBDefaultInitRefResolverImpl(Operation operation, String str) {
        this.urlHandler = operation;
        this.orbDefaultInitRef = str;
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Object resolve(String str) {
        String str2;
        if (this.orbDefaultInitRef == null) {
            return null;
        }
        if (this.orbDefaultInitRef.startsWith("corbaloc:")) {
            str2 = this.orbDefaultInitRef + "/" + str;
        } else {
            str2 = this.orbDefaultInitRef + FXMLLoader.CONTROLLER_METHOD_PREFIX + str;
        }
        return (Object) this.urlHandler.operate(str2);
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Set list() {
        return new HashSet();
    }
}
