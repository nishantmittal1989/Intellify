package com.amex.intellify.model;

public class SwipeInfo {

	private String cardType;
	private int cardYear;
	private int emv;
	private int customerId;
	private String month;
	private String merchantId;
	private String paymentValue;
	
	public String getPaymentValue() {
		return paymentValue;
	}
	public void setPaymentValue(String paymentValue) {
		this.paymentValue = paymentValue;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public int getCardYear() {
		return cardYear;
	}
	public void setCardYear(int cardYear) {
		this.cardYear = cardYear;
	}
	public int getEmv() {
		return emv;
	}
	public void setEmv(int emv) {
		this.emv = emv;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	
}
