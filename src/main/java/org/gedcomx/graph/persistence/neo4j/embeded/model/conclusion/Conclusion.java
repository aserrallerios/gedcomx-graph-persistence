package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public class Conclusion extends GENgraphNode {

	private ConclusionSubnode subnode;

	public Conclusion(final Node underlyingNode, final org.gedcomx.conclusion.Conclusion gedcomXConclusion) {
		super(underlyingNode, NodeTypes.CONCLUSION);
	}

	public ConclusionSubnode getSubnode() {
		return this.subnode;
	}

	public void setSubnode(final ConclusionSubnode subnode) {
		this.subnode = subnode;
	}

}
