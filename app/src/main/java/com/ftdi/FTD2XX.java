package com.ftdi;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.ShortByReference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

/* loaded from: JavaFTD2XX.jar:com/ftdi/FTD2XX.class */
interface FTD2XX extends Library {
    public static final FTD2XX INSTANCE = (FTD2XX) Native.loadLibrary(Loader.getNative(), FTD2XX.class);
    public static final int FT_FLAGS_OPENED = 1;
    public static final int FT_LIST_NUMBER_ONLY = 524288;
    public static final int FT_LIST_BY_INDEX = 1073741824;
    public static final int FT_LIST_ALL = 536870912;
    public static final int FT_OPEN_BY_SERIAL_NUMBER = 1;
    public static final int FT_OPEN_BY_DESCRIPTION = 2;
    public static final int FT_OPEN_BY_LOCATION = 4;

    /* loaded from: JavaFTD2XX.jar:com/ftdi/FTD2XX$FT_DEVICE_LIST_INFO_NODE.class */
    public static class FT_DEVICE_LIST_INFO_NODE extends Structure {
        public int Flags;
        public int Type;
        public int ID;
        public int LocId;
        public Memory SerialNumber = new Memory(16);
        public Memory Description = new Memory(64);
        public long ftHandle;
    }

    /* loaded from: JavaFTD2XX.jar:com/ftdi/FTD2XX$FT_PROGRAM_DATA.class */
    public static class FT_PROGRAM_DATA extends Structure {
        public short VendorId;
        public short ProductId;
        public short MaxPower;
        public short PnP;
        public short SelfPowered;
        public short RemoteWakeup;
        public byte Rev4;
        public byte IsoIn;
        public byte IsoOut;
        public byte PullDownEnable;
        public byte SerNumEnable;
        public byte USBVersionEnable;
        public short USBVersion;
        public byte Rev5;
        public byte IsoInA;
        public byte IsoInB;
        public byte IsoOutA;
        public byte IsoOutB;
        public byte PullDownEnable5;
        public byte SerNumEnable5;
        public byte USBVersionEnable5;
        public short USBVersion5;
        public byte AIsHighCurrent;
        public byte BIsHighCurrent;
        public byte IFAIsFifo;
        public byte IFAIsFifoTar;
        public byte IFAIsFastSer;
        public byte AIsVCP;
        public byte IFBIsFifo;
        public byte IFBIsFifoTar;
        public byte IFBIsFastSer;
        public byte BIsVCP;
        public byte UseExtOsc;
        public byte HighDriveIOs;
        public byte EndpointSize;
        public byte PullDownEnableR;
        public byte SerNumEnableR;
        public byte InvertTXD;
        public byte InvertRXD;
        public byte InvertRTS;
        public byte InvertCTS;
        public byte InvertDTR;
        public byte InvertDSR;
        public byte InvertDCD;
        public byte InvertRI;
        public byte Cbus0;
        public byte Cbus1;
        public byte Cbus2;
        public byte Cbus3;
        public byte Cbus4;
        public byte RIsD2XX;
        public byte PullDownEnable7;
        public byte SerNumEnable7;
        public byte ALSlowSlew;
        public byte ALSchmittInput;
        public byte ALDriveCurrent;
        public byte AHSlowSlew;
        public byte AHSchmittInput;
        public byte AHDriveCurrent;
        public byte BLSlowSlew;
        public byte BLSchmittInput;
        public byte BLDriveCurrent;
        public byte BHSlowSlew;
        public byte BHSchmittInput;
        public byte BHDriveCurrent;
        public byte IFAIsFifo7;
        public byte IFAIsFifoTar7;
        public byte IFAIsFastSer7;
        public byte AIsVCP7;
        public byte IFBIsFifo7;
        public byte IFBIsFifoTar7;
        public byte IFBIsFastSer7;
        public byte BIsVCP7;
        public byte PowerSaveEnable;
        public byte PullDownEnable8;
        public byte SerNumEnable8;
        public byte ASlowSlew;
        public byte ASchmittInput;
        public byte ADriveCurrent;
        public byte BSlowSlew;
        public byte BSchmittInput;
        public byte BDriveCurrent;
        public byte CSlowSlew;
        public byte CSchmittInput;
        public byte CDriveCurrent;
        public byte DSlowSlew;
        public byte DSchmittInput;
        public byte DDriveCurrent;
        public byte ARIIsTXDEN;
        public byte BRIIsTXDEN;
        public byte CRIIsTXDEN;
        public byte DRIIsTXDEN;
        public byte AIsVCP8;
        public byte BIsVCP8;
        public byte CIsVCP8;
        public byte DIsVCP8;
        public byte PullDownEnableH;
        public byte SerNumEnableH;
        public byte ACSlowSlewH;
        public byte ACSchmittInputH;
        public byte ACDriveCurrentH;
        public byte ADSlowSlewH;
        public byte ADSchmittInputH;
        public byte ADDriveCurrentH;
        public byte Cbus0H;
        public byte Cbus1H;
        public byte Cbus2H;
        public byte Cbus3H;
        public byte Cbus4H;
        public byte Cbus5H;
        public byte Cbus6H;
        public byte Cbus7H;
        public byte Cbus8H;
        public byte Cbus9H;
        public byte IsFifoH;
        public byte IsFifoTarH;
        public byte IsFastSerH;
        public byte IsFT1248H;
        public byte FT1248CpolH;
        public byte FT1248LsbH;
        public byte FT1248FlowControlH;
        public byte IsVCPH;
        public byte PowerSaveEnableH;
        public int Signature1 = 0;
        public int Signature2 = -1;
        public int Version = 0;
        public Pointer Manufacturer = new Memory(32);
        public Pointer ManufacturerId = new Memory(16);
        public Pointer Description = new Memory(64);
        public Pointer SerialNumber = new Memory(16);

