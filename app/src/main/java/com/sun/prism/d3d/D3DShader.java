package com.sun.prism.d3d;

import com.sun.prism.d3d.D3DResource;
import com.sun.prism.impl.BufferUtil;
import com.sun.prism.ps.Shader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/prism/d3d/D3DShader.class */
final class D3DShader extends D3DResource implements Shader {
    private static IntBuffer itmp;
    private static FloatBuffer ftmp;
    private final Map<String, Integer> registers;
    private boolean valid;

    static native long init(long j2, ByteBuffer byteBuffer, int i2, boolean z2, boolean z3);

    private static native int enable(long j2, long j3);

    private static native int disable(long j2, long j3);

    private static native int setConstantsF(long j2, long j3, int i2, FloatBuffer floatBuffer, int i3, int i4);

    private static native int setConstantsI(long j2, long j3, int i2, IntBuffer intBuffer, int i3, int i4);

    private static native int nGetRegister(long j2, long j3, String str);

    D3DShader(D3DContext context, long pData, Map<String, Integer> registers) {
        super(new D3DResource.D3DRecord(context, pData));
        this.valid = pData != 0;
        this.registers = registers;
    }

    @Override // com.sun.prism.ps.Shader
    public void enable() {
        int res = enable(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource());
        this.valid &= res >= 0;
        this.d3dResRecord.getContext();
        D3DContext.validate(res);
    }

    @Override // com.sun.prism.ps.Shader
    public void disable() {
        int res = disable(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource());
        this.valid &= res >= 0;
        this.d3dResRecord.getContext();
        D3DContext.validate(res);
    }

    private static void checkTmpIntBuf() {
        if (itmp == null) {
            itmp = BufferUtil.newIntBuffer(4);
        }
        itmp.clear();
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstant(String name, int i0) {
        setConstant(name, i0);
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstant(String name, int i0, int i1) {
        setConstant(name, i0, i1);
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstant(String name, int i0, int i1, int i2) {
        setConstant(name, i0, i1, i2);
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstant(String name, int i0, int i1, int i2, int i3) {
        setConstant(name, i0, i1, i2, i3);
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstants(String name, IntBuffer buf, int off, int count) {
        throw new InternalError("Not yet implemented");
    }

    private static void checkTmpFloatBuf() {
        if (ftmp == null) {
            ftmp = BufferUtil.newFloatBuffer(4);
        }
        ftmp.clear();
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstant(String name, float f0) {
        checkTmpFloatBuf();
        ftmp.put(f0);
        setConstants(name, ftmp, 0, 1);
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstant(String name, float f0, float f1) {
        checkTmpFloatBuf();
        ftmp.put(f0);
        ftmp.put(f1);
        setConstants(name, ftmp, 0, 1);
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstant(String name, float f0, float f1, float f2) {
        checkTmpFloatBuf();
        ftmp.put(f0);
        ftmp.put(f1);
        ftmp.put(f2);
        setConstants(name, ftmp, 0, 1);
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstant(String name, float f0, float f1, float f2, float f3) {
        checkTmpFloatBuf();
        ftmp.put(f0);
        ftmp.put(f1);
        ftmp.put(f2);
        ftmp.put(f3);
        setConstants(name, ftmp, 0, 1);
    }

    @Override // com.sun.prism.ps.Shader
    public void setConstants(String name, FloatBuffer buf, int off, int count) {
        int res = setConstantsF(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource(), getRegister(name), buf, off, count);
        this.valid &= res >= 0;
        this.d3dResRecord.getContext();
        D3DContext.validate(res);
    }

    private int getRegister(String name) {
        Integer reg = this.registers.get(name);
        if (reg == null) {
            int nRegister = nGetRegister(this.d3dResRecord.getContext().getContextHandle(), this.d3dResRecord.getResource(), name);
            if (nRegister < 0) {
                throw new IllegalArgumentException("Register not found for: " + name);
            }
            this.registers.put(name, Integer.valueOf(nRegister));
            return nRegister;
        }
        return reg.intValue();
    }

    @Override // com.sun.prism.ps.Shader
    public boolean isValid() {
        return this.valid;
    }

    @Override // com.sun.prism.d3d.D3DResource, com.sun.prism.impl.BaseGraphicsResource, com.sun.prism.GraphicsResource
    public void dispose() {
        super.dispose();
        this.valid = false;
    }
}
