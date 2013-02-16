package org.gedcomx.graph.persistence.neo4j.embeded.service.impl;

import java.util.Collection;
import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.dao.GENgraphDAO;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.service.GENgraphPersistenceService;

public class GENgraphPersistenceServiceImpl implements GENgraphPersistenceService {

	private GENgraphDAO dao;

	public GENgraphPersistenceServiceImpl() {
	}

	public GENgraphPersistenceServiceImpl(final GENgraphDAO dao) {
		this.dao = dao;
	}

	@Override
	public GENgraph createGedcomXGraph(final Map<String, String> metadata, final Collection<Object> gedcomxElements)
			throws MissingFieldException {

		final GENgraph graph = new GENgraph(this.dao, metadata, gedcomxElements);
		return graph;
	}

	@Override
	public GENgraph getGedcomXGraph() {
		return null;
	}
}
