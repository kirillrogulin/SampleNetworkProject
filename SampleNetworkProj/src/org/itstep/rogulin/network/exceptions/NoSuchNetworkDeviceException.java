package org.itstep.rogulin.network.exceptions;

public class NoSuchNetworkDeviceException extends RuntimeException {
	private static final long serialVersionUID = 8748435728307161040L;

	public NoSuchNetworkDeviceException(String message) {
		super(message);
	}
}
