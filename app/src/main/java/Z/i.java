package z;

import bH.I;
import bH.R;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import java.io.File;
import java.util.regex.Pattern;
import jssc.SerialPortList;

/* loaded from: TunerStudioMS.jar:z/i.class */
public class i {
    public String[] a() {
        return b();
    }

    public String[] b() {
        if (!I.b()) {
            return SerialPortList.getPortNames();
        }
        String[] list = new File("/", "dev").list(new j(this));
        if (list == null || list.length <= 0) {
            return SerialPortList.getPortNames("/dev/", Pattern.compile("tty\\.*"));
        }
        for (int i2 = 0; i2 < list.length; i2++) {
            list[i2] = "/dev/" + list[i2];
        }
        return R.a(list);
    }

    public String[] c() {
        return new String[]{"2400", "4800", "9600", "14400", "19200", "28800", "38400", "57600", "115200", "150000", "230400", "250000", "300000", "460800", "500000", ORBConstants.NAME_SERVICE_SERVER_ID};
    }
}
