package org.itstep.rogulin.network.core;

import org.itstep.rogulin.network.enumerations.DeviceType;
import org.itstep.rogulin.network.interfaces.Device;

public class DeviceFactory {
	public static Device createDevice(DeviceType type, String ip) {
		Device createdDevice = null;
		switch(type) {
		case SMARTPHONE:
			createdDevice = new DeviceImpl(DeviceType.SMARTPHONE, ip);
			break;
		case TABLET:
			createdDevice = new DeviceImpl(DeviceType.TABLET, ip);
			break;
		case NOTEBOOK:
			createdDevice = new DeviceImpl(DeviceType.NOTEBOOK, ip);
			break;
		case WORKSTATION:
			createdDevice = new DeviceImpl(DeviceType.WORKSTATION, ip);
			break;
		case SERVER:
			createdDevice = new DeviceImpl(DeviceType.SERVER, ip);
			break;
		default:
			throw new IllegalArgumentException("Unknown NetworkType object. Should update factory method version!");
		}
		return createdDevice;
	}
}