        /* loaded from: JavaFTD2XX.jar:com/ftdi/FTD2XX$FT_PROGRAM_DATA$ByReference.class */
        public static class ByReference extends FT_PROGRAM_DATA implements Structure.ByReference {
        }
    }

    /* loaded from: JavaFTD2XX.jar:com/ftdi/FTD2XX$NotificationEvents.class */
    public static class NotificationEvents {
        public static final int FT_EVENT_RXCHAR = 1;
        public static final int FT_EVENT_MODEM_STATUS = 2;
        public static final int FT_EVENT_LINE_STATUS = 4;
    }

    int FT_SetVIDPID(int i2, int i3);

    int FT_GetVIDPID(IntByReference intByReference, IntByReference intByReference2);

    int FT_CreateDeviceInfoList(IntByReference intByReference);

    int FT_GetDeviceInfoList(FT_DEVICE_LIST_INFO_NODE[] ft_device_list_info_nodeArr, IntByReference intByReference);

    int FT_GetDeviceInfoDetail(int i2, IntByReference intByReference, IntByReference intByReference2, IntByReference intByReference3, IntByReference intByReference4, Pointer pointer, Pointer pointer2, LongByReference longByReference);

    int FT_GetDeviceInfoDetail(int i2, IntByReference intByReference, IntByReference intByReference2, IntByReference intByReference3, IntByReference intByReference4, Pointer pointer, Pointer pointer2, IntByReference intByReference5);

    int FT_ListDevices(Pointer pointer, Pointer pointer2, int i2);

    int FT_Open(int i2, LongByReference longByReference);

    int FT_Open(int i2, IntByReference intByReference);

    int FT_OpenEx(Pointer pointer, int i2, LongByReference longByReference);

    int FT_OpenEx(Pointer pointer, int i2, IntByReference intByReference);

    int FT_Close(long j2);

    int FT_Read(long j2, Pointer pointer, int i2, IntByReference intByReference);

    int FT_Read(int i2, Pointer pointer, int i3, IntByReference intByReference);

    int FT_Write(long j2, Pointer pointer, int i2, IntByReference intByReference);

    int FT_Write(int i2, Pointer pointer, int i3, IntByReference intByReference);

    int FT_SetBaudRate(long j2, int i2);

    int FT_SetBaudRate(int i2, int i3);

    int FT_SetDivisor(long j2, short s2);

    int FT_SetDivisor(int i2, short s2);

    int FT_SetDataCharacteristics(long j2, byte b2, byte b3, byte b4);

