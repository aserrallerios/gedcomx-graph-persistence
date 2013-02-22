package org.gedcomx.graph.persistence.neo4j.embeded.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.dao.GENgraphDAO;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion.Conclusion;
import org.gedcomx.graph.persistence.neo4j.embeded.model.contributor.Agent;
import org.gedcomx.graph.persistence.neo4j.embeded.model.source.SourceDescription;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;
import org.neo4j.graphdb.Node;

public class GENgraph {

	private final Node rootNode;

	private final Collection<Agent> agents = new ArrayList<>();
	private final Collection<SourceDescription> sources = new ArrayList<>();
	private final Collection<Conclusion> conclusions = new ArrayList<>();

	private final GENgraphDAO dao;

	private final Map<URI, Agent> agentIndex = new HashMap<>();
	private final Map<URI, SourceDescription> sourceIndex = new HashMap<>();
	private final Map<URI, Conclusion> conclusionIndex = new HashMap<>();

	private final Collection<GENgraphNode> nodesToResolveReferences = new ArrayList<>();

	public GENgraph(final GENgraphDAO dao, final Map<String, String> metadata, final Collection<Object> gedcomxElements)
			throws MissingFieldException {
		this.dao = dao;

		this.rootNode = dao.getReferenceNode();

		this.getDao().addNodeProperties(this.rootNode, metadata);

		// Top level Data Types
		for (final Object gedcomxElement : gedcomxElements) {
			if (gedcomxElement instanceof org.gedcomx.contributor.Agent) {
				this.addAgent(new Agent(this, (org.gedcomx.contributor.Agent) gedcomxElement));
			}
			if ((gedcomxElement instanceof org.gedcomx.conclusion.Conclusion)
					&& ((gedcomxElement instanceof org.gedcomx.conclusion.Person)
							|| (gedcomxElement instanceof org.gedcomx.conclusion.Document)
							|| (gedcomxElement instanceof org.gedcomx.conclusion.Event)
							|| (gedcomxElement instanceof org.gedcomx.conclusion.Relationship) || (gedcomxElement instanceof org.gedcomx.conclusion.PlaceDescription))) {
				this.addConclusion(new Conclusion(this, (org.gedcomx.conclusion.Conclusion) gedcomxElement));
			}
			if (gedcomxElement instanceof org.gedcomx.source.SourceDescription) {
				this.addSources(new SourceDescription(this, (org.gedcomx.source.SourceDescription) gedcomxElement));
			}
		}
		for (final GENgraphNode node : this.nodesToResolveReferences) {
			node.resolveReferences();
		}
	}

	public void addAgent(final Agent agent) {
		this.agents.add(agent);
		this.getDao().createRelationship(this.rootNode, RelTypes.HAS_AGENT, agent.getUnderlyingNode());
		if (agent.getId() != null) {
			this.agentIndex.put(new URI(agent.getId()), agent);
		}
	}

	public void addConclusion(final Conclusion conclusion) {
		if (conclusion.getSubnode() instanceof GENgraphTopLevelNode) {
			this.conclusions.add(conclusion);
			this.getDao().createRelationship(this.rootNode, RelTypes.HAS_CONCLUSION, conclusion.getUnderlyingNode());
			if (conclusion.getId() != null) {
				this.conclusionIndex.put(new URI(conclusion.getId()), conclusion);
			}
		}
	}

	void addNodeToResolveReferences(final GENgraphNode node) {
		this.nodesToResolveReferences.add(node);
	}

	public void addSources(final SourceDescription source) {
		this.sources.add(source);
		this.getDao().createRelationship(this.rootNode, RelTypes.HAS_SOURCE_DESCRIPTION, source.getUnderlyingNode());
		if (source.getId() != null) {
			this.sourceIndex.put(new URI(source.getId()), source);
		}
	}

	public Agent getAgent(final URI reference) {
		return this.agentIndex.get(reference);
	}

	public Collection<Agent> getAgents() {
		return this.agents;
	}

	public Conclusion getConclusion(final URI reference) {
		return this.conclusionIndex.get(reference);
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

	public SourceDescription getSource(final URI reference) {
		return this.sourceIndex.get(reference);
	}

	public Collection<SourceDescription> getSources() {
		return this.sources;
	}
}
