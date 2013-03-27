package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.GenderType;
import org.neo4j.graphdb.Node;

@NodeType("GENDER")
public class Gender extends Conclusion {

	public Gender(final GenderType type) throws MissingFieldException {
		super(new Object[] { type });
	}

	protected Gender(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	public Gender(final org.gedcomx.conclusion.Gender gedcomXGender) throws MissingFieldException {
		super(gedcomXGender);
	}

	public Gender(final URI type) throws MissingFieldException {
		super(new Object[] { type });
	}

	@Override
	protected void deleteAllConcreteReferences() {
		return;
	}

	@Override
	public org.gedcomx.conclusion.Gender getGedcomX() {
		final org.gedcomx.conclusion.Gender gedcomXGender = new org.gedcomx.conclusion.Gender();

		super.getGedcomXConclusion(gedcomXGender);

		gedcomXGender.setType(this.getType());
		gedcomXGender.setKnownType(this.getKnownType());

		return gedcomXGender;
	}

	public GenderType getKnownType() {
		return GenderType.fromQNameURI(this.getType());
	}

	public Person getPerson() {
		return (Person) this.getParentNode(RelationshipTypes.GENDER);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	@Override
	protected void resolveConcreteReferences() {
		return;
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Gender gedcomXGender = (org.gedcomx.conclusion.Gender) gedcomXObject;
		this.setType(gedcomXGender.getType());
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) {
		return;
	}

	public void setKnownType(final GenderType type) {
		this.setType(type.toQNameURI());
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		if (properties[0] instanceof URI) {
			this.setType((URI) properties[0]);
		} else if (properties[0] instanceof GenderType) {
			this.setKnownType((GenderType) properties[0]);
		}
	}

	@Deprecated
	public void setType(final URI type) {
		this.setProperty(GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getType())) {
			throw new MissingRequiredPropertyException(this.getAnnotatedNodeType(), GenericProperties.TYPE.toString());
		}
	}

}
