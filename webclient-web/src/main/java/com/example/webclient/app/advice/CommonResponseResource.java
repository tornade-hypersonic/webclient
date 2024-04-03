package com.example.webclient.app.advice;

public class CommonResponseResource {
    private String message;
	private Object bizResponse;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBizResponse() {
		return bizResponse;
	}

	public void setBizResponse(Object bizResponse) {
		this.bizResponse = bizResponse;
	}

}
