package com.sun.naming.internal;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/naming/internal/ResourceManager.class */
public final class ResourceManager {
    private static final String PROVIDER_RESOURCE_FILE_NAME = "jndiprovider.properties";
    private static final String APP_RESOURCE_FILE_NAME = "jndi.properties";
    private static final String JRELIB_PROPERTY_FILE_NAME = "jndi.properties";
    private static final String DISABLE_APP_RESOURCE_FILES = "com.sun.naming.disable.app.resource.files";
    private static final String[] listProperties = {Context.OBJECT_FACTORIES, Context.URL_PKG_PREFIXES, Context.STATE_FACTORIES, LdapContext.CONTROL_FACTORIES};
    private static final VersionHelper helper = VersionHelper.getVersionHelper();
    private static final WeakHashMap<Object, Hashtable<? super String, Object>> propertiesCache = new WeakHashMap<>(11);
    private static final WeakHashMap<ClassLoader, Map<String, List<NamedWeakReference<Object>>>> factoryCache = new WeakHashMap<>(11);
    private static final WeakHashMap<ClassLoader, Map<String, WeakReference<Object>>> urlFactoryCache = new WeakHashMap<>(11);
    private static final WeakReference<Object> NO_FACTORY = new WeakReference<>(null);

    /* loaded from: rt.jar:com/sun/naming/internal/ResourceManager$AppletParameter.class */
    private static class AppletParameter {
        private static final Class<?> clazz = getClass("java.applet.Applet");
        private static final Method getMethod = getMethod(clazz, Constants.GET_PARAMETER, String.class);

        private AppletParameter() {
        }

        private static Class<?> getClass(String str) {
            try {
                return Class.forName(str, true, null);
            } catch (ClassNotFoundException e2) {
                return null;
            }
        }

        private static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
            if (cls != null) {
                try {
                    return cls.getMethod(str, clsArr);
                } catch (NoSuchMethodException e2) {
                    throw new AssertionError(e2);
                }
            }
            return null;
        }

