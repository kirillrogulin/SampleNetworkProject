package org.itstep.rogulin.network.exceptions;

public class NetworkDeviceMaxLimitException extends RuntimeException {
	private static final long serialVersionUID = -140871601904203036L;

	public NetworkDeviceMaxLimitException(String message) {
		super(message);
	}
}
