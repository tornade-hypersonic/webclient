package com.example.webclient.domain.model;

import com.example.webclient.domain.enums.Kengen;
import com.example.webclient.domain.enums.Status;

public class Member {

	private String id;
	private String name;
	private Status status;
	private Kengen kengen;
	
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
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Kengen getKengen() {
		return kengen;
	}
	public void setKengen(Kengen kengen) {
		this.kengen = kengen;
	}
}
