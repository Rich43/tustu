package com.intel.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothRFCommConnection.class */
abstract class BluetoothRFCommConnection implements StreamConnection, BluetoothConnectionAccess {
    protected BluetoothStack bluetoothStack;
    protected volatile long handle;
    private BluetoothRFCommInputStream in;
    private BluetoothRFCommOutputStream out;
    private boolean isClosed = false;
    protected int securityOpt;
    RemoteDevice remoteDevice;

    abstract void closeConnectionHandle(long j2) throws IOException;

    protected BluetoothRFCommConnection(BluetoothStack bluetoothStack, long handle) {
        this.bluetoothStack = bluetoothStack;
        this.handle = handle;
    }

    void streamClosed() throws IOException {
        if (!this.isClosed) {
            return;
        }
        if (this.in != null && !this.in.isClosed()) {
            return;
        }
        if (this.out != null && !this.out.isClosed()) {
            return;
        }
        shutdown();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void shutdown() throws IOException {
        long synchronizedHandle;
        if (this.handle != 0) {
            DebugLog.debug("closing RFCOMM Connection", this.handle);
            synchronized (this) {
                synchronizedHandle = this.handle;
                this.handle = 0L;
            }
            if (synchronizedHandle != 0) {
                closeConnectionHandle(synchronizedHandle);
            }
        }
    }

    @Override // javax.microedition.io.InputConnection
    public InputStream openInputStream() throws IOException {
        if (this.isClosed) {
            throw new IOException("RFCOMM Connection is already closed");
        }
        if (this.in == null) {
            this.in = new BluetoothRFCommInputStream(this);
            return this.in;
        }
        if (this.in.isClosed()) {
            throw new IOException("Stream cannot be reopened");
        }
        throw new IOException("Another InputStream already opened");
    }

    @Override // javax.microedition.io.InputConnection
    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    @Override // javax.microedition.io.OutputConnection
    public OutputStream openOutputStream() throws IOException {
        if (this.isClosed) {
            throw new IOException("RFCOMM Connection is already closed");
        }
        if (this.out == null) {
            this.out = new BluetoothRFCommOutputStream(this);
            return this.out;
        }
        if (this.out.isClosed()) {
            throw new IOException("Stream cannot be reopened");
        }
        throw new IOException("Another OutputStream already opened");
    }

    @Override // javax.microedition.io.OutputConnection
    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        if (this.isClosed) {
            return;
        }
        this.isClosed = true;
        streamClosed();
    }

    protected void finalize() {
        try {
            close();
        } catch (IOException e2) {
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public boolean isClosed() {
        return this.isClosed;
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void markAuthenticated() {
        if (this.securityOpt == 0) {
            this.securityOpt = 1;
        }
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public int getSecurityOpt() {
        try {
            this.securityOpt = this.bluetoothStack.rfGetSecurityOpt(this.handle, this.securityOpt);
        } catch (IOException e2) {
        }
        return this.securityOpt;
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public boolean encrypt(long address, boolean on) throws IOException {
        if (this.isClosed) {
            throw new IOException("RFCOMM Connection is already closed");
        }
        boolean changed = this.bluetoothStack.rfEncrypt(address, this.handle, on);
        if (changed) {
            if (on) {
                this.securityOpt = 2;
            } else {
                this.securityOpt = 1;
            }
        }
        return changed;
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public long getRemoteAddress() throws IOException {
        if (this.isClosed) {
            throw new IOException("Connection closed");
        }
        return this.bluetoothStack.getConnectionRfRemoteAddress(this.handle);
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public RemoteDevice getRemoteDevice() {
        return this.remoteDevice;
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void setRemoteDevice(RemoteDevice remoteDevice) {
        this.remoteDevice = remoteDevice;
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public BluetoothStack getBluetoothStack() {
        return this.bluetoothStack;
    }
}
