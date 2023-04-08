package com.chokshi.deep.pos_system;

public class Machine {
    int machineId;
    String machineName;
    String machineIP;

    public Machine(int machineId, String machineName, String machineIP) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.machineIP = machineIP;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineIP() {
        return machineIP;
    }

    public void setMachineIP(String machineIP) {
        this.machineIP = machineIP;
    }
}