    int FT_SetDataCharacteristics(int i2, byte b2, byte b3, byte b4);

    int FT_SetTimeouts(long j2, int i2, int i3);

    int FT_SetTimeouts(int i2, int i3, int i4);

    int FT_SetFlowControl(long j2, short s2, byte b2, byte b3);

    int FT_SetFlowControl(int i2, short s2, byte b2, byte b3);

    int FT_SetDtr(long j2);

    int FT_SetDtr(int i2);

    int FT_ClrDtr(long j2);

    int FT_ClrDtr(int i2);

    int FT_SetRts(long j2);

    int FT_SetRts(int i2);

    int FT_ClrRts(long j2);

    int FT_ClrRts(int i2);

    int FT_GetModemStatus(long j2, IntByReference intByReference);

    int FT_GetModemStatus(int i2, IntByReference intByReference);

    int FT_GetQueueStatus(long j2, IntByReference intByReference);

    int FT_GetQueueStatus(int i2, IntByReference intByReference);

    int FT_GetDeviceInfo(long j2, IntByReference intByReference, IntByReference intByReference2, Pointer pointer, Pointer pointer2, Pointer pointer3);

    int FT_GetDeviceInfo(int i2, IntByReference intByReference, IntByReference intByReference2, Pointer pointer, Pointer pointer2, Pointer pointer3);

    int FT_GetDriverVersion(long j2, IntByReference intByReference);

    int FT_GetDriverVersion(int i2, IntByReference intByReference);

    int FT_GetLibraryVersion(IntByReference intByReference);

    int FT_GetComPortNumber(long j2, IntByReference intByReference);

    int FT_GetComPortNumber(int i2, IntByReference intByReference);

    int FT_GetStatus(long j2, IntByReference intByReference, IntByReference intByReference2, IntByReference intByReference3);

    int FT_GetStatus(int i2, IntByReference intByReference, IntByReference intByReference2, IntByReference intByReference3);

    int FT_SetEventNotification(long j2, int i2, Pointer pointer);

    int FT_SetEventNotification(int i2, int i3, Pointer pointer);

    int FT_SetChars(long j2, byte b2, byte b3, byte b4, byte b5);

    int FT_SetChars(int i2, byte b2, byte b3, byte b4, byte b5);

    int FT_SetBreakOn(long j2);

    int FT_SetBreakOn(int i2);

    int FT_SetBreakOff(long j2);

    int FT_SetBreakOff(int i2);

    int FT_Purge(long j2, int i2);

    int FT_Purge(int i2, int i3);

    int FT_ResetDevice(long j2);

    int FT_ResetDevice(int i2);

    int FT_ResetPort(long j2);

    int FT_ResetPort(int i2);

    int FT_CyclePort(long j2);

    int FT_CyclePort(int i2);

    int FT_Rescan();

    int FT_Reload(short s2, short s3);

    int FT_SetResetPipeRetryCount(long j2, int i2);

    int FT_SetResetPipeRetryCount(int i2, int i3);

    int FT_StopInTask(long j2);

    int FT_StopInTask(int i2);

    int FT_RestartInTask(long j2);

    int FT_RestartInTask(int i2);

    int FT_SetDeadmanTimeout(long j2, int i2);

    int FT_SetDeadmanTimeout(int i2, int i3);

    int FT_ReadEE(long j2, int i2, ShortByReference shortByReference);

    int FT_ReadEE(int i2, int i3, ShortByReference shortByReference);

    int FT_WriteEE(long j2, int i2, short s2);

    int FT_WriteEE(int i2, int i3, short s2);

    int FT_EraseEE(long j2);

    int FT_EraseEE(int i2);

    int FT_EE_Read(long j2, FT_PROGRAM_DATA.ByReference byReference);

    int FT_EE_Read(int i2, FT_PROGRAM_DATA.ByReference byReference);

    int FT_EE_ReadEx(long j2, FT_PROGRAM_DATA.ByReference byReference, String str, String str2, String str3, String str4);

    int FT_EE_ReadEx(int i2, FT_PROGRAM_DATA.ByReference byReference, String str, String str2, String str3, String str4);

    int FT_EE_Program(long j2, FT_PROGRAM_DATA.ByReference byReference);

