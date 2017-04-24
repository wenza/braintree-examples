package com.worstentrepreneur.test.dto;

import java.util.Calendar;

public class CreditcardDTO {
	public boolean isExpired;
	public Calendar createdAt;
	public String bin;
	public String last4;
	public String cardholderName;
	public String cardType;
	public String expirationMonth;
	public String expirationYear;
	public String issuingBank;
	public String token;
	public boolean isDefault;
	public String maskedNumber = bin + "******" + last4;
	public String expirationDate = this.expirationMonth + "/" + this.expirationYear;

	public CreditcardDTO(boolean isExpired, Calendar createdAt, String bin, String last4, String cardholderName, String cardType,
						 String expirationMonth, String expirationYear, String issuingBank, String token, boolean isDefault) {
		this.isExpired = isExpired;
		this.createdAt = createdAt;
		this.bin = bin;
		this.last4 = last4;
		this.cardholderName = cardholderName;
		this.cardType = cardType;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
		this.issuingBank = issuingBank;
		this.token = token;
		this.isDefault = isDefault;

		this.maskedNumber = bin + "******" + last4;
		this.expirationDate = expirationMonth + "/" + expirationYear;
	}
}
