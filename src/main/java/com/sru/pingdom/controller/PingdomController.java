package com.sru.pingdom.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sru.pingdom.components.AllChecks;
import com.sru.pingdom.service.PingdomService;

@RestController
public class PingdomController {

	@Autowired
	private PingdomService pingdomService;

	@RequestMapping(value = "/getChecks", method = RequestMethod.GET)
	public ResponseEntity<AllChecks> getChecks() {
		return pingdomService.requestGETChecks();
	}

	@RequestMapping(value = "/processChecks", method = RequestMethod.POST)
	public void processChecks() throws IOException {
		pingdomService.processPingdomRequest();
	}

}
