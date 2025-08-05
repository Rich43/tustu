package org.apache.commons.net.imap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.SocketClient;
import org.apache.commons.net.io.CRLFLineReader;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/imap/IMAP.class */
public class IMAP extends SocketClient {
    public static final int DEFAULT_PORT = 143;
    protected static final String __DEFAULT_ENCODING = "ISO-8859-1";
    private IMAPState __state;
    protected BufferedWriter __writer;
    protected BufferedReader _reader;
    private int _replyCode;
    private final List<String> _replyLines;
    public static final IMAPChunkListener TRUE_CHUNK_LISTENER = new IMAPChunkListener() { // from class: org.apache.commons.net.imap.IMAP.1
        @Override // org.apache.commons.net.imap.IMAP.IMAPChunkListener
        public boolean chunkReceived(IMAP imap) {
            return true;
        }
    };
    private volatile IMAPChunkListener __chunkListener;
    private final char[] _initialID = {'A', 'A', 'A', 'A'};

    /* loaded from: commons-net-3.6.jar:org/apache/commons/net/imap/IMAP$IMAPChunkListener.class */
    public interface IMAPChunkListener {
        boolean chunkReceived(IMAP imap);
    }

    /* loaded from: commons-net-3.6.jar:org/apache/commons/net/imap/IMAP$IMAPState.class */
    public enum IMAPState {
        DISCONNECTED_STATE,
        NOT_AUTH_STATE,
        AUTH_STATE,
        LOGOUT_STATE
    }

    public IMAP() {
        setDefaultPort(143);
        this.__state = IMAPState.DISCONNECTED_STATE;
        this._reader = null;
        this.__writer = null;
        this._replyLines = new ArrayList();
        createCommandSupport();
    }

    private void __getReply() throws IOException {
        __getReply(true);
    }

    private void __getReply(boolean wantTag) throws IOException {
        IMAPChunkListener il;
        this._replyLines.clear();
        String line = this._reader.readLine();
        if (line == null) {
            throw new EOFException("Connection closed without indication.");
        }
        this._replyLines.add(line);
        if (wantTag) {
            while (IMAPReply.isUntagged(line)) {
                int literalCount = IMAPReply.literalCount(line);
                boolean isMultiLine = literalCount >= 0;
                while (literalCount >= 0) {
                    String line2 = this._reader.readLine();
                    if (line2 == null) {
                        throw new EOFException("Connection closed without indication.");
                    }
                    this._replyLines.add(line2);
                    literalCount -= line2.length() + 2;
                }
                if (isMultiLine && (il = this.__chunkListener) != null) {
                    boolean clear = il.chunkReceived(this);
                    if (clear) {
                        fireReplyReceived(3, getReplyString());
                        this._replyLines.clear();
                    }
                }
                line = this._reader.readLine();
                if (line == null) {
                    throw new EOFException("Connection closed without indication.");
                }
                this._replyLines.add(line);
            }
            this._replyCode = IMAPReply.getReplyCode(line);
        } else {
            this._replyCode = IMAPReply.getUntaggedReplyCode(line);
        }
        fireReplyReceived(this._replyCode, getReplyString());
    }

    @Override // org.apache.commons.net.SocketClient
    protected void fireReplyReceived(int replyCode, String ignored) {
        if (getCommandSupport().getListenerCount() > 0) {
            getCommandSupport().fireReplyReceived(replyCode, getReplyString());
        }
    }

    @Override // org.apache.commons.net.SocketClient
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        this._reader = new CRLFLineReader(new InputStreamReader(this._input_, "ISO-8859-1"));
        this.__writer = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
        int tmo = getSoTimeout();
        if (tmo <= 0) {
            setSoTimeout(this.connectTimeout);
        }
        __getReply(false);
        if (tmo <= 0) {
            setSoTimeout(tmo);
        }
        setState(IMAPState.NOT_AUTH_STATE);
    }

    protected void setState(IMAPState state) {
        this.__state = state;
    }

    public IMAPState getState() {
        return this.__state;
    }

    @Override // org.apache.commons.net.SocketClient
    public void disconnect() throws IOException {
        super.disconnect();
        this._reader = null;
        this.__writer = null;
        this._replyLines.clear();
        setState(IMAPState.DISCONNECTED_STATE);
    }

    private int sendCommandWithID(String commandID, String command, String args) throws IOException {
        StringBuilder __commandBuffer = new StringBuilder();
        if (commandID != null) {
            __commandBuffer.append(commandID);
            __commandBuffer.append(' ');
        }
        __commandBuffer.append(command);
        if (args != null) {
            __commandBuffer.append(' ');
            __commandBuffer.append(args);
        }
        __commandBuffer.append("\r\n");
        String message = __commandBuffer.toString();
        this.__writer.write(message);
        this.__writer.flush();
        fireCommandSent(command, message);
        __getReply();
        return this._replyCode;
    }

    public int sendCommand(String command, String args) throws IOException {
        return sendCommandWithID(generateCommandID(), command, args);
    }

    public int sendCommand(String command) throws IOException {
        return sendCommand(command, (String) null);
    }

    public int sendCommand(IMAPCommand command, String args) throws IOException {
        return sendCommand(command.getIMAPCommand(), args);
    }

    public boolean doCommand(IMAPCommand command, String args) throws IOException {
        return IMAPReply.isSuccess(sendCommand(command, args));
    }

    public int sendCommand(IMAPCommand command) throws IOException {
        return sendCommand(command, (String) null);
    }

    public boolean doCommand(IMAPCommand command) throws IOException {
        return IMAPReply.isSuccess(sendCommand(command));
    }

    public int sendData(String command) throws IOException {
        return sendCommandWithID(null, command, null);
    }

    public String[] getReplyStrings() {
        return (String[]) this._replyLines.toArray(new String[this._replyLines.size()]);
    }

    public String getReplyString() {
        StringBuilder buffer = new StringBuilder(256);
        for (String s2 : this._replyLines) {
            buffer.append(s2);
            buffer.append("\r\n");
        }
        return buffer.toString();
    }

    public void setChunkListener(IMAPChunkListener listener) {
        this.__chunkListener = listener;
    }

    protected String generateCommandID() {
        String res = new String(this._initialID);
        boolean carry = true;
        for (int i2 = this._initialID.length - 1; carry && i2 >= 0; i2--) {
            if (this._initialID[i2] == 'Z') {
                this._initialID[i2] = 'A';
            } else {
                char[] cArr = this._initialID;
                int i3 = i2;
                cArr[i3] = (char) (cArr[i3] + 1);
                carry = false;
            }
        }
        return res;
    }
}
