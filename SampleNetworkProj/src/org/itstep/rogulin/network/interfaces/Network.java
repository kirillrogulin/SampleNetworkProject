package org.itstep.rogulin.network.interfaces;

import java.util.List;

import org.itstep.rogulin.network.enumerations.NetworkType;

public interface Network {
	void addDevice(Device d);
	void removeDevice(Device d);
	void removeAllDevices();
	List<Device> getNetworkMembers();
	NetworkType getNetworkType();
	String getNetworkName();
	int hashCode();
	boolean equals(Object o);
}
