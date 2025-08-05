package com.intel.bluetooth;

import com.intel.bluetooth.gcf.socket.ServerSocketConnection;
import com.intel.bluetooth.gcf.socket.SocketConnection;
import com.intel.bluetooth.obex.OBEXClientSessionImpl;
import com.intel.bluetooth.obex.OBEXConnectionParams;
import com.intel.bluetooth.obex.OBEXSessionNotifierImpl;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import javax.bluetooth.BluetoothConnectionException;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.InputConnection;
import javax.microedition.io.OutputConnection;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/MicroeditionConnector.class */
public abstract class MicroeditionConnector {
    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int READ_WRITE = 3;
    private static Hashtable suportScheme = new Hashtable();
    private static Hashtable srvParams = new Hashtable();
    private static Hashtable cliParams = new Hashtable();
    private static Hashtable cliParamsL2CAP = new Hashtable();
    private static Hashtable srvParamsL2CAP = new Hashtable();
    private static final String AUTHENTICATE = "authenticate";
    private static final String AUTHORIZE = "authorize";
    private static final String ENCRYPT = "encrypt";
    private static final String MASTER = "master";
    private static final String NAME = "name";
    private static final String RECEIVE_MTU = "receivemtu";
    private static final String TRANSMIT_MTU = "transmitmtu";
    private static final String EXT_BLUECOVE_L2CAP_PSM = "bluecovepsm";

    static {
        cliParams.put(AUTHENTICATE, AUTHENTICATE);
        cliParams.put(ENCRYPT, ENCRYPT);
        cliParams.put(MASTER, MASTER);
        copyAll(srvParams, cliParams);
        srvParams.put(AUTHORIZE, AUTHORIZE);
        srvParams.put("name", "name");
        copyAll(cliParamsL2CAP, cliParams);
        cliParamsL2CAP.put(RECEIVE_MTU, RECEIVE_MTU);
        cliParamsL2CAP.put(TRANSMIT_MTU, TRANSMIT_MTU);
        copyAll(srvParamsL2CAP, cliParamsL2CAP);
        srvParamsL2CAP.put(AUTHORIZE, AUTHORIZE);
        srvParamsL2CAP.put("name", "name");
        srvParamsL2CAP.put(EXT_BLUECOVE_L2CAP_PSM, EXT_BLUECOVE_L2CAP_PSM);
        suportScheme.put(BluetoothConsts.PROTOCOL_SCHEME_RFCOMM, Boolean.TRUE);
        suportScheme.put(BluetoothConsts.PROTOCOL_SCHEME_BT_OBEX, Boolean.TRUE);
        suportScheme.put(BluetoothConsts.PROTOCOL_SCHEME_TCP_OBEX, Boolean.TRUE);
        suportScheme.put(BluetoothConsts.PROTOCOL_SCHEME_L2CAP, Boolean.TRUE);
        suportScheme.put("socket", Boolean.TRUE);
    }

    private MicroeditionConnector() {
    }

    static void copyAll(Hashtable dest, Hashtable src) {
        Enumeration en = src.keys();
        while (en.hasMoreElements()) {
            Object key = en.nextElement2();
            dest.put(key, src.get(key));
        }
    }

    static String validParamName(Hashtable map, String paramName) {
        String validName = (String) map.get(paramName.toLowerCase());
        if (validName != null) {
            return validName;
        }
        return null;
    }

    public static Connection open(String name) throws IOException {
        return openImpl(name, 3, false, true);
    }

