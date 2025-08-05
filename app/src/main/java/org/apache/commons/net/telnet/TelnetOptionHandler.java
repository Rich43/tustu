package org.apache.commons.net.telnet;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/telnet/TelnetOptionHandler.class */
public abstract class TelnetOptionHandler {
    private int optionCode;
    private boolean initialLocal;
    private boolean initialRemote;
    private boolean acceptLocal;
    private boolean acceptRemote;
    private boolean doFlag = false;
    private boolean willFlag = false;

    public TelnetOptionHandler(int optcode, boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
        this.optionCode = -1;
        this.initialLocal = false;
        this.initialRemote = false;
        this.acceptLocal = false;
        this.acceptRemote = false;
        this.optionCode = optcode;
        this.initialLocal = initlocal;
        this.initialRemote = initremote;
        this.acceptLocal = acceptlocal;
        this.acceptRemote = acceptremote;
    }

    public int getOptionCode() {
        return this.optionCode;
    }

    public boolean getAcceptLocal() {
        return this.acceptLocal;
    }

    public boolean getAcceptRemote() {
        return this.acceptRemote;
    }

    public void setAcceptLocal(boolean accept) {
        this.acceptLocal = accept;
    }

    public void setAcceptRemote(boolean accept) {
        this.acceptRemote = accept;
    }

    public boolean getInitLocal() {
        return this.initialLocal;
    }

    public boolean getInitRemote() {
        return this.initialRemote;
    }

    public void setInitLocal(boolean init) {
        this.initialLocal = init;
    }

    public void setInitRemote(boolean init) {
        this.initialRemote = init;
    }

    public int[] answerSubnegotiation(int[] suboptionData, int suboptionLength) {
        return null;
    }

    public int[] startSubnegotiationLocal() {
        return null;
    }

    public int[] startSubnegotiationRemote() {
        return null;
    }

    boolean getWill() {
        return this.willFlag;
    }

    void setWill(boolean state) {
        this.willFlag = state;
    }

    boolean getDo() {
        return this.doFlag;
    }

    void setDo(boolean state) {
        this.doFlag = state;
    }
}
