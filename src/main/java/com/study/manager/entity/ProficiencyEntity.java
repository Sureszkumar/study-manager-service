package com.study.manager.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PROFICIENCY")
public class ProficiencyEntity extends BaseEntity {

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}