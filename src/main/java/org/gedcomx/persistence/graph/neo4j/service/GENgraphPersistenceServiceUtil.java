package org.gedcomx.persistence.graph.neo4j.service;

import java.util.Collection;
import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.GuiceModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GENgraphPersistenceServiceUtil {

	public static GENgraphPersistenceService service;

	public static void createGraphByGedcomX(final Map<String, String> metadata, final Collection<Object> gedcomxElements) {
		GENgraphPersistenceServiceUtil.getService().createGraphByGedcomX(metadata, gedcomxElements);
	}

	private static GENgraphPersistenceService getService() {
		if (GENgraphPersistenceServiceUtil.service == null) {
			final Injector injector = Guice.createInjector(new GuiceModule());
			GENgraphPersistenceServiceUtil.service = injector.getInstance(GENgraphPersistenceService.class);
		}
		return GENgraphPersistenceServiceUtil.service;
	}

}