    private static Connection openImpl(String name, int mode, boolean timeouts, boolean allowServer) throws IOException, NoSuchElementException, IllegalArgumentException {
        String host;
        boolean isServer;
        String bluecove_ext_psm;
        Hashtable params;
        DebugLog.debug("connecting", name);
        String portORuuid = null;
        Hashtable values = new Hashtable();
        int schemeEnd = name.indexOf("://");
        if (schemeEnd == -1) {
            throw new ConnectionNotFoundException(name);
        }
        String scheme = name.substring(0, schemeEnd);
        if (!suportScheme.containsKey(scheme)) {
            throw new ConnectionNotFoundException(scheme);
        }
        boolean schemeBluetooth = scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_RFCOMM) || scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_BT_OBEX) || scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_L2CAP);
        boolean isL2CAP = scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_L2CAP);
        boolean isTCPOBEX = scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_TCP_OBEX);
        BluetoothStack bluetoothStack = null;
        if (schemeBluetooth) {
            bluetoothStack = BlueCoveImpl.instance().getBluetoothStack();
        }
        int hostEnd = name.indexOf(58, scheme.length() + 3);
        if (hostEnd > -1) {
            host = name.substring(scheme.length() + 3, hostEnd);
            isServer = host.equals("localhost");
            if (isTCPOBEX) {
                params = new Hashtable();
                isServer = host.length() == 0;
            } else if (isL2CAP) {
                if (isServer) {
                    params = srvParamsL2CAP;
                } else {
                    params = cliParamsL2CAP;
                }
            } else if (isServer) {
                params = srvParams;
            } else {
                params = cliParams;
            }
            String paramsStr = name.substring(hostEnd + 1);
            UtilsStringTokenizer tok = new UtilsStringTokenizer(paramsStr, ";");
            if (tok.hasMoreTokens()) {
                portORuuid = tok.nextToken();
            } else {
                portORuuid = paramsStr;
            }
            while (tok.hasMoreTokens()) {
                String t2 = tok.nextToken();
                int equals = t2.indexOf(61);
                if (equals > -1) {
                    String param = t2.substring(0, equals);
                    String value = t2.substring(equals + 1);
                    String validName = validParamName(params, param);
                    if (validName != null) {
                        String hasValue = (String) values.get(validName);
                        if (hasValue != null && !hasValue.equals(value)) {
                            throw new IllegalArgumentException(new StringBuffer().append("duplicate param [").append(param).append("] value [").append(value).append("]").toString());
                        }
                        values.put(validName, value);
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("invalid param [").append(param).append("] value [").append(value).append("]").toString());
                    }
                } else {
                    throw new IllegalArgumentException(new StringBuffer().append("invalid param [").append(t2).append("]").toString());
                }
            }
        } else if (isTCPOBEX) {
            host = name.substring(scheme.length() + 3);
            isServer = host.length() == 0;
        } else {
            throw new IllegalArgumentException(name.substring(scheme.length() + 3));
        }
        if (isTCPOBEX && (portORuuid == null || portORuuid.length() == 0)) {
            portORuuid = String.valueOf(BluetoothConsts.TCP_OBEX_DEFAULT_PORT);
        }
        if (host == null || portORuuid == null) {
            throw new IllegalArgumentException();
        }
        BluetoothConnectionNotifierParams notifierParams = null;
        BluetoothConnectionParams connectionParams = null;
        int channel = 0;
        if (isServer) {
            if (!allowServer) {
                throw new IllegalArgumentException("Can't use server connection URL");
            }
            if (values.get("name") == null) {
                values.put("name", "BlueCove");
            } else if (schemeBluetooth) {
                validateBluetoothServiceName((String) values.get("name"));
            }
            if (schemeBluetooth) {
                notifierParams = new BluetoothConnectionNotifierParams(new UUID(portORuuid, false), paramBoolean(values, AUTHENTICATE), paramBoolean(values, ENCRYPT), paramBoolean(values, AUTHORIZE), (String) values.get("name"), paramBoolean(values, MASTER));
                notifierParams.timeouts = timeouts;
                if (notifierParams.encrypt && !notifierParams.authenticate) {
                    if (values.get(AUTHENTICATE) == null) {
                        notifierParams.authenticate = true;
                    } else {
                        throw new BluetoothConnectionException(6, "encryption requires authentication");
                    }
                }
                if (notifierParams.authorize && !notifierParams.authenticate) {
                    if (values.get(AUTHENTICATE) == null) {
                        notifierParams.authenticate = true;
                    } else {
                        throw new BluetoothConnectionException(6, "authorization requires authentication");
                    }
                }
                if (isL2CAP && (bluecove_ext_psm = (String) values.get(EXT_BLUECOVE_L2CAP_PSM)) != null) {
                    int psm = Integer.parseInt(bluecove_ext_psm, 16);
                    validateL2CAPPSM(psm, bluecove_ext_psm);
                    notifierParams.bluecove_ext_psm = psm;
                }
            }
        } else {
            try {
                channel = Integer.parseInt(portORuuid, isL2CAP ? 16 : 10);
                if (channel < 0) {
                    throw new IllegalArgumentException(new StringBuffer().append("channel ").append(portORuuid).toString());
                }
                if (schemeBluetooth) {
                    if (isL2CAP) {
                        validateL2CAPPSM(channel, portORuuid);
                    } else if (channel < 1 || channel > 30) {
                        throw new IllegalArgumentException(new StringBuffer().append("RFCOMM channel ").append(portORuuid).toString());
                    }
                    connectionParams = new BluetoothConnectionParams(RemoteDeviceHelper.getAddress(host), channel, paramBoolean(values, AUTHENTICATE), paramBoolean(values, ENCRYPT));
                    connectionParams.timeouts = timeouts;
                    if (connectionParams.encrypt && !connectionParams.authenticate) {
                        if (values.get(AUTHENTICATE) == null) {
                            connectionParams.authenticate = true;
                        } else {
                            throw new BluetoothConnectionException(6, "encryption requires authentication");
                        }
                    }
                    connectionParams.timeout = BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_CONNECT_TIMEOUT, 120000);
                }
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException(new StringBuffer().append("channel ").append(portORuuid).toString());
            }
        }
        OBEXConnectionParams obexConnectionParams = null;
        if (scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_TCP_OBEX) || scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_BT_OBEX)) {
            obexConnectionParams = new OBEXConnectionParams();
            obexConnectionParams.timeouts = timeouts;
            obexConnectionParams.timeout = BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_OBEX_TIMEOUT, 120000);
            obexConnectionParams.mtu = BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_OBEX_MTU, 1024);
        }
        if (scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_RFCOMM)) {
            if (isServer) {
                return new BluetoothRFCommConnectionNotifier(bluetoothStack, notifierParams);
            }
            return new BluetoothRFCommClientConnection(bluetoothStack, connectionParams);
        }
        if (scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_BT_OBEX)) {
            if (isServer) {
                notifierParams.obex = true;
                return new OBEXSessionNotifierImpl(new BluetoothRFCommConnectionNotifier(bluetoothStack, notifierParams), obexConnectionParams);
            }
            return new OBEXClientSessionImpl(new BluetoothRFCommClientConnection(bluetoothStack, connectionParams), obexConnectionParams);
        }
        if (scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_L2CAP)) {
            if (isServer) {
                return new BluetoothL2CAPConnectionNotifier(bluetoothStack, notifierParams, paramL2CAPMTU(values, RECEIVE_MTU), paramL2CAPMTU(values, TRANSMIT_MTU));
            }
            return new BluetoothL2CAPClientConnection(bluetoothStack, connectionParams, paramL2CAPMTU(values, RECEIVE_MTU), paramL2CAPMTU(values, TRANSMIT_MTU));
        }
        if (scheme.equals(BluetoothConsts.PROTOCOL_SCHEME_TCP_OBEX)) {
            if (isServer) {
                try {
                    return new OBEXSessionNotifierImpl(new ServerSocketConnection(Integer.parseInt(portORuuid)), obexConnectionParams);
                } catch (NumberFormatException e3) {
                    throw new IllegalArgumentException(new StringBuffer().append("port ").append(portORuuid).toString());
                }
            }
            return new OBEXClientSessionImpl(new SocketConnection(host, channel), obexConnectionParams);
        }
        if (scheme.equals("socket")) {
            if (isServer) {
                try {
                    return new ServerSocketConnection(Integer.parseInt(portORuuid));
                } catch (NumberFormatException e4) {
                    throw new IllegalArgumentException(new StringBuffer().append("port ").append(portORuuid).toString());
                }
            }
            return new SocketConnection(host, channel);
        }
        throw new ConnectionNotFoundException(new StringBuffer().append("scheme [").append(scheme).append("]").toString());
    }

    private static void validateL2CAPPSM(int channel, String channelAsString) throws IllegalArgumentException {
        if (channel < 5 || channel > 65535) {
            throw new IllegalArgumentException(new StringBuffer().append("PCM ").append(channelAsString).toString());
        }
        if (channel < 4097 && !BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_JSR_82_PSM_MINIMUM_OFF, false)) {
            throw new IllegalArgumentException(new StringBuffer().append("PCM ").append(channelAsString).append(", PCM values restricted by JSR-82 to minimum ").append(4097).append(", see BlueCoveConfigProperties.PROPERTY_JSR_82_PSM_MINIMUM_OFF").toString());
        }
        if ((channel & 256) != 0) {
            throw new IllegalArgumentException(new StringBuffer().append("9th bit set in PCM ").append(channelAsString).toString());
        }
        byte lsByte = (byte) (255 & channel);
        if (lsByte % 2 == 0) {
            throw new IllegalArgumentException(new StringBuffer().append("PSM value ").append(channelAsString).append(" least significant byte must be odd").toString());
        }
        byte msByte = (byte) ((65280 & channel) >> 8);
        if (msByte % 2 == 1) {
            throw new IllegalArgumentException(new StringBuffer().append("PSM value ").append(channelAsString).append(" most significant byte must be even").toString());
        }
    }

    private static void validateBluetoothServiceName(String serviceName) {
        if (serviceName.length() == 0) {
            throw new IllegalArgumentException("zero length service name");
        }
        for (int i2 = 0; i2 < serviceName.length(); i2++) {
            char c2 = serviceName.charAt(i2);
            if ((c2 < 'A' || c2 > 'Z') && ((c2 < 'a' || c2 > 'z') && ((c2 < '0' || c2 > '9') && " -_".indexOf(c2) == -1))) {
                throw new IllegalArgumentException(new StringBuffer().append("Illegal character '").append(c2).append("' in service name").toString());
            }
        }
    }

    private static boolean paramBoolean(Hashtable values, String name) {
        String v2 = (String) values.get(name);
        if (v2 == null) {
            return false;
        }
        if ("true".equals(v2)) {
            return true;
        }
        if ("false".equals(v2)) {
            return false;
        }
        throw new IllegalArgumentException(new StringBuffer().append("invalid param value ").append(name).append("=").append(v2).toString());
    }

    private static int paramL2CAPMTU(Hashtable values, String name) {
        String v2 = (String) values.get(name);
        if (v2 == null) {
            if (name.equals(TRANSMIT_MTU)) {
                return -1;
            }
            return L2CAPConnection.DEFAULT_MTU;
        }
        try {
            int mtu = Integer.parseInt(v2);
            if (mtu >= 48) {
                return mtu;
            }
            if (mtu > 0 && mtu < 48) {
                if (name.equals(TRANSMIT_MTU)) {
                    return 48;
                }
            }
            throw new IllegalArgumentException(new StringBuffer().append("invalid MTU param value ").append(name).append("=").append(v2).toString());
        } catch (NumberFormatException e2) {
            throw new IllegalArgumentException(new StringBuffer().append("invalid MTU value ").append(v2).toString());
        }
    }

    public static Connection open(String name, int mode) throws IOException {
        return openImpl(name, mode, false, true);
    }

    public static Connection open(String name, int mode, boolean timeouts) throws IOException {
        return openImpl(name, mode, timeouts, true);
    }

    public static DataInputStream openDataInputStream(String name) throws IOException {
        return new DataInputStream(openInputStream(name));
    }

    public static DataOutputStream openDataOutputStream(String name) throws IOException {
        return new DataOutputStream(openOutputStream(name));
    }

    public static InputStream openInputStream(String name) throws IOException {
        InputConnection con = (InputConnection) openImpl(name, 1, false, false);
        try {
            InputStream inputStreamOpenInputStream = con.openInputStream();
            con.close();
            return inputStreamOpenInputStream;
        } catch (Throwable th) {
            con.close();
            throw th;
        }
    }

    public static OutputStream openOutputStream(String name) throws IOException {
        OutputConnection con = (OutputConnection) openImpl(name, 2, false, false);
        try {
            OutputStream outputStreamOpenOutputStream = con.openOutputStream();
            con.close();
            return outputStreamOpenOutputStream;
        } catch (Throwable th) {
            con.close();
            throw th;
        }
    }
}
