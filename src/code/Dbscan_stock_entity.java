package code;

public class Dbscan_stock_entity {
		private String name;
	    private boolean isVisit;
	    private int cluster;
	    private boolean isNoised;

	    public Dbscan_stock_entity(String name) {
	        this.name = name;
	    	this.isVisit = false;
	        this.cluster = 0;
	        this.isNoised = false;
	    }


	    @Override
		public String toString() {
			return "Dbscan_stock_entity [name=" + name + ", isVisit=" + isVisit + ", cluster=" + cluster + ", isNoised="
					+ isNoised + "]";
		}


		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public void setVisit(boolean isVisit) {
	        this.isVisit = isVisit;
	    }

	    public boolean getVisit() {
	        return isVisit;
	    }

	    public int getCluster() {
	        return cluster;
	    }

	    public void setNoised(boolean isNoised) {
	        this.isNoised = isNoised;
	    }

	    public void setCluster(int cluster) {
	        this.cluster = cluster;
	    }

	    public boolean getNoised() {
	        return this.isNoised;
	    }



	}

