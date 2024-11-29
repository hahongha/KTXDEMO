package com.utc.dormitory_managing.utils.exception;

public class ResponseEntityErrorException extends RuntimeException {
	private static final long serialVersionUID = -3156815846745801694L;

	private transient String reason;

	public ResponseEntityErrorException(String reason) {
		this.reason = reason;
	}

	public String getApiResponse() {
		return reason;
	}
}
