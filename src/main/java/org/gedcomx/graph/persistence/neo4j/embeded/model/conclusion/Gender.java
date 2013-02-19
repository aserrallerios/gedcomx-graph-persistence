package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public class Gender extends ConclusionSubnode {

	protected Gender(final GENgraph graph, final org.gedcomx.conclusion.Gender gedcomXGender) throws MissingFieldException {
		super(graph, NodeTypes.GENDER, gedcomXGender);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Gender gedcomXGender = (org.gedcomx.conclusion.Gender) gedcomXObject;
		if (gedcomXGender.getType() == null) {
			throw new MissingRequiredPropertyException(Gender.class, NodeProperties.Generic.TYPE);
		}
	}

	public URI getType() {
		return new URI((String) this.getProperty(NodeProperties.Generic.TYPE));
	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Gender gedcomXGender = (org.gedcomx.conclusion.Gender) gedcomXObject;
		this.setType(gedcomXGender.getType());
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

}
