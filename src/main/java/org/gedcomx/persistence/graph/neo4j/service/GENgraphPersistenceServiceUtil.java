package org.gedcomx.persistence.graph.neo4j.service;

import java.util.Collection;
import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.exception.GenericError;
import org.gedcomx.persistence.graph.neo4j.messages.ErrorMessages;
import org.reflections.Reflections;

public class GENgraphPersistenceServiceUtil {

	private static GENgraphPersistenceService service;

	public static void createGraphByGedcomX(final Map<String, String> metadata, final Collection<Object> gedcomxElements) {
		GENgraphPersistenceServiceUtil.getService().createGraphByGedcomX(metadata, gedcomxElements);

	}

	private static GENgraphPersistenceService getService() {
		if (GENgraphPersistenceServiceUtil.service == null) {
			final Reflections reflections = new Reflections(GENgraphPersistenceServiceUtil.class.getPackage().getName());

			for (final Class<? extends GENgraphPersistenceService> subclass : reflections.getSubTypesOf(GENgraphPersistenceService.class)) {
				if (subclass != null) {
					try {
						GENgraphPersistenceServiceUtil.service = subclass.newInstance();
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
						e.printStackTrace();
						throw new GenericError(ErrorMessages.SERVICE_CANT_INSTANTIATE);
					}
				}
			}
		}
		return GENgraphPersistenceServiceUtil.service;
	}

}
