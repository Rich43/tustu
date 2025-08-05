package com.sun.corba.se.impl.resolver;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.resolver.Resolver;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.HashSet;
import java.util.Set;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;

/* loaded from: rt.jar:com/sun/corba/se/impl/resolver/BootstrapResolverImpl.class */
public class BootstrapResolverImpl implements Resolver {
    private Delegate bootstrapDelegate;
    private ORBUtilSystemException wrapper;

    public BootstrapResolverImpl(ORB orb, String str, int i2) {
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.ORB_RESOLVER);
        ObjectKey objectKeyCreate = orb.getObjectKeyFactory().create("INIT".getBytes());
        IIOPProfileTemplate iIOPProfileTemplateMakeIIOPProfileTemplate = IIOPFactories.makeIIOPProfileTemplate(orb, GIOPVersion.V1_0, IIOPFactories.makeIIOPAddress(orb, str, i2));
        IORTemplate iORTemplateMakeIORTemplate = IORFactories.makeIORTemplate(objectKeyCreate.getTemplate());
        iORTemplateMakeIORTemplate.add(iIOPProfileTemplateMakeIIOPProfileTemplate);
        this.bootstrapDelegate = ORBUtility.makeClientDelegate(iORTemplateMakeIORTemplate.makeIOR(orb, "", objectKeyCreate.getId()));
    }

    private InputStream invoke(String str, String str2) {
        boolean z2 = true;
        InputStream inputStreamInvoke = null;
        while (z2) {
            z2 = false;
            OutputStream outputStreamRequest = this.bootstrapDelegate.request(null, str, true);
            if (str2 != null) {
                outputStreamRequest.write_string(str2);
            }
            try {
                inputStreamInvoke = this.bootstrapDelegate.invoke(null, outputStreamRequest);
            } catch (ApplicationException e2) {
                throw this.wrapper.bootstrapApplicationException(e2);
            } catch (RemarshalException e3) {
                z2 = true;
            }
        }
        return inputStreamInvoke;
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Object resolve(String str) {
        InputStream inputStreamInvoke = null;
        try {
            inputStreamInvoke = invoke("get", str);
            Object object = inputStreamInvoke.read_Object();
            this.bootstrapDelegate.releaseReply(null, inputStreamInvoke);
            return object;
        } catch (Throwable th) {
            this.bootstrapDelegate.releaseReply(null, inputStreamInvoke);
            throw th;
        }
    }

    @Override // com.sun.corba.se.spi.resolver.Resolver
    public Set list() {
        InputStream inputStreamInvoke = null;
        HashSet hashSet = new HashSet();
        try {
            inputStreamInvoke = invoke(SchemaSymbols.ATTVAL_LIST, null);
            int i2 = inputStreamInvoke.read_long();
            for (int i3 = 0; i3 < i2; i3++) {
                hashSet.add(inputStreamInvoke.read_string());
            }
            this.bootstrapDelegate.releaseReply(null, inputStreamInvoke);
            return hashSet;
        } catch (Throwable th) {
            this.bootstrapDelegate.releaseReply(null, inputStreamInvoke);
            throw th;
        }
    }
}
