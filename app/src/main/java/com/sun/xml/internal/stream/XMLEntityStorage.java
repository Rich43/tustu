package com.sun.xml.internal.stream;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.xml.internal.stream.Entity;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/stream/XMLEntityStorage.class */
public class XMLEntityStorage {
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String WARN_ON_DUPLICATE_ENTITYDEF = "http://apache.org/xml/features/warn-on-duplicate-entitydef";
    protected boolean fWarnDuplicateEntityDef;
    protected Entity.ScannedEntity fCurrentEntity;
    private XMLEntityManager fEntityManager;
    protected XMLErrorReporter fErrorReporter;
    protected PropertyManager fPropertyManager;
    private static String gUserDir;
    private static String gEscapedUserDir;
    private static boolean[] gNeedEscaping = new boolean[128];
    private static char[] gAfterEscaping1 = new char[128];
    private static char[] gAfterEscaping2 = new char[128];
    private static char[] gHexChs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected Map<String, Entity> fEntities = new HashMap();
    protected boolean fInExternalSubset = false;

    public XMLEntityStorage(PropertyManager propertyManager) {
        this.fPropertyManager = propertyManager;
    }

    public XMLEntityStorage(XMLEntityManager entityManager) {
        this.fEntityManager = entityManager;
    }

    public void reset(PropertyManager propertyManager) {
        this.fErrorReporter = (XMLErrorReporter) propertyManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fEntities.clear();
        this.fCurrentEntity = null;
    }

