package com.ftdi;

import com.ftdi.FTD2XX;
import com.sun.jna.Memory;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: JavaFTD2XX.jar:com/ftdi/FTDevice.class */
public class FTDevice {
    private static final FTD2XX ftd2xx = FTD2XX.INSTANCE;
    private final int devID;
    private final int devLocationID;
    private final int flag;
    private final DeviceType devType;
    private long ftHandle;
    private final String devSerialNumber;
    private final String devDescription;
    private FTDeviceInputStream fTDeviceInputStream = null;
    private FTDeviceOutputStream fTDeviceOutputStream = null;

    private FTDevice(DeviceType devType, int devID, int devLocationID, String devSerialNumber, String devDescription, long ftHandle, int flag) {
        this.devType = devType;
        this.devID = devID;
        this.devLocationID = devLocationID;
        this.devSerialNumber = devSerialNumber;
        this.devDescription = devDescription;
        this.ftHandle = ftHandle;
        this.flag = flag;
    }

    public String getDevDescription() {
        return this.devDescription;
    }

    public int getDevID() {
        return this.devID;
    }

    public String getDevSerialNumber() {
        return this.devSerialNumber;
    }

    public DeviceType getDevType() {
        return this.devType;
    }

    public int getDevLocationID() {
        return this.devLocationID;
    }

