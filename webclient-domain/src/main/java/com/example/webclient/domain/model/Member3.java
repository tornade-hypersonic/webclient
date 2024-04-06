package com.example.webclient.domain.model;

import com.example.webclient.domain.enums.Kengen3;
import com.example.webclient.domain.enums.Status3;

public class Member3 {

	private String id;
	private String name;
	private Status3 status;
	private Kengen3 kengen;
	
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
	public Status3 getStatus() {
		return status;
	}
	public void setStatus(Status3 status) {
		this.status = status;
	}
	public Kengen3 getKengen() {
		return kengen;
	}
	public void setKengen(Kengen3 kengen) {
		this.kengen = kengen;
	}
}
