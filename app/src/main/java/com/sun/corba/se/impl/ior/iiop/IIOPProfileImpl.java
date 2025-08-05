package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.impl.encoding.EncapsInputStream;
import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.ior.EncapsulationUtility;
import com.sun.corba.se.impl.logging.IORSystemException;
import com.sun.corba.se.impl.util.JDKBridge;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IdentifiableBase;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.ior.iiop.JavaCodebaseComponent;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersion;
import java.util.Iterator;
import org.omg.CORBA.SystemException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import org.omg.IOP.TaggedProfile;
import org.omg.IOP.TaggedProfileHelper;
import sun.corba.EncapsInputStreamFactory;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/IIOPProfileImpl.class */
public class IIOPProfileImpl extends IdentifiableBase implements IIOPProfile {
    private ORB orb;
    private IORSystemException wrapper;
    private ObjectId oid;
    private IIOPProfileTemplate proftemp;
    private ObjectKeyTemplate oktemp;
    protected String codebase;
    protected boolean cachedCodebase;
    private boolean checkedIsLocal;
    private boolean cachedIsLocal;
    private GIOPVersion giopVersion;

    /* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/IIOPProfileImpl$LocalCodeBaseSingletonHolder.class */
    private static class LocalCodeBaseSingletonHolder {
        public static JavaCodebaseComponent comp;

        private LocalCodeBaseSingletonHolder() {
        }