    int FT_EE_Program(int i2, FT_PROGRAM_DATA.ByReference byReference);

    int FT_EE_ProgramEx(long j2, FT_PROGRAM_DATA.ByReference byReference, String str, String str2, String str3, String str4);

    int FT_EE_ProgramEx(int i2, FT_PROGRAM_DATA.ByReference byReference, String str, String str2, String str3, String str4);

    int FT_EE_UASize(long j2, IntByReference intByReference);

    int FT_EE_UASize(int i2, IntByReference intByReference);

    int FT_EE_UARead(long j2, Pointer pointer, int i2, IntByReference intByReference);

    int FT_EE_UARead(int i2, Pointer pointer, int i3, IntByReference intByReference);

    int FT_EE_UAWrite(long j2, Pointer pointer, int i2);

    int FT_EE_UAWrite(int i2, Pointer pointer, int i3);

    int FT_SetLatencyTimer(long j2, byte b2);

    int FT_SetLatencyTimer(int i2, byte b2);

    int FT_GetLatencyTimer(long j2, ByteByReference byteByReference);

    int FT_GetLatencyTimer(int i2, ByteByReference byteByReference);

    int FT_SetBitMode(long j2, byte b2, byte b3);

    int FT_SetBitMode(int i2, byte b2, byte b3);

    int FT_GetBitmode(long j2, ByteByReference byteByReference);

    int FT_GetBitmode(int i2, ByteByReference byteByReference);

    int FT_SetUSBParameters(long j2, int i2, int i3);

    int FT_SetUSBParameters(int i2, int i3, int i4);

    /* loaded from: JavaFTD2XX.jar:com/ftdi/FTD2XX$Loader.class */
    public static class Loader {
        private Loader() {
        }

        static String getNative() {
            String res;
            String res2;
            InputStream in = null;
            FileOutputStream fos = null;
            File fileOut = null;
            System.setProperty("jna.library.path", System.getProperty("java.io.tmpdir"));
            if (Platform.isMac()) {
                in = Loader.class.getResourceAsStream("/natives/libftd2xx.dylib");
            } else if (Platform.is64Bit()) {
                if (Platform.isLinux()) {
                    in = Loader.class.getResourceAsStream("/natives/x86_64/libftd2xx.so");
                } else if (Platform.isWindows()) {
                    in = Loader.class.getResourceAsStream("/natives/x86_64/ftd2xx.dll");
                }
            } else if (Platform.isLinux()) {
                in = Loader.class.getResourceAsStream("/natives/i386/libftd2xx.so");
            } else if (Platform.isWindows()) {
                in = Loader.class.getResourceAsStream("/natives/i386/ftd2xx.dll");
            }
            try {
                if (in != null) {
                    try {
                        fileOut = File.createTempFile(Platform.isMac() ? "lib" : "ftd2xx", Platform.isWindows() ? ".dll" : Platform.isLinux() ? ".so" : ".dylib");
                        fileOut.deleteOnExit();
                        fos = new FileOutputStream(fileOut);
                        byte[] buf = new byte[1024];
                        while (true) {
                            int count = in.read(buf, 0, buf.length);
                            if (count <= 0) {
                                break;
                            }
                            fos.write(buf, 0, count);
                        }
                        try {
                            in.close();
                        } catch (IOException e2) {
                        }
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e3) {
                            }
                        }
                        if (Platform.isMac()) {
                            StringTokenizer st = new StringTokenizer(fileOut.getName(), ".");
                            res2 = st.nextToken().substring(3);
                        } else {
                            res2 = fileOut.getName();
                        }
                        return res2;
                    } catch (IOException ex) {
                        throw new Error("Failed to create temporary file for d2xx library: " + ((Object) ex));
                    }
                }
                throw new Error("Not supported OS");
            } catch (Throwable th) {
                try {
                    in.close();
                } catch (IOException e4) {
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e5) {
                    }
                }
                if (Platform.isMac()) {
                    StringTokenizer st2 = new StringTokenizer(fileOut.getName(), ".");
                    res = st2.nextToken().substring(3);
                } else {
                    res = fileOut.getName();
                }
                return res;
            }
        }
    }
}
