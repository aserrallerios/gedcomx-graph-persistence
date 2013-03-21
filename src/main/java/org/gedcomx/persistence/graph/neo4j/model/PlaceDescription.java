package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("PLACE_DESCRIPTION")
public class PlaceDescription extends Conclusion {

	protected PlaceDescription(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	protected PlaceDescription(final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription) throws MissingFieldException {
		super(gedcomXPlaceDescription);
	}

	protected PlaceDescription(final String nameValue) throws MissingFieldException {
		super(new Object[] { nameValue });
	}

	public void addIdentifier(final Identifier identifier) {
		this.addRelationship(RelTypes.HAS_IDENTIFIER, identifier);
	}

	public void addName(final TextValue name) {
		this.addRelationship(RelTypes.HAS_NAME, name);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReferencedNodes(this.getIdentifiers());
		this.deleteReferencedNodes(this.getNames());
	}

	public URI getAbout() {
		return new URI((String) this.getProperty(GenericProperties.ABOUT));
	}

	@Override
	protected org.gedcomx.conclusion.PlaceDescription getGedcomX() {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = new org.gedcomx.conclusion.PlaceDescription();

		this.getGedcomXConclusion(gedcomXPlaceDescription);

		gedcomXPlaceDescription.setAbout(this.getAbout());
		gedcomXPlaceDescription.setLatitude(this.getLatitude());
		gedcomXPlaceDescription.setLongitude(this.getLongitude());
		gedcomXPlaceDescription.setSpatialDescription(this.getSpatialDescription());

		final org.gedcomx.conclusion.Date date = new org.gedcomx.conclusion.Date();
		date.setFormal(this.getTemporalDescriptionFormal());
		date.setOriginal(this.getTemporalDescriptionOriginal());
		gedcomXPlaceDescription.setTemporalDescription(date);

		gedcomXPlaceDescription.setNames(this.getGedcomXList(org.gedcomx.common.TextValue.class, this.getNames()));
		gedcomXPlaceDescription.setIdentifiers(this.getGedcomXList(org.gedcomx.conclusion.Identifier.class, this.getIdentifiers()));

		return gedcomXPlaceDescription;
	}

	public List<Identifier> getIdentifiers() {
		return this.getNodesByRelationship(Identifier.class, RelTypes.HAS_IDENTIFIER);
	}

	public Double getLatitude() {
		return (Double) this.getProperty(ConclusionProperties.LATITUDE);

	}

	public Double getLongitude() {
		return (Double) this.getProperty(ConclusionProperties.LONGITUDE);

	}

	public List<TextValue> getNames() {
		return this.getNodesByRelationship(TextValue.class, RelTypes.HAS_NAME);
	}

	public ResourceReference getSpatialDescription() {
		return new ResourceReference(new URI((String) this.getProperty(ConclusionProperties.SPATIAL_DESCRIPTION)));
	}

	public String getTemporalDescriptionFormal() {
		return (String) this.getProperty(ConclusionProperties.TEMPORAL_DESCRIPTION_FORMAL);

	}

	public String getTemporalDescriptionOriginal() {
		return (String) this.getProperty(ConclusionProperties.TEMPORAL_DESCRIPTION_ORIGINAL);

	}

	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	public void setAbout(final URI about) {
		this.setProperty(GenericProperties.ABOUT, about);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

		this.setAbout(gedcomXPlaceDescription.getAbout());
		this.setType(gedcomXPlaceDescription.getType());
		this.setLatitude(gedcomXPlaceDescription.getLatitude());
		this.setLongitude(gedcomXPlaceDescription.getLatitude());
		this.setSpatialDescription(gedcomXPlaceDescription.getSpatialDescription());

		if (gedcomXPlaceDescription.getTemporalDescription() != null) {
			this.setTemporalDescriptionOriginal(gedcomXPlaceDescription.getTemporalDescription().getOriginal());
			this.setTemporalDescriptionFormal(gedcomXPlaceDescription.getTemporalDescription().getFormal());
		}

	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.PlaceDescription gedcomXPlaceDescription = (org.gedcomx.conclusion.PlaceDescription) gedcomXObject;

		for (final org.gedcomx.common.TextValue gedcomXName : gedcomXPlaceDescription.getNames()) {
			this.addName(new TextValue(gedcomXName));
		}
		for (final org.gedcomx.conclusion.Identifier gedcomXIdentifier : gedcomXPlaceDescription.getIdentifiers()) {
			this.addIdentifier(new Identifier(gedcomXIdentifier));
		}
	}

	public void setLatitude(final Double latitude) {
		this.setProperty(ConclusionProperties.LATITUDE, latitude);

	}

	public void setLongitude(final Double longitude) {
		this.setProperty(ConclusionProperties.LONGITUDE, longitude);

	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.addName(new TextValue((String) properties[0]));
	}

	public void setSpatialDescription(final ResourceReference spatialDescription) {
		this.setProperty(ConclusionProperties.SPATIAL_DESCRIPTION, spatialDescription);
	}

	public void setTemporalDescriptionFormal(final String formal) {
		this.setProperty(ConclusionProperties.TEMPORAL_DESCRIPTION_FORMAL, formal);

	}

	public void setTemporalDescriptionOriginal(final String original) {
		this.setProperty(ConclusionProperties.TEMPORAL_DESCRIPTION_ORIGINAL, original);

	}

	public void setType(final URI type) {
		this.setProperty(GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getNames())) {
			throw new MissingRequiredRelationshipException(PlaceDescription.class, this.getId(), RelTypes.HAS_NAME.toString());
		}
	}

}
