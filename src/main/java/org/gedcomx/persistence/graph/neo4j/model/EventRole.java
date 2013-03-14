package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;

public class EventRole extends ConclusionSubnode {

	private Person person;
	private URI personURI;

	protected EventRole(final GENgraph graph, final org.gedcomx.conclusion.EventRole gedcomXEventRole) throws MissingFieldException {
		super(graph, NodeTypes.EVENT_ROLE, gedcomXEventRole);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		if (gedcomXEventRole.getPerson() == null) {
			throw new MissingRequiredRelationshipException(EventRole.class, gedcomXEventRole.getId(), GENgraphRelTypes.PERSON);
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

	@Override
	protected void resolveReferences() {
		if ((this.person == null) && (this.personURI != null)) {
			final Conclusion conclusion = this.getGraph().getConclusion(this.personURI);
			if (conclusion != null) {
				this.setPerson((Person) conclusion.getSubnode());
			}
		}
	}

	public void setDetails(final String details) {
		this.setProperty(NodeProperties.Conclusion.DETAILS, details);

	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		this.setType(gedcomXEventRole.getType());
		this.setDetails(gedcomXEventRole.getDetails());
	}

	public void setPerson(final Person person) {
		this.person = person;
		this.createRelationship(GENgraphRelTypes.PERSON, person);
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		this.personURI = gedcomXEventRole.getPerson().getResource();
		final Conclusion conclusion = this.getGraph().getConclusion(this.personURI);
		if (conclusion != null) {
			this.setPerson((Person) conclusion.getSubnode());
		} else {
			this.addNodeToResolveReferences();
		}
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type);
	}

}
