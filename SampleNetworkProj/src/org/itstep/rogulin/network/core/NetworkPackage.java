package org.itstep.rogulin.network.core;

import java.util.Random;

import org.itstep.rogulin.network.enumerations.NetworkPackageState;
import org.itstep.rogulin.network.enumerations.NetworkPackageType;
import org.itstep.rogulin.network.interfaces.Device;

public final class NetworkPackage {
	private int packageID;
	private int packageRefID; // if packageType == RESPONSE, this contains REQUEST ID
	private Object data;
	private NetworkPackageType packageType;
	private Device deviceFrom;
	private Device deviceTo;
	private NetworkPackageState packageState;
	
	private NetworkPackage(NetworkPackage.Builder b) {
		this.setPackageID(new Random().nextInt(Integer.MAX_VALUE));
		this.setPackageState(NetworkPackageState.CREATED);
		this.data = b.data;
		this.packageType = b.packageType;
		this.deviceFrom = b.deviceFrom;
		this.deviceTo = b.deviceTo;
	}

	public int getPackageID() { return packageID; }
	protected void setPackageID(int packageID) { this.packageID = packageID; }
	
	public int getPackageRefID() { return packageRefID; }
	protected void setPackageRefID(int packageRefID) { this.packageRefID = packageRefID; }
	
	public Object getData() { return data; }
	protected void setData(Object data) { this.data = data; }
	
	public NetworkPackageType getPackageType() { return packageType; }
	protected void setPackageType(NetworkPackageType packageType) { this.packageType = packageType; }
	
	public Device getDeviceFrom() { return deviceFrom; }
	protected void setDeviceFrom(Device deviceFrom) { this.deviceFrom = deviceFrom; }
	
	public Device getDeviceTo() { return deviceTo; }
	protected void setDeviceTo(Device deviceTo) { this.deviceTo = deviceTo; }
	
	public NetworkPackageState getPackageState() { return packageState; }
	public void setPackageState(NetworkPackageState packageState) { this.packageState = packageState; }
	
	@Override
	public String toString() {
		return "\nFrom: " + deviceFrom.toString() + "\n" + 
				"To: " + deviceTo.toString() + "\n" + 
				"Type: " + packageType.toString() + "\n" + 
				"Data: \"" + data.toString() + "\"\n----------";
	}
	
	public static class Builder {
		@SuppressWarnings("unused")
		private int packageRefID = -1;
		private Object data;
		private NetworkPackageType packageType;
		private Device deviceFrom;
		private Device deviceTo;
		
		public NetworkPackage.Builder setData(Object data) { this.data = data; return this; }
		public NetworkPackage.Builder setDeviceFrom(Device deviceFrom) { this.deviceFrom = deviceFrom; return this; }
		public NetworkPackage.Builder setDeviceTo(Device deviceTo) { this.deviceTo = deviceTo; return this; }
		public NetworkPackage.Builder setPackageType(NetworkPackageType packageType) {
			if(packageType == NetworkPackageType.RESPONSE) {
				throw new IllegalArgumentException("Illegal Builder=>setPackageType(..) call! Should use setPackageType(NetworkPackageType packageType, NetworkPackage requestPackage)");
			}
			this.packageType = packageType;
			return this;
		}
		public NetworkPackage.Builder setPackageType(NetworkPackageType packageType, NetworkPackage requestPackage) {
			if(packageType != NetworkPackageType.RESPONSE) {
				throw new IllegalArgumentException("Illegal Builder=>setPackageType(..) call! Should use setPackageType(NetworkPackageType packageType)");
			}
			this.packageType = packageType;
			this.packageRefID = requestPackage.getPackageID();
			return this;
		}
		
		private void checkNulls() {
			if(this.packageType == null) throw new NullPointerException("NetworkPackage.Builder=>packageType is NULL!");
			if(this.deviceFrom == null) throw new NullPointerException("NetworkPackage.Builder=>deviceFrom is NULL!");
			if(this.deviceTo == null) throw new NullPointerException("NetworkPackage.Builder=>deviceTo is NULL!");
		}
		public NetworkPackage build() {
			checkNulls();
			return new NetworkPackage(this);
		}
	}
	
}
