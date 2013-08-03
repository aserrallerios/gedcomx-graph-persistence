package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.types.EventType;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

@NodeType(NodeTypes.EVENT)
public class Event extends Conclusion {

	protected Event() {
		super();
	}

	protected Event(final Node node) {
		super(node);
	}

	@Inject
	protected Event(final @Assisted org.gedcomx.conclusion.Event gedcomXEvent) {
		super(gedcomXEvent);
	}

	private EventRole addRole(final EventRole role) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_ROLE, role);
		return role;
	}

	public EventRole addRole(final Person person) {
		return this.addRole(new EventRole(person));
	}

	@Override
	protected void deleteAllConcreteReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReferencedNode(this
				.getPlaceReference());
		NodeWrapper.nodeWrapperOperations
				.deleteReferencedNodes(this.getRoles());
	}

	public String getDateFormal() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.DATE_FORMAL);
	}

	public String getDateOriginal() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.DATE_ORIGINAL);
	}

	@Override
	public org.gedcomx.conclusion.Event getGedcomX() {
		final org.gedcomx.conclusion.Event gedcomXEvent = new org.gedcomx.conclusion.Event();

		this.getGedcomXConclusion(gedcomXEvent);

		final org.gedcomx.conclusion.Date date = new org.gedcomx.conclusion.Date();
		date.setFormal(this.getDateFormal());
		date.setOriginal(this.getDateOriginal());
		gedcomXEvent.setDate(date);

		gedcomXEvent.setKnownType(this.getKnownType());
		gedcomXEvent.setPlace(this.getPlaceReference().getGedcomX());
		gedcomXEvent.setRoles(NodeWrapper.nodeWrapperOperations.getGedcomXList(
				org.gedcomx.conclusion.EventRole.class, this.getRoles()));

		return gedcomXEvent;
	}

	public EventType getKnownType() {
		return EventType.fromQNameURI(this.getType());
	}

	public PlaceReference getPlaceReference() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				PlaceReference.class, RelationshipTypes.PLACE);
	}

	public List<EventRole> getRoles() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				EventRole.class, RelationshipTypes.HAS_ROLE);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.TYPE));
	}

	@Override
	protected void resolveConcreteReferences() {
		this.getPlaceReference().resolveReferences();
		for (final EventRole role : this.getRoles()) {
			role.resolveReferences();
		}
	}

	public void setDateFormal(final String value) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.DATE_FORMAL, value);
	}

	public void setDateOriginal(final String value) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.DATE_ORIGINAL, value);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Event gedcomXEvent = (org.gedcomx.conclusion.Event) gedcomXObject;
		this.setType(gedcomXEvent.getType());

		if (gedcomXEvent.getDate() != null) {
			this.setDateFormal(gedcomXEvent.getDate().getFormal());
			this.setDateOriginal(gedcomXEvent.getDate().getOriginal());
		}

	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Event gedcomXEvent = (org.gedcomx.conclusion.Event) gedcomXObject;

		if (gedcomXEvent.getPlace() != null) {
			this.setPlaceReference(new PlaceReference(gedcomXEvent.getPlace()));
		}
		if (gedcomXEvent.getRoles() != null) {
			for (final org.gedcomx.conclusion.EventRole role : gedcomXEvent
					.getRoles()) {
				this.addRole(new EventRole(role));
			}
		}
	}

	public void setKnownType(final EventType type) {
		this.setType(type.toQNameURI());
	}

	public void setPlaceReference(final PlaceReference placeReference) {
		NodeWrapper.nodeWrapperOperations.createRelationship(this,
				RelationshipTypes.PLACE, placeReference);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		return;
	}

	@Deprecated
	public void setType(final URI type) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() {
		return;
	}
}
