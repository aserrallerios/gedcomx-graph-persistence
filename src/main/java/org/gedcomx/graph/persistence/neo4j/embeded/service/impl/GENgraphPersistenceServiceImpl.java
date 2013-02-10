package org.gedcomx.graph.persistence.neo4j.embeded.service.impl;

import java.util.Collection;
import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.dao.GENgraphDAO;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion.Conclusion;
import org.gedcomx.graph.persistence.neo4j.embeded.model.contributor.Agent;
import org.gedcomx.graph.persistence.neo4j.embeded.model.source.SourceDescription;
import org.gedcomx.graph.persistence.neo4j.embeded.service.GENgraphPersistenceService;

public class GENgraphPersistenceServiceImpl implements GENgraphPersistenceService {

	private GENgraphDAO dao;

	public GENgraphPersistenceServiceImpl() {
	}

	public GENgraphPersistenceServiceImpl(final GENgraphDAO dao) {
		this.dao = dao;
	}

	private Agent createAgent(final org.gedcomx.contributor.Agent gedcomXAgent) throws MissingRequiredPropertyException {
		final Agent agent = new Agent(gedcomXAgent);
		return agent;
	}

	private Conclusion createConclusion(final org.gedcomx.conclusion.Conclusion gedcomXConclusion) throws MissingRequiredPropertyException {
		final Conclusion conclusion = new Conclusion(gedcomXConclusion);
		return conclusion;
	}

	@Override
	public GENgraph createGedcomXGraph(final Map<String, String> metadata, final Collection<Object> gedcomxElements)
			throws MissingRequiredPropertyException {

		final GENgraph graph = new GENgraph(metadata);

		for (final Object gedcomxElement : gedcomxElements) {
			if (gedcomxElement instanceof org.gedcomx.contributor.Agent) {
				graph.addAgent(this.createAgent((org.gedcomx.contributor.Agent) gedcomxElement));
			}
			if (gedcomxElement instanceof org.gedcomx.conclusion.Conclusion) {
				graph.addConclusion(this.createConclusion((org.gedcomx.conclusion.Conclusion) gedcomxElement));
			}
			if (gedcomxElement instanceof org.gedcomx.source.SourceDescription) {
				graph.addSources(this.createSourceDescription((org.gedcomx.source.SourceDescription) gedcomxElement));
			}
		}
		return graph;
	}

	private SourceDescription createSourceDescription(final org.gedcomx.source.SourceDescription gedcomXSourceDescription)
			throws MissingRequiredPropertyException {
		final SourceDescription sourceDescription = new SourceDescription(gedcomXSourceDescription);
		return sourceDescription;
	}

	@Override
	public GENgraph loadGedcomXGraph() {
		this.dao.getNode(0l);
		return null;
	}
}
