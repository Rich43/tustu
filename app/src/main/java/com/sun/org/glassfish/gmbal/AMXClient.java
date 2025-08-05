package com.sun.org.glassfish.gmbal;

import com.sun.org.glassfish.external.amx.AMX;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.ModelMBeanInfo;

/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/AMXClient.class */
public class AMXClient implements AMXMBeanInterface {
    public static final ObjectName NULL_OBJECTNAME = makeObjectName("null:type=Null,name=Null");
    private MBeanServerConnection server;
    private ObjectName oname;

    private static ObjectName makeObjectName(String str) {
        try {
            return new ObjectName(str);
        } catch (MalformedObjectNameException e2) {
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AMXClient)) {
            return false;
        }
        AMXClient other = (AMXClient) obj;
        return this.oname.equals(other.oname);
    }

    public int hashCode() {
        int hash = (47 * 5) + (this.oname != null ? this.oname.hashCode() : 0);
        return hash;
    }

    public String toString() {
        return "AMXClient[" + ((Object) this.oname) + "]";
    }

    private <T> T fetchAttribute(String name, Class<T> type) {
        try {
            Object obj = this.server.getAttribute(this.oname, name);
            if (NULL_OBJECTNAME.equals(obj)) {
                return null;
            }
            return type.cast(obj);
        } catch (IOException exc) {
            throw new GmbalException("Exception in fetchAttribute", exc);
        } catch (JMException exc2) {
            throw new GmbalException("Exception in fetchAttribute", exc2);
        }
    }

    public AMXClient(MBeanServerConnection server, ObjectName oname) {
        this.server = server;
        this.oname = oname;
    }

    private AMXClient makeAMX(ObjectName on) {
        if (on == null) {
            return null;
        }
        return new AMXClient(this.server, on);
    }

    @Override // com.sun.org.glassfish.gmbal.AMXMBeanInterface
    public String getName() {
        return (String) fetchAttribute("Name", String.class);
    }

    @Override // com.sun.org.glassfish.gmbal.AMXMBeanInterface
    public Map<String, ?> getMeta() {
        try {
            ModelMBeanInfo mbi = (ModelMBeanInfo) this.server.getMBeanInfo(this.oname);
            Descriptor desc = mbi.getMBeanDescriptor();
            Map<String, Object> result = new HashMap<>();
            for (String str : desc.getFieldNames()) {
                result.put(str, desc.getFieldValue(str));
            }
            return result;
        } catch (IOException ex) {
            throw new GmbalException("Exception in getMeta", ex);
        } catch (InstanceNotFoundException ex2) {
            throw new GmbalException("Exception in getMeta", ex2);
        } catch (IntrospectionException ex3) {
            throw new GmbalException("Exception in getMeta", ex3);
        } catch (MBeanException ex4) {
            throw new GmbalException("Exception in getMeta", ex4);
        } catch (ReflectionException ex5) {
            throw new GmbalException("Exception in getMeta", ex5);
        } catch (RuntimeOperationsException ex6) {
            throw new GmbalException("Exception in getMeta", ex6);
        }
    }

    @Override // com.sun.org.glassfish.gmbal.AMXMBeanInterface
    public AMXClient getParent() {
        ObjectName res = (ObjectName) fetchAttribute(AMX.ATTR_PARENT, ObjectName.class);
        return makeAMX(res);
    }

    @Override // com.sun.org.glassfish.gmbal.AMXMBeanInterface
    public AMXClient[] getChildren() {
        ObjectName[] onames = (ObjectName[]) fetchAttribute(AMX.ATTR_CHILDREN, ObjectName[].class);
        return makeAMXArray(onames);
    }

    private AMXClient[] makeAMXArray(ObjectName[] onames) {
        AMXClient[] result = new AMXClient[onames.length];
        int ctr = 0;
        for (ObjectName on : onames) {
            int i2 = ctr;
            ctr++;
            result[i2] = makeAMX(on);
        }
        return result;
    }

    public Object getAttribute(String attribute) {
        try {
            return this.server.getAttribute(this.oname, attribute);
        } catch (IOException ex) {
            throw new GmbalException("Exception in getAttribute", ex);
        } catch (AttributeNotFoundException ex2) {
            throw new GmbalException("Exception in getAttribute", ex2);
        } catch (InstanceNotFoundException ex3) {
            throw new GmbalException("Exception in getAttribute", ex3);
        } catch (MBeanException ex4) {
            throw new GmbalException("Exception in getAttribute", ex4);
        } catch (ReflectionException ex5) {
            throw new GmbalException("Exception in getAttribute", ex5);
        }
    }

    public void setAttribute(String name, Object value) {
        Attribute attr = new Attribute(name, value);
        setAttribute(attr);
    }

    public void setAttribute(Attribute attribute) {
        try {
            this.server.setAttribute(this.oname, attribute);
        } catch (IOException ex) {
            throw new GmbalException("Exception in setAttribute", ex);
        } catch (AttributeNotFoundException ex2) {
            throw new GmbalException("Exception in setAttribute", ex2);
        } catch (InstanceNotFoundException ex3) {
            throw new GmbalException("Exception in setAttribute", ex3);
        } catch (InvalidAttributeValueException ex4) {
            throw new GmbalException("Exception in setAttribute", ex4);
        } catch (MBeanException ex5) {
            throw new GmbalException("Exception in setAttribute", ex5);
        } catch (ReflectionException ex6) {
            throw new GmbalException("Exception in setAttribute", ex6);
        }
    }

    public AttributeList getAttributes(String[] attributes) {
        try {
            return this.server.getAttributes(this.oname, attributes);
        } catch (IOException ex) {
            throw new GmbalException("Exception in getAttributes", ex);
        } catch (InstanceNotFoundException ex2) {
            throw new GmbalException("Exception in getAttributes", ex2);
        } catch (ReflectionException ex3) {
            throw new GmbalException("Exception in getAttributes", ex3);
        }
    }

    public AttributeList setAttributes(AttributeList attributes) {
        try {
            return this.server.setAttributes(this.oname, attributes);
        } catch (IOException ex) {
            throw new GmbalException("Exception in setAttributes", ex);
        } catch (InstanceNotFoundException ex2) {
            throw new GmbalException("Exception in setAttributes", ex2);
        } catch (ReflectionException ex3) {
            throw new GmbalException("Exception in setAttributes", ex3);
        }
    }

    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        try {
            return this.server.invoke(this.oname, actionName, params, signature);
        } catch (IOException ex) {
            throw new GmbalException("Exception in invoke", ex);
        } catch (InstanceNotFoundException ex2) {
            throw new GmbalException("Exception in invoke", ex2);
        }
    }

    public MBeanInfo getMBeanInfo() {
        try {
            return this.server.getMBeanInfo(this.oname);
        } catch (IOException ex) {
            throw new GmbalException("Exception in invoke", ex);
        } catch (InstanceNotFoundException ex2) {
            throw new GmbalException("Exception in invoke", ex2);
        } catch (IntrospectionException ex3) {
            throw new GmbalException("Exception in invoke", ex3);
        } catch (ReflectionException ex4) {
            throw new GmbalException("Exception in invoke", ex4);
        }
    }

    public ObjectName objectName() {
        return this.oname;
    }
}
