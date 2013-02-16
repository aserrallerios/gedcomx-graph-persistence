package org.gedcomx.graph.persistence.neo4j.embeded.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.dao.GENgraphDAO;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion.Conclusion;
import org.gedcomx.graph.persistence.neo4j.embeded.model.contributor.Agent;
import org.gedcomx.graph.persistence.neo4j.embeded.model.source.SourceDescription;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;
import org.neo4j.graphdb.Node;

public class GENgraph {

	private final Node rootNode;

	private final Collection<Agent> agents;

	private final Collection<SourceDescription> sources;

	private final Collection<Conclusion> conclusions;

	private final GENgraphDAO dao;

	public GENgraph(final GENgraphDAO dao, final Map<String, String> metadata, final Collection<Object> gedcomxElements)
			throws MissingFieldException {
		this.dao = dao;
		this.rootNode = dao.getReferenceNode();
		this.agents = new HashSet<>();
		this.sources = new HashSet<>();
		this.conclusions = new HashSet<>();
		this.getDao().addNodeProperties(this.rootNode, metadata);

		for (final Object gedcomxElement : gedcomxElements) {
			if (gedcomxElement instanceof org.gedcomx.contributor.Agent) {
				this.addAgent(new Agent(this, (org.gedcomx.contributor.Agent) gedcomxElement));
			}
			if (gedcomxElement instanceof org.gedcomx.conclusion.Conclusion) {
				this.addConclusion(new Conclusion(this, (org.gedcomx.conclusion.Conclusion) gedcomxElement));
			}
			if (gedcomxElement instanceof org.gedcomx.source.SourceDescription) {
				this.addSources(new SourceDescription(this, (org.gedcomx.source.SourceDescription) gedcomxElement));
			}
		}
	}

	public void addAgent(final Agent agent) {
		this.agents.add(agent);
		this.getDao().createRelationship(this.rootNode, RelTypes.HAS_AGENT, agent.getUnderlyingNode());
	}

	public void addConclusion(final Conclusion conclusion) {
		this.conclusions.add(conclusion);
		this.getDao().createRelationship(this.rootNode, RelTypes.HAS_CONCLUSION, conclusion.getUnderlyingNode());
	}

	public void addSources(final SourceDescription source) {
		this.sources.add(source);
		this.getDao().createRelationship(this.rootNode, RelTypes.HAS_SOURCE_DESCRIPTION, source.getUnderlyingNode());
	}

	public Collection<Agent> getAgents() {
		return this.agents;
	}

	public Collection<Conclusion> getConclusions() {
		return this.conclusions;
	}

	GENgraphDAO getDao() {
		return this.dao;
	}

	public Node getRootNode() {
		return this.rootNode;
	}

	public Collection<SourceDescription> getSources() {
		return this.sources;
	}
}
