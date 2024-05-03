package junithelper;

import java.io.Serializable;
//import org.joda.time.DateTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ContractDto implements Serializable{

	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public String getBunkatsuNo() {
		return bunkatsuNo;
	}
	public void setBunkatsuNo(String bunkatsuNo) {
		this.bunkatsuNo = bunkatsuNo;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public LocalDateTime getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getUpdateCnt() {
		return updateCnt;
	}
	public void setUpdateCnt(Integer updateCnt) {
		this.updateCnt = updateCnt;
	}
	public int getTestInt() {
		return testInt;
	}
	public void setTestInt(int testInt) {
		this.testInt = testInt;
	}
	public long getTestLong() {
		return testLong;
	}
	public void setTestLong(long testLong) {
		this.testLong = testLong;
	}
	public float getTestFloat() {
		return testFloat;
	}
	public void setTestFloat(float testFloat) {
		this.testFloat = testFloat;
	}
	public List<AddressDto> getAddressList() {
		return addressList;
	}
	public void setAddressList(List<AddressDto> addressList) {
		this.addressList = addressList;
	}
	public Map<String, String> getStatusPulldown() {
		return statusPulldown;
	}
	public void setStatusPulldown(Map<String, String> statusPulldown) {
		this.statusPulldown = statusPulldown;
	}
	public List<String> getStringList() {
		return stringList;
	}
	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}
	public java.util.ArrayList<java.lang.Integer> getIntegerList() {
		return integerList;
	}
	public void setIntegerList(java.util.ArrayList<java.lang.Integer> integerList) {
		this.integerList = integerList;
	}
	public java.util.ArrayList<java.lang.Long> getLongList() {
		return longList;
	}
	public void setLongList(java.util.ArrayList<java.lang.Long> longList) {
		this.longList = longList;
	}
	public java.util.ArrayList<java.lang.Float> getFloatList() {
		return floatList;
	}
	public void setFloatList(java.util.ArrayList<java.lang.Float> floatList) {
		this.floatList = floatList;
	}
	public String[] getArrayString() {
		return arrayString;
	}
	public void setArrayString(String[] arrayString) {
		this.arrayString = arrayString;
	}
	public AddressDto[] getArrayAddress() {
		return arrayAddress;
	}
	public void setArrayAddress(AddressDto[] arrayAddress) {
		this.arrayAddress = arrayAddress;
	}
	public Integer[] getArrayInteger() {
		return arrayInteger;
	}
	public void setArrayInteger(Integer[] arrayInteger) {
		this.arrayInteger = arrayInteger;
	}
	public int[] getArrayInt() {
		return arrayInt;
	}
	public void setArrayInt(int[] arrayInt) {
		this.arrayInt = arrayInt;
	}
	public Long[] getArrayLongClass() {
		return arrayLongClass;
	}
	public void setArrayLongClass(Long[] arrayLongClass) {
		this.arrayLongClass = arrayLongClass;
	}
	public long[] getArrayLong() {
		return arrayLong;
	}
	public void setArrayLong(long[] arrayLong) {
		this.arrayLong = arrayLong;
	}
	public Float[] getArrayFloatClass() {
		return arrayFloatClass;
	}
	public void setArrayFloatClass(Float[] arrayFloatClass) {
		this.arrayFloatClass = arrayFloatClass;
	}
	public float[] getArrayFloat() {
		return arrayFloat;
	}
	public void setArrayFloat(float[] arrayFloat) {
		this.arrayFloat = arrayFloat;
	}
	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}
	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	public String getHaishi() {
		return haishi;
	}
	public void setHaishi(String haishi) {
		this.haishi = haishi;
	}
	public ServiceInfo getServiceInfo2() {
		return serviceInfo2;
	}
	public void setServiceInfo2(ServiceInfo serviceInfo2) {
		this.serviceInfo2 = serviceInfo2;
	}
	public AddressDto getAddress() {
		return address;
	}
	public void setAddress(AddressDto address) {
		this.address = address;
	}
	public ServiceInfo[] getServiceInfoArrayLevel() {
		return serviceInfoArrayLevel;
	}
	public void setServiceInfoArrayLevel(ServiceInfo[] serviceInfoArrayLevel) {
		this.serviceInfoArrayLevel = serviceInfoArrayLevel;
	}
	public List<ServiceInfo> getServiceInfoListLevel() {
		return serviceInfoListLevel;
	}
	public void setServiceInfoListLevel(List<ServiceInfo> serviceInfoListLevel) {
		this.serviceInfoListLevel = serviceInfoListLevel;
	}

	private String telNo;
	private String bunkatsuNo;
	private String contractName;
	private LocalDateTime updateDate;
	private Integer updateCnt;
	private int testInt;
	private long testLong;
	private float testFloat;
	private List<AddressDto> addressList;
	private Map<String, String> statusPulldown;
	private List<String> stringList;
	private java.util.ArrayList<java.lang.Integer> integerList;
	private java.util.ArrayList<java.lang.Long> longList;
	private java.util.ArrayList<java.lang.Float> floatList;
	private String[] arrayString;
	private AddressDto[] arrayAddress;
	private Integer[] arrayInteger;
	private int[] arrayInt;
	private Long[] arrayLongClass;
	private long[] arrayLong;
	private Float[] arrayFloatClass;
	private float[] arrayFloat;
	private ServiceInfo serviceInfo;
	private String haishi;
	private ServiceInfo serviceInfo2;
	private AddressDto address;
	private ServiceInfo[] serviceInfoArrayLevel;
	private List<ServiceInfo> serviceInfoListLevel;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
