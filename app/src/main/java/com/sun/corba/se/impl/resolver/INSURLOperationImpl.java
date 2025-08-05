package com.sun.corba.se.impl.resolver;

import com.sun.corba.se.impl.encoding.EncapsInputStream;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.naming.namingutil.CorbalocURL;
import com.sun.corba.se.impl.naming.namingutil.CorbanameURL;
import com.sun.corba.se.impl.naming.namingutil.IIOPEndpointInfo;
import com.sun.corba.se.impl.naming.namingutil.INSURL;
import com.sun.corba.se.impl.naming.namingutil.INSURLHandler;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.resolver.Resolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import sun.corba.EncapsInputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/resolver/INSURLOperationImpl.class */
public class INSURLOperationImpl implements Operation {
    ORB orb;
    ORBUtilSystemException wrapper;
    OMGSystemException omgWrapper;
    Resolver bootstrapResolver;
    private NamingContextExt rootNamingContextExt;
    private Object rootContextCacheLock = new Object();
    private INSURLHandler insURLHandler = INSURLHandler.getINSURLHandler();
    private static final int NIBBLES_PER_BYTE = 2;
    private static final int UN_SHIFT = 4;

    public INSURLOperationImpl(ORB orb, Resolver resolver) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.ORB_RESOLVER);
        this.omgWrapper = OMGSystemException.get(orb, CORBALogDomains.ORB_RESOLVER);
        this.bootstrapResolver = resolver;
    }

    private Object getIORFromString(String str) {
        if ((str.length() & 1) == 1) {
            throw this.wrapper.badStringifiedIorLen();
        }
        byte[] bArr = new byte[(str.length() - ORBConstants.STRINGIFY_PREFIX.length()) / 2];
        int length = ORBConstants.STRINGIFY_PREFIX.length();
        int i2 = 0;
        while (length < str.length()) {
            bArr[i2] = (byte) ((ORBUtility.hexOf(str.charAt(length)) << 4) & 240);
            int i3 = i2;
            bArr[i3] = (byte) (bArr[i3] | ((byte) (ORBUtility.hexOf(str.charAt(length + 1)) & 15)));
            length += 2;
            i2++;
        }
        EncapsInputStream encapsInputStreamNewEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(this.orb, bArr, bArr.length, this.orb.getORBData().getGIOPVersion());
        encapsInputStreamNewEncapsInputStream.consumeEndian();
        return encapsInputStreamNewEncapsInputStream.read_Object();
    }

    @Override // com.sun.corba.se.spi.orb.Operation
    public Object operate(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.startsWith(ORBConstants.STRINGIFY_PREFIX)) {
                return getIORFromString(str);
            }
            INSURL url = this.insURLHandler.parseURL(str);
            if (url == null) {
                throw this.omgWrapper.soBadSchemeName();
            }
            return resolveINSURL(url);
        }
        throw this.wrapper.stringExpected();
    }

    private Object resolveINSURL(INSURL insurl) {
        if (insurl.isCorbanameURL()) {
            return resolveCorbaname((CorbanameURL) insurl);
        }
        return resolveCorbaloc((CorbalocURL) insurl);
    }

    private Object resolveCorbaloc(CorbalocURL corbalocURL) {
        Object iORUsingCorbaloc;
        if (corbalocURL.getRIRFlag()) {
            iORUsingCorbaloc = this.bootstrapResolver.resolve(corbalocURL.getKeyString());
        } else {
            iORUsingCorbaloc = getIORUsingCorbaloc(corbalocURL);
        }
        return iORUsingCorbaloc;
    }

    private Object resolveCorbaname(CorbanameURL corbanameURL) {
        NamingContextExt namingContextExtNarrow;
        try {
            if (corbanameURL.getRIRFlag()) {
                namingContextExtNarrow = getDefaultRootNamingContext();
            } else {
                Object iORUsingCorbaloc = getIORUsingCorbaloc(corbanameURL);
                if (iORUsingCorbaloc == null) {
                    return null;
                }
                namingContextExtNarrow = NamingContextExtHelper.narrow(iORUsingCorbaloc);
            }
            String stringifiedName = corbanameURL.getStringifiedName();
            if (stringifiedName == null) {
                return namingContextExtNarrow;
            }
            return namingContextExtNarrow.resolve_str(stringifiedName);
        } catch (Exception e2) {
            clearRootNamingContextCache();
            return null;
        }
    }

    private Object getIORUsingCorbaloc(INSURL insurl) {
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        List<IIOPEndpointInfo> endpointInfo = insurl.getEndpointInfo();
        String keyString = insurl.getKeyString();
        if (keyString == null) {
            return null;
        }
        ObjectKey objectKeyCreate = this.orb.getObjectKeyFactory().create(keyString.getBytes());
        IORTemplate iORTemplateMakeIORTemplate = IORFactories.makeIORTemplate(objectKeyCreate.getTemplate());
        for (IIOPEndpointInfo iIOPEndpointInfo : endpointInfo) {
            IIOPAddress iIOPAddressMakeIIOPAddress = IIOPFactories.makeIIOPAddress(this.orb, iIOPEndpointInfo.getHost(), iIOPEndpointInfo.getPort());
            GIOPVersion gIOPVersion = GIOPVersion.getInstance((byte) iIOPEndpointInfo.getMajor(), (byte) iIOPEndpointInfo.getMinor());
            if (gIOPVersion.equals(GIOPVersion.V1_0)) {
                arrayList.add(IIOPFactories.makeIIOPProfileTemplate(this.orb, gIOPVersion, iIOPAddressMakeIIOPAddress));
            } else if (map.get(gIOPVersion) == null) {
                map.put(gIOPVersion, IIOPFactories.makeIIOPProfileTemplate(this.orb, gIOPVersion, iIOPAddressMakeIIOPAddress));
            } else {
                ((IIOPProfileTemplate) map.get(gIOPVersion)).add(IIOPFactories.makeAlternateIIOPAddressComponent(iIOPAddressMakeIIOPAddress));
            }
        }
        GIOPVersion gIOPVersion2 = this.orb.getORBData().getGIOPVersion();
        IIOPProfileTemplate iIOPProfileTemplate = (IIOPProfileTemplate) map.get(gIOPVersion2);
        if (iIOPProfileTemplate != null) {
            iORTemplateMakeIORTemplate.add(iIOPProfileTemplate);
            map.remove(gIOPVersion2);
        }
        Comparator comparator = new Comparator() { // from class: com.sun.corba.se.impl.resolver.INSURLOperationImpl.1
            @Override // java.util.Comparator
            public int compare(Object obj, Object obj2) {
                GIOPVersion gIOPVersion3 = (GIOPVersion) obj;
                GIOPVersion gIOPVersion4 = (GIOPVersion) obj2;
                if (gIOPVersion3.lessThan(gIOPVersion4)) {
                    return 1;
                }
                return gIOPVersion3.equals(gIOPVersion4) ? 0 : -1;
            }
        };
        ArrayList arrayList2 = new ArrayList(map.keySet());
        Collections.sort(arrayList2, comparator);
        Iterator<E> it = arrayList2.iterator();
        while (it.hasNext()) {
            iORTemplateMakeIORTemplate.add((IIOPProfileTemplate) map.get(it.next()));
        }
        iORTemplateMakeIORTemplate.addAll(arrayList);
        return ORBUtility.makeObjectReference(iORTemplateMakeIORTemplate.makeIOR(this.orb, "", objectKeyCreate.getId()));
    }

    private NamingContextExt getDefaultRootNamingContext() {
        synchronized (this.rootContextCacheLock) {
            if (this.rootNamingContextExt == null) {
                try {
                    this.rootNamingContextExt = NamingContextExtHelper.narrow(this.orb.getLocalResolver().resolve(ORBConstants.PERSISTENT_NAME_SERVICE_NAME));
                } catch (Exception e2) {
                    this.rootNamingContextExt = null;
                }
            }
        }
        return this.rootNamingContextExt;
    }

    private void clearRootNamingContextCache() {
        synchronized (this.rootContextCacheLock) {
            this.rootNamingContextExt = null;
        }
    }
}
