package com.worstentrepreneur.braintreetest.dto;

import javax.xml.bind.annotation.XmlElement;
public class AddCreditcardDTO extends AbsDTO{
	@XmlElement(name = "payment_method_nonce")
	public String nonce;
}
