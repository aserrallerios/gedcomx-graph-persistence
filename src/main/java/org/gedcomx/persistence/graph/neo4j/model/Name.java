package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.NameType;
import org.neo4j.graphdb.Node;

@NodeType("NAME")
public class Name extends Conclusion {

	public Name() throws MissingFieldException {
		super();
	}

	public Name(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	protected Name(final org.gedcomx.conclusion.Name gedcomXName) throws MissingFieldException {
		super(gedcomXName);
	}

	public void addNameForms(final NameForm nameForms) {
		this.addRelationship(GENgraphRelTypes.HAS_NAME_FORM, nameForms);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		this.deleteReferencedNodes(this.getNameForms());
	}

	public String getDateFormal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_FORMAL);
	}

	public String getDateOriginal() {
		return (String) this.getProperty(NodeProperties.Conclusion.DATE_ORIGINAL);
	}

	@Override
	protected org.gedcomx.conclusion.Name getGedcomX() {
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
		return this.getNodesByRelationship(NameForm.class, GENgraphRelTypes.HAS_NAME_FORM);
	}

	public Person getPerson() {
		return (Person) this.getParentNode(GENgraphRelTypes.HAS_NAME);
	}

	public Boolean getPreferred() {
		return (Boolean) this.getProperty(NodeProperties.Conclusion.PREFERRED);
	}

	public URI getType() {
		final String type = (String) this.getProperty(NodeProperties.Generic.TYPE);
		return new URI(type);
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	public void setDateFormal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_FORMAL, value);
	}

	public void setDateOriginal(final String value) {
		this.setProperty(NodeProperties.Conclusion.DATE_ORIGINAL, value);
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
		this.setProperty(NodeProperties.Conclusion.PREFERRED, preferred);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.addNameForms(new NameForm());
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getNameForms())) {
			throw new MissingRequiredRelationshipException(Name.class, this.getId(), GENgraphRelTypes.HAS_NAME_FORM);
		}
	}
}
