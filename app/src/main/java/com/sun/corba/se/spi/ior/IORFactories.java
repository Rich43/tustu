package com.sun.corba.se.spi.ior;

import com.sun.corba.se.impl.ior.IORImpl;
import com.sun.corba.se.impl.ior.IORTemplateImpl;
import com.sun.corba.se.impl.ior.IORTemplateListImpl;
import com.sun.corba.se.impl.ior.ObjectIdImpl;
import com.sun.corba.se.impl.ior.ObjectKeyFactoryImpl;
import com.sun.corba.se.impl.ior.ObjectKeyImpl;
import com.sun.corba.se.impl.ior.ObjectReferenceFactoryImpl;
import com.sun.corba.se.impl.ior.ObjectReferenceProducerBase;
import com.sun.corba.se.impl.ior.ObjectReferenceTemplateImpl;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.orb.ORB;
import java.io.Serializable;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ValueFactory;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/IORFactories.class */
public class IORFactories {
    private IORFactories() {
    }

    public static ObjectId makeObjectId(byte[] bArr) {
        return new ObjectIdImpl(bArr);
    }

    public static ObjectKey makeObjectKey(ObjectKeyTemplate objectKeyTemplate, ObjectId objectId) {
        return new ObjectKeyImpl(objectKeyTemplate, objectId);
    }

    public static IOR makeIOR(ORB orb, String str) {
        return new IORImpl(orb, str);
    }

    public static IOR makeIOR(ORB orb) {
        return new IORImpl(orb);
    }

    public static IOR makeIOR(InputStream inputStream) {
        return new IORImpl(inputStream);
    }

    public static IORTemplate makeIORTemplate(ObjectKeyTemplate objectKeyTemplate) {
        return new IORTemplateImpl(objectKeyTemplate);
    }

    public static IORTemplate makeIORTemplate(InputStream inputStream) {
        return new IORTemplateImpl(inputStream);
    }

    public static IORTemplateList makeIORTemplateList() {
        return new IORTemplateListImpl();
    }

    public static IORTemplateList makeIORTemplateList(InputStream inputStream) {
        return new IORTemplateListImpl(inputStream);
    }

    public static IORFactory getIORFactory(ObjectReferenceTemplate objectReferenceTemplate) {
        if (objectReferenceTemplate instanceof ObjectReferenceTemplateImpl) {
            return ((ObjectReferenceTemplateImpl) objectReferenceTemplate).getIORFactory();
        }
        throw new BAD_PARAM();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static IORTemplateList getIORTemplateList(ObjectReferenceFactory objectReferenceFactory) {
        if (objectReferenceFactory instanceof ObjectReferenceProducerBase) {
            return ((ObjectReferenceProducerBase) objectReferenceFactory).getIORTemplateList();
        }
        throw new BAD_PARAM();
    }

    public static ObjectReferenceTemplate makeObjectReferenceTemplate(ORB orb, IORTemplate iORTemplate) {
        return new ObjectReferenceTemplateImpl(orb, iORTemplate);
    }

    public static ObjectReferenceFactory makeObjectReferenceFactory(ORB orb, IORTemplateList iORTemplateList) {
        return new ObjectReferenceFactoryImpl(orb, iORTemplateList);
    }

    public static ObjectKeyFactory makeObjectKeyFactory(ORB orb) {
        return new ObjectKeyFactoryImpl(orb);
    }

    public static IOR getIOR(Object object) {
        return ORBUtility.getIOR(object);
    }

    public static Object makeObjectReference(IOR ior) {
        return ORBUtility.makeObjectReference(ior);
    }

    public static void registerValueFactories(ORB orb) {
        orb.register_value_factory(ObjectReferenceTemplateImpl.repositoryId, new ValueFactory() { // from class: com.sun.corba.se.spi.ior.IORFactories.1
            @Override // org.omg.CORBA.portable.ValueFactory
            public Serializable read_value(InputStream inputStream) {
                return new ObjectReferenceTemplateImpl(inputStream);
            }
        });
        orb.register_value_factory(ObjectReferenceFactoryImpl.repositoryId, new ValueFactory() { // from class: com.sun.corba.se.spi.ior.IORFactories.2
            @Override // org.omg.CORBA.portable.ValueFactory
            public Serializable read_value(InputStream inputStream) {
                return new ObjectReferenceFactoryImpl(inputStream);
            }
        });
    }
}
