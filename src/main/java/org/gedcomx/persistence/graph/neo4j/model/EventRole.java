package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.EventRoleType;
import org.neo4j.graphdb.Node;

@NodeType("EVENT_ROLE")
public class EventRole extends Conclusion {

	protected EventRole(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	public EventRole(final org.gedcomx.conclusion.EventRole gedcomXEventRole) throws MissingFieldException {
		super(gedcomXEventRole);
	}

	public EventRole(final Person person) throws MissingFieldException {
		super(person);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReference(GENgraphRelTypes.PERSON);

	}

	public String getDetails() {
		return (String) this.getProperty(NodeProperties.Conclusion.DETAILS);
	}

	public Event getEvent() {
		return (Event) super.getParentNode(GENgraphRelTypes.HAS_ROLE);
	}

	@Override
	protected org.gedcomx.conclusion.EventRole getGedcomX() {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = new org.gedcomx.conclusion.EventRole();

		this.getGedcomXConclusion(gedcomXEventRole);

		gedcomXEventRole.setType(this.getType());
		gedcomXEventRole.setKnownType(this.getKnownType());
		gedcomXEventRole.setDetails(this.getDetails());

		gedcomXEventRole.setPerson(this.getPerson());

		// TODO
		return gedcomXEventRole;
	}

	public EventRoleType getKnownType() {
		return EventRoleType.fromQNameURI(this.getType());
	}

	public Person getPerson() {
		return this.getNodeByRelationship(Person.class, GENgraphRelTypes.PERSON);
	}

	public URI getType() {
		return new URI((String) this.getProperty(NodeProperties.Generic.TYPE));
	}

	@Override
	protected void resolveReferences() {
		// TODO
	}

	public void setDetails(final String details) {
		this.setProperty(NodeProperties.Conclusion.DETAILS, details);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		this.setType(gedcomXEventRole.getType());
		this.setDetails(gedcomXEventRole.getDetails());
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		// final org.gedcomx.conclusion.EventRole gedcomXEventRole =
		// (org.gedcomx.conclusion.EventRole) gedcomXObject;

		// this.personURI = gedcomXEventRole.getPerson().getResource();
		// final Conclusion conclusion =
		// this.getGraph().getConclusion(this.personURI);
		// if (conclusion != null) {
		// this.setPerson((Person) conclusion.getSubnode());
		// } else {
		// this.addNodeToResolveReferences();
		// }
		// TODO
	}

	public void setKnownType(final EventRoleType type) {
		this.setType(type.toQNameURI());
	}

	public void setPerson(final Person person) {
		this.createRelationship(GENgraphRelTypes.PERSON, person);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.setPerson((Person) properties[0]);
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getPerson())) {
			throw new MissingRequiredRelationshipException(EventRole.class, this.getId(), GENgraphRelTypes.PERSON);
		}
	}

}
