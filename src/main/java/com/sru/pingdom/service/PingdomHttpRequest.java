package com.sru.pingdom.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sru.pingdom.components.AllChecks;

@Service
public class PingdomHttpRequest {
	@Value("${pingdom.appKey}")
	private String applicationKey;

	@Value("${pingdom.username}")
	private String username;

	@Value("${pingdom.password}")
	private String password;

	@Value("${pingdom.baseUrl}")
	private String baseUrl;

	private HttpHeaders createHttpHeaders(String user, String password) {
		String plainClientCredentials = user + ":" + password;
		String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64ClientCredentials);
		headers.add("app-key", applicationKey);
		return headers;
	}

	public ResponseEntity<AllChecks> createHttpRequest(String requestUrl, String httpMethod) {

		String finalUrl = baseUrl + "/api/2.1/checks" + requestUrl;
		RestTemplate restTemplate = new RestTemplate();

		System.out.println(finalUrl);

		HttpHeaders httpHeaders = createHttpHeaders(username, password);
		System.out.println(httpHeaders);

		HttpEntity<String> request = new HttpEntity<String>(httpHeaders);

		ResponseEntity<AllChecks> response = restTemplate.exchange(finalUrl, HttpMethod.valueOf(httpMethod), request,
				AllChecks.class);

		System.out.println(response);
		return response;

	}
}