        static Object get(Object obj, String str) {
            if (clazz == null || !clazz.isInstance(obj)) {
                throw new ClassCastException(obj.getClass().getName());
            }
            try {
                return getMethod.invoke(obj, str);
            } catch (IllegalAccessException | InvocationTargetException e2) {
                throw new AssertionError(e2);
            }
        }
    }

    private ResourceManager() {
    }

    public static Hashtable<?, ?> getInitialEnvironment(Hashtable<?, ?> hashtable) throws NamingException {
        String[] strArr = VersionHelper.PROPS;
        if (hashtable == null) {
            hashtable = new Hashtable<>(11);
        }
        Object obj = hashtable.get(Context.APPLET);
        Object[] jndiProperties = helper.getJndiProperties();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            Object jndiProperty = hashtable.get(strArr[i2]);
            if (jndiProperty == null) {
                if (obj != null) {
                    jndiProperty = AppletParameter.get(obj, strArr[i2]);
                }
                if (jndiProperty == null) {
                    jndiProperty = jndiProperties != null ? jndiProperties[i2] : helper.getJndiProperty(i2);
                }
                if (jndiProperty != null) {
                    hashtable.put(strArr[i2], jndiProperty);
                }
            }
        }
        String str = (String) hashtable.get(DISABLE_APP_RESOURCE_FILES);
        if (str != null && str.equalsIgnoreCase("true")) {
            return hashtable;
        }
        mergeTables(hashtable, getApplicationResources());
        return hashtable;
    }

    public static String getProperty(String str, Hashtable<?, ?> hashtable, Context context, boolean z2) throws NamingException {
        String str2 = hashtable != null ? (String) hashtable.get(str) : null;
        if (context == null || (str2 != null && !z2)) {
            return str2;
        }
        String str3 = (String) getProviderResource(context).get(str);
        if (str2 == null) {
            return str3;
        }
        if (str3 == null || !z2) {
            return str2;
        }
        return str2 + CallSiteDescriptor.TOKEN_DELIMITER + str3;
    }

    public static FactoryEnumeration getFactories(String str, Hashtable<?, ?> hashtable, Context context) throws NamingException {
        Map<String, List<NamedWeakReference<Object>>> map;
        String property = getProperty(str, hashtable, context, true);
        if (property == null) {
            return null;
        }
        ClassLoader contextClassLoader = helper.getContextClassLoader();
        synchronized (factoryCache) {
            map = factoryCache.get(contextClassLoader);
            if (map == null) {
                map = new HashMap(11);
                factoryCache.put(contextClassLoader, map);
            }
        }
        synchronized (map) {
            List<NamedWeakReference<Object>> list = map.get(property);
            if (list != null) {
                return list.size() == 0 ? null : new FactoryEnumeration(list, contextClassLoader);
            }
            StringTokenizer stringTokenizer = new StringTokenizer(property, CallSiteDescriptor.TOKEN_DELIMITER);
            ArrayList arrayList = new ArrayList(5);
            while (stringTokenizer.hasMoreTokens()) {
                try {
                    String strNextToken = stringTokenizer.nextToken();
                    arrayList.add(new NamedWeakReference<>(helper.loadClass(strNextToken, contextClassLoader), strNextToken));
                } catch (Exception e2) {
                }
            }
            map.put(property, arrayList);
            return new FactoryEnumeration(arrayList, contextClassLoader);
        }
    }

    public static Object getFactory(String str, Hashtable<?, ?> hashtable, Context context, String str2, String str3) throws NamingException {
        String str4;
        Map<String, WeakReference<Object>> map;
        String property = getProperty(str, hashtable, context, true);
        if (property != null) {
            str4 = property + CallSiteDescriptor.TOKEN_DELIMITER + str3;
        } else {
            str4 = str3;
        }
        ClassLoader contextClassLoader = helper.getContextClassLoader();
        String str5 = str2 + " " + str4;
        synchronized (urlFactoryCache) {
            map = urlFactoryCache.get(contextClassLoader);
            if (map == null) {
                map = new HashMap(11);
                urlFactoryCache.put(contextClassLoader, map);
            }
        }
        synchronized (map) {
            Object objNewInstance = null;
            WeakReference<Object> weakReference = map.get(str5);
            if (weakReference == NO_FACTORY) {
                return null;
            }
            if (weakReference != null) {
                objNewInstance = weakReference.get();
                if (objNewInstance != null) {
                    return objNewInstance;
                }
            }
            StringTokenizer stringTokenizer = new StringTokenizer(str4, CallSiteDescriptor.TOKEN_DELIMITER);
            while (objNewInstance == null && stringTokenizer.hasMoreTokens()) {
                String str6 = stringTokenizer.nextToken() + str2;
                try {
                    try {
                        objNewInstance = helper.loadClass(str6, contextClassLoader).newInstance();
                    } catch (InstantiationException e2) {
                        NamingException namingException = new NamingException("Cannot instantiate " + str6);
                        namingException.setRootCause(e2);
                        throw namingException;
                    } catch (Exception e3) {
                    }
                } catch (IllegalAccessException e4) {
                    NamingException namingException2 = new NamingException("Cannot access " + str6);
                    namingException2.setRootCause(e4);
                    throw namingException2;
                }
            }
            map.put(str5, objNewInstance != null ? new WeakReference<>(objNewInstance) : NO_FACTORY);
            return objNewInstance;
        }
    }

    private static Hashtable<? super String, Object> getProviderResource(Object obj) throws NamingException {
        if (obj == null) {
            return new Hashtable<>(1);
        }
        synchronized (propertiesCache) {
            Class<?> cls = obj.getClass();
            Hashtable<? super String, Object> hashtable = propertiesCache.get(cls);
            if (hashtable != null) {
                return hashtable;
            }
            Properties properties = new Properties();
            InputStream resourceAsStream = helper.getResourceAsStream(cls, PROVIDER_RESOURCE_FILE_NAME);
            if (resourceAsStream != null) {
                try {
                    properties.load(resourceAsStream);
                } catch (IOException e2) {
                    ConfigurationException configurationException = new ConfigurationException("Error reading provider resource file for " + ((Object) cls));
                    configurationException.setRootCause(e2);
                    throw configurationException;
                }
            }
            propertiesCache.put(cls, properties);
            return properties;
        }
    }

    /* JADX WARN: Finally extract failed */
    private static Hashtable<? super String, Object> getApplicationResources() throws NamingException {
        boolean zHasMore;
        ClassLoader contextClassLoader = helper.getContextClassLoader();
        synchronized (propertiesCache) {
            Hashtable<? super String, Object> hashtable = propertiesCache.get(contextClassLoader);
            if (hashtable != null) {
                return hashtable;
            }
            try {
                NamingEnumeration<InputStream> resources = helper.getResources(contextClassLoader, "jndi.properties");
                while (resources.hasMore()) {
                    try {
                        Properties properties = new Properties();
                        InputStream next = resources.next();
                        try {
                            properties.load(next);
                            next.close();
                            if (hashtable == null) {
                                hashtable = properties;
                            } else {
                                mergeTables(hashtable, properties);
                            }
                        } finally {
                        }
                    } finally {
                        while (resources.hasMore()) {
                            resources.next().close();
                        }
                    }
                }
                while (true) {
                    if (!zHasMore) {
                        break;
                    }
                }
                InputStream javaHomeLibStream = helper.getJavaHomeLibStream("jndi.properties");
                if (javaHomeLibStream != null) {
                    try {
                        Properties properties2 = new Properties();
                        properties2.load(javaHomeLibStream);
                        if (hashtable == null) {
                            hashtable = properties2;
                        } else {
                            mergeTables(hashtable, properties2);
                        }
                        javaHomeLibStream.close();
                    } catch (Throwable th) {
                        javaHomeLibStream.close();
                        throw th;
                    }
                }
                if (hashtable == null) {
                    hashtable = new Hashtable<>(11);
                }
                propertiesCache.put(contextClassLoader, hashtable);
                return hashtable;
            } catch (IOException e2) {
                ConfigurationException configurationException = new ConfigurationException("Error reading application resource file");
                configurationException.setRootCause(e2);
                throw configurationException;
            }
        }
    }

    private static void mergeTables(Hashtable<? super String, Object> hashtable, Hashtable<? super String, Object> hashtable2) {
        for (String str : hashtable2.keySet()) {
            Object obj = hashtable.get(str);
            if (obj == null) {
                hashtable.put(str, hashtable2.get(str));
            } else if (isListProperty(str)) {
                hashtable.put(str, ((String) obj) + CallSiteDescriptor.TOKEN_DELIMITER + ((String) hashtable2.get(str)));
            }
        }
    }

    private static boolean isListProperty(String str) {
        String strIntern = str.intern();
        for (int i2 = 0; i2 < listProperties.length; i2++) {
            if (strIntern == listProperties[i2]) {
                return true;
            }
        }
        return false;
    }
}
