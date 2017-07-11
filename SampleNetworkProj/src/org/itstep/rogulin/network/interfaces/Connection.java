package org.itstep.rogulin.network.interfaces;

import org.itstep.rogulin.network.core.NetworkPackage;

public interface Connection {
	String getLocalPort();
	String getRemotePort();
	Device getLocalDevice();
	Device getRemoteDevice();
	void pushData(NetworkPackage nPackage);
	NetworkPackage popData(Device d);
	void killConnection();
}
