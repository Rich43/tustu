package com.sun.corba.se.spi.copyobject;

import com.sun.corba.se.impl.copyobject.FallbackObjectCopierImpl;
import com.sun.corba.se.impl.copyobject.JavaStreamObjectCopierImpl;
import com.sun.corba.se.impl.copyobject.ORBStreamObjectCopierImpl;
import com.sun.corba.se.impl.copyobject.ReferenceObjectCopierImpl;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/copyobject/CopyobjectDefaults.class */
public abstract class CopyobjectDefaults {
    private static final ObjectCopier referenceObjectCopier = new ReferenceObjectCopierImpl();
    private static ObjectCopierFactory referenceObjectCopierFactory = new ObjectCopierFactory() { // from class: com.sun.corba.se.spi.copyobject.CopyobjectDefaults.3
        @Override // com.sun.corba.se.spi.copyobject.ObjectCopierFactory
        public ObjectCopier make() {
            return CopyobjectDefaults.referenceObjectCopier;
        }
    };

    private CopyobjectDefaults() {
    }

    public static ObjectCopierFactory makeORBStreamObjectCopierFactory(final ORB orb) {
        return new ObjectCopierFactory() { // from class: com.sun.corba.se.spi.copyobject.CopyobjectDefaults.1
            @Override // com.sun.corba.se.spi.copyobject.ObjectCopierFactory
            public ObjectCopier make() {
                return new ORBStreamObjectCopierImpl(orb);
            }
        };
    }

    public static ObjectCopierFactory makeJavaStreamObjectCopierFactory(final ORB orb) {
        return new ObjectCopierFactory() { // from class: com.sun.corba.se.spi.copyobject.CopyobjectDefaults.2
            @Override // com.sun.corba.se.spi.copyobject.ObjectCopierFactory
            public ObjectCopier make() {
                return new JavaStreamObjectCopierImpl(orb);
            }
        };
    }

    public static ObjectCopierFactory getReferenceObjectCopierFactory() {
        return referenceObjectCopierFactory;
    }

    public static ObjectCopierFactory makeFallbackObjectCopierFactory(final ObjectCopierFactory objectCopierFactory, final ObjectCopierFactory objectCopierFactory2) {
        return new ObjectCopierFactory() { // from class: com.sun.corba.se.spi.copyobject.CopyobjectDefaults.4
            @Override // com.sun.corba.se.spi.copyobject.ObjectCopierFactory
            public ObjectCopier make() {
                return new FallbackObjectCopierImpl(objectCopierFactory.make(), objectCopierFactory2.make());
            }
        };
    }
}
