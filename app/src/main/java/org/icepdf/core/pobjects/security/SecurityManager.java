package org.icepdf.core.pobjects.security;

import java.io.InputStream;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/security/SecurityManager.class */
public class SecurityManager {
    private static final Logger logger = Logger.getLogger(SecurityManager.class.toString());
    private EncryptionDictionary encryptDictionary;
    private SecurityHandler securityHandler;
    private static boolean foundJCE;

    static {
        foundJCE = false;
        String defaultSecurityProvider = "org.bouncycastle.jce.provider.BouncyCastleProvider";
        String customSecurityProvider = Defs.sysProperty("org.icepdf.core.security.jceProvider");
        if (customSecurityProvider != null) {
            defaultSecurityProvider = customSecurityProvider;
        }
        try {
            Object provider = Class.forName(defaultSecurityProvider).newInstance();
            Security.insertProviderAt((Provider) provider, 2);
        } catch (ClassNotFoundException e2) {
            logger.log(Level.FINE, "Optional BouncyCastle security provider not found");
        } catch (IllegalAccessException e3) {
            logger.log(Level.FINE, "Optional BouncyCastle security provider could not be created");
        } catch (InstantiationException e4) {
            logger.log(Level.FINE, "Optional BouncyCastle security provider could not be instantiated");
        }
        try {
            Class.forName("javax.crypto.Cipher");
            foundJCE = true;
        } catch (ClassNotFoundException e5) {
            logger.log(Level.SEVERE, "Sun JCE Support Not Found");
        }
    }

    public void dispose() {
    }

    public SecurityManager(Library library, HashMap<Object, Object> encryptionDictionary, List fileID) throws PDFSecurityException {
        this.encryptDictionary = null;
        this.securityHandler = null;
        if (!foundJCE) {
            logger.log(Level.SEVERE, "Sun JCE support was not found on classpath");
            throw new PDFSecurityException("Sun JCE Support Not Found");
        }
        this.encryptDictionary = new EncryptionDictionary(library, encryptionDictionary, fileID);
        if (this.encryptDictionary.getPreferredSecurityHandlerName().getName().equalsIgnoreCase("Standard")) {
            this.securityHandler = new StandardSecurityHandler(this.encryptDictionary);
            this.securityHandler.init();
            return;
        }
        throw new PDFSecurityException("Security Provider Not Found.");
    }

    public Permissions getPermissions() {
        return this.securityHandler.getPermissions();
    }

    public SecurityHandler getSecurityHandler() {
        return this.securityHandler;
    }

    public EncryptionDictionary getEncryptionDictionary() {
        return this.encryptDictionary;
    }

    public byte[] getEncryptionKey() {
        return this.securityHandler.getEncryptionKey();
    }

    public byte[] getDecryptionKey() {
        return this.securityHandler.getDecryptionKey();
    }

    public byte[] encrypt(Reference objectReference, byte[] encryptionKey, byte[] data) {
        return this.securityHandler.encrypt(objectReference, encryptionKey, data);
    }

    public byte[] decrypt(Reference objectReference, byte[] encryptionKey, byte[] data) {
        return this.securityHandler.decrypt(objectReference, encryptionKey, data);
    }

    public InputStream getEncryptionInputStream(Reference objectReference, byte[] encryptionKey, HashMap decodeParams, InputStream input, boolean returnInputIfNullResult) {
        InputStream result = this.securityHandler.getEncryptionInputStream(objectReference, encryptionKey, decodeParams, input);
        if (returnInputIfNullResult && result == null) {
            result = input;
        }
        return result;
    }

    public boolean isAuthorized(String password) {
        return this.securityHandler.isAuthorized(password);
    }
}
