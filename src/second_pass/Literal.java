package second_pass;

public class Literal {
	private String literal;
	private Integer address;

	public Literal(String literal, Integer address) {
		this.setLiteral(literal);
		this.setAddress(address);
	}

	/**
	 * @return the address
	 */
	public Integer getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Integer address) {
		this.address = address;
	}

	/**
	 * @return the literal
	 */
	public String getLiteral() {
		return literal.replace("'", "");
	}

	/**
	 * @param literal the literal to set
	 */
	public void setLiteral(String literal) {
		this.literal = literal;
	}

	@Override
	public String toString() {
		return String.format("{%s, %d}", literal, address);
	}
}