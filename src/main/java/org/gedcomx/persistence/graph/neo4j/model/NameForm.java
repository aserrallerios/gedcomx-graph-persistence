package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.NAME_FORM)
public class NameForm extends NodeWrapper {

	protected NameForm() {
		super();
	}

	protected NameForm(final Node node) {
		super(node);
	}

	protected NameForm(final org.gedcomx.conclusion.NameForm gedcomXNameForm) {
		super(gedcomXNameForm);
	}

	public void addNameParts(final NamePart namePart) {
		NodeWrapper.nodeWrapperOperations.addRelationship(this,
				RelationshipTypes.HAS_NAME_PART, namePart);
	}

	@Override
	protected void deleteAllReferences() {
		NodeWrapper.nodeWrapperOperations.deleteReferencedNodes(this
				.getNameParts());
	}

	public String getFullText() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.FULL_TEXT);
	}

	@Override
	public org.gedcomx.conclusion.NameForm getGedcomX() {
		final org.gedcomx.conclusion.NameForm gedcomXNameForm = new org.gedcomx.conclusion.NameForm();

		gedcomXNameForm.setFullText(this.getFullText());
		gedcomXNameForm.setLang(this.getLang());
		gedcomXNameForm.setParts(NodeWrapper.nodeWrapperOperations
				.getGedcomXList(org.gedcomx.conclusion.NamePart.class,
						this.getNameParts()));

		return gedcomXNameForm;
	}

	public String getLang() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.LANG);
	}

	public Name getName() {
		return (Name) NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.HAS_NAME_FORM);
	}

	public List<NamePart> getNameParts() {
		return NodeWrapper.nodeWrapperOperations.getNodesByRelationship(this,
				NamePart.class, RelationshipTypes.HAS_NAME_PART);
	}

	@Override
	public void resolveReferences() {
		return;
	}

	public void setFullText(final String fullText) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.FULL_TEXT, fullText);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.NameForm gedcomXNameForm = (org.gedcomx.conclusion.NameForm) gedcomXObject;

		this.setLang(gedcomXNameForm.getLang());
		this.setFullText(gedcomXNameForm.getFullText());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.conclusion.NameForm gedcomXNameForm = (org.gedcomx.conclusion.NameForm) gedcomXObject;

		if (gedcomXNameForm.getParts() != null) {
			for (final org.gedcomx.conclusion.NamePart namePart : gedcomXNameForm
					.getParts()) {
				this.addNameParts(new NamePart(namePart));
			}
		}
	}

	public void setLang(final String lang) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.LANG, lang);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		return;
	}

	@Override
	protected void validateUnderlyingNode() {
		return;
	}

}
