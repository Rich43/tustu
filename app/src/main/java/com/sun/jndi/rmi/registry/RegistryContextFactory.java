package com.sun.jndi.rmi.registry;

import com.sun.jndi.url.rmi.rmiURLContextFactory;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.ObjectFactory;

/* loaded from: rt.jar:com/sun/jndi/rmi/registry/RegistryContextFactory.class */
public class RegistryContextFactory implements ObjectFactory, InitialContextFactory {
    public static final String ADDRESS_TYPE = "URL";

    @Override // javax.naming.spi.InitialContextFactory
    public Context getInitialContext(Hashtable<?, ?> hashtable) throws NamingException {
        if (hashtable != null) {
            hashtable = (Hashtable) hashtable.clone();
        }
        return URLToContext(getInitCtxURL(hashtable), hashtable);
    }

    @Override // javax.naming.spi.ObjectFactory
    public Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws NamingException {
        if (!isRegistryRef(obj)) {
            return null;
        }
        Object objURLsToObject = URLsToObject(getURLs((Reference) obj), hashtable);
        if (objURLsToObject instanceof RegistryContext) {
            ((RegistryContext) objURLsToObject).reference = (Reference) obj;
        }
        return objURLsToObject;
    }

    private static Context URLToContext(String str, Hashtable<?, ?> hashtable) throws NamingException {
        Object objectInstance = new rmiURLContextFactory().getObjectInstance(str, null, null, hashtable);
        if (objectInstance instanceof Context) {
            return (Context) objectInstance;
        }
        throw new NotContextException(str);
    }

    private static Object URLsToObject(String[] strArr, Hashtable<?, ?> hashtable) throws NamingException {
        return new rmiURLContextFactory().getObjectInstance(strArr, null, null, hashtable);
    }

    private static String getInitCtxURL(Hashtable<?, ?> hashtable) {
        String str = null;
        if (hashtable != null) {
            str = (String) hashtable.get(Context.PROVIDER_URL);
        }
        return str != null ? str : "rmi:";
    }

    private static boolean isRegistryRef(Object obj) {
        if (!(obj instanceof Reference)) {
            return false;
        }
        return RegistryContextFactory.class.getName().equals(((Reference) obj).getFactoryClassName());
    }

    private static String[] getURLs(Reference reference) throws NamingException {
        int i2 = 0;
        String[] strArr = new String[reference.size()];
        Enumeration<RefAddr> all = reference.getAll();
        while (all.hasMoreElements()) {
            RefAddr refAddrNextElement2 = all.nextElement2();
            if ((refAddrNextElement2 instanceof StringRefAddr) && refAddrNextElement2.getType().equals("URL")) {
                int i3 = i2;
                i2++;
                strArr[i3] = (String) refAddrNextElement2.getContent();
            }
        }
        if (i2 == 0) {
            throw new ConfigurationException("Reference contains no valid addresses");
        }
        if (i2 == reference.size()) {
            return strArr;
        }
        String[] strArr2 = new String[i2];
        System.arraycopy(strArr, 0, strArr2, 0, i2);
        return strArr2;
    }
}