    public int getFlag() {
        return this.flag;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FTDevice)) {
            return false;
        }
        FTDevice eq = (FTDevice) obj;
        return eq.ftHandle == this.ftHandle;
    }

    public int hashCode() {
        long hash = (97 * 5) + this.ftHandle;
        return (int) hash;
    }

    public String toString() {
        return "FTDevice{devDescription=" + this.devDescription + ", devSerialNumber=" + this.devSerialNumber + '}';
    }

    private static boolean use32Bit() {
        return !Platform.is64Bit();
    }

    private static void ensureFTStatus(int ftstatus) throws FTD2XXException {
        if (ftstatus != FT_STATUS.OK.constant()) {
            throw new FTD2XXException(ftstatus);
        }
    }

    private static FTDevice getXthDevice(int Xth) throws FTD2XXException {
        IntByReference flag = new IntByReference();
        IntByReference devType = new IntByReference();
        IntByReference devID = new IntByReference();
        IntByReference locID = new IntByReference();
        Memory devSerNum = new Memory(16L);
        Memory devDesc = new Memory(64L);
        if (use32Bit()) {
            IntByReference ftHandle = new IntByReference();
            ensureFTStatus(ftd2xx.FT_GetDeviceInfoDetail(Xth, flag, devType, devID, locID, devSerNum, devDesc, ftHandle));
            return new FTDevice(DeviceType.values()[devType.getValue()], devID.getValue(), locID.getValue(), devSerNum.getString(0L), devDesc.getString(0L), ftHandle.getValue(), flag.getValue());
        }
        LongByReference ftHandle2 = new LongByReference();
        ensureFTStatus(ftd2xx.FT_GetDeviceInfoDetail(Xth, flag, devType, devID, locID, devSerNum, devDesc, ftHandle2));
        return new FTDevice(DeviceType.values()[devType.getValue()], devID.getValue(), locID.getValue(), devSerNum.getString(0L), devDesc.getString(0L), ftHandle2.getValue(), flag.getValue());
    }

    public static List<FTDevice> getDevices() throws FTD2XXException {
        return getDevices(false);
    }

    public static List<FTDevice> getDevices(boolean isIncludeOpenedDevices) throws FTD2XXException {
        IntByReference devNum = new IntByReference();
        ensureFTStatus(ftd2xx.FT_CreateDeviceInfoList(devNum));
        ArrayList<FTDevice> devs = new ArrayList<>(devNum.getValue());
        for (int i2 = 0; i2 < devNum.getValue(); i2++) {
            FTDevice device = getXthDevice(i2);
            if (isIncludeOpenedDevices) {
                devs.add(device);
            } else if ((device.flag & 1) == 0) {
                devs.add(device);
            }
        }
        Logger.getLogger(FTDevice.class.getName()).log(Level.INFO, "Found devs: {0} (All:{1})", new Object[]{Integer.valueOf(devs.size()), Integer.valueOf(devNum.getValue())});
        return devs;
    }

    public static List<FTDevice> getDevicesByDescription(String description) throws FTD2XXException {
        IntByReference devNum = new IntByReference();
        ensureFTStatus(ftd2xx.FT_CreateDeviceInfoList(devNum));
        ArrayList<FTDevice> devs = new ArrayList<>(devNum.getValue());
        for (int i2 = 0; i2 < devNum.getValue(); i2++) {
            FTDevice device = getXthDevice(i2);
            if ((device.flag & 1) == 0 && description.equals(device.devDescription)) {
                devs.add(device);
            }
        }
        Logger.getLogger(FTDevice.class.getName()).log(Level.INFO, "Found devs: {0} (All:{1})", new Object[]{Integer.valueOf(devs.size()), Integer.valueOf(devNum.getValue())});
        return devs;
    }

    public static List<FTDevice> getDevicesBySerialNumber(String serialNumber) throws FTD2XXException {
        IntByReference devNum = new IntByReference();
        ensureFTStatus(ftd2xx.FT_CreateDeviceInfoList(devNum));
        ArrayList<FTDevice> devs = new ArrayList<>(devNum.getValue());
        for (int i2 = 0; i2 < devNum.getValue(); i2++) {
            FTDevice device = getXthDevice(i2);
            if ((device.getFlag() & 1) == 0 && serialNumber.equals(device.devSerialNumber)) {
                devs.add(device);
            }
        }
        Logger.getLogger(FTDevice.class.getName()).log(Level.INFO, "Found devs: {0} (All:{1})", new Object[]{Integer.valueOf(devs.size()), Integer.valueOf(devNum.getValue())});
        return devs;
    }

    public static List<FTDevice> getDevicesByDeviceType(DeviceType deviceType) throws FTD2XXException {
        IntByReference devNum = new IntByReference();
        ensureFTStatus(ftd2xx.FT_CreateDeviceInfoList(devNum));
        ArrayList<FTDevice> devs = new ArrayList<>(devNum.getValue());
        for (int i2 = 0; i2 < devNum.getValue(); i2++) {
            FTDevice device = getXthDevice(i2);
            if ((device.flag & 1) == 0 && device.devType.equals(deviceType)) {
                devs.add(device);
            }
        }
        Logger.getLogger(FTDevice.class.getName()).log(Level.INFO, "Found devs: {0} (All:{1})", new Object[]{Integer.valueOf(devs.size()), Integer.valueOf(devNum.getValue())});
        return devs;
    }

    public void open() throws FTD2XXException {
        Memory memory = new Memory(16L);
        memory.setString(0L, this.devSerialNumber);
        if (use32Bit()) {
            IntByReference handle = new IntByReference();
            ensureFTStatus(ftd2xx.FT_OpenEx(memory, 1, handle));
            this.ftHandle = handle.getValue();
        } else {
            LongByReference handle2 = new LongByReference();
            ensureFTStatus(ftd2xx.FT_OpenEx(memory, 1, handle2));
            this.ftHandle = handle2.getValue();
        }
    }

    public void close() throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_Close((int) this.ftHandle));
        } else {
            ensureFTStatus(ftd2xx.FT_Close(this.ftHandle));
        }
    }

    public void setBaudRate(long baudRate) throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_SetBaudRate((int) this.ftHandle, (int) baudRate));
        } else {
            ensureFTStatus(ftd2xx.FT_SetBaudRate(this.ftHandle, (int) baudRate));
        }
    }

    public void setDivisor(int divisor) throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_SetDivisor((int) this.ftHandle, (short) divisor));
        } else {
            ensureFTStatus(ftd2xx.FT_SetDivisor(this.ftHandle, (short) divisor));
        }
    }

    public void setDataCharacteristics(WordLength wordLength, StopBits stopBits, Parity parity) throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_SetDataCharacteristics((int) this.ftHandle, (byte) wordLength.constant(), (byte) stopBits.constant(), (byte) parity.constant()));
        } else {
            ensureFTStatus(ftd2xx.FT_SetDataCharacteristics(this.ftHandle, (byte) wordLength.constant(), (byte) stopBits.constant(), (byte) parity.constant()));
        }
    }

    public void setTimeouts(long readTimeout, long writeTimeout) throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_SetTimeouts((int) this.ftHandle, (int) readTimeout, (int) writeTimeout));
        } else {
            ensureFTStatus(ftd2xx.FT_SetTimeouts(this.ftHandle, (int) readTimeout, (int) writeTimeout));
        }
    }

    public void setFlowControl(FlowControl flowControl) throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_SetFlowControl((int) this.ftHandle, (short) flowControl.constant(), (byte) 0, (byte) 0));
        } else {
            ensureFTStatus(ftd2xx.FT_SetFlowControl(this.ftHandle, (short) flowControl.constant(), (byte) 0, (byte) 0));
        }
    }

    public void setFlowControl(FlowControl flowControl, byte uXon, byte uXoff) throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_SetFlowControl((int) this.ftHandle, (short) flowControl.constant(), uXon, uXoff));
        } else {
            ensureFTStatus(ftd2xx.FT_SetFlowControl(this.ftHandle, (short) flowControl.constant(), uXon, uXoff));
        }
    }

    public void setDtr(boolean status) throws FTD2XXException {
        if (use32Bit()) {
            if (status) {
                ensureFTStatus(ftd2xx.FT_SetDtr((int) this.ftHandle));
                return;
            } else {
                ensureFTStatus(ftd2xx.FT_ClrDtr((int) this.ftHandle));
                return;
            }
        }
        if (status) {
            ensureFTStatus(ftd2xx.FT_SetDtr(this.ftHandle));
        } else {
            ensureFTStatus(ftd2xx.FT_ClrDtr(this.ftHandle));
        }
    }

    public void setRts(boolean status) throws FTD2XXException {
        if (use32Bit()) {
            if (status) {
                ensureFTStatus(ftd2xx.FT_SetRts((int) this.ftHandle));
                return;
            } else {
                ensureFTStatus(ftd2xx.FT_ClrRts((int) this.ftHandle));
                return;
            }
        }
        if (status) {
            ensureFTStatus(ftd2xx.FT_SetRts(this.ftHandle));
        } else {
            ensureFTStatus(ftd2xx.FT_ClrRts(this.ftHandle));
        }
    }

    public EnumSet<DeviceStatus> getDeviceStatus() throws FTD2XXException {
        IntByReference modstat = new IntByReference();
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_GetModemStatus((int) this.ftHandle, modstat));
            return DeviceStatus.parseToEnumset(modstat.getValue());
        }
        ensureFTStatus(ftd2xx.FT_GetModemStatus(this.ftHandle, modstat));
        return DeviceStatus.parseToEnumset(modstat.getValue());
    }

    public int getQueueStatus() throws FTD2XXException {
        IntByReference reference = new IntByReference();
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_GetQueueStatus((int) this.ftHandle, reference));
            return reference.getValue();
        }
        ensureFTStatus(ftd2xx.FT_GetQueueStatus(this.ftHandle, reference));
        return reference.getValue();
    }

    public void purgeBuffer(boolean rxBuffer, boolean txBuffer) throws FTD2XXException {
        int mask = 0;
        if (rxBuffer) {
            mask = 0 | Purge.PURGE_RX.constant();
        }
        if (txBuffer) {
            mask |= Purge.PURGE_TX.constant();
        }
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_Purge((int) this.ftHandle, mask));
        } else {
            ensureFTStatus(ftd2xx.FT_Purge(this.ftHandle, mask));
        }
    }

    public void resetDevice() throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_ResetDevice((int) this.ftHandle));
        } else {
            ensureFTStatus(ftd2xx.FT_ResetDevice(this.ftHandle));
        }
    }

    public void setLatencyTimer(short timer) throws FTD2XXException, IllegalArgumentException {
        if (timer < 1 || timer >= 255) {
            throw new IllegalArgumentException("Valid range is 1 – 255!");
        }
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_SetLatencyTimer((int) this.ftHandle, (byte) timer));
        } else {
            ensureFTStatus(ftd2xx.FT_SetLatencyTimer(this.ftHandle, (byte) timer));
        }
    }

    public short getLatencyTimer() throws FTD2XXException {
        ByteByReference byReference = new ByteByReference();
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_GetLatencyTimer((int) this.ftHandle, byReference));
            return (short) (byReference.getValue() & 255);
        }
        ensureFTStatus(ftd2xx.FT_GetLatencyTimer(this.ftHandle, byReference));
        return (short) (byReference.getValue() & 255);
    }

    public void setBitMode(byte ucMask, BitModes bitMode) throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_SetBitMode((int) this.ftHandle, ucMask, (byte) bitMode.constant()));
        } else {
            ensureFTStatus(ftd2xx.FT_SetBitMode(this.ftHandle, ucMask, (byte) bitMode.constant()));
        }
    }

    public BitModes getBitMode() throws FTD2XXException {
        ByteByReference byt = new ByteByReference();
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_GetBitmode((int) this.ftHandle, byt));
            return BitModes.parse(byt.getValue());
        }
        ensureFTStatus(ftd2xx.FT_GetBitmode(this.ftHandle, byt));
        return BitModes.parse(byt.getValue());
    }

    public void setUSBParameters(int inTransferSize, int outTransferSize) throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_SetUSBParameters(this.ftHandle, inTransferSize, outTransferSize));
        } else {
            ensureFTStatus(ftd2xx.FT_SetUSBParameters((int) this.ftHandle, inTransferSize, outTransferSize));
        }
    }

    public void writeEEPROM(EEPROMData programData) throws FTD2XXException {
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_EE_Program((int) this.ftHandle, programData.ft_program_data));
        } else {
            ensureFTStatus(ftd2xx.FT_EE_Program(this.ftHandle, programData.ft_program_data));
        }
    }

    public EEPROMData readEEPROM() throws FTD2XXException {
        if (use32Bit()) {
            FTD2XX.FT_PROGRAM_DATA.ByReference ftByReference = new FTD2XX.FT_PROGRAM_DATA.ByReference();
            ensureFTStatus(ftd2xx.FT_EE_Read((int) this.ftHandle, ftByReference));
            return new EEPROMData(ftByReference);
        }
        FTD2XX.FT_PROGRAM_DATA.ByReference ftByReference2 = new FTD2XX.FT_PROGRAM_DATA.ByReference();
        ensureFTStatus(ftd2xx.FT_EE_Read(this.ftHandle, ftByReference2));
        return new EEPROMData(ftByReference2);
    }

    public int getEEPROMUserAreaSize() throws FTD2XXException {
        if (use32Bit()) {
            IntByReference size = new IntByReference();
            ensureFTStatus(ftd2xx.FT_EE_UASize((int) this.ftHandle, size));
            return size.getValue();
        }
        IntByReference size2 = new IntByReference();
        ensureFTStatus(ftd2xx.FT_EE_UASize(this.ftHandle, size2));
        return size2.getValue();
    }

    public byte[] readEEPROMUserArea(int numberOfBytes) throws FTD2XXException {
        IntByReference actually = new IntByReference();
        Memory dest = new Memory(numberOfBytes);
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_EE_UARead((int) this.ftHandle, (Pointer) dest, numberOfBytes, actually));
            return dest.getByteArray(0L, actually.getValue());
        }
        ensureFTStatus(ftd2xx.FT_EE_UARead(this.ftHandle, dest, numberOfBytes, actually));
        return dest.getByteArray(0L, actually.getValue());
    }

    public byte[] readFullEEPROMUserArea() throws FTD2XXException {
        int numberOfBytes = getEEPROMUserAreaSize();
        return readEEPROMUserArea(numberOfBytes);
    }

    public String readFullEEPROMUserAreaAsString() throws IOException {
        IntByReference actually = new IntByReference();
        int numberOfBytes = getEEPROMUserAreaSize();
        Memory dest = new Memory(numberOfBytes);
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_EE_UARead((int) this.ftHandle, (Pointer) dest, numberOfBytes, actually));
            return dest.getString(0L);
        }
        ensureFTStatus(ftd2xx.FT_EE_UARead(this.ftHandle, dest, numberOfBytes, actually));
        return dest.getString(0L);
    }

    public void writeEEPROMUserArea(byte[] data) throws FTD2XXException {
        Memory source = new Memory(data.length);
        source.write(0L, data, 0, data.length);
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_EE_UAWrite((int) this.ftHandle, (Pointer) source, data.length));
        } else {
            ensureFTStatus(ftd2xx.FT_EE_UAWrite(this.ftHandle, source, data.length));
        }
    }

    public void writeEEPROMUserArea(String data) throws FTD2XXException {
        Memory source = new Memory(data.length());
        source.setString(0L, data);
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_EE_UAWrite((int) this.ftHandle, (Pointer) source, data.length()));
        } else {
            ensureFTStatus(ftd2xx.FT_EE_UAWrite(this.ftHandle, source, data.length()));
        }
    }

    public int write(byte[] bytes, int offset, int length) throws FTD2XXException {
        Memory memory = new Memory(length);
        memory.write(0L, bytes, offset, length);
        IntByReference wrote = new IntByReference();
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_Write((int) this.ftHandle, (Pointer) memory, length, wrote));
        } else {
            ensureFTStatus(ftd2xx.FT_Write(this.ftHandle, memory, length, wrote));
        }
        return wrote.getValue();
    }

    public int write(byte[] bytes) throws FTD2XXException {
        return write(bytes, 0, bytes.length);
    }

    public boolean write(int b2) throws FTD2XXException {
        byte[] c2 = {(byte) b2};
        return write(c2) == 1;
    }

    public int read(byte[] bytes, int offset, int lenght) throws FTD2XXException {
        Memory memory = new Memory(lenght);
        IntByReference read = new IntByReference();
        if (use32Bit()) {
            ensureFTStatus(ftd2xx.FT_Read((int) this.ftHandle, (Pointer) memory, lenght, read));
            memory.read(0L, bytes, offset, lenght);
            return read.getValue();
        }
        ensureFTStatus(ftd2xx.FT_Read(this.ftHandle, memory, lenght, read));
        memory.read(0L, bytes, offset, lenght);
        return read.getValue();
    }

    public int read(byte[] bytes) throws FTD2XXException {
        return read(bytes, 0, bytes.length);
    }

    public int read() throws FTD2XXException {
        byte[] c2 = new byte[1];
        int ret = read(c2);
        if (ret == 1) {
            return c2[0] & 255;
        }
        return -1;
    }

    public byte[] read(int number) throws FTD2XXException {
        byte[] ret = new byte[number];
        int actually = read(ret);
        if (actually != number) {
            byte[] shrink = new byte[actually];
            System.arraycopy(ret, 0, shrink, 0, actually);
            return shrink;
        }
        return ret;
    }

    public InputStream getInputStream() {
        if (this.fTDeviceInputStream == null) {
            this.fTDeviceInputStream = new FTDeviceInputStream(this);
        }
        return this.fTDeviceInputStream;
    }

    public OutputStream getOutputStream() {
        if (this.fTDeviceOutputStream == null) {
            this.fTDeviceOutputStream = new FTDeviceOutputStream(this);
        }
        return this.fTDeviceOutputStream;
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } catch (FTD2XXException e2) {
        }
        super.finalize();
    }
}
