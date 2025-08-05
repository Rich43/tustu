package com.sun.corba.se.impl.orb;

import com.sun.corba.se.impl.orbutil.GetPropertyAction;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.orb.DataCollector;
import com.sun.corba.se.spi.orb.PropertyParser;
import java.applet.Applet;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/DataCollectorBase.class */
public abstract class DataCollectorBase implements DataCollector {
    private PropertyParser parser;
    private Set propertyNames;
    private Set propertyPrefixes;
    private Set URLPropertyNames = new HashSet();
    protected String localHostName;
    protected String configurationHostName;
    private boolean setParserCalled;
    private Properties originalProps;
    private Properties resultProps;

    @Override // com.sun.corba.se.spi.orb.DataCollector
    public abstract boolean isApplet();

    protected abstract void collect();

    public DataCollectorBase(Properties properties, String str, String str2) {
        this.URLPropertyNames.add(ORBConstants.INITIAL_SERVICES_PROPERTY);
        this.propertyNames = new HashSet();
        this.propertyNames.add(ORBConstants.ORB_INIT_REF_PROPERTY);
        this.propertyPrefixes = new HashSet();
        this.originalProps = properties;
        this.localHostName = str;
        this.configurationHostName = str2;
        this.setParserCalled = false;
        this.resultProps = new Properties();
    }

    @Override // com.sun.corba.se.spi.orb.DataCollector
    public boolean initialHostIsLocal() {
        checkSetParserCalled();
        return this.localHostName.equals(this.resultProps.getProperty(ORBConstants.INITIAL_HOST_PROPERTY));
    }

    @Override // com.sun.corba.se.spi.orb.DataCollector
    public void setParser(PropertyParser propertyParser) {
        Iterator it = propertyParser.iterator();
        while (it.hasNext()) {
            ParserAction parserAction = (ParserAction) it.next();
            if (parserAction.isPrefix()) {
                this.propertyPrefixes.add(parserAction.getPropertyName());
            } else {
                this.propertyNames.add(parserAction.getPropertyName());
            }
        }
        collect();
        this.setParserCalled = true;
    }

    @Override // com.sun.corba.se.spi.orb.DataCollector
    public Properties getProperties() {
        checkSetParserCalled();
        return this.resultProps;
    }

    protected void checkPropertyDefaults() {
        String property = this.resultProps.getProperty(ORBConstants.INITIAL_HOST_PROPERTY);
        if (property == null || property.equals("")) {
            setProperty(ORBConstants.INITIAL_HOST_PROPERTY, this.configurationHostName);
        }
        String property2 = this.resultProps.getProperty(ORBConstants.SERVER_HOST_PROPERTY);
        if (property2 == null || property2.equals("") || property2.equals("0.0.0.0") || property2.equals("::") || property2.toLowerCase().equals("::ffff:0.0.0.0")) {
            setProperty(ORBConstants.SERVER_HOST_PROPERTY, this.localHostName);
            setProperty(ORBConstants.LISTEN_ON_ALL_INTERFACES, ORBConstants.LISTEN_ON_ALL_INTERFACES);
        }
    }

    protected void findPropertiesFromArgs(String[] strArr) {
        if (strArr == null) {
            return;
        }
        int i2 = 0;
        while (i2 < strArr.length) {
            String str = null;
            String strFindMatchingPropertyName = null;
            if (strArr[i2] != null && strArr[i2].startsWith("-ORB")) {
                strFindMatchingPropertyName = findMatchingPropertyName(this.propertyNames, strArr[i2].substring(1));
                if (strFindMatchingPropertyName != null && i2 + 1 < strArr.length && strArr[i2 + 1] != null) {
                    i2++;
                    str = strArr[i2];
                }
            }
            if (str != null) {
                setProperty(strFindMatchingPropertyName, str);
            }
            i2++;
        }
    }

    protected void findPropertiesFromApplet(final Applet applet) {
        if (applet == null) {
            return;
        }
        findPropertiesByName(this.propertyNames.iterator(), new PropertyCallback() { // from class: com.sun.corba.se.impl.orb.DataCollectorBase.1
            @Override // com.sun.corba.se.impl.orb.PropertyCallback
            public String get(String str) {
                return applet.getParameter(str);
            }
        });
        findPropertiesByName(this.URLPropertyNames.iterator(), new PropertyCallback() { // from class: com.sun.corba.se.impl.orb.DataCollectorBase.2
            @Override // com.sun.corba.se.impl.orb.PropertyCallback
            public String get(String str) {
                String property = DataCollectorBase.this.resultProps.getProperty(str);
                if (property == null) {
                    return null;
                }
                try {
                    return new URL(applet.getDocumentBase(), property).toExternalForm();
                } catch (MalformedURLException e2) {
                    return property;
                }
            }
        });
    }

