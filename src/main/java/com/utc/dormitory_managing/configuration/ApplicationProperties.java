package com.utc.dormitory_managing.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Data;

@Service
@Data
public class ApplicationProperties {
	@Value("${app.expiredTime}")
	private String expiredTime;
	
	//contract waiting la contract tam thoi chua thanh toan
	//contract active la contract da thanh toan
	//contract suspend la contract da het han hop dong
	//contract not_active la contract da ki nhung chua xep phong
	private List<String> STATUS_CONTRACT = Arrays.asList("WAITING", "ACTIVE", "SUSPEND", "NOT_ACTIVE");

	public static enum StatusContractRef {
		WAITING, ACTIVE, SUSPEND, NOT_ACTIVE
	}
	
	private List<String> CLOCK_NAME = Arrays.asList("ELECTRONIC", "WATER");

	public static enum NameClockRef {
		ELECTRONIC, WATER
	}
	
	private List<String> STATUS_BILL = Arrays.asList("WAITING", "COMPLETE", "OVERDUE");

	public static enum StatusBilltRef {
		WAITING, COMPLETE, OVERDUE
	}
}