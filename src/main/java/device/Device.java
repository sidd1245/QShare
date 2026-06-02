package device;

public class Device {

    private String name;
    private String ipAddress;

    //Constructors
    public Device(String n, String ip) {
        setName(n);
        setIpAddress(ip);
    }
    public Device(Device d) {
        setName(d.getName());
        setIpAddress(d.getIpAddress());
    }

    //Getter, Setter and Display
    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setName(String name) {
        if(name == null || name.isBlank())
            System.out.println("enter valid name");
        else
            this.name = name;
    }

    public void setIpAddress(String ip) {
        if(ip == null || ip.isBlank())
            System.out.println("enter valid ip");
        else
            this.ipAddress = ip;
    }
    public String getInfo() {
        return (name + " : " + ipAddress);
    }

}
