package org.apache.commons.net.telnet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/telnet/TelnetClient.class */
public class TelnetClient extends Telnet {
    private InputStream __input;
    private OutputStream __output;
    protected boolean readerThread;
    private TelnetInputListener inputListener;

    public TelnetClient() {
        super("VT100");
        this.readerThread = true;
        this.__input = null;
        this.__output = null;
    }

    public TelnetClient(String termtype) {
        super(termtype);
        this.readerThread = true;
        this.__input = null;
        this.__output = null;
    }

    void _flushOutputStream() throws IOException {
        this._output_.flush();
    }

    void _closeOutputStream() throws IOException {
        try {
            this._output_.close();
            this._output_ = null;
        } catch (Throwable th) {
            this._output_ = null;
            throw th;
        }
    }

    @Override // org.apache.commons.net.telnet.Telnet, org.apache.commons.net.SocketClient
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        TelnetInputStream tmp = new TelnetInputStream(this._input_, this, this.readerThread);
        if (this.readerThread) {
            tmp._start();
        }
        this.__input = new BufferedInputStream(tmp);
        this.__output = new TelnetOutputStream(this);
    }

    @Override // org.apache.commons.net.SocketClient
    public void disconnect() throws IOException {
        try {
            if (this.__input != null) {
                this.__input.close();
            }
            if (this.__output != null) {
                this.__output.close();
            }
        } finally {
            this.__output = null;
            this.__input = null;
            super.disconnect();
        }
    }

    public OutputStream getOutputStream() {
        return this.__output;
    }

    public InputStream getInputStream() {
        return this.__input;
    }

    public boolean getLocalOptionState(int option) {
        return _stateIsWill(option) && _requestedWill(option);
    }

    public boolean getRemoteOptionState(int option) {
        return _stateIsDo(option) && _requestedDo(option);
    }

    public boolean sendAYT(long timeout) throws InterruptedException, IOException, IllegalArgumentException {
        return _sendAYT(timeout);
    }

    public void sendSubnegotiation(int[] message) throws IOException, IllegalArgumentException {
        if (message.length < 1) {
            throw new IllegalArgumentException("zero length message");
        }
        _sendSubnegotiation(message);
    }

    public void sendCommand(byte command) throws IOException, IllegalArgumentException {
        _sendCommand(command);
    }

    @Override // org.apache.commons.net.telnet.Telnet
    public void addOptionHandler(TelnetOptionHandler opthand) throws IOException, InvalidTelnetOptionException {
        super.addOptionHandler(opthand);
    }

    @Override // org.apache.commons.net.telnet.Telnet
    public void deleteOptionHandler(int optcode) throws IOException, InvalidTelnetOptionException {
        super.deleteOptionHandler(optcode);
    }

    public void registerSpyStream(OutputStream spystream) {
        super._registerSpyStream(spystream);
    }

    public void stopSpyStream() {
        super._stopSpyStream();
    }

    @Override // org.apache.commons.net.telnet.Telnet
    public void registerNotifHandler(TelnetNotificationHandler notifhand) {
        super.registerNotifHandler(notifhand);
    }

    @Override // org.apache.commons.net.telnet.Telnet
    public void unregisterNotifHandler() {
        super.unregisterNotifHandler();
    }

    public void setReaderThread(boolean flag) {
        this.readerThread = flag;
    }

    public boolean getReaderThread() {
        return this.readerThread;
    }

    public synchronized void registerInputListener(TelnetInputListener listener) {
        this.inputListener = listener;
    }

    public synchronized void unregisterInputListener() {
        this.inputListener = null;
    }

    void notifyInputListener() {
        TelnetInputListener listener;
        synchronized (this) {
            listener = this.inputListener;
        }
        if (listener != null) {
            listener.telnetInputAvailable();
        }
    }
}
