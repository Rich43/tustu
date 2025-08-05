package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.spi.ior.Identifiable;
import com.sun.corba.se.spi.ior.TaggedComponent;
import com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.IOP.TaggedComponentHelper;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/TaggedComponentFactoryFinderImpl.class */
public class TaggedComponentFactoryFinderImpl extends IdentifiableFactoryFinderBase implements TaggedComponentFactoryFinder {
    public TaggedComponentFactoryFinderImpl(ORB orb) {
        super(orb);
    }

    @Override // com.sun.corba.se.impl.ior.IdentifiableFactoryFinderBase
    public Identifiable handleMissingFactory(int i2, InputStream inputStream) {
        return new GenericTaggedComponent(i2, inputStream);
    }

    @Override // com.sun.corba.se.spi.ior.TaggedComponentFactoryFinder
    public TaggedComponent create(org.omg.CORBA.ORB orb, org.omg.IOP.TaggedComponent taggedComponent) {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((ORB) orb);
        TaggedComponentHelper.write(encapsOutputStreamNewEncapsOutputStream, taggedComponent);
        InputStream inputStream = (InputStream) encapsOutputStreamNewEncapsOutputStream.create_input_stream();
        inputStream.read_ulong();
        return (TaggedComponent) create(taggedComponent.tag, inputStream);
    }
}
