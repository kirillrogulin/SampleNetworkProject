package org.itstep.rogulin;

import java.util.Arrays;

import org.itstep.rogulin.network.core.*;
import org.itstep.rogulin.network.enumerations.*;
import org.itstep.rogulin.network.interfaces.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Program {
	public static Logger lg;
	static {
		lg = LogManager.getLogger(); 
	}

	public static void main(String[] args) {
		
		Network net1 = NetworkFactory.createNetwork(NetworkType.LAN);
			lg.debug("CREATED: " + net1.toString());
		Device dev1 = new DeviceImpl(DeviceType.NOTEBOOK);
			lg.debug("CREATED: " + dev1.toString());
		Device dev2 = new DeviceImpl(DeviceType.WORKSTATION);
			lg.debug("CREATED: " + dev2.toString());
		dev1.joinNetwork(net1, "192.168.1.11");
			lg.debug("JOINED: " + dev1.toString());
		dev2.joinNetwork(net1, "192.168.1.177");
			lg.debug("JOINED: " + dev2.toString());
			lg.debug("NET MEMBERS:\n" + Arrays.deepToString(net1.getNetworkMembers().toArray()));
		Connection conDev1Dev2 = dev1.connectTo(dev2);
		Connection conDev2Dev1 = dev2.acceptConnectionFrom(conDev1Dev2);
		NetworkPackage np1 = new NetworkPackage.Builder().setData("Package 1 test")
				.setDeviceFrom(dev1)
				.setDeviceTo(dev2)
				.setPackageType(NetworkPackageType.REQUEST).build();
			lg.debug("\nPACKAGE CREATED: " + np1.toString() + "\n");
		dev1.response(conDev2Dev1, np1);
		NetworkPackage np1_recieved = dev2.request(conDev2Dev1);
			lg.debug("\nPACKAGE RECIEVED: " + np1_recieved.toString() + "\n");
	}
}
