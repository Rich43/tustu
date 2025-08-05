package com.sun.xml.internal.ws.server.sei;

import com.sun.xml.internal.ws.model.ParameterImpl;
import javax.xml.ws.Holder;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointValueSetter.class */
public abstract class EndpointValueSetter {
    private static final EndpointValueSetter[] POOL = new EndpointValueSetter[16];

    abstract void put(Object obj, Object[] objArr);

    private EndpointValueSetter() {
    }

    static {
        for (int i2 = 0; i2 < POOL.length; i2++) {
            POOL[i2] = new Param(i2);
        }
    }

    public static EndpointValueSetter get(ParameterImpl p2) {
        int idx = p2.getIndex();
        if (p2.isIN()) {
            if (idx < POOL.length) {
                return POOL[idx];
            }
            return new Param(idx);
        }
        return new HolderParam(idx);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointValueSetter$Param.class */
    static class Param extends EndpointValueSetter {
        protected final int idx;

        public Param(int idx) {
            super();
            this.idx = idx;
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointValueSetter
        void put(Object obj, Object[] args) {
            if (obj != null) {
                args[this.idx] = obj;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointValueSetter$HolderParam.class */
    static final class HolderParam extends Param {
        public HolderParam(int idx) {
            super(idx);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.ws.server.sei.EndpointValueSetter.Param, com.sun.xml.internal.ws.server.sei.EndpointValueSetter
        void put(Object obj, Object[] args) {
            Holder holder = new Holder();
            if (obj != 0) {
                holder.value = obj;
            }
            args[this.idx] = holder;
        }
    }
}
