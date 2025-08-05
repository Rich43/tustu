package javafx.embed.swt;

import java.nio.ByteBuffer;
import java.sql.Types;
import javafx.beans.NamedArg;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

@Deprecated
/* loaded from: jfxswt.jar:javafx/embed/swt/CustomTransfer.class */
public class CustomTransfer extends ByteArrayTransfer {
    private String name;
    private String mime;

    public CustomTransfer(@NamedArg("name") String name, @NamedArg("mime") String mime) {
        this.name = name;
        this.mime = mime;
    }

    public String getName() {
        return this.name;
    }

    public String getMime() {
        return this.mime;
    }

    public void javaToNative(Object object, TransferData transferData) {
        if (!checkCustom(object) || !isSupportedType(transferData)) {
            DND.error(Types.ARRAY);
        }
        byte[] bytes = null;
        if (object instanceof ByteBuffer) {
            bytes = ((ByteBuffer) object).array();
        } else if (object instanceof byte[]) {
            bytes = (byte[]) object;
        }
        if (bytes == null) {
            DND.error(Types.ARRAY);
        }
        super.javaToNative(bytes, transferData);
    }

    public Object nativeToJava(TransferData transferData) {
        if (isSupportedType(transferData)) {
            return super.nativeToJava(transferData);
        }
        return null;
    }

    protected String[] getTypeNames() {
        return new String[]{this.name};
    }

    protected int[] getTypeIds() {
        return new int[]{registerType(this.name)};
    }

    boolean checkByteArray(Object object) {
        return object != null && (object instanceof byte[]) && ((byte[]) object).length > 0;
    }

    boolean checkByteBuffer(Object object) {
        return object != null && (object instanceof ByteBuffer) && ((ByteBuffer) object).limit() > 0;
    }

    boolean checkCustom(Object object) {
        return checkByteArray(object) || checkByteBuffer(object);
    }

    protected boolean validate(Object object) {
        return checkCustom(object);
    }
}
