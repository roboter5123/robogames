package com.roboter5123.robogames.service.model;
import org.bukkit.Material;
public class LootTableEntry {

	private Material type;
	private Integer weight;
	private Integer amount;

	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}
