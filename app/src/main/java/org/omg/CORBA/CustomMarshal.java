package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/CustomMarshal.class */
public interface CustomMarshal {
    void marshal(DataOutputStream dataOutputStream);

    void unmarshal(DataInputStream dataInputStream);
}
