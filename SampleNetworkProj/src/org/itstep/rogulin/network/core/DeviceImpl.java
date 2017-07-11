package org.itstep.rogulin.network.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.itstep.rogulin.network.enumerations.DeviceType;
import org.itstep.rogulin.network.exceptions.LocalDeviceConnectionException;
import org.itstep.rogulin.network.interfaces.Connection;
import org.itstep.rogulin.network.interfaces.Device;
import org.itstep.rogulin.network.interfaces.Network;

public class DeviceImpl implements Device {
	private DeviceType deviceType;
	private String ipAddress;
	private boolean[] localPortsAvailable;
	private List<Connection> connections;
	private Network registeredNetwork;
	private final String IP_V4_TEST_STRING = "(\\d{1,3}.{1}){3}\\d{1,3}";
	
	{
		localPortsAvailable = new boolean[65536];
		Arrays.fill(localPortsAvailable, true);
		connections = new ArrayList<Connection>();
	}
	
	public DeviceImpl(DeviceType type) {
		this(type, null);
	}
	
	public DeviceImpl(DeviceType type, String ip) {
		this.deviceType = type;
		this.ipAddress = ip;
	}
	
	@Override
	public void joinNetwork(Network network) {
		this.joinNetwork(network, ipAddress);
	}
	
	@Override
	public void joinNetwork(Network network, String ip) {
		if (ip == null) {
			throw new NullPointerException("You cannot join network without IP-address (ipAddress = null)!");
		}
		if (!ip.matches(IP_V4_TEST_STRING)) {
			throw new IllegalArgumentException("Both IPs or One of ip is not a LEGAL IPv4 address!");
		}
		this.ipAddress = ip;
		this.registeredNetwork = network;
		network.addDevice(this);
	}
	
	public String getFreePort() {
		Random r = new Random();
		int port = 0;
		do {
			port = r.nextInt(65536);
		} while (!localPortsAvailable[port]);
		localPortsAvailable[port] = false;
		return String.valueOf(port);
	}
	
	@Override
	public String getIPAddress() {
		return ipAddress;
	}
	
	@Override
	public Connection connectTo(Device otherDevice) {
		Connection connection = new ConnectionImpl.Builder()
			.setDevices(this, otherDevice)
			.setPorts(this.getFreePort(), otherDevice.getFreePort())
			.build();
		connections.add(connection);
		otherDevice.acceptConnectionFrom(connection);
		return connection;
	}
	
	@Override
	public Connection acceptConnectionFrom(Connection otherDeviceConnection) {
		try {
			Connection connection = new ConnectionImpl.Builder()
				.setDevices(this, otherDeviceConnection.getLocalDevice())
				.setPorts(otherDeviceConnection.getRemotePort(), otherDeviceConnection.getLocalPort())
				.build();
			connections.add(connection);
			return connection;
		}
		catch (Exception ex) {
			// logger.error(ex.getMessage());
			return null;
		}
	}

	@Override
	public void disconnect(Connection con) {
		if(con == null) return;
		// passed Connection is locally stored connection (within current instance)
		if(this.equals(con.getLocalDevice())) {
			con.getRemoteDevice().disconnect(con);
			localPortsAvailable[Integer.parseInt(con.getLocalPort())] = true;
			con.killConnection();
			this.connections.remove(con);
		}
		// passed Connection is remoteDevice connection (within remote instance)
		else if (this.equals(con.getRemoteDevice())) {
			Connection remoteDeviceConnection = null;
			for(Connection c : this.connections) {
				if (c.getLocalDevice().equals(con.getRemoteDevice()) &&
						c.getLocalPort().equals(con.getRemotePort()) &&
						c.getRemoteDevice().equals(con.getLocalDevice()) &&
						c.getRemotePort().equals(con.getLocalPort())) {
					remoteDeviceConnection = c;
				}
			}
			localPortsAvailable[Integer.parseInt(remoteDeviceConnection.getLocalPort())] = true;
			remoteDeviceConnection.killConnection();
			this.connections.remove(remoteDeviceConnection);
		} else {
			throw new LocalDeviceConnectionException("There is no such connection within this device! (this.connections.contains(con) = false)");
		}
	}
	
	@Override
	public void disconnect(Device remoteDev) {
		for(Connection c : this.connections) {
			if (c.getRemoteDevice().equals(remoteDev)) {
				this.disconnect(c);
			}
		}
	}
	
	@Override
	public void disconnectAll() {
		for(Connection c : this.connections) {
			this.disconnect(c);
		}
	}
	
	@Override
	public void freePort(String port) {
		Connection conWithOccupiedPort = null;
		for(Connection c : this.connections) {
			if(c.getLocalPort().equals(port)) {
				this.disconnect(conWithOccupiedPort);
			}
		}
		this.localPortsAvailable[Integer.parseInt(port)] = true;
	}
	
	@Override
	public NetworkPackage request(Connection con) {
		return con.popData(this);
	}

	@Override
	public void response(Connection con, NetworkPackage anp) {
		
		con.pushData(anp);
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceType == null) ? 0 : deviceType.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((registeredNetwork == null) ? 0 : registeredNetwork.hashCode());
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
		DeviceImpl other = (DeviceImpl) obj;
		if (deviceType != other.deviceType)
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (registeredNetwork == null) {
			if (other.registeredNetwork != null)
				return false;
		} else if (!registeredNetwork.equals(other.registeredNetwork))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Device: ")
		  .append(ipAddress)
		  .append(" - network \"")
		  .append(registeredNetwork == null ? "not connected" : registeredNetwork.getNetworkName())
		  .append("\" [")
		  .append(registeredNetwork == null ? "   " : registeredNetwork.getNetworkType().toString())
		  .append("]");
		return sb.toString();
	}
}
