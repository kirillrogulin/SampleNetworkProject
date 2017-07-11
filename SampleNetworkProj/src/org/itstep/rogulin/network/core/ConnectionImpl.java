package org.itstep.rogulin.network.core;

import java.util.Deque;
import java.util.LinkedList;

import org.itstep.rogulin.network.enumerations.NetworkPackageState;
import org.itstep.rogulin.network.interfaces.Connection;
import org.itstep.rogulin.network.interfaces.Device;

public class ConnectionImpl implements Connection {
	private Deque<NetworkPackage> buffer;
	private Device localDevice;
	private String localPort;
	private Device remoteDevice;
	private String remotePort;
	
	private ConnectionImpl(ConnectionImpl.Builder b) {
		buffer = new LinkedList<NetworkPackage>();
		this.localDevice = b.localDevice;
		this.localPort = b.localPort;
		this.remoteDevice = b.remoteDevice;
		this.remotePort = b.remotePort;
	}
	
	public static class Builder {
		private Device localDevice;
		private String localPort;
		private Device remoteDevice;
		private String remotePort;
		private final String TCP_PORT_TEST_STRING = "\\d{1,5}";
		
		public ConnectionImpl.Builder setDevices(Device localDevice, Device remoteDevice) {
			if (localDevice == null || remoteDevice == null) {
				throw new NullPointerException("Both Devices or One of Device is NULL! All Devices should be set!");
			}
			this.localDevice = localDevice;
			this.remoteDevice = remoteDevice;
			return this;
		}
		
		public ConnectionImpl.Builder setPorts(String localPort, String remotePort) {
			if (localPort == null || remotePort == null) {
				throw new NullPointerException("Both TCP ports or One of TCP port is NULL! All TCP ports should be set!");
			}
			if (!localPort.matches(TCP_PORT_TEST_STRING) || !remotePort.matches(TCP_PORT_TEST_STRING)) {
				throw new IllegalArgumentException("Both TCP ports or One of TCP port is not a LEGAL TCP Port (should be decimal string)!");
			} else {
				int tempPort = Integer.valueOf(localPort).intValue();
				if(tempPort < 0 || tempPort > 65535) {
					throw new IllegalArgumentException("\"portOne\" is an invalid TCP port! Should be in 0-65535 range!");
				}
				tempPort = Integer.valueOf(remotePort).intValue();
				if(tempPort < 0 || tempPort > 65535) {
					throw new IllegalArgumentException("\"portTwo\" is an invalid TCP port! Should be in 0-65535 range!");
				}
			}
			this.localPort = localPort;
			this.remotePort = remotePort;
			return this;
		}
		
		public ConnectionImpl build() {
			return new ConnectionImpl(this);
		}
	}

	@Override
	public void pushData(NetworkPackage nPackage) {
		buffer.add(nPackage);
		nPackage.setPackageState(NetworkPackageState.SENT);
	}

	@Override
	public NetworkPackage popData(Device d) {
		for(NetworkPackage p : buffer) {
			if(p.getDeviceTo() == d) {
				buffer.peek().setPackageState(NetworkPackageState.DELIVERED);
				return buffer.peek();
			}
		}
		return null;
	}

	@Override
	public String getLocalPort() { return localPort; }

	@Override
	public String getRemotePort() { return remotePort; }

	@Override
	public Device getLocalDevice() { return localDevice; }

	@Override
	public Device getRemoteDevice() { return remoteDevice; }

	@Override
	public void killConnection() {
		this.buffer.clear();
		this.buffer = null;
		this.localDevice = null;
		this.remoteDevice = null;
	}
}
