package com.sru.pingdom.service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sru.pingdom.components.Checks;

@Service
public class GitRepoHandler {

	@Value("${pingdom.datapath}")
	private String dataPath;

	@Value("${pingdom.filename}")
	private String fileName;

	@Value("${pingdom.gitUri}")
	private String gitUri;

	public List<Checks> getChecksFromCsv() throws IOException {
		cloneRepo();
		try (Reader reader = Files.newBufferedReader(Paths.get(dataPath + fileName));) {
			CsvToBean<Checks> csvToBean = new CsvToBeanBuilder<Checks>(reader).withType(Checks.class)
					.withIgnoreLeadingWhiteSpace(true).build();

			List<Checks> csvChecks = csvToBean.parse();
			System.out.println(csvChecks);

			return csvChecks;
		}
	}

	private void cloneRepo() {
		FileRepository localRepo = null;
		try {
			localRepo = new FileRepository(dataPath + "/.git");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			if (Files.notExists(Paths.get(dataPath))) {
				System.out.println("Cloning " + gitUri + " into " + dataPath);
				Git.cloneRepository().setURI(gitUri).setDirectory(Paths.get(dataPath).toFile()).call();
				System.out.println("Completed Cloning");
			} else {
				Git git = new Git(localRepo);
				git.pull().call();
				System.out.println("Pulled Changes instead");
			}

		} catch (GitAPIException e) {
			System.out.println("Exception occurred while cloning repo");
			e.printStackTrace();
		}
	}
}