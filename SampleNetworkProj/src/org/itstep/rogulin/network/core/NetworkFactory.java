package org.itstep.rogulin.network.core;

import org.itstep.rogulin.network.enumerations.NetworkType;
import org.itstep.rogulin.network.interfaces.Network;
import org.itstep.rogulin.network.core.NetworkImpl;

public class NetworkFactory {
	public static Network createNetwork(NetworkType type) {
		Network createdNetwork = null;
		switch(type) {
		case LAN:
			createdNetwork = new NetworkImpl(NetworkType.LAN, "LAN network sample", 256);
			break;
		case MAN:
			createdNetwork = new NetworkImpl(NetworkType.MAN, "MAN network sample", 512);
			break;
		case WAN:
			createdNetwork = new NetworkImpl(NetworkType.WAN, "WAN network sample", 1024);
			break;
		default:
			throw new IllegalArgumentException("Unknown NetworkType object. Should update factory method version!");
		}
		return createdNetwork;
	}
}
