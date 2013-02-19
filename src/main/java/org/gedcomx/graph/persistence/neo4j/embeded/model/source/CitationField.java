package org.gedcomx.graph.persistence.neo4j.embeded.model.source;

import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public class CitationField extends GENgraphNode {

	protected CitationField(final GENgraph graph, final org.gedcomx.source.CitationField gedcomXCitationField) throws MissingFieldException {
		super(graph, NodeTypes.CITATION_FIELD, gedcomXCitationField);
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		// TODO Auto-generated method stub

	}

}
