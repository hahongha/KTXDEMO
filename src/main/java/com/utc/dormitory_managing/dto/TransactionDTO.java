package com.utc.dormitory_managing.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TransactionDTO {
    private String name;
    private String description;
    private String orderInfo;
    private Date date;
    private Long amount;
}

