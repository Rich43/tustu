package javax.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.CryptoPolicyParser;

/* loaded from: jce.jar:javax/crypto/CryptoPermissions.class */
final class CryptoPermissions extends PermissionCollection implements Serializable {
    private static final long serialVersionUID = 4946547168093391015L;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("perms", Hashtable.class)};
    private transient ConcurrentHashMap<String, PermissionCollection> perms = new ConcurrentHashMap<>(7);

    CryptoPermissions() {
    }

    void load(InputStream inputStream) throws CryptoPolicyParser.ParsingException, IOException {
        CryptoPolicyParser cryptoPolicyParser = new CryptoPolicyParser();
        cryptoPolicyParser.read(new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
        for (CryptoPermission cryptoPermission : cryptoPolicyParser.getPermissions()) {
            add(cryptoPermission);
        }
    }

    boolean isEmpty() {
        return this.perms.isEmpty();
    }

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        if (isReadOnly()) {
            throw new SecurityException("Attempt to add a Permission to a readonly CryptoPermissions object");
        }
        if (!(permission instanceof CryptoPermission)) {
            return;
        }
        CryptoPermission cryptoPermission = (CryptoPermission) permission;
        PermissionCollection permissionCollection = getPermissionCollection(cryptoPermission);
        permissionCollection.add(cryptoPermission);
        this.perms.putIfAbsent(cryptoPermission.getAlgorithm(), permissionCollection);
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        if (!(permission instanceof CryptoPermission)) {
            return false;
        }
        CryptoPermission cryptoPermission = (CryptoPermission) permission;
        return getPermissionCollection(cryptoPermission.getAlgorithm()).implies(cryptoPermission);
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        return new PermissionsEnumerator(this.perms.elements());
    }

    CryptoPermissions getMinimum(CryptoPermissions cryptoPermissions) {
        CryptoPermission[] minimum;
        if (cryptoPermissions == null) {
            return null;
        }
        if (this.perms.containsKey("CryptoAllPermission")) {
            return cryptoPermissions;
        }
        if (cryptoPermissions.perms.containsKey("CryptoAllPermission")) {
            return this;
        }
        CryptoPermissions cryptoPermissions2 = new CryptoPermissions();
        PermissionCollection permissionCollection = cryptoPermissions.perms.get("*");
        int maxKeySize = 0;
        if (permissionCollection != null) {
            maxKeySize = ((CryptoPermission) permissionCollection.elements().nextElement2()).getMaxKeySize();
        }
        Enumeration<String> enumerationKeys = this.perms.keys();
        while (enumerationKeys.hasMoreElements()) {
            String strNextElement2 = enumerationKeys.nextElement2();
            PermissionCollection permissionCollection2 = this.perms.get(strNextElement2);
            PermissionCollection permissionCollection3 = cryptoPermissions.perms.get(strNextElement2);
            if (permissionCollection3 == null) {
                if (permissionCollection != null) {
                    minimum = getMinimum(maxKeySize, permissionCollection2);
                }
            } else {
                minimum = getMinimum(permissionCollection2, permissionCollection3);
            }
            for (CryptoPermission cryptoPermission : minimum) {
                cryptoPermissions2.add(cryptoPermission);
            }
        }
        PermissionCollection permissionCollection4 = this.perms.get("*");
        if (permissionCollection4 == null) {
            return cryptoPermissions2;
        }
        int maxKeySize2 = ((CryptoPermission) permissionCollection4.elements().nextElement2()).getMaxKeySize();
        Enumeration<String> enumerationKeys2 = cryptoPermissions.perms.keys();
        while (enumerationKeys2.hasMoreElements()) {
            String strNextElement22 = enumerationKeys2.nextElement2();
            if (!this.perms.containsKey(strNextElement22)) {
                for (CryptoPermission cryptoPermission2 : getMinimum(maxKeySize2, cryptoPermissions.perms.get(strNextElement22))) {
                    cryptoPermissions2.add(cryptoPermission2);
                }
            }
        }
        return cryptoPermissions2;
    }

    private CryptoPermission[] getMinimum(PermissionCollection permissionCollection, PermissionCollection permissionCollection2) {
        Vector vector = new Vector(2);
        Enumeration<Permission> enumerationElements = permissionCollection.elements();
        while (enumerationElements.hasMoreElements()) {
            CryptoPermission cryptoPermission = (CryptoPermission) enumerationElements.nextElement2();
            Enumeration<Permission> enumerationElements2 = permissionCollection2.elements();
            while (true) {
                if (enumerationElements2.hasMoreElements()) {
                    CryptoPermission cryptoPermission2 = (CryptoPermission) enumerationElements2.nextElement2();
                    if (cryptoPermission2.implies(cryptoPermission)) {
                        vector.addElement(cryptoPermission);
                        break;
                    }
                    if (cryptoPermission.implies(cryptoPermission2)) {
                        vector.addElement(cryptoPermission2);
                    }
                }
            }
        }
        CryptoPermission[] cryptoPermissionArr = new CryptoPermission[vector.size()];
        vector.copyInto(cryptoPermissionArr);
        return cryptoPermissionArr;
    }

    private CryptoPermission[] getMinimum(int i2, PermissionCollection permissionCollection) {
        Vector vector = new Vector(1);
        Enumeration<Permission> enumerationElements = permissionCollection.elements();
        while (enumerationElements.hasMoreElements()) {
            CryptoPermission cryptoPermission = (CryptoPermission) enumerationElements.nextElement2();
            if (cryptoPermission.getMaxKeySize() <= i2) {
                vector.addElement(cryptoPermission);
            } else if (cryptoPermission.getCheckParam()) {
                vector.addElement(new CryptoPermission(cryptoPermission.getAlgorithm(), i2, cryptoPermission.getAlgorithmParameterSpec(), cryptoPermission.getExemptionMechanism()));
            } else {
                vector.addElement(new CryptoPermission(cryptoPermission.getAlgorithm(), i2, cryptoPermission.getExemptionMechanism()));
            }
        }
        CryptoPermission[] cryptoPermissionArr = new CryptoPermission[vector.size()];
        vector.copyInto(cryptoPermissionArr);
        return cryptoPermissionArr;
    }

    PermissionCollection getPermissionCollection(String str) {
        PermissionCollection permissionCollection = this.perms.get("CryptoAllPermission");
        if (permissionCollection == null) {
            permissionCollection = this.perms.get(str);
            if (permissionCollection == null) {
                permissionCollection = this.perms.get("*");
            }
        }
        return permissionCollection;
    }

    private PermissionCollection getPermissionCollection(CryptoPermission cryptoPermission) {
        PermissionCollection permissionCollectionNewPermissionCollection = this.perms.get(cryptoPermission.getAlgorithm());
        if (permissionCollectionNewPermissionCollection == null) {
            permissionCollectionNewPermissionCollection = cryptoPermission.newPermissionCollection();
        }
        return permissionCollectionNewPermissionCollection;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Hashtable hashtable = (Hashtable) objectInputStream.readFields().get("perms", (Object) null);
        if (hashtable != null) {
            this.perms = new ConcurrentHashMap<>(hashtable);
        } else {
            this.perms = new ConcurrentHashMap<>();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.putFields().put("perms", new Hashtable(this.perms));
        objectOutputStream.writeFields();
    }
}
