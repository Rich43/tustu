package com.sun.corba.se.spi.ior;

import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import org.omg.CORBA.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.IOP.TaggedComponentHelper;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/TaggedComponentBase.class */
public abstract class TaggedComponentBase extends IdentifiableBase implements TaggedComponent {
    @Override // com.sun.corba.se.spi.ior.TaggedComponent
    public org.omg.IOP.TaggedComponent getIOPComponent(ORB orb) {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((com.sun.corba.se.spi.orb.ORB) orb);
        write(encapsOutputStreamNewEncapsOutputStream);
        return TaggedComponentHelper.read((InputStream) encapsOutputStreamNewEncapsOutputStream.create_input_stream());
    }
}
