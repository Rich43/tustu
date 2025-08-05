package sun.misc;

import java.io.IOException;
import java.io.ObjectInputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;

/* loaded from: rt.jar:sun/misc/JavaxCryptoSealedObjectAccess.class */
public interface JavaxCryptoSealedObjectAccess {
    ObjectInputStream getExtObjectInputStream(SealedObject sealedObject, Cipher cipher) throws BadPaddingException, IllegalBlockSizeException, IOException;
}
