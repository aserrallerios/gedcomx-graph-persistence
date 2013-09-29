package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.gedcomx.types.FactType;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.FACT)
public class Fact extends Conclusion {

	protected Fact(final FactType type) {
		super(type);
	}

	protected Fact(final Node node) {
		super(node);
	}

	protected Fact(final org.gedcomx.conclusion.Fact gedcomXFact) {
		super(gedcomXFact);
	}

	protected Fact(final URI type) {
		super(new Object[] { type });
	}

	public Qualifier addQualifier() {
		return this.addQualifier(new Qualifier());
	}

	private Qualifier addQualifier(final Qualifier qualifier) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_QUALIFIER, qualifier);
		return qualifier;
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.getPlaceReference().delete();
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
	public org.gedcomx.conclusion.Fact getGedcomX() {
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

		gedcomXFact.setQualifiers(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.common.Qualifier.class,
						this.getQualifiers()));

		return gedcomXFact;
	}

	public FactType getKnownType() {
		return FactType.fromQNameURI(this.getType());
	}

	@Override
	public NodeWrapper getParentNode() {
		return NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.HAS_FACT);
	}

	public PlaceReference getPlaceReference() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				PlaceReference.class, RelationshipTypes.PLACE);
	}

	public List<Qualifier> getQualifiers() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				Qualifier.class, RelationshipTypes.HAS_QUALIFIER);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.TYPE));
	}

	public String getValue() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.VALUE);
	}

	@Override
	protected void resolveConcreteReferences() {
		this.getPlaceReference().resolveReferences();
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
		final org.gedcomx.conclusion.Fact gedcomXFact = (org.gedcomx.conclusion.Fact) gedcomXObject;
		this.setValue(gedcomXFact.getValue());
		this.setType(gedcomXFact.getType());

		if (gedcomXFact.getDate() != null) {
			this.setDateFormal(gedcomXFact.getDate().getFormal());
			this.setDateOriginal(gedcomXFact.getDate().getOriginal());
		}
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Fact gedcomXFact = (org.gedcomx.conclusion.Fact) gedcomXObject;

		if (gedcomXFact.getPlace() != null) {
			this.setPlaceReference(new PlaceReference(gedcomXFact.getPlace()));
		}
		if (gedcomXFact.getQualifiers() != null) {
			for (final org.gedcomx.common.Qualifier q : gedcomXFact
					.getQualifiers()) {
				this.addQualifier(new Qualifier(q));
			}
		}
	}

	public void setKnownType(final FactType type) {
		this.setType(type.toQNameURI());
	}

	public PlaceReference setPlaceReference() {
		return this.setPlaceReference(new PlaceReference());
	}

	private PlaceReference setPlaceReference(final PlaceReference placeReference) {
		NodeWrapper.nodeWrapperOperations.createRelationship(this,
				RelationshipTypes.PLACE, placeReference);
		return placeReference;
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		if (properties[0] instanceof URI) {
			this.setType((URI) properties[0]);
		} else if (properties[0] instanceof FactType) {
			this.setKnownType((FactType) properties[0]);
		}
	}

	@Deprecated
	public void setType(final URI type) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.TYPE, type);
	}

	public void setValue(final String value) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getType())) {
			throw new MissingRequiredPropertyException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					this.getId(), GenericProperties.TYPE.toString());
		}
	}

}
