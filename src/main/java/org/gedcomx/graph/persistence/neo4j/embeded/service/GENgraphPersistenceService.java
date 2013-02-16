package org.gedcomx.graph.persistence.neo4j.embeded.service;

import java.util.Collection;
import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;

public interface GENgraphPersistenceService {

	GENgraph createGedcomXGraph(final Map<String, String> metadata, final Collection<Object> gedcomxElements) throws MissingFieldException;

	GENgraph getGedcomXGraph();

}
