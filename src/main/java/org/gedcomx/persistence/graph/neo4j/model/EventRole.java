package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.gedcomx.types.EventRoleType;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.EVENT_ROLE)
public class EventRole extends Conclusion {

	protected EventRole(final Node node) {
		super(node);
	}

	protected EventRole(final org.gedcomx.conclusion.EventRole gedcomXEventRole) {
		super(gedcomXEventRole);
	}

	protected EventRole(final Person person) {
		super(person);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReference(this,
				RelationshipTypes.PERSON);

	}

	public String getDetails() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.DETAILS);
	}

	public Event getEvent() {
		return (Event) NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.HAS_ROLE);
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
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				Person.class, RelationshipTypes.PERSON);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.TYPE));
	}

	@Override
	protected void resolveConcreteReferences() {
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.PERSON, ConclusionProperties.PERSON_REFERENCE);
	}

	public void setDetails(final String details) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.DETAILS, details);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		this.setType(gedcomXEventRole.getType());
		this.setDetails(gedcomXEventRole.getDetails());
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.EventRole gedcomXEventRole = (org.gedcomx.conclusion.EventRole) gedcomXObject;

		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.PERSON_REFERENCE,
				gedcomXEventRole.getPerson());
	}

	public void setKnownType(final EventRoleType type) {
		this.setType(type.toQNameURI());
	}

	public void setPerson(final Person person) {
		NodeWrapper.nodeWrapperOperations.createReferenceRelationship(this,
				RelationshipTypes.PERSON, person);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setPerson((Person) properties[0]);
	}

	@Deprecated
	public void setType(final URI type) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getPerson())) {
			throw new MissingRequiredRelationshipException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					this.getId(), RelationshipTypes.PERSON.toString());
		}
	}

}
