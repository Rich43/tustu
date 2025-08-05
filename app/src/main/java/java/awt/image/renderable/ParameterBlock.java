package java.awt.image.renderable;

import java.awt.image.RenderedImage;
import java.io.Serializable;
import java.util.Vector;

/* loaded from: rt.jar:java/awt/image/renderable/ParameterBlock.class */
public class ParameterBlock implements Cloneable, Serializable {
    protected Vector<Object> sources = new Vector<>();
    protected Vector<Object> parameters = new Vector<>();

    public ParameterBlock() {
    }

    public ParameterBlock(Vector<Object> vector) {
        setSources(vector);
    }

    public ParameterBlock(Vector<Object> vector, Vector<Object> vector2) {
        setSources(vector);
        setParameters(vector2);
    }

    public Object shallowClone() {
        try {
            return super.clone();
        } catch (Exception e2) {
            return null;
        }
    }

    public Object clone() {
        try {
            ParameterBlock parameterBlock = (ParameterBlock) super.clone();
            if (this.sources != null) {
                parameterBlock.setSources((Vector) this.sources.clone());
            }
            if (this.parameters != null) {
                parameterBlock.setParameters((Vector) this.parameters.clone());
            }
            return parameterBlock;
        } catch (Exception e2) {
            return null;
        }
    }

    public ParameterBlock addSource(Object obj) {
        this.sources.addElement(obj);
        return this;
    }

    public Object getSource(int i2) {
        return this.sources.elementAt(i2);
    }

    public ParameterBlock setSource(Object obj, int i2) {
        int i3 = i2 + 1;
        if (this.sources.size() < i3) {
            this.sources.setSize(i3);
        }
        this.sources.setElementAt(obj, i2);
        return this;
    }

    public RenderedImage getRenderedSource(int i2) {
        return (RenderedImage) this.sources.elementAt(i2);
    }

    public RenderableImage getRenderableSource(int i2) {
        return (RenderableImage) this.sources.elementAt(i2);
    }

    public int getNumSources() {
        return this.sources.size();
    }

    public Vector<Object> getSources() {
        return this.sources;
    }

    public void setSources(Vector<Object> vector) {
        this.sources = vector;
    }

    public void removeSources() {
        this.sources = new Vector<>();
    }

    public int getNumParameters() {
        return this.parameters.size();
    }

    public Vector<Object> getParameters() {
        return this.parameters;
    }

    public void setParameters(Vector<Object> vector) {
        this.parameters = vector;
    }

    public void removeParameters() {
        this.parameters = new Vector<>();
    }

    public ParameterBlock add(Object obj) {
        this.parameters.addElement(obj);
        return this;
    }

    public ParameterBlock add(byte b2) {
        return add(new Byte(b2));
    }

    public ParameterBlock add(char c2) {
        return add(new Character(c2));
    }

    public ParameterBlock add(short s2) {
        return add(new Short(s2));
    }

    public ParameterBlock add(int i2) {
        return add(new Integer(i2));
    }

    public ParameterBlock add(long j2) {
        return add(new Long(j2));
    }

    public ParameterBlock add(float f2) {
        return add(new Float(f2));
    }

    public ParameterBlock add(double d2) {
        return add(new Double(d2));
    }

    public ParameterBlock set(Object obj, int i2) {
        int i3 = i2 + 1;
        if (this.parameters.size() < i3) {
            this.parameters.setSize(i3);
        }
        this.parameters.setElementAt(obj, i2);
        return this;
    }

    public ParameterBlock set(byte b2, int i2) {
        return set(new Byte(b2), i2);
    }

    public ParameterBlock set(char c2, int i2) {
        return set(new Character(c2), i2);
    }

    public ParameterBlock set(short s2, int i2) {
        return set(new Short(s2), i2);
    }

    public ParameterBlock set(int i2, int i3) {
        return set(new Integer(i2), i3);
    }

    public ParameterBlock set(long j2, int i2) {
        return set(new Long(j2), i2);
    }

    public ParameterBlock set(float f2, int i2) {
        return set(new Float(f2), i2);
    }

    public ParameterBlock set(double d2, int i2) {
        return set(new Double(d2), i2);
    }

    public Object getObjectParameter(int i2) {
        return this.parameters.elementAt(i2);
    }

    public byte getByteParameter(int i2) {
        return ((Byte) this.parameters.elementAt(i2)).byteValue();
    }

    public char getCharParameter(int i2) {
        return ((Character) this.parameters.elementAt(i2)).charValue();
    }

    public short getShortParameter(int i2) {
        return ((Short) this.parameters.elementAt(i2)).shortValue();
    }

    public int getIntParameter(int i2) {
        return ((Integer) this.parameters.elementAt(i2)).intValue();
    }

    public long getLongParameter(int i2) {
        return ((Long) this.parameters.elementAt(i2)).longValue();
    }

    public float getFloatParameter(int i2) {
        return ((Float) this.parameters.elementAt(i2)).floatValue();
    }

    public double getDoubleParameter(int i2) {
        return ((Double) this.parameters.elementAt(i2)).doubleValue();
    }

    public Class[] getParamClasses() {
        int numParameters = getNumParameters();
        Class[] clsArr = new Class[numParameters];
        for (int i2 = 0; i2 < numParameters; i2++) {
            Object objectParameter = getObjectParameter(i2);
            if (objectParameter instanceof Byte) {
                clsArr[i2] = Byte.TYPE;
            } else if (objectParameter instanceof Character) {
                clsArr[i2] = Character.TYPE;
            } else if (objectParameter instanceof Short) {
                clsArr[i2] = Short.TYPE;
            } else if (objectParameter instanceof Integer) {
                clsArr[i2] = Integer.TYPE;
            } else if (objectParameter instanceof Long) {
                clsArr[i2] = Long.TYPE;
            } else if (objectParameter instanceof Float) {
                clsArr[i2] = Float.TYPE;
            } else if (objectParameter instanceof Double) {
                clsArr[i2] = Double.TYPE;
            } else {
                clsArr[i2] = objectParameter.getClass();
            }
        }
        return clsArr;
    }
}
