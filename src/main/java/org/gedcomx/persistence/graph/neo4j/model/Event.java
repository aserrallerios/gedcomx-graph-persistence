package org.gedcomx.persistence.graph.neo4j.model;

import java.util.LinkedList;
import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.GENgraph;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;

public class Event extends ConclusionSubnode implements GENgraphTopLevelNode {

	PlaceReference placeReference;
	List<EventRole> roles = new LinkedList<>();

	protected Event(final GENgraph graph, final org.gedcomx.conclusion.Event gedcomXEvent) throws MissingFieldException {
		super(graph, NodeTypes.EVENT, gedcomXEvent);
	}

	public void addRole(final EventRole role) {
		this.roles.add(role);
		this.createRelationship(GENgraphRelTypes.HAS_ROLE, role);
	}

	public String getDateFormal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_FORMAL);
	}

	public String getDateOriginal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_ORIGINAL);
	}

	public PlaceReference getPlaceReference() {
		return this.placeReference;
	}

	public List<EventRole> getRoles() {
		return this.roles;
	}

	public URI getType() {
		final String type = (String) this.getProperty(NodeProperties.Generic.TYPE);
		return new URI(type);
	}

	public void setDateFormal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_FORMAL, value);
	}

	public void setDateOriginal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_ORIGINAL, value);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Event gedcomXEvent = (org.gedcomx.conclusion.Event) gedcomXObject;
		this.setType(gedcomXEvent.getType());

		if (gedcomXEvent.getDate() != null) {
			this.setDateFormal(gedcomXEvent.getDate().getFormal());
			this.setDateOriginal(gedcomXEvent.getDate().getOriginal());
		}

	}

	public void setPlaceReference(final PlaceReference placeReference) {
		this.placeReference = placeReference;
		this.createRelationship(GENgraphRelTypes.PLACE, placeReference);
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Event gedcomXEvent = (org.gedcomx.conclusion.Event) gedcomXObject;

		if (gedcomXEvent.getPlace() != null) {
			this.setPlaceReference(new PlaceReference(this.getGraph(), gedcomXEvent.getPlace()));
		}
		for (final org.gedcomx.conclusion.EventRole role : gedcomXEvent.getRoles()) {
			this.addRole(new EventRole(this.getGraph(), role));
		}
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}
}
