package ak;

import bH.C0995c;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;

/* renamed from: ak.aj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/aj.class */
class C0541aj {

    /* renamed from: t, reason: collision with root package name */
    protected int f4738t;

    /* renamed from: u, reason: collision with root package name */
    protected int f4739u;

    /* renamed from: v, reason: collision with root package name */
    Map f4740v;

    /* renamed from: w, reason: collision with root package name */
    final /* synthetic */ Z f4741w;

    public C0541aj(Z z2, int i2) throws SecurityException {
        this.f4741w = z2;
        this.f4740v = new HashMap();
        this.f4738t = i2;
        l();
    }

    public C0541aj(Z z2) {
        this(z2, 0);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected final void l() throws SecurityException {
        this.f4739u = this.f4738t;
        if (this.f4738t > 0) {
            for (Field field : getClass().getDeclaredFields()) {
                field.setAccessible(true);
                X x2 = (X) field.getAnnotation(X.class);
                if (x2 != null && this.f4741w.f4644j >= x2.c()) {
                    try {
                        int iA = x2.a().a() * x2.b();
                        switch (x2.a()) {
                            case UINT8:
                            case UINT16:
                            case UINT32:
                            case UINT64:
                            case LINK:
                                field.setInt(this, C0995c.a(this.f4741w.f4639e, this.f4739u, iA, this.f4741w.f4645k, false));
                                this.f4739u += iA;
                                break;
                            case INT16:
                                field.setInt(this, C0995c.a(this.f4741w.f4639e, this.f4739u, iA, this.f4741w.f4645k, true));
                                this.f4739u += iA;
                                break;
                            case CHAR:
                                field.set(this, this.f4741w.a(this.f4739u, iA).trim());
                                this.f4739u += iA;
                                break;
                            case REAL:
                                field.set(this, Double.valueOf(ByteBuffer.wrap(this.f4741w.f4639e, this.f4739u, iA).order(this.f4741w.f4645k ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN).getDouble()));
                                this.f4739u += iA;
                                break;
                            case BOOL:
                                boolean z2 = C0995c.a(this.f4741w.f4639e, this.f4739u, iA, true, false) != 0;
                                field.setBoolean(this, z2);
                                if (x2.d()) {
                                    this.f4741w.f4645k = z2;
                                }
                                this.f4739u += iA;
                                break;
                            default:
                                this.f4739u += iA;
                                break;
                        }
                    } catch (IllegalAccessException | IllegalArgumentException e2) {
                        Logger.getLogger(C0541aj.class.getName()).log(Level.SEVERE, (String) null, e2);
                    }
                }
            }
        }
    }

    public String toString() throws SecurityException {
        StringBuilder sb = new StringBuilder(((Object) getClass()) + "{\n");
        for (Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            X x2 = (X) field.getAnnotation(X.class);
            if (x2 == null || this.f4741w.f4644j >= x2.c()) {
                try {
                    Object obj = field.get(this);
                    if (obj == null) {
                        obj = FXMLLoader.NULL_KEYWORD;
                    }
                    if (x2 != null) {
                        sb.append("\t").append(field.getName());
                        sb.append("(").append(x2.a().name()).append("[").append(x2.b()).append("])=");
                        sb.append(x2.a() == Y.LINK ? String.format("0x%08X", Integer.valueOf(Integer.parseInt(obj.toString()))) : obj);
                        sb.append(",\n");
                    } else {
                        sb.append("\t").append(field.getName()).append("=").append(obj.toString()).append("\n");
                    }
                } catch (IllegalAccessException | IllegalArgumentException e2) {
                    Logger.getLogger(C0541aj.class.getName()).log(Level.SEVERE, (String) null, e2);
                }
            }
        }
        return sb.append("}").toString();
    }
}
