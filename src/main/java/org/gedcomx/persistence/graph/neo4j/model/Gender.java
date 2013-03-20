package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.types.GenderType;
import org.neo4j.graphdb.Node;

@NodeType("GENDER")
public class Gender extends Conclusion {

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
	protected org.gedcomx.conclusion.Gender getGedcomX() {
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
		return (Person) this.getParentNode(GENgraphRelTypes.GENDER);
	}

	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	@Override
	protected void resolveReferences() {
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
		this.setType((URI) properties[0]);
	}

	public void setType(final URI type) {
		this.setProperty(GenericProperties.TYPE, type.toString());
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (this.getType() == null) {
			throw new MissingRequiredPropertyException(Gender.class, GenericProperties.TYPE);
		}
	}

}
