package com.worstentrepreneur.test.dto;

import javax.xml.bind.annotation.XmlElement;

public class CreateCustomerDTO extends AbsDTO {
	@XmlElement(name = "payment_method_nonce")
	public String nonce;
	public String phone;
	public String name;
	public String email;
}
