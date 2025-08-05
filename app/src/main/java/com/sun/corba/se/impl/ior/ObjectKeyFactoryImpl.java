package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.encoding.EncapsInputStream;
import com.sun.corba.se.impl.logging.IORSystemException;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.ObjectKeyFactory;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.io.IOException;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.OctetSeqHolder;
import org.omg.CORBA_2_3.portable.InputStream;
import sun.corba.EncapsInputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectKeyFactoryImpl.class */
public class ObjectKeyFactoryImpl implements ObjectKeyFactory {
    public static final int MAGIC_BASE = -1347695874;
    public static final int JAVAMAGIC_OLD = -1347695874;
    public static final int JAVAMAGIC_NEW = -1347695873;
    public static final int JAVAMAGIC_NEWER = -1347695872;
    public static final int MAX_MAGIC = -1347695872;
    public static final byte JDK1_3_1_01_PATCH_LEVEL = 1;
    private final ORB orb;
    private IORSystemException wrapper;
    private Handler fullKey = new Handler() { // from class: com.sun.corba.se.impl.ior.ObjectKeyFactoryImpl.1
        @Override // com.sun.corba.se.impl.ior.Handler
        public ObjectKeyTemplate handle(int i2, int i3, InputStream inputStream, OctetSeqHolder octetSeqHolder) {
            ObjectKeyTemplate jIDLObjectKeyTemplate = null;
            if (i3 >= 32 && i3 <= 63) {
                jIDLObjectKeyTemplate = i2 >= -1347695872 ? new POAObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, i2, i3, inputStream, octetSeqHolder) : new OldPOAObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, i2, i3, inputStream, octetSeqHolder);
            } else if (i3 >= 0 && i3 < 32) {
                jIDLObjectKeyTemplate = i2 >= -1347695872 ? new JIDLObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, i2, i3, inputStream, octetSeqHolder) : new OldJIDLObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, i2, i3, inputStream, octetSeqHolder);
            }
            return jIDLObjectKeyTemplate;
        }
    };
    private Handler oktempOnly = new Handler() { // from class: com.sun.corba.se.impl.ior.ObjectKeyFactoryImpl.2
        @Override // com.sun.corba.se.impl.ior.Handler
        public ObjectKeyTemplate handle(int i2, int i3, InputStream inputStream, OctetSeqHolder octetSeqHolder) {
            ObjectKeyTemplate jIDLObjectKeyTemplate = null;
            if (i3 >= 32 && i3 <= 63) {
                jIDLObjectKeyTemplate = i2 >= -1347695872 ? new POAObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, i2, i3, inputStream) : new OldPOAObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, i2, i3, inputStream);
            } else if (i3 >= 0 && i3 < 32) {
                jIDLObjectKeyTemplate = i2 >= -1347695872 ? new JIDLObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, i2, i3, inputStream) : new OldJIDLObjectKeyTemplate(ObjectKeyFactoryImpl.this.orb, i2, i3, inputStream);
            }
            return jIDLObjectKeyTemplate;
        }
    };

    public ObjectKeyFactoryImpl(ORB orb) {
        this.orb = orb;
        this.wrapper = IORSystemException.get(orb, CORBALogDomains.OA_IOR);
    }

    private boolean validMagic(int i2) {
        return i2 >= -1347695874 && i2 <= -1347695872;
    }

    private ObjectKeyTemplate create(InputStream inputStream, Handler handler, OctetSeqHolder octetSeqHolder) {
        ObjectKeyTemplate objectKeyTemplateHandle = null;
        try {
            inputStream.mark(0);
            int i2 = inputStream.read_long();
            if (validMagic(i2)) {
                objectKeyTemplateHandle = handler.handle(i2, inputStream.read_long(), inputStream, octetSeqHolder);
            }
        } catch (MARSHAL e2) {
        }
        if (objectKeyTemplateHandle == null) {
            try {
                inputStream.reset();
            } catch (IOException e3) {
            }
        }
        return objectKeyTemplateHandle;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyFactory
    public ObjectKey create(byte[] bArr) {
        OctetSeqHolder octetSeqHolder = new OctetSeqHolder();
        EncapsInputStream encapsInputStreamNewEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(this.orb, bArr, bArr.length);
        ObjectKeyTemplate objectKeyTemplateCreate = create(encapsInputStreamNewEncapsInputStream, this.fullKey, octetSeqHolder);
        if (objectKeyTemplateCreate == null) {
            objectKeyTemplateCreate = new WireObjectKeyTemplate(encapsInputStreamNewEncapsInputStream, octetSeqHolder);
        }
        return new ObjectKeyImpl(objectKeyTemplateCreate, new ObjectIdImpl(octetSeqHolder.value));
    }

    @Override // com.sun.corba.se.spi.ior.ObjectKeyFactory
    public ObjectKeyTemplate createTemplate(InputStream inputStream) {
        ObjectKeyTemplate objectKeyTemplateCreate = create(inputStream, this.oktempOnly, null);
        if (objectKeyTemplateCreate == null) {
            objectKeyTemplateCreate = new WireObjectKeyTemplate(this.orb);
        }
        return objectKeyTemplateCreate;
    }
}
