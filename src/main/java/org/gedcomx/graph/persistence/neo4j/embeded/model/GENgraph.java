package org.gedcomx.graph.persistence.neo4j.embeded.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.gedcomx.graph.persistence.neo4j.embeded.dao.impl.GENgraphNodeUtils;
import org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion.Conclusion;
import org.gedcomx.graph.persistence.neo4j.embeded.model.contributor.Agent;
import org.gedcomx.graph.persistence.neo4j.embeded.model.source.SourceDescription;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.RelTypes;
import org.neo4j.graphdb.Node;

public class GENgraph {

	private final Node rootNode;

	private final Collection<Agent> agents;

	private final Collection<SourceDescription> sources;

	private final Collection<Conclusion> conclusions;

	public GENgraph(final Node rootNode, final Map<String, String> metadata) {
		this.rootNode = rootNode;
		this.agents = new HashSet<>();
		this.sources = new HashSet<>();
		this.conclusions = new HashSet<>();
		GENgraphNodeUtils.addNodeProperties(rootNode, metadata);
	}

	public void addAgent(final Agent agent) {
		this.agents.add(agent);
		GENgraphNodeUtils.createRelationship(this.rootNode, RelTypes.HAS_AGENT, agent.getUnderlyingNode());
	}

	public void addConclusion(final Conclusion conclusion) {
		this.conclusions.add(conclusion);
		GENgraphNodeUtils.createRelationship(this.rootNode, RelTypes.HAS_CONCLUSION,
				conclusion.getUnderlyingNode());
	}

	public void addSources(final SourceDescription source) {
		this.sources.add(source);
		GENgraphNodeUtils.createRelationship(this.rootNode, RelTypes.HAS_SOURCE_DESCRIPTION,
				source.getUnderlyingNode());
	}

	public Collection<Agent> getAgents() {
		return this.agents;
	}

	public Collection<Conclusion> getConclusions() {
		return this.conclusions;
	}

	public Node getRootNode() {
		return this.rootNode;
	}

	public Collection<SourceDescription> getSources() {
		return this.sources;
	}
}
