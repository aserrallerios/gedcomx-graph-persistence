package org.gedcomx.graph.persistence.neo4j.embeded.service.impl;

import java.util.Collection;
import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.dao.GENgraphDAO;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion.Conclusion;
import org.gedcomx.graph.persistence.neo4j.embeded.model.contributor.Agent;
import org.gedcomx.graph.persistence.neo4j.embeded.model.source.SourceDescription;
import org.gedcomx.graph.persistence.neo4j.embeded.service.GENgraphPersistenceService;
import org.neo4j.graphdb.Node;

public class GENgraphPersistenceServiceImpl implements GENgraphPersistenceService {

	private GENgraphDAO dao;

	public GENgraphPersistenceServiceImpl() {
	}

	public GENgraphPersistenceServiceImpl(final GENgraphDAO dao) {
		this.dao = dao;
	}

	public Agent createAgent(final org.gedcomx.contributor.Agent gedcomXAgent) {
		final Node agentNode = this.dao.createNode();
		final Agent agent = new Agent(agentNode, gedcomXAgent);
		return agent;
	}

	public Conclusion createConclusion(final org.gedcomx.conclusion.Conclusion gedcomXConclusion) {
		final Node conclusionNode = this.dao.createNode();
		final Conclusion conclusion = new Conclusion(conclusionNode, gedcomXConclusion);
		return conclusion;
	}

	@Override
	public GENgraph createGedcomXGraph(final Map<String, String> metadata,
			final Collection<Object> gedcomxElements) {

		final Node rootNode = this.dao.getReferenceNode();
		final GENgraph graph = new GENgraph(rootNode, metadata);

		for (final Object gedcomxElement : gedcomxElements) {
			if (gedcomxElement instanceof org.gedcomx.contributor.Agent) {
				graph.addAgent(this.createAgent((org.gedcomx.contributor.Agent) gedcomxElement));
			}
			if (gedcomxElement instanceof org.gedcomx.conclusion.Conclusion) {
				graph.addConclusion(this.createConclusion((org.gedcomx.conclusion.Conclusion) gedcomxElement));
			}
			if (gedcomxElement instanceof org.gedcomx.source.SourceDescription) {
				graph.addSources(this
						.createSourceDescription((org.gedcomx.source.SourceDescription) gedcomxElement));
			}
		}
		return graph;
	}

	public SourceDescription createSourceDescription(
			final org.gedcomx.source.SourceDescription gedcomXSourceDescription) {
		final Node sourceDescriptionNode = this.dao.createNode();
		final SourceDescription sourceDescription = new SourceDescription(sourceDescriptionNode,
				gedcomXSourceDescription);
		return sourceDescription;
	}

	@Override
	public GENgraph loadGedcomXGraph() {
		// TODO Auto-generated method stub
		return null;
	}
}
