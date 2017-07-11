package org.itstep.rogulin.network.core;

import java.util.ArrayList;
import java.util.List;

import org.itstep.rogulin.network.enumerations.NetworkType;
import org.itstep.rogulin.network.exceptions.*;
import org.itstep.rogulin.network.interfaces.Device;
import org.itstep.rogulin.network.interfaces.Network;

public class NetworkImpl implements Network {
	private NetworkType networkType;
	private String networkName;
	private List<Device> networkMembers;
	private final int MAX_DEVICE_COUNT;
	
	// constructors
	public NetworkImpl() {
		this(NetworkType.LAN, null, 256);
	}
	
	public NetworkImpl(NetworkType networkType) {
		this(networkType, null, 256);
	}

	public NetworkImpl(NetworkType networkType, String nwName, int MAX_DEV_COUNT) {
		this.networkMembers = new ArrayList<Device>();
		this.networkType = networkType;
		this.networkName = nwName;
		this.MAX_DEVICE_COUNT = MAX_DEV_COUNT;
	}
	
	// business-logic methods
	@Override
	public void addDevice(Device d) {
		if(!this.hasAvailableIPs()) {
			throw new NetworkDeviceMaxLimitException("Max device count exceeded!");
		}
		if(this.hasSameIP(d)) {
			throw new DeviceIPAddressShadowingException("Device IP-Address already exists within the network! Should change it!");
		}
		this.networkMembers.add(d);
	}

	@Override
	public void removeDevice(Device d) {
		if(!networkMembers.remove(d)) {
			throw new NoSuchNetworkDeviceException("Device \"" + d + "\" is not connected to this network!");
		}
	}

	@Override
	public void removeAllDevices() {
		networkMembers.clear();
	}
	
	// getters & setters
	public List<Device> getNetworkMembers() { return networkMembers; }
	public NetworkType getNetworkType() { return networkType; }
	public String getNetworkName() { return networkName; }
	
	
	// support private methods
	private boolean hasAvailableIPs() {
		return this.networkMembers.size() < this.MAX_DEVICE_COUNT;
	}
	
	private boolean hasSameIP(Device d) {
		for (Device dev : networkMembers) {
			if(dev.getIPAddress().equals(d.getIPAddress()))
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + MAX_DEVICE_COUNT;
		result = prime * result + ((networkName == null) ? 0 : networkName.hashCode());
		result = prime * result + ((networkType == null) ? 0 : networkType.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NetworkImpl other = (NetworkImpl) obj;
		if (MAX_DEVICE_COUNT != other.MAX_DEVICE_COUNT)
			return false;
		if (networkName == null) {
			if (other.networkName != null)
				return false;
		} else if (!networkName.equals(other.networkName))
			return false;
		if (networkType != other.networkType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.networkName + " [" + this.networkType.toString() + 
				"], registered devices: " + this.networkMembers.size();
	}
	
}
