package com.ftdi;

import com.ftdi.FTD2XX;
import com.sun.jna.Memory;

/* loaded from: JavaFTD2XX.jar:com/ftdi/EEPROMData.class */
public class EEPROMData {
    final FTD2XX.FT_PROGRAM_DATA.ByReference ft_program_data;

    EEPROMData(FTD2XX.FT_PROGRAM_DATA.ByReference ft_program_data) {
        this.ft_program_data = ft_program_data;
    }

    public EEPROMData() {
        this.ft_program_data = new FTD2XX.FT_PROGRAM_DATA.ByReference();
    }

    public short getVendorId() {
        return this.ft_program_data.VendorId;
    }

    public short getProductId() {
        return this.ft_program_data.ProductId;
    }

    public String getManufacturer() {
        return this.ft_program_data.Manufacturer.getString(0L);
    }

    public String getManufacturerID() {
        return this.ft_program_data.ManufacturerId.getString(0L);
    }

    public String getDescription() {
        return this.ft_program_data.Description.getString(0L);
    }

    public String getSerialNumber() {
        return this.ft_program_data.SerialNumber.getString(0L);
    }

    public short getMaxPower() {
        return this.ft_program_data.MaxPower;
    }

    public boolean isPnP() {
        return this.ft_program_data.PnP != 0;
    }

    public boolean isSelfPowered() {
        return this.ft_program_data.SelfPowered != 0;
    }

    public boolean isRemoteWakeup() {
        return this.ft_program_data.RemoteWakeup != 0;
    }

    public void setVendorId(short ventorId) {
        this.ft_program_data.VendorId = ventorId;
    }

    public void setProductId(short productId) {
        this.ft_program_data.ProductId = productId;
    }

    public void setManufacturer(String manufacturer) {
        Memory memory = new Memory(manufacturer.length() + 1);
        memory.setString(0L, manufacturer);
        this.ft_program_data.Manufacturer = memory;
    }

    public void setManufacturerID(String manufacturerID) {
        Memory memory = new Memory(manufacturerID.length() + 1);
        memory.setString(0L, manufacturerID);
        this.ft_program_data.ManufacturerId = memory;
    }

    public void setDescription(String description) {
        Memory memory = new Memory(description.length() + 1);
        memory.setString(0L, description);
        this.ft_program_data.Description = memory;
    }

    public void setSerialNumber(String serialNumber) {
        Memory memory = new Memory(serialNumber.length() + 1);
        memory.setString(0L, serialNumber);
        this.ft_program_data.SerialNumber = memory;
    }

    public void setMaxPower(short maxPower) {
        this.ft_program_data.MaxPower = maxPower;
    }

    public void setPnP(boolean pnP) {
        this.ft_program_data.PnP = (short) (pnP ? 1 : 0);
    }

    public void setSelfPowered(boolean selfPowered) {
        this.ft_program_data.SelfPowered = (short) (selfPowered ? 1 : 0);
    }

    public void setRemoteWakeup(boolean remoteWakeup) {
        this.ft_program_data.RemoteWakeup = (short) (remoteWakeup ? 1 : 0);
    }

    public String toString() {
        return "FTProgramData{VendorId=" + ((int) getVendorId()) + ", ProductId=" + ((int) getProductId()) + ", Manufacturer=" + getManufacturer() + ", ManufacturerId=" + getManufacturerID() + ", Description=" + getDescription() + ", SerialNumber=" + getSerialNumber() + ", MaxPower=" + ((int) getMaxPower()) + ", PnP=" + isPnP() + ", SelfPowered=" + isSelfPowered() + ", RemoteWakeup=" + isRemoteWakeup() + '}';
    }
}
