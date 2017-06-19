package com.sicd.xjtu.process;

import java.util.Map;

public class Link<K, V> {

	private String sourceID;
	private String targetID;
	private Map<K, V> attributes;
	private int size;
	public String getSourceID() {
		return sourceID;
	}
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}
	public String getTargetID() {
		return targetID;
	}
	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}
	public Map<K, V> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<K, V> attributes) {
		this.attributes = attributes;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "Link [sourceID=" + sourceID + ", targetID=" + targetID + ", attributes=" + attributes + ", size=" + size
				+ "]";
	}
	
	
}
