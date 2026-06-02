package device;

import java.util.ArrayList;
import java.util.Objects;

public class DeviceManager {
    private ArrayList<Device> devices;
    public DeviceManager() {
        devices = new ArrayList<>();
    }
    public void addDevice(Device d){
        if(findDeviceByName(d.getName())!=null){
            System.out.println("Device "+d.getInfo()+" already exists");
        }
        else{devices.add(d);}
    }
    public Device findDeviceByName(String name){
        for(Device d : devices){
            if(Objects.equals(d.getName(), name))
                return new Device(d);
        }
        return null;
    }
    public boolean removeDeviceByName(String name){
        return devices.removeIf(d -> Objects.equals(d.getName(), name));
    }
    public void showAllDevices(){
        for(Device d : devices){
            System.out.println(d.getName() + " : " + d.getIpAddress());
        }
    }

}
