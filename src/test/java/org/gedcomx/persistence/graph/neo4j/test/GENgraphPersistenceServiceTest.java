package org.gedcomx.persistence.graph.neo4j.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gedcomx.fileformat.GedcomxFile;
import org.gedcomx.fileformat.GedcomxFileEntry;
import org.gedcomx.persistence.graph.neo4j.ServiceInjector;
import org.gedcomx.persistence.graph.neo4j.service.GENgraphPersistenceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;

public class GENgraphPersistenceServiceTest {

	private static class GedcomxData {
		Map<String, String> attributes;
	}

	private GENgraphPersistenceService service;
	private static Logger logger = LogManager.getLogger("GENGraphServiceTest");

	@Test
	public void createGraphByGedcomXFile() {
		// GedcomxData data = null;
		try {
			// data =
			this.readGedcomXFile();
		} catch (URISyntaxException | IOException e) {
			GENgraphPersistenceServiceTest.logger
					.error("Error reading gedx file");
		}
	}

	@Test
	public void createGraphByGedcomXObjects() {
		GENgraphPersistenceServiceTest.logger.info("Starting createGraph");
		try {
			final List<Object> data = ExampleGedcomxFileData.create();

			final Map<String, String> attributes = new HashMap<>();
			attributes.put("autor", "albert");
			attributes.put("create-date", new Date().toString());

			this.service.createGraphByGedcomX(attributes, data);

			GENgraphPersistenceServiceTest.logger
					.info("createGraph successful");
		} catch (final Exception e) {
			GENgraphPersistenceServiceTest.logger.error("createGraph Error");
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Before
	public void injectServices() {
		this.service = Guice.createInjector(new ServiceInjector()).getInstance(
				GENgraphPersistenceService.class);
	}

	private GedcomxData readGedcomXFile() throws URISyntaxException,
			IOException {
		final URL gedcomxUrl = this.getClass().getClassLoader()
				.getResource("resources/ged55stresstox.jar");
		final File gedcomxFile = new File(gedcomxUrl.toURI());
		final JarFile jarFile = new JarFile(gedcomxFile);
		final GedcomxFile gedxFile = new GedcomxFile(jarFile);

		final GedcomxData data = new GedcomxData();
		final List<Object> gedxObjects = new ArrayList<>();
		data.attributes = gedxFile.getAttributes();

		final Iterable<GedcomxFileEntry> entries = gedxFile.getEntries();
		for (final GedcomxFileEntry entry : entries) {
			final String name = entry.getJarEntry().getName();
			if (name != null && !"META-INF/MANIFEST.MF".equals(name)) {
				gedxObjects.add(gedxFile.readResource(entry));
			}
		}
		gedxFile.close();
		jarFile.close();

		return data;
	}
}
