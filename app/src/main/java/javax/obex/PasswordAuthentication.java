package javax.obex;

import com.intel.bluetooth.Utils;

/* loaded from: bluecove-2.1.1.jar:javax/obex/PasswordAuthentication.class */
public class PasswordAuthentication {
    private byte[] userName;
    private byte[] password;

    public PasswordAuthentication(byte[] userName, byte[] password) {
        if (password == null) {
            throw new NullPointerException("password is null");
        }
        this.userName = Utils.clone(userName);
        this.password = Utils.clone(password);
    }

    public byte[] getUserName() {
        return Utils.clone(this.userName);
    }

    public byte[] getPassword() {
        return Utils.clone(this.password);
    }
}
