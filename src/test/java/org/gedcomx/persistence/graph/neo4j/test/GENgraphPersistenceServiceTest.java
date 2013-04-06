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
import org.gedcomx.persistence.graph.neo4j.service.GENgraphPersistenceServiceUtil;
import org.junit.Test;

public class GENgraphPersistenceServiceTest {

	private static class GedcomxData {
		List<Object> entries;
		Map<String, String> attributes;
	}

	private static Logger logger = LogManager.getLogger("GENGraphServiceTest");

	@Test
	public void createGraphByGedcomXFile() {
		GENgraphPersistenceServiceTest.logger.info("Starting createGraphByGedcomX test");
		GedcomxData data = null;
		try {
			data = this.readGedcomXFile();
		} catch (URISyntaxException | IOException e) {
			GENgraphPersistenceServiceTest.logger.error("Error reading gedx file");
		}
		GENgraphPersistenceServiceUtil.createGraphByGedcomX(data.attributes, data.entries);
	}

	@Test
	public void createGraphByGedcomXObjects() {
		GENgraphPersistenceServiceTest.logger.info("Starting createGraphByGedcomX test");
		try {
			final List<Object> data = ExampleGedcomxFileData.create();

			final Map<String, String> attributes = new HashMap<>();
			attributes.put("autor", "albert");
			attributes.put("create-date", new Date().toString());

			GENgraphPersistenceServiceUtil.createGraphByGedcomX(attributes, data);
		} catch (final Exception e) {
			GENgraphPersistenceServiceTest.logger.info("Uncatched error during createGraphByGedcomX test");
			e.printStackTrace();
		}
	}

	private GedcomxData readGedcomXFile() throws URISyntaxException, IOException {
		final URL gedcomxUrl = this.getClass().getClassLoader().getResource("resources/ged55stresstox.jar");
		final File gedcomxFile = new File(gedcomxUrl.toURI());
		final JarFile jarFile = new JarFile(gedcomxFile);
		final GedcomxFile gedxFile = new GedcomxFile(jarFile);

		final GedcomxData data = new GedcomxData();
		final List<Object> gedxObjects = new ArrayList<>();
		data.entries = gedxObjects;
		data.attributes = gedxFile.getAttributes();

		final Iterable<GedcomxFileEntry> entries = gedxFile.getEntries();
		for (final GedcomxFileEntry entry : entries) {
			final String name = entry.getJarEntry().getName();
			if ((name != null) && (!"META-INF/MANIFEST.MF".equals(name))) {
				gedxObjects.add(gedxFile.readResource(entry));
			}
		}
		gedxFile.close();
		jarFile.close();

		return data;
	}
}
