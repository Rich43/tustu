package com.intel.bluetooth;

import java.io.IOException;
import javax.bluetooth.L2CAPConnection;
import javax.bluetooth.RemoteDevice;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothL2CAPConnection.class */
abstract class BluetoothL2CAPConnection implements L2CAPConnection, BluetoothConnectionAccess {
    protected BluetoothStack bluetoothStack;
    protected volatile long handle;
    protected int transmitMTU;
    protected int securityOpt;
    private RemoteDevice remoteDevice;
    private boolean isClosed = false;

    abstract void closeConnectionHandle(long j2) throws IOException;

    protected BluetoothL2CAPConnection(BluetoothStack bluetoothStack, long handle) {
        this.bluetoothStack = bluetoothStack;
        this.handle = handle;
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public long getRemoteAddress() throws IOException {
        if (this.isClosed) {
            throw new IOException("Connection closed");
        }
        return this.bluetoothStack.l2RemoteAddress(this.handle);
    }

    @Override // javax.bluetooth.L2CAPConnection
    public int getReceiveMTU() throws IOException {
        if (this.isClosed) {
            throw new IOException("Connection closed");
        }
        return this.bluetoothStack.l2GetReceiveMTU(this.handle);
    }

    @Override // javax.bluetooth.L2CAPConnection
    public int getTransmitMTU() throws IOException {
        if (this.isClosed) {
            throw new IOException("Connection closed");
        }
        return this.bluetoothStack.l2GetTransmitMTU(this.handle);
    }

    @Override // javax.bluetooth.L2CAPConnection
    public boolean ready() throws IOException {
        if (this.isClosed) {
            throw new IOException("Connection closed");
        }
        return this.bluetoothStack.l2Ready(this.handle);
    }

    @Override // javax.bluetooth.L2CAPConnection
    public int receive(byte[] inBuf) throws IOException {
        if (this.isClosed) {
            throw new IOException("Connection closed");
        }
        if (inBuf == null) {
            throw new NullPointerException("inBuf is null");
        }
        return this.bluetoothStack.l2Receive(this.handle, inBuf);
    }

    @Override // javax.bluetooth.L2CAPConnection
    public void send(byte[] data) throws IOException {
        if (this.isClosed) {
            throw new IOException("Connection closed");
        }
        if (data == null) {
            throw new NullPointerException("data is null");
        }
        this.bluetoothStack.l2Send(this.handle, data, this.transmitMTU);
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        if (this.isClosed) {
            return;
        }
        this.isClosed = true;
        shutdown();
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public void shutdown() throws IOException {
        long synchronizedHandle;
        if (this.handle != 0) {
            DebugLog.debug("closing L2CAP Connection", this.handle);
            synchronized (this) {
                synchronizedHandle = this.handle;
                this.handle = 0L;
            }
            if (synchronizedHandle != 0) {
                closeConnectionHandle(synchronizedHandle);
            }
        }
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
            this.securityOpt = this.bluetoothStack.l2GetSecurityOpt(this.handle, this.securityOpt);
        } catch (IOException e2) {
        }
        return this.securityOpt;
    }

    @Override // com.intel.bluetooth.BluetoothConnectionAccess
    public boolean encrypt(long address, boolean on) throws IOException {
        if (this.isClosed) {
            throw new IOException("L2CAP Connection is already closed");
        }
        boolean changed = this.bluetoothStack.l2Encrypt(address, this.handle, on);
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
