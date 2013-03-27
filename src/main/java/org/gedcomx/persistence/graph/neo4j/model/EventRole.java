package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.EventRoleType;
import org.neo4j.graphdb.Node;

@NodeType("EVENT_ROLE")
public class EventRole extends Conclusion {

	protected EventRole(final Node node) throws MissingFieldException, UnknownNodeType {
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
		this.deleteReference(RelationshipTypes.PERSON);

	}

	public String getDetails() {
		return (String) this.getProperty(ConclusionProperties.DETAILS);
	}

	public Event getEvent() {
		return (Event) super.getParentNode(RelationshipTypes.HAS_ROLE);
	}

	@Override
	public org.gedcomx.conclusion.EventRole getGedcomX() {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = new org.gedcomx.conclusion.EventRole();

		this.getGedcomXConclusion(gedcomXEventRole);

		gedcomXEventRole.setType(this.getType());
		gedcomXEventRole.setKnownType(this.getKnownType());
		gedcomXEventRole.setDetails(this.getDetails());

		gedcomXEventRole.setPerson(this.getPerson().getResourceReference());

		return gedcomXEventRole;
	}

	public EventRoleType getKnownType() {
		return EventRoleType.fromQNameURI(this.getType());
	}

	public Person getPerson() {
		return this.getNodeByRelationship(Person.class, RelationshipTypes.PERSON);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	@Override
	protected void resolveConcreteReferences() {
		this.createReferenceRelationship(RelationshipTypes.PERSON, ConclusionProperties.PERSON_REFERENCE);
	}

	public void setDetails(final String details) {
		this.setProperty(ConclusionProperties.DETAILS, details);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		this.setType(gedcomXEventRole.getType());
		this.setDetails(gedcomXEventRole.getDetails());
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		this.setProperty(ConclusionProperties.PERSON_REFERENCE, gedcomXEventRole.getPerson());
	}

	public void setKnownType(final EventRoleType type) {
		this.setType(type.toQNameURI());
	}

	public void setPerson(final Person person) {
		this.createReferenceRelationship(RelationshipTypes.PERSON, person);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.setPerson((Person) properties[0]);
	}

	@Deprecated
	public void setType(final URI type) {
		this.setProperty(GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getPerson())) {
			throw new MissingRequiredRelationshipException(this.getAnnotatedNodeType(), this.getId(), RelationshipTypes.PERSON.toString());
		}
	}

}
