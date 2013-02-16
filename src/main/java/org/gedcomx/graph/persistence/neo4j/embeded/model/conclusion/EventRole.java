package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredRelationshipException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.RelTypes;

public class EventRole extends GENgraphNode {

	private Person person;

	protected EventRole(final GENgraph graph, final org.gedcomx.conclusion.EventRole gedcomXEventRole) throws MissingFieldException {
		super(graph, NodeTypes.EVENT_ROLE, gedcomXEventRole);

		// TODO
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		if (gedcomXEventRole.getPerson() == null) {
			throw new MissingRequiredRelationshipException(EventRole.class, RelTypes.PERSON);
		}
	}

	public String getDetails() {
		return (String) this.getProperty(NodeProperties.Conclusion.DETAILS);
	}

	public Person getPerson() {
		return this.person;
	}

	public URI getType() {
		return new URI((String) this.getProperty(NodeProperties.Generic.TYPE));
	}

	public void setDetails(final String details) {
		this.setProperty(NodeProperties.Conclusion.DETAILS, details);

	}

	@Override
	protected void setInitialProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		this.setType(gedcomXEventRole.getType());
		this.setDetails(gedcomXEventRole.getDetails());
	}

	public void setPerson(final Person person) {
		this.person = person;
		this.createRelationship(RelTypes.PERSON, person);
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type);
	}

}