package com.sun.jndi.ldap;

import com.sun.jndi.ldap.spi.LdapDnsProviderResult;
import com.sun.jndi.url.ldap.ldapURLContextFactory;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.naming.AuthenticationException;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.ldap.Control;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.ObjectFactory;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapCtxFactory.class */
public final class LdapCtxFactory implements ObjectFactory, InitialContextFactory {
    public static final String ADDRESS_TYPE = "URL";

    @Override // javax.naming.spi.ObjectFactory
    public Object getObjectInstance(Object obj, Name name, Context context, Hashtable<?, ?> hashtable) throws Exception {
        if (!isLdapRef(obj)) {
            return null;
        }
        return new ldapURLContextFactory().getObjectInstance(getURLs((Reference) obj), name, context, hashtable);
    }

    @Override // javax.naming.spi.InitialContextFactory
    public Context getInitialContext(Hashtable<?, ?> hashtable) throws NamingException {
        String str;
        if (hashtable != null) {
            try {
                str = (String) hashtable.get(Context.PROVIDER_URL);
            } catch (LdapReferralException e2) {
                if (hashtable != null && "throw".equals(hashtable.get(Context.REFERRAL))) {
                    throw e2;
                }
                return (LdapCtx) e2.getReferralContext(hashtable, hashtable != null ? (Control[]) hashtable.get("java.naming.ldap.control.connect") : null);
            }
        } else {
            str = null;
        }
        String str2 = str;
        if (str2 == null) {
            return new LdapCtx("", "localhost", LdapCtx.DEFAULT_PORT, hashtable, false);
        }
        String[] strArrFromList = LdapURL.fromList(str2);
        if (strArrFromList.length == 0) {
            throw new ConfigurationException("java.naming.provider.url property does not contain a URL");
        }
        return getLdapCtxInstance(strArrFromList, hashtable);
    }

    private static boolean isLdapRef(Object obj) {
        if (!(obj instanceof Reference)) {
            return false;
        }
        return LdapCtxFactory.class.getName().equals(((Reference) obj).getFactoryClassName());
    }

    private static String[] getURLs(Reference reference) throws NamingException {
        int i2 = 0;
        String[] strArr = new String[reference.size()];
        Enumeration<RefAddr> all = reference.getAll();
        while (all.hasMoreElements()) {
            RefAddr refAddrNextElement = all.nextElement2();
            if ((refAddrNextElement instanceof StringRefAddr) && refAddrNextElement.getType().equals("URL")) {
                int i3 = i2;
                i2++;
                strArr[i3] = (String) refAddrNextElement.getContent();
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

    public static DirContext getLdapCtxInstance(Object obj, Hashtable<?, ?> hashtable) throws NamingException {
        if (obj instanceof String) {
            return getUsingURL((String) obj, hashtable);
        }
        if (obj instanceof String[]) {
            return getUsingURLs((String[]) obj, hashtable);
        }
        throw new IllegalArgumentException("argument must be an LDAP URL String or array of them");
    }

    private static DirContext getUsingURL(String str, Hashtable<?, ?> hashtable) throws NamingException {
        try {
            LdapDnsProviderResult ldapDnsProviderResultLookupEndpoints = LdapDnsProviderService.getInstance().lookupEndpoints(str, hashtable);
            NamingException namingException = null;
            Iterator<String> it = ldapDnsProviderResultLookupEndpoints.getEndpoints().iterator();
            while (it.hasNext()) {
                try {
                    return getLdapCtxFromUrl(ldapDnsProviderResultLookupEndpoints.getDomainName(), str, new LdapURL(it.next()), hashtable);
                } catch (AuthenticationException e2) {
                    throw e2;
                } catch (NamingException e3) {
                    namingException = e3;
                }
            }
            if (namingException != null) {
                throw namingException;
            }
            throw new NamingException("Could not resolve a valid ldap host");
        } catch (NamingException e4) {
            throw e4;
        } catch (Exception e5) {
            NamingException namingException2 = new NamingException();
            namingException2.setRootCause(e5);
            throw namingException2;
        }
    }

    private static LdapCtx getLdapCtxFromUrl(String str, String str2, LdapURL ldapURL, Hashtable<?, ?> hashtable) throws NamingException {
        LdapCtx ldapCtx = new LdapCtx(ldapURL.getDN(), ldapURL.getHost(), ldapURL.getPort(), hashtable, ldapURL.useSsl());
        ldapCtx.setDomainName(str);
        ldapCtx.setProviderUrl(str2);
        return ldapCtx;
    }

    private static DirContext getUsingURLs(String[] strArr, Hashtable<?, ?> hashtable) throws NamingException {
        NamingException namingException = null;
        for (String str : strArr) {
            try {
                return getUsingURL(str, hashtable);
            } catch (AuthenticationException e2) {
                throw e2;
            } catch (NamingException e3) {
                namingException = e3;
            }
        }
        throw namingException;
    }

    public static Attribute createTypeNameAttr(Class<?> cls) {
        String[] typeNames = getTypeNames(cls, new Vector(10));
        if (typeNames.length > 0) {
            BasicAttribute basicAttribute = new BasicAttribute(Obj.JAVA_ATTRIBUTES[6]);
            for (String str : typeNames) {
                basicAttribute.add(str);
            }
            return basicAttribute;
        }
        return null;
    }

    private static String[] getTypeNames(Class<?> cls, Vector<String> vector) {
        getClassesAux(cls, vector);
        for (Class<?> cls2 : cls.getInterfaces()) {
            getClassesAux(cls2, vector);
        }
        String[] strArr = new String[vector.size()];
        int i2 = 0;
        Iterator<String> it = vector.iterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            strArr[i3] = it.next();
        }
        return strArr;
    }

    private static void getClassesAux(Class<?> cls, Vector<String> vector) {
        if (!vector.contains(cls.getName())) {
            vector.addElement(cls.getName());
        }
        Class<? super Object> superclass = cls.getSuperclass();
        while (true) {
            Class<? super Object> cls2 = superclass;
            if (cls2 != null) {
                getTypeNames(cls2, vector);
                superclass = cls2.getSuperclass();
            } else {
                return;
            }
        }
    }
}
