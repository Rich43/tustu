package com.sun.corba.se.impl.oa.poa;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAPolicyMediatorFactory.class */
abstract class POAPolicyMediatorFactory {
    POAPolicyMediatorFactory() {
    }

    static POAPolicyMediator create(Policies policies, POAImpl pOAImpl) {
        if (policies.retainServants()) {
            if (policies.useActiveMapOnly()) {
                return new POAPolicyMediatorImpl_R_AOM(policies, pOAImpl);
            }
            if (policies.useDefaultServant()) {
                return new POAPolicyMediatorImpl_R_UDS(policies, pOAImpl);
            }
            if (policies.useServantManager()) {
                return new POAPolicyMediatorImpl_R_USM(policies, pOAImpl);
            }
            throw pOAImpl.invocationWrapper().pmfCreateRetain();
        }
        if (policies.useDefaultServant()) {
            return new POAPolicyMediatorImpl_NR_UDS(policies, pOAImpl);
        }
        if (policies.useServantManager()) {
            return new POAPolicyMediatorImpl_NR_USM(policies, pOAImpl);
        }
        throw pOAImpl.invocationWrapper().pmfCreateNonRetain();
    }
}
