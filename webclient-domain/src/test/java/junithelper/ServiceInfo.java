package junithelper;

import java.io.Serializable;

public class ServiceInfo implements Serializable{

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	private String id;
	private String name;
}
