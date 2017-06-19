package code;

public class Node {
	private String id;
	private Integer cluster;

	public Integer getCluster() {
		return cluster;
	}

	public void setCluster(Integer cluster) {
		this.cluster = cluster;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", cluster=" + cluster + "]";
	}

	
	
}
