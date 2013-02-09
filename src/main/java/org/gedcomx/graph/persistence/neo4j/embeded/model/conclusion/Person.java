package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.model.utils.NodeTypes;
import org.neo4j.graphdb.Node;

public abstract class Person extends GENgraphNode implements ConclusionSubnode {

	public Person(final Node underlyingNode, final org.gedcomx.conclusion.Conclusion gedcomXConclusion) {
		super(underlyingNode, NodeTypes.PERSON);
	}

}
