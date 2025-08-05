package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.logging.InterceptorsSystemException;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.legacy.interceptor.ORBInitInfoExt;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;
import org.omg.IOP.CodecFactory;
import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.omg.PortableInterceptor.IORInterceptor;
import org.omg.PortableInterceptor.ORBInitInfo;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.omg.PortableInterceptor.ORBInitInfoPackage.InvalidName;
import org.omg.PortableInterceptor.PolicyFactory;
import org.omg.PortableInterceptor.ServerRequestInterceptor;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/ORBInitInfoImpl.class */
public final class ORBInitInfoImpl extends LocalObject implements ORBInitInfo, ORBInitInfoExt {
    private ORB orb;
    private InterceptorsSystemException wrapper;
    private ORBUtilSystemException orbutilWrapper;
    private OMGSystemException omgWrapper;
    private String[] args;
    private String orbId;
    private CodecFactory codecFactory;
    private int stage = 0;
    public static final int STAGE_PRE_INIT = 0;
    public static final int STAGE_POST_INIT = 1;
    public static final int STAGE_CLOSED = 2;
    private static final String MESSAGE_ORBINITINFO_INVALID = "ORBInitInfo object is only valid during ORB_init";

    ORBInitInfoImpl(ORB orb, String[] strArr, String str, CodecFactory codecFactory) {
        this.orb = orb;
        this.wrapper = InterceptorsSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.orbutilWrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.omgWrapper = OMGSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.args = strArr;
        this.orbId = str;
        this.codecFactory = codecFactory;
    }

    @Override // com.sun.corba.se.spi.legacy.interceptor.ORBInitInfoExt
    public ORB getORB() {
        return this.orb;
    }

    void setStage(int i2) {
        this.stage = i2;
    }

    private void checkStage() {
        if (this.stage == 2) {
            throw this.wrapper.orbinitinfoInvalid();
        }
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public String[] arguments() {
        checkStage();
        return this.args;
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public String orb_id() {
        checkStage();
        return this.orbId;
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public CodecFactory codec_factory() {
        checkStage();
        return this.codecFactory;
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public void register_initial_reference(String str, Object object) throws BAD_PARAM, InvalidName {
        checkStage();
        if (str == null) {
            nullParam();
        }
        if (object == null) {
            throw this.omgWrapper.rirWithNullObject();
        }
        try {
            this.orb.register_initial_reference(str, object);
        } catch (org.omg.CORBA.ORBPackage.InvalidName e2) {
            InvalidName invalidName = new InvalidName(e2.getMessage());
            invalidName.initCause(e2);
            throw invalidName;
        }
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public Object resolve_initial_references(String str) throws BAD_PARAM, InvalidName {
        checkStage();
        if (str == null) {
            nullParam();
        }
        if (this.stage == 0) {
            throw this.wrapper.rirInvalidPreInit();
        }
        try {
            return this.orb.resolve_initial_references(str);
        } catch (org.omg.CORBA.ORBPackage.InvalidName e2) {
            throw new InvalidName();
        }
    }

    public void add_client_request_interceptor_with_policy(ClientRequestInterceptor clientRequestInterceptor, Policy[] policyArr) throws BAD_PARAM, DuplicateName {
        add_client_request_interceptor(clientRequestInterceptor);
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public void add_client_request_interceptor(ClientRequestInterceptor clientRequestInterceptor) throws BAD_PARAM, DuplicateName {
        checkStage();
        if (clientRequestInterceptor == null) {
            nullParam();
        }
        this.orb.getPIHandler().register_interceptor(clientRequestInterceptor, 0);
    }

    public void add_server_request_interceptor_with_policy(ServerRequestInterceptor serverRequestInterceptor, Policy[] policyArr) throws BAD_PARAM, PolicyError, DuplicateName {
        add_server_request_interceptor(serverRequestInterceptor);
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public void add_server_request_interceptor(ServerRequestInterceptor serverRequestInterceptor) throws BAD_PARAM, DuplicateName {
        checkStage();
        if (serverRequestInterceptor == null) {
            nullParam();
        }
        this.orb.getPIHandler().register_interceptor(serverRequestInterceptor, 1);
    }

    public void add_ior_interceptor_with_policy(IORInterceptor iORInterceptor, Policy[] policyArr) throws BAD_PARAM, PolicyError, DuplicateName {
        add_ior_interceptor(iORInterceptor);
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public void add_ior_interceptor(IORInterceptor iORInterceptor) throws BAD_PARAM, DuplicateName {
        checkStage();
        if (iORInterceptor == null) {
            nullParam();
        }
        this.orb.getPIHandler().register_interceptor(iORInterceptor, 2);
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public int allocate_slot_id() {
        checkStage();
        return ((PICurrent) this.orb.getPIHandler().getPICurrent()).allocateSlotId();
    }

    @Override // org.omg.PortableInterceptor.ORBInitInfoOperations
    public void register_policy_factory(int i2, PolicyFactory policyFactory) throws BAD_PARAM {
        checkStage();
        if (policyFactory == null) {
            nullParam();
        }
        this.orb.getPIHandler().registerPolicyFactory(i2, policyFactory);
    }

    private void nullParam() throws BAD_PARAM {
        throw this.orbutilWrapper.nullParam();
    }
}
