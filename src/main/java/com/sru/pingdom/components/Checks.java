package com.sru.pingdom.components;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @JsonIgnoreProperties(ignoreUnknown = true)
public class Checks {

	private String id;

	@CsvBindByName
	private String name;

	@CsvBindByName
	private String hostname;

	@CsvBindByName
	private String type;

	@Override
	public boolean equals(Object checks) {
		if (StringUtils.equalsIgnoreCase(this.getName(), ((Checks) checks).getName())) {
			return true;
		}

		return false;
	}

}