    private void doProperties(final Properties properties) {
        PropertyCallback propertyCallback = new PropertyCallback() { // from class: com.sun.corba.se.impl.orb.DataCollectorBase.3
            @Override // com.sun.corba.se.impl.orb.PropertyCallback
            public String get(String str) {
                return properties.getProperty(str);
            }
        };
        findPropertiesByName(this.propertyNames.iterator(), propertyCallback);
        findPropertiesByPrefix(this.propertyPrefixes, makeIterator(properties.propertyNames()), propertyCallback);
    }

    protected void findPropertiesFromFile() {
        Properties fileProperties = getFileProperties();
        if (fileProperties == null) {
            return;
        }
        doProperties(fileProperties);
    }

    protected void findPropertiesFromProperties() {
        if (this.originalProps == null) {
            return;
        }
        doProperties(this.originalProps);
    }

    protected void findPropertiesFromSystem() {
        Set cORBAPrefixes = getCORBAPrefixes(this.propertyNames);
        Set cORBAPrefixes2 = getCORBAPrefixes(this.propertyPrefixes);
        PropertyCallback propertyCallback = new PropertyCallback() { // from class: com.sun.corba.se.impl.orb.DataCollectorBase.4
            @Override // com.sun.corba.se.impl.orb.PropertyCallback
            public String get(String str) {
                return DataCollectorBase.getSystemProperty(str);
            }
        };
        findPropertiesByName(cORBAPrefixes.iterator(), propertyCallback);
        findPropertiesByPrefix(cORBAPrefixes2, getSystemPropertyNames(), propertyCallback);
    }

    private void setProperty(String str, String str2) {
        if (str.equals(ORBConstants.ORB_INIT_REF_PROPERTY)) {
            StringTokenizer stringTokenizer = new StringTokenizer(str2, "=");
            if (stringTokenizer.countTokens() != 2) {
                throw new IllegalArgumentException();
            }
            this.resultProps.setProperty(str + "." + stringTokenizer.nextToken(), stringTokenizer.nextToken());
            return;
        }
        this.resultProps.setProperty(str, str2);
    }

    private void checkSetParserCalled() {
        if (!this.setParserCalled) {
            throw new IllegalStateException("setParser not called.");
        }
    }

    private void findPropertiesByPrefix(Set set, Iterator it, PropertyCallback propertyCallback) {
        while (it.hasNext()) {
            String str = (String) it.next();
            Iterator it2 = set.iterator();
            while (it2.hasNext()) {
                if (str.startsWith((String) it2.next())) {
                    setProperty(str, propertyCallback.get(str));
                }
            }
        }
    }

    private void findPropertiesByName(Iterator it, PropertyCallback propertyCallback) {
        while (it.hasNext()) {
            String str = (String) it.next();
            String str2 = propertyCallback.get(str);
            if (str2 != null) {
                setProperty(str, str2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getSystemProperty(String str) {
        return (String) AccessController.doPrivileged(new GetPropertyAction(str));
    }

    private String findMatchingPropertyName(Set set, String str) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (str2.endsWith(str)) {
                return str2;
            }
        }
        return null;
    }

    private static Iterator makeIterator(final Enumeration enumeration) {
        return new Iterator() { // from class: com.sun.corba.se.impl.orb.DataCollectorBase.5
            @Override // java.util.Iterator
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override // java.util.Iterator
            public Object next() {
                return enumeration.nextElement2();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static Iterator getSystemPropertyNames() {
        return makeIterator((Enumeration) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.orb.DataCollectorBase.6
            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperties().propertyNames();
            }
        }));
    }

    private void getPropertiesFromFile(Properties properties, String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                return;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                properties.load(fileInputStream);
                fileInputStream.close();
            } catch (Throwable th) {
                fileInputStream.close();
                throw th;
            }
        } catch (Exception e2) {
        }
    }

    private Properties getFileProperties() {
        Properties properties = new Properties();
        getPropertiesFromFile(properties, getSystemProperty("java.home") + File.separator + "lib" + File.separator + "orb.properties");
        Properties properties2 = new Properties(properties);
        getPropertiesFromFile(properties2, getSystemProperty("user.home") + File.separator + "orb.properties");
        return properties2;
    }

    private boolean hasCORBAPrefix(String str) {
        return str.startsWith(ORBConstants.ORG_OMG_PREFIX) || str.startsWith(ORBConstants.SUN_PREFIX) || str.startsWith(ORBConstants.SUN_LC_PREFIX) || str.startsWith(ORBConstants.SUN_LC_VERSION_PREFIX);
    }

    private Set getCORBAPrefixes(Set set) {
        HashSet hashSet = new HashSet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (hasCORBAPrefix(str)) {
                hashSet.add(str);
            }
        }
        return hashSet;
    }
}
