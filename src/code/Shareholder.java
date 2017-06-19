package code;

public class Shareholder {
	@Override
	public String toString() {
		return "Shareholder [date=" + date + ", company_name=" + company_name + ", hold_num=" + hold_num
				+ ", hold_change=" + hold_change + ", hold_percentage=" + hold_percentage + ", hold_change_percentage="
				+ hold_change_percentage + ", hold_type=" + hold_type + ", stock_name=" + stock_name + ", stock_id="
				+ stock_id + "]";
	}
	private String date;
	private String company_name;
	private String hold_num;
	private String hold_change;
	private String hold_percentage;
	private String hold_change_percentage;
	private String hold_type;
	private String stock_name;
	private String stock_id;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getHold_num() {
		return hold_num;
	}
	public void setHold_num(String hold_num) {
		this.hold_num = hold_num;
	}
	public String getHold_change() {
		return hold_change;
	}
	public void setHold_change(String hold_change) {
		this.hold_change = hold_change;
	}
	public String getHold_percentage() {
		return hold_percentage;
	}
	public void setHold_percentage(String hold_percentage) {
		this.hold_percentage = hold_percentage;
	}
	public String getHold_change_percentage() {
		return hold_change_percentage;
	}
	public void setHold_change_percentage(String hold_change_percentage) {
		this.hold_change_percentage = hold_change_percentage;
	}
	public String getHold_type() {
		return hold_type;
	}
	public void setHold_type(String hold_type) {
		this.hold_type = hold_type;
	}
	public String getStock_name() {
		return stock_name;
	}
	public void setStock_name(String stock_name) {
		this.stock_name = stock_name;
	}
	public String getStock_id() {
		return stock_id;
	}
	public void setStock_id(String stock_id) {
		this.stock_id = stock_id;
	}
}