        static {
            String localCodebase = JDKBridge.getLocalCodebase();
            if (localCodebase == null) {
                comp = null;
            } else {
                comp = IIOPFactories.makeJavaCodebaseComponent(localCodebase);
            }
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IIOPProfileImpl)) {
            return false;
        }
        IIOPProfileImpl iIOPProfileImpl = (IIOPProfileImpl) obj;
        return this.oid.equals(iIOPProfileImpl.oid) && this.proftemp.equals(iIOPProfileImpl.proftemp) && this.oktemp.equals(iIOPProfileImpl.oktemp);
    }

    public int hashCode() {
        return (this.oid.hashCode() ^ this.proftemp.hashCode()) ^ this.oktemp.hashCode();
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public ObjectId getObjectId() {
        return this.oid;
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public TaggedProfileTemplate getTaggedProfileTemplate() {
        return this.proftemp;
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public ObjectKeyTemplate getObjectKeyTemplate() {
        return this.oktemp;
    }

    private IIOPProfileImpl(ORB orb) {
        this.codebase = null;
        this.cachedCodebase = false;
        this.checkedIsLocal = false;
        this.cachedIsLocal = false;
        this.giopVersion = null;
        this.orb = orb;
        this.wrapper = IORSystemException.get(orb, CORBALogDomains.OA_IOR);
    }

    public IIOPProfileImpl(ORB orb, ObjectKeyTemplate objectKeyTemplate, ObjectId objectId, IIOPProfileTemplate iIOPProfileTemplate) {
        this(orb);
        this.oktemp = objectKeyTemplate;
        this.oid = objectId;
        this.proftemp = iIOPProfileTemplate;
    }

    public IIOPProfileImpl(InputStream inputStream) {
        this((ORB) inputStream.orb());
        init(inputStream);
    }

    public IIOPProfileImpl(ORB orb, TaggedProfile taggedProfile) {
        this(orb);
        if (taggedProfile == null || taggedProfile.tag != 0 || taggedProfile.profile_data == null) {
            throw this.wrapper.invalidTaggedProfile();
        }
        EncapsInputStream encapsInputStreamNewEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(orb, taggedProfile.profile_data, taggedProfile.profile_data.length);
        encapsInputStreamNewEncapsInputStream.consumeEndian();
        init(encapsInputStreamNewEncapsInputStream);
    }

    private void init(InputStream inputStream) {
        GIOPVersion gIOPVersion = new GIOPVersion();
        gIOPVersion.read(inputStream);
        IIOPAddressImpl iIOPAddressImpl = new IIOPAddressImpl(inputStream);
        ObjectKey objectKeyCreate = this.orb.getObjectKeyFactory().create(EncapsulationUtility.readOctets(inputStream));
        this.oktemp = objectKeyCreate.getTemplate();
        this.oid = objectKeyCreate.getId();
        this.proftemp = IIOPFactories.makeIIOPProfileTemplate(this.orb, gIOPVersion, iIOPAddressImpl);
        if (gIOPVersion.getMinor() > 0) {
            EncapsulationUtility.readIdentifiableSequence(this.proftemp, this.orb.getTaggedComponentFactoryFinder(), inputStream);
        }
        if (uncachedGetCodeBase() == null) {
            JavaCodebaseComponent javaCodebaseComponent = LocalCodeBaseSingletonHolder.comp;
            if (javaCodebaseComponent != null) {
                if (gIOPVersion.getMinor() > 0) {
                    this.proftemp.add(javaCodebaseComponent);
                }
                this.codebase = javaCodebaseComponent.getURLs();
            }
            this.cachedCodebase = true;
        }
    }

    @Override // com.sun.corba.se.spi.ior.WriteContents
    public void writeContents(OutputStream outputStream) {
        this.proftemp.write(this.oktemp, this.oid, outputStream);
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return this.proftemp.getId();
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public boolean isEquivalent(com.sun.corba.se.spi.ior.TaggedProfile taggedProfile) {
        if (!(taggedProfile instanceof IIOPProfile)) {
            return false;
        }
        IIOPProfile iIOPProfile = (IIOPProfile) taggedProfile;
        return this.oid.equals(iIOPProfile.getObjectId()) && this.proftemp.isEquivalent(iIOPProfile.getTaggedProfileTemplate()) && this.oktemp.equals(iIOPProfile.getObjectKeyTemplate());
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public ObjectKey getObjectKey() {
        return IORFactories.makeObjectKey(this.oktemp, this.oid);
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public TaggedProfile getIOPProfile() {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.orb);
        encapsOutputStreamNewEncapsOutputStream.write_long(getId());
        write(encapsOutputStreamNewEncapsOutputStream);
        return TaggedProfileHelper.read((InputStream) encapsOutputStreamNewEncapsOutputStream.create_input_stream());
    }

    private String uncachedGetCodeBase() {
        Iterator itIteratorById = this.proftemp.iteratorById(25);
        if (itIteratorById.hasNext()) {
            return ((JavaCodebaseComponent) itIteratorById.next()).getURLs();
        }
        return null;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPProfile
    public synchronized String getCodebase() {
        if (!this.cachedCodebase) {
            this.cachedCodebase = true;
            this.codebase = uncachedGetCodeBase();
        }
        return this.codebase;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPProfile
    public ORBVersion getORBVersion() {
        return this.oktemp.getORBVersion();
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public synchronized boolean isLocal() {
        if (!this.checkedIsLocal) {
            this.checkedIsLocal = true;
            this.cachedIsLocal = this.orb.isLocalHost(this.proftemp.getPrimaryAddress().getHost()) && this.orb.isLocalServerId(this.oktemp.getSubcontractId(), this.oktemp.getServerId()) && this.orb.getLegacyServerSocketManager().legacyIsLocalServerPort(this.proftemp.getPrimaryAddress().getPort());
        }
        return this.cachedIsLocal;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPProfile
    public Object getServant() {
        if (!isLocal()) {
            return null;
        }
        ObjectAdapterFactory objectAdapterFactory = this.orb.getRequestDispatcherRegistry().getObjectAdapterFactory(this.oktemp.getSubcontractId());
        ObjectAdapterId objectAdapterId = this.oktemp.getObjectAdapterId();
        try {
            return objectAdapterFactory.find(objectAdapterId).getLocalServant(this.oid.getId());
        } catch (SystemException e2) {
            this.wrapper.getLocalServantFailure(e2, objectAdapterId.toString());
            return null;
        }
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPProfile
    public synchronized GIOPVersion getGIOPVersion() {
        return this.proftemp.getGIOPVersion();
    }

    @Override // com.sun.corba.se.spi.ior.MakeImmutable
    public void makeImmutable() {
        this.proftemp.makeImmutable();
    }
}
