package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.FactType;
import org.neo4j.graphdb.Node;

@NodeType("FACT")
public class Fact extends Conclusion {

	public Fact(final FactType type) throws MissingFieldException {
		super(new Object[] { type });
	}

	protected Fact(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	public Fact(final org.gedcomx.conclusion.Fact gedcomXFact) throws MissingFieldException {
		super(gedcomXFact);
	}

	public Fact(final URI type) throws MissingFieldException {
		super(new Object[] { type });
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReferencedNode(this.getPlaceReference());
	}

	@Override
	protected void deleteAllReferences() {
		super.deleteAllReferences();

		this.getPlaceReference().delete();
	}

	public String getDateFormal() {
		return (String) this.getProperty(ConclusionProperties.DATE_FORMAL);
	}

	public String getDateOriginal() {
		return (String) this.getProperty(ConclusionProperties.DATE_ORIGINAL);
	}

	@Override
	protected org.gedcomx.conclusion.Fact getGedcomX() {
		final org.gedcomx.conclusion.Fact gedcomXFact = new org.gedcomx.conclusion.Fact();

		this.getGedcomXConclusion(gedcomXFact);

		final org.gedcomx.conclusion.Date date = new org.gedcomx.conclusion.Date();
		date.setFormal(this.getDateFormal());
		date.setOriginal(this.getDateOriginal());
		gedcomXFact.setDate(date);

		gedcomXFact.setKnownType(this.getKnownType());
		gedcomXFact.setType(this.getType());
		gedcomXFact.setValue(this.getValue());

		gedcomXFact.setPlace(this.getPlaceReference().getGedcomX());

		return gedcomXFact;
	}

	public FactType getKnownType() {
		return FactType.fromQNameURI(this.getType());
	}

	public NodeWrapper getParentNode() {
		return super.getParentNode(RelationshipTypes.HAS_FACT);
	}

	public PlaceReference getPlaceReference() {
		return this.getNodeByRelationship(PlaceReference.class, RelationshipTypes.PLACE);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	public String getValue() {
		return (String) this.getProperty(GenericProperties.VALUE);
	}

	@Override
	public void resolveReferences() {
		return;
	}

	public void setDateFormal(final String value) {
		this.setProperty(ConclusionProperties.DATE_FORMAL, value);
	}

	public void setDateOriginal(final String value) {
		this.setProperty(ConclusionProperties.DATE_ORIGINAL, value);
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Fact gedcomXFact = (org.gedcomx.conclusion.Fact) gedcomXObject;
		this.setValue(gedcomXFact.getValue());
		this.setType(gedcomXFact.getType());

		if (gedcomXFact.getDate() != null) {
			this.setDateFormal(gedcomXFact.getDate().getFormal());
			this.setDateOriginal(gedcomXFact.getDate().getOriginal());
		}
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Fact gedcomXFact = (org.gedcomx.conclusion.Fact) gedcomXObject;

		if (gedcomXFact.getPlace() != null) {
			this.setPlaceReference(new PlaceReference(gedcomXFact.getPlace()));
		}
	}

	public void setKnownType(final FactType type) {
		this.setType(type.toQNameURI());
	}

	public void setPlaceReference(final PlaceReference placeReference) {
		this.createRelationship(RelationshipTypes.PLACE, placeReference);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		if (properties[0] instanceof URI) {
			this.setType((URI) properties[0]);
		} else if (properties[0] instanceof FactType) {
			this.setKnownType((FactType) properties[0]);
		}
	}

	@Deprecated
	public void setType(final URI type) {
		this.setProperty(GenericProperties.TYPE, type);
	}

	public void setValue(final String value) {
		this.setProperty(GenericProperties.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getType())) {
			throw new MissingRequiredPropertyException(this.getAnnotatedNodeType(), this.getId(), GenericProperties.TYPE.toString());
		}
	}

}
