package com.utc.dormitory_managing.dto;

import lombok.Data;

@Data
public class MailDTO {
	private String from = "phamha03122003@gmail.com";
	private String to;
	private String toName;
	private String subject;
	private String content;
}