    public void reset() {
        this.fEntities.clear();
        this.fCurrentEntity = null;
    }

    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        this.fWarnDuplicateEntityDef = componentManager.getFeature(WARN_ON_DUPLICATE_ENTITYDEF, false);
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fEntities.clear();
        this.fCurrentEntity = null;
    }

    public Entity getEntity(String name) {
        return this.fEntities.get(name);
    }

    public boolean hasEntities() {
        return this.fEntities != null;
    }

    public int getEntitySize() {
        return this.fEntities.size();
    }

    public Enumeration getEntityKeys() {
        return Collections.enumeration(this.fEntities.keySet());
    }

    public void addInternalEntity(String name, String text) {
        if (!this.fEntities.containsKey(name)) {
            Entity entity = new Entity.InternalEntity(name, text, this.fInExternalSubset);
            this.fEntities.put(name, entity);
        } else if (this.fWarnDuplicateEntityDef) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[]{name}, (short) 0);
        }
    }

    public void addExternalEntity(String name, String publicId, String literalSystemId, String baseSystemId) {
        if (!this.fEntities.containsKey(name)) {
            if (baseSystemId == null && this.fCurrentEntity != null && this.fCurrentEntity.entityLocation != null) {
                baseSystemId = this.fCurrentEntity.entityLocation.getExpandedSystemId();
            }
            this.fCurrentEntity = this.fEntityManager.getCurrentEntity();
            Entity entity = new Entity.ExternalEntity(name, new XMLResourceIdentifierImpl(publicId, literalSystemId, baseSystemId, expandSystemId(literalSystemId, baseSystemId)), null, this.fInExternalSubset);
            this.fEntities.put(name, entity);
            return;
        }
        if (this.fWarnDuplicateEntityDef) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[]{name}, (short) 0);
        }
    }

    public boolean isExternalEntity(String entityName) {
        Entity entity = this.fEntities.get(entityName);
        if (entity == null) {
            return false;
        }
        return entity.isExternal();
    }

    public boolean isEntityDeclInExternalSubset(String entityName) {
        Entity entity = this.fEntities.get(entityName);
        if (entity == null) {
            return false;
        }
        return entity.isEntityDeclInExternalSubset();
    }

    public void addUnparsedEntity(String name, String publicId, String systemId, String baseSystemId, String notation) {
        this.fCurrentEntity = this.fEntityManager.getCurrentEntity();
        if (!this.fEntities.containsKey(name)) {
            Entity entity = new Entity.ExternalEntity(name, new XMLResourceIdentifierImpl(publicId, systemId, baseSystemId, null), notation, this.fInExternalSubset);
            this.fEntities.put(name, entity);
        } else if (this.fWarnDuplicateEntityDef) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "MSG_DUPLICATE_ENTITY_DEFINITION", new Object[]{name}, (short) 0);
        }
    }

    public boolean isUnparsedEntity(String entityName) {
        Entity entity = this.fEntities.get(entityName);
        if (entity == null) {
            return false;
        }
        return entity.isUnparsed();
    }

    public boolean isDeclaredEntity(String entityName) {
        Entity entity = this.fEntities.get(entityName);
        return entity != null;
    }

    public static String expandSystemId(String systemId) {
        return expandSystemId(systemId, null);
    }

    static {
        for (int i2 = 0; i2 <= 31; i2++) {
            gNeedEscaping[i2] = true;
            gAfterEscaping1[i2] = gHexChs[i2 >> 4];
            gAfterEscaping2[i2] = gHexChs[i2 & 15];
        }
        gNeedEscaping[127] = true;
        gAfterEscaping1[127] = '7';
        gAfterEscaping2[127] = 'F';
        char[] escChs = {' ', '<', '>', '#', '%', '\"', '{', '}', '|', '\\', '^', '~', '[', ']', '`'};
        for (char ch : escChs) {
            gNeedEscaping[ch] = true;
            gAfterEscaping1[ch] = gHexChs[ch >> 4];
            gAfterEscaping2[ch] = gHexChs[ch & 15];
        }
    }

    private static synchronized String getUserDir() {
        int ch;
        int ch2;
        String userDir = "";
        try {
            userDir = SecuritySupport.getSystemProperty("user.dir");
        } catch (SecurityException e2) {
        }
        if (userDir.length() == 0) {
            return "";
        }
        if (userDir.equals(gUserDir)) {
            return gEscapedUserDir;
        }
        gUserDir = userDir;
        char separator = File.separatorChar;
        String userDir2 = userDir.replace(separator, '/');
        int len = userDir2.length();
        StringBuffer buffer = new StringBuffer(len * 3);
        if (len >= 2 && userDir2.charAt(1) == ':' && (ch2 = Character.toUpperCase(userDir2.charAt(0))) >= 65 && ch2 <= 90) {
            buffer.append('/');
        }
        int i2 = 0;
        while (i2 < len && (ch = userDir2.charAt(i2)) < 128) {
            if (gNeedEscaping[ch]) {
                buffer.append('%');
                buffer.append(gAfterEscaping1[ch]);
                buffer.append(gAfterEscaping2[ch]);
            } else {
                buffer.append((char) ch);
            }
            i2++;
        }
        if (i2 < len) {
            try {
                byte[] bytes = userDir2.substring(i2).getBytes("UTF-8");
                for (byte b2 : bytes) {
                    if (b2 < 0) {
                        int ch3 = b2 + 256;
                        buffer.append('%');
                        buffer.append(gHexChs[ch3 >> 4]);
                        buffer.append(gHexChs[ch3 & 15]);
                    } else if (gNeedEscaping[b2]) {
                        buffer.append('%');
                        buffer.append(gAfterEscaping1[b2]);
                        buffer.append(gAfterEscaping2[b2]);
                    } else {
                        buffer.append((char) b2);
                    }
                }
            } catch (UnsupportedEncodingException e3) {
                return userDir2;
            }
        }
        if (!userDir2.endsWith("/")) {
            buffer.append('/');
        }
        gEscapedUserDir = buffer.toString();
        return gEscapedUserDir;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0036 A[Catch: Exception -> 0x00b9, TryCatch #2 {Exception -> 0x00b9, blocks: (B:14:0x0027, B:16:0x002e, B:19:0x004e, B:25:0x00ab, B:21:0x005f, B:23:0x0069, B:24:0x007e, B:18:0x0036), top: B:37:0x0027, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00c2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String expandSystemId(java.lang.String r8, java.lang.String r9) {
        /*
            Method dump skipped, instructions count: 200
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.stream.XMLEntityStorage.expandSystemId(java.lang.String, java.lang.String):java.lang.String");
    }

    protected static String fixURI(String str) {
        String str2 = str.replace(File.separatorChar, '/');
        if (str2.length() >= 2) {
            char ch1 = str2.charAt(1);
            if (ch1 == ':') {
                char ch0 = Character.toUpperCase(str2.charAt(0));
                if (ch0 >= 'A' && ch0 <= 'Z') {
                    str2 = "/" + str2;
                }
            } else if (ch1 == '/' && str2.charAt(0) == '/') {
                str2 = "file:" + str2;
            }
        }
        return str2;
    }

    public void startExternalSubset() {
        this.fInExternalSubset = true;
    }

    public void endExternalSubset() {
        this.fInExternalSubset = false;
    }
}
