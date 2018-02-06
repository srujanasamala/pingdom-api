package com.sru.pingdom.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sru.pingdom.components.AllChecks;
import com.sru.pingdom.components.Checks;

@Service

public class PingdomService {

	@Autowired
	private PingdomHttpRequest httpRequest;

	@Autowired
	private GitRepoHandler gitRepo;

	public ResponseEntity<AllChecks> requestGETChecks() {
		return httpRequest.createHttpRequest("", "GET");
	}

	public void processPingdomRequest() throws IOException {

		ResponseEntity<AllChecks> response;

		List<Checks> existingChecks = requestGETChecks().getBody().getChecks();
		System.out.println("Existing Checks :" + existingChecks.size());

		List<Checks> checksFromCsv = gitRepo.getChecksFromCsv();
		System.out.println("Checks from Csv :" + checksFromCsv.size());

		for (Checks check : checksFromCsv) {
			boolean found = false;
			for (Checks compareCheck : existingChecks) {
				/*
				 * Only HostName can be updated . Pingdom API does not support change of Type
				 */
				if (StringUtils.equalsIgnoreCase(check.getName(), compareCheck.getName())) {
					found = true;
					if (!StringUtils.equalsIgnoreCase(check.getHostname(), compareCheck.getHostname())) {
						check.setId(compareCheck.getId());
					}
					break;
				}
			}
			if (found && (check.getId() == null || check.getId().isEmpty())) {
				System.out.println("Found a matching entry but no changes found - " + check.getName());

			} else if (found) {
				System.out.println("update now");
				String requestUrl = String.format("/%s?name=%s&host=%s", check.getId(), check.getName(),
						check.getHostname());
				response = httpRequest.createHttpRequest(requestUrl, "PUT");
				System.out.println(response);

			} else {
				System.out.println("Create check");
				String requestUrl = String.format("?name=%s&type=%s&host=%s", check.getName(), check.getType(),
						check.getHostname());
				response = httpRequest.createHttpRequest(requestUrl, "POST");
				System.out.println(response);
			}

		}
		existingChecks.removeAll(checksFromCsv);
		System.out.println(String.format("size of checks found - %s and to be deleted - %s", checksFromCsv.size(),
				existingChecks.size()));
		for (Checks remainingChecks : existingChecks) {
			System.out.println("Delete now");
			String requestUrl = String.format("/%s", remainingChecks.getId());
			response = httpRequest.createHttpRequest(requestUrl, "DELETE");
			System.out.println(response);
		}

	}

}
