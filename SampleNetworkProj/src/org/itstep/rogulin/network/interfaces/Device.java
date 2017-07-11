package org.itstep.rogulin.network.interfaces;

import org.itstep.rogulin.network.core.NetworkPackage;

public interface Device {
	String getIPAddress();
	void joinNetwork(Network network);
	void joinNetwork(Network network, String ip);
	String getFreePort();
	void freePort(String port);
	Connection connectTo(Device otherDevice);
	Connection acceptConnectionFrom(Connection otherDeviceConnection);
	void disconnect(Connection con);
	void disconnect(Device dev);
	void disconnectAll();
	NetworkPackage request(Connection con);
	void response(Connection con, NetworkPackage anp);
	public int hashCode();
	public boolean equals(Object obj);
}
