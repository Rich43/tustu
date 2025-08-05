package com.sun.corba.se.spi.ior.iiop;

import com.sun.corba.se.impl.ior.iiop.AlternateIIOPAddressComponentImpl;
import com.sun.corba.se.impl.ior.iiop.CodeSetsComponentImpl;
import com.sun.corba.se.impl.ior.iiop.IIOPAddressImpl;
import com.sun.corba.se.impl.ior.iiop.IIOPProfileImpl;
import com.sun.corba.se.impl.ior.iiop.IIOPProfileTemplateImpl;
import com.sun.corba.se.impl.ior.iiop.JavaCodebaseComponentImpl;
import com.sun.corba.se.impl.ior.iiop.JavaSerializationComponent;
import com.sun.corba.se.impl.ior.iiop.MaxStreamFormatVersionComponentImpl;
import com.sun.corba.se.impl.ior.iiop.ORBTypeComponentImpl;
import com.sun.corba.se.impl.ior.iiop.RequestPartitioningComponentImpl;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.ior.EncapsulationFactoryBase;
import com.sun.corba.se.spi.ior.Identifiable;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.IOP.TaggedProfile;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/iiop/IIOPFactories.class */
public abstract class IIOPFactories {
    private IIOPFactories() {
    }

    public static IdentifiableFactory makeRequestPartitioningComponentFactory() {
        return new EncapsulationFactoryBase(ORBConstants.TAG_REQUEST_PARTITIONING_ID) { // from class: com.sun.corba.se.spi.ior.iiop.IIOPFactories.1
            @Override // com.sun.corba.se.spi.ior.EncapsulationFactoryBase
            public Identifiable readContents(InputStream inputStream) {
                return new RequestPartitioningComponentImpl(inputStream.read_ulong());
            }
        };
    }

    public static RequestPartitioningComponent makeRequestPartitioningComponent(int i2) {
        return new RequestPartitioningComponentImpl(i2);
    }

    public static IdentifiableFactory makeAlternateIIOPAddressComponentFactory() {
        return new EncapsulationFactoryBase(3) { // from class: com.sun.corba.se.spi.ior.iiop.IIOPFactories.2
            @Override // com.sun.corba.se.spi.ior.EncapsulationFactoryBase
            public Identifiable readContents(InputStream inputStream) {
                return new AlternateIIOPAddressComponentImpl(new IIOPAddressImpl(inputStream));
            }
        };
    }

    public static AlternateIIOPAddressComponent makeAlternateIIOPAddressComponent(IIOPAddress iIOPAddress) {
        return new AlternateIIOPAddressComponentImpl(iIOPAddress);
    }

    public static IdentifiableFactory makeCodeSetsComponentFactory() {
        return new EncapsulationFactoryBase(1) { // from class: com.sun.corba.se.spi.ior.iiop.IIOPFactories.3
            @Override // com.sun.corba.se.spi.ior.EncapsulationFactoryBase
            public Identifiable readContents(InputStream inputStream) {
                return new CodeSetsComponentImpl(inputStream);
            }
        };
    }

    public static CodeSetsComponent makeCodeSetsComponent(ORB orb) {
        return new CodeSetsComponentImpl(orb);
    }

    public static IdentifiableFactory makeJavaCodebaseComponentFactory() {
        return new EncapsulationFactoryBase(25) { // from class: com.sun.corba.se.spi.ior.iiop.IIOPFactories.4
            @Override // com.sun.corba.se.spi.ior.EncapsulationFactoryBase
            public Identifiable readContents(InputStream inputStream) {
                return new JavaCodebaseComponentImpl(inputStream.read_string());
            }
        };
    }

    public static JavaCodebaseComponent makeJavaCodebaseComponent(String str) {
        return new JavaCodebaseComponentImpl(str);
    }

    public static IdentifiableFactory makeORBTypeComponentFactory() {
        return new EncapsulationFactoryBase(0) { // from class: com.sun.corba.se.spi.ior.iiop.IIOPFactories.5
            @Override // com.sun.corba.se.spi.ior.EncapsulationFactoryBase
            public Identifiable readContents(InputStream inputStream) {
                return new ORBTypeComponentImpl(inputStream.read_ulong());
            }
        };
    }

    public static ORBTypeComponent makeORBTypeComponent(int i2) {
        return new ORBTypeComponentImpl(i2);
    }

    public static IdentifiableFactory makeMaxStreamFormatVersionComponentFactory() {
        return new EncapsulationFactoryBase(38) { // from class: com.sun.corba.se.spi.ior.iiop.IIOPFactories.6
            @Override // com.sun.corba.se.spi.ior.EncapsulationFactoryBase
            public Identifiable readContents(InputStream inputStream) {
                return new MaxStreamFormatVersionComponentImpl(inputStream.read_octet());
            }
        };
    }

    public static MaxStreamFormatVersionComponent makeMaxStreamFormatVersionComponent() {
        return new MaxStreamFormatVersionComponentImpl();
    }

    public static IdentifiableFactory makeJavaSerializationComponentFactory() {
        return new EncapsulationFactoryBase(ORBConstants.TAG_JAVA_SERIALIZATION_ID) { // from class: com.sun.corba.se.spi.ior.iiop.IIOPFactories.7
            @Override // com.sun.corba.se.spi.ior.EncapsulationFactoryBase
            public Identifiable readContents(InputStream inputStream) {
                return new JavaSerializationComponent(inputStream.read_octet());
            }
        };
    }

    public static JavaSerializationComponent makeJavaSerializationComponent() {
        return JavaSerializationComponent.singleton();
    }

    public static IdentifiableFactory makeIIOPProfileFactory() {
        return new EncapsulationFactoryBase(0) { // from class: com.sun.corba.se.spi.ior.iiop.IIOPFactories.8
            @Override // com.sun.corba.se.spi.ior.EncapsulationFactoryBase
            public Identifiable readContents(InputStream inputStream) {
                return new IIOPProfileImpl(inputStream);
            }
        };
    }

    public static IIOPProfile makeIIOPProfile(ORB orb, ObjectKeyTemplate objectKeyTemplate, ObjectId objectId, IIOPProfileTemplate iIOPProfileTemplate) {
        return new IIOPProfileImpl(orb, objectKeyTemplate, objectId, iIOPProfileTemplate);
    }

    public static IIOPProfile makeIIOPProfile(ORB orb, TaggedProfile taggedProfile) {
        return new IIOPProfileImpl(orb, taggedProfile);
    }

    public static IdentifiableFactory makeIIOPProfileTemplateFactory() {
        return new EncapsulationFactoryBase(0) { // from class: com.sun.corba.se.spi.ior.iiop.IIOPFactories.9
            @Override // com.sun.corba.se.spi.ior.EncapsulationFactoryBase
            public Identifiable readContents(InputStream inputStream) {
                return new IIOPProfileTemplateImpl(inputStream);
            }
        };
    }

    public static IIOPProfileTemplate makeIIOPProfileTemplate(ORB orb, GIOPVersion gIOPVersion, IIOPAddress iIOPAddress) {
        return new IIOPProfileTemplateImpl(orb, gIOPVersion, iIOPAddress);
    }

    public static IIOPAddress makeIIOPAddress(ORB orb, String str, int i2) {
        return new IIOPAddressImpl(orb, str, i2);
    }

    public static IIOPAddress makeIIOPAddress(InputStream inputStream) {
        return new IIOPAddressImpl(inputStream);
    }
}
