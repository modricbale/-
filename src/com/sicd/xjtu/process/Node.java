package com.sicd.xjtu.process;

import java.util.Map;

public class Node<K,V> {
	private String color;
	private String label;
	private Map<K, V> attributes;
	private double x; 
	private double y;
	private String id;
	private double size;
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Map<K, V> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<K, V> attributes) {
		this.attributes = attributes;
	}
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "Node [color=" + color + ", label=" + label + ", attributes=" + attributes + ", x=" + x + ", y=" + y
				+ ", id=" + id + ", size=" + size + "]";
	}
	
}
