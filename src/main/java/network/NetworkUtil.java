package network;

import java.net.*;
import java.util.*;

public class NetworkUtil {

    public static String getLocalIp() throws SocketException {
        Enumeration<NetworkInterface> interfaces =
                NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();

            if (!ni.isUp() || ni.isLoopback())
                continue;

            if (ni.getDisplayName().contains("VMware"))
                continue;

            Enumeration<InetAddress> addresses =
                    ni.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();

                if (addr instanceof Inet4Address) {
                    return addr.getHostAddress();
                }
            }
        }

        return null;
    }

    public static void main(String[] args) throws SocketException {
        System.out.println(NetworkUtil.getLocalIp());
    }
}