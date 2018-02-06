package com.sru.pingdom.components;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AllChecks {

	private List<Checks> checks;

}
