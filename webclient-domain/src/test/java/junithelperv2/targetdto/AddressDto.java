package junithelperv2.targetdto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AddressDto implements Serializable {


	public String getAddressCode() {
		return addressCode;
	}
	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}
	public String getZipNo() {
		return zipNo;
	}
	public void setZipNo(String zipNo) {
		this.zipNo = zipNo;
	}
	public String getPrefectures() {
		return prefectures;
	}
	public void setPrefectures(String prefectures) {
		this.prefectures = prefectures;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getBantigo() {
		return bantigo;
	}
	public void setBantigo(String bantigo) {
		this.bantigo = bantigo;
	}
	private String addressCode;
	private String zipNo;
	private String prefectures;
	private String city;
	private String bantigo;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
