package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.gedcomx.types.NameType;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.NAME)
public class Name extends Conclusion {

	protected Name() {
		super();
	}

	protected Name(final Node node) {
		super(node);
	}

	protected Name(final org.gedcomx.conclusion.Name gedcomXName) {
		super(gedcomXName);
	}

	public NameForm addNameForm() {
		return this.addNameForm(new NameForm());
	}

	private NameForm addNameForm(final NameForm nameForm) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_NAME_FORM, nameForm);
		return nameForm;
	}

	@Override
	protected void deleteAllConcreteReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getNameForms());
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

		gedcomXName.setNameForms(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.conclusion.NameForm.class,
						this.getNameForms()));

		return gedcomXName;
	}

	public NameType getKnownType() {
		return NameType.fromQNameURI(this.getType());
	}

	public List<NameForm> getNameForms() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				NameForm.class, RelationshipTypes.HAS_NAME_FORM);
	}

	@Override
	public Person getParentNode() {
		return this.getPerson();
	}

	public Person getPerson() {
		return (Person) NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.HAS_NAME);
	}

	public Boolean getPreferred() {
		return (Boolean) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.PREFERRED);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.TYPE));
	}

	@Override
	protected void resolveConcreteReferences() {
		return;
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
		final org.gedcomx.conclusion.Name gedcomXName = (org.gedcomx.conclusion.Name) gedcomXObject;
		this.setPreferred(gedcomXName.getPreferred());
		this.setType(gedcomXName.getType());

		if (gedcomXName.getDate() != null) {
			this.setDateFormal(gedcomXName.getDate().getFormal());
			this.setDateOriginal(gedcomXName.getDate().getOriginal());
		}
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Name gedcomXName = (org.gedcomx.conclusion.Name) gedcomXObject;

		for (final org.gedcomx.conclusion.NameForm nameForm : gedcomXName
				.getNameForms()) {
			this.addNameForm(new NameForm(nameForm));
		}
	}

	public void setKnownType(final NameType type) {
		this.setType(type.toQNameURI());
	}

	public void setPreferred(final Boolean preferred) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.PREFERRED, preferred);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.addNameForm();
	}

	@Deprecated
	public void setType(final URI type) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getNameForms())) {
			throw new MissingRequiredRelationshipException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					this.getId(), RelationshipTypes.HAS_NAME_FORM.toString());
		}
	}
}
