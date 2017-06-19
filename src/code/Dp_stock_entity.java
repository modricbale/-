package code;

public class Dp_stock_entity {
	private String name;
	private int cluster;
	private int h = 0;
	private double p;
	private double cta;
	public int getCluster() {
		return cluster;
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	public double getCta() {
		return cta;
	}
	public void setCta(double cta) {
		this.cta = cta;
	}
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	@Override
	public String toString() {
		return "Dp_stock_entity [name=" + name + ", cluster=" + cluster + ", h=" + h + ", p=" + p + ", cta=" + cta
				+ "]";
	}
	
}
