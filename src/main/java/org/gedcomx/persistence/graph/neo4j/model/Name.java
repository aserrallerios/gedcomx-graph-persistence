package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.NameType;
import org.neo4j.graphdb.Node;

@NodeType("NAME")
public class Name extends Conclusion {

	public Name(final NameForm nameForm) throws MissingFieldException {
		super(new Object[] { nameForm });
	}

	public Name(final Node node) throws MissingFieldException, UnknownNodeType {
		super(node);
	}

	protected Name(final org.gedcomx.conclusion.Name gedcomXName) throws MissingFieldException {
		super(gedcomXName);
	}

	public void addNameForms(final NameForm nameForm) {
		this.addRelationship(RelationshipTypes.HAS_NAME_FORM, nameForm);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReferencedNodes(this.getNameForms());
	}

	public String getDateFormal() {
		return (String) this.getProperty(ConclusionProperties.DATE_FORMAL);
	}

	public String getDateOriginal() {
		return (String) this.getProperty(ConclusionProperties.DATE_ORIGINAL);
	}

	@Override
	public org.gedcomx.conclusion.Name getGedcomX() {
		final org.gedcomx.conclusion.Name gedcomXName = new org.gedcomx.conclusion.Name();

		super.getGedcomXConclusion(gedcomXName);

		final org.gedcomx.conclusion.Date date = new org.gedcomx.conclusion.Date();
		date.setFormal(this.getDateFormal());
		date.setOriginal(this.getDateOriginal());
		gedcomXName.setDate(date);

		gedcomXName.setKnownType(this.getKnownType());
		gedcomXName.setType(this.getType());
		gedcomXName.setPreferred(this.getPreferred());

		gedcomXName.setNameForms(this.getGedcomXList(org.gedcomx.conclusion.NameForm.class, this.getNameForms()));

		return gedcomXName;
	}

	public NameType getKnownType() {
		return NameType.fromQNameURI(this.getType());
	}

	public List<NameForm> getNameForms() {
		return this.getNodesByRelationship(NameForm.class, RelationshipTypes.HAS_NAME_FORM);
	}

	public Person getPerson() {
		return (Person) this.getParentNode(RelationshipTypes.HAS_NAME);
	}

	public Boolean getPreferred() {
		return (Boolean) this.getProperty(ConclusionProperties.PREFERRED);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) this.getProperty(GenericProperties.TYPE));
	}

	@Override
	protected void resolveConcreteReferences() {
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
		final org.gedcomx.conclusion.Name gedcomXName = (org.gedcomx.conclusion.Name) gedcomXObject;
		this.setPreferred(gedcomXName.getPreferred());
		this.setType(gedcomXName.getType());

		if (gedcomXName.getDate() != null) {
			this.setDateFormal(gedcomXName.getDate().getFormal());
			this.setDateOriginal(gedcomXName.getDate().getOriginal());
		}
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Name gedcomXName = (org.gedcomx.conclusion.Name) gedcomXObject;

		for (final org.gedcomx.conclusion.NameForm nameForm : gedcomXName.getNameForms()) {
			this.addNameForms(new NameForm(nameForm));
		}
	}

	public void setKnownType(final NameType type) {
		this.setType(type.toQNameURI());
	}

	public void setPreferred(final Boolean preferred) {
		this.setProperty(ConclusionProperties.PREFERRED, preferred);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.addNameForms((NameForm) properties[0]);
	}

	@Deprecated
	public void setType(final URI type) {
		this.setProperty(GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getNameForms())) {
			throw new MissingRequiredRelationshipException(this.getAnnotatedNodeType(), this.getId(),
					RelationshipTypes.HAS_NAME_FORM.toString());
		}
	}
}
