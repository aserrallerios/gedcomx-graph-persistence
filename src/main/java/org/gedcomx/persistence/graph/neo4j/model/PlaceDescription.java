package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

@NodeType(NodeTypes.PLACE_DESCRIPTION)
public class PlaceDescription extends Subject {

	protected PlaceDescription(final Node node) {
		super(node);
	}

	@Inject
	protected PlaceDescription(
			final @Assisted org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription) {
		super(gedcomXPlaceDescription);
	}

	protected PlaceDescription(final String nameValue) {
		super(nameValue);
	}

	private Identifier addIdentifier(final Identifier identifier) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_IDENTIFIER, identifier);
		return identifier;
	}

	@Override
	public Identifier addIdentifier(final URI value) {
		return this.addIdentifier(new Identifier(value));
	}

	public TextValue addName(final String value) {
		return this.addName(new TextValue(value));
	}

	private TextValue addName(final TextValue name) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_NAME, name);
		return name;
	}

	@Override
	protected void deleteAllConcreteReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getIdentifiers());
		NodeWrapper.nodeWrapperOperations
				.deleteReferencedNodes(this.getNames());
	}

	public URI getAbout() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.ABOUT));
	}

	@Override
	public org.gedcomx.conclusion.PlaceDescription getGedcomX() {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = new org.gedcomx.conclusion.PlaceDescription();

		this.getGedcomXConclusion(gedcomXPlaceDescription);

		gedcomXPlaceDescription.setLatitude(this.getLatitude());
		gedcomXPlaceDescription.setLongitude(this.getLongitude());
		gedcomXPlaceDescription.setSpatialDescription(this
				.getSpatialDescription());

		final org.gedcomx.conclusion.Date date = new org.gedcomx.conclusion.Date();
		date.setFormal(this.getTemporalDescriptionFormal());
		date.setOriginal(this.getTemporalDescriptionOriginal());
		gedcomXPlaceDescription.setTemporalDescription(date);

		gedcomXPlaceDescription.setNames(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.common.TextValue.class,
						this.getNames()));
		gedcomXPlaceDescription
				.setIdentifiers(NodeWrapper.nodeWrapperOperations
						.getGedcomXList(
								org.gedcomx.conclusion.Identifier.class,
								this.getIdentifiers()));

		return gedcomXPlaceDescription;
	}

	@Override
	public List<Identifier> getIdentifiers() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				Identifier.class, RelationshipTypes.HAS_IDENTIFIER);
	}

	public Double getLatitude() {
		return (Double) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.LATITUDE);

	}

	public Double getLongitude() {
		return (Double) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.LONGITUDE);
	}

	public List<TextValue> getNames() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				TextValue.class, RelationshipTypes.HAS_NAME);
	}

	@Override
	public NodeWrapper getParentNode() {
		return null;
	}

	public ResourceReference getSpatialDescription() {
		return new ResourceReference(new URI(
				(String) NodeWrapper.nodeWrapperOperations.getProperty(this,
						ConclusionProperties.SPATIAL_DESCRIPTION)));
	}

	public String getTemporalDescriptionFormal() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.TEMPORAL_DESCRIPTION_FORMAL);

	}

	public String getTemporalDescriptionOriginal() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.TEMPORAL_DESCRIPTION_ORIGINAL);

	}

	public URI getType() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.TYPE));
	}

	@Override
	protected void resolveConcreteReferences() {
		return;
	}

	public void setAbout(final URI about) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.ABOUT, about);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

		this.setType(gedcomXPlaceDescription.getType());
		this.setLatitude(gedcomXPlaceDescription.getLatitude());
		this.setLongitude(gedcomXPlaceDescription.getLatitude());
		this.setSpatialDescription(gedcomXPlaceDescription
				.getSpatialDescription());

		if (gedcomXPlaceDescription.getTemporalDescription() != null) {
			this.setTemporalDescriptionOriginal(gedcomXPlaceDescription
					.getTemporalDescription().getOriginal());
			this.setTemporalDescriptionFormal(gedcomXPlaceDescription
					.getTemporalDescription().getFormal());
		}

	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

		for (final org.gedcomx.common.TextValue gedcomXName : gedcomXPlaceDescription
				.getNames()) {
			this.addName(new TextValue(gedcomXName));
		}
		if (gedcomXPlaceDescription.getIdentifiers() != null) {
			for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXPlaceDescription
					.getIdentifiers()) {
				this.addIdentifier(new Identifier(gedcomXIdentifier));
			}
		}
	}

	public void setLatitude(final Double latitude) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.LATITUDE, latitude);

	}

	public void setLongitude(final Double longitude) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.LONGITUDE, longitude);

	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.addName(new TextValue((String) properties[0]));
	}

	public void setSpatialDescription(final ResourceReference spatialDescription) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.SPATIAL_DESCRIPTION, spatialDescription);
	}

	public void setTemporalDescriptionFormal(final String formal) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.TEMPORAL_DESCRIPTION_FORMAL, formal);

	}

	public void setTemporalDescriptionOriginal(final String original) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.TEMPORAL_DESCRIPTION_ORIGINAL, original);

	}

	public void setType(final URI type) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getNames())) {
			throw new MissingRequiredRelationshipException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					this.getId(), RelationshipTypes.HAS_NAME.toString());
		}
	}

}
