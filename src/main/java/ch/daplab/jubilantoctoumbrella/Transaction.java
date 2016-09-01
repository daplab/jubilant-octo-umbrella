package ch.daplab.jubilantoctoumbrella;

public class Transaction {

	private boolean whiteListed;
	private String merchantId;
	private String merchantCountry;

	private String payload;
	private String seq;
	private long timestamp;
	private String currency;
	private double amount;

	private byte ccv;
	private String cardNumber;
	private String expiration;
	private String name;
	private String clientCountry;

	public Transaction(boolean whiteListed, String merchantId, String merchantCountry, String payload, String seq,
			long timestamp, String currency, double amount, byte ccv, String cardNumber, String expiration, String name,
			String clientCountry) {
		super();
		this.whiteListed = whiteListed;
		this.merchantId = merchantId;
		this.merchantCountry = merchantCountry;
		this.payload = payload;
		this.seq = seq;
		this.timestamp = timestamp;
		this.currency = currency;
		this.amount = amount;
		this.ccv = ccv;
		this.cardNumber = cardNumber;
		this.expiration = expiration;
		this.name = name;
		this.clientCountry = clientCountry;
	}

	public boolean isWhiteListed() {
		return whiteListed;
	}

	public void setWhiteListed(boolean whiteListed) {
		this.whiteListed = whiteListed;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantCountry() {
		return merchantCountry;
	}

	public void setMerchantCountry(String merchantCountry) {
		this.merchantCountry = merchantCountry;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public byte getCcv() {
		return ccv;
	}

	public void setCcv(byte ccv) {
		this.ccv = ccv;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientCountry() {
		return clientCountry;
	}

	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
	}

}
