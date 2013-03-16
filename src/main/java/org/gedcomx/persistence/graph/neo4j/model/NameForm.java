package org.gedcomx.persistence.graph.neo4j.model;

import java.util.List;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.neo4j.graphdb.Node;

@NodeType("NAME_FORM")
public class NameForm extends NodeWrapper {

	public NameForm() throws MissingFieldException {
		super();
	}

	protected NameForm(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	public NameForm(final org.gedcomx.conclusion.NameForm gedcomXNameForm) throws MissingFieldException {
		super(gedcomXNameForm);
	}

	public void addNameParts(final NamePart namePart) {
		this.addRelationship(GENgraphRelTypes.HAS_NAME_PART, namePart);
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReferencedNodes(this.getNameParts());
	}

	public String getFullText() {
		return (String) this.getProperty(NodeProperties.Conclusion.FULL_TEXT);
	}

	@Override
	protected org.gedcomx.conclusion.NameForm getGedcomX() {
		final org.gedcomx.conclusion.NameForm gedcomXNameForm = new org.gedcomx.conclusion.NameForm();

		gedcomXNameForm.setFullText(this.getFullText());
		gedcomXNameForm.setLang(this.getLang());
		gedcomXNameForm.setParts(this.getGedcomXList(org.gedcomx.conclusion.NamePart.class, this.getNameParts()));

		return gedcomXNameForm;
	}

	public String getLang() {
		return (String) this.getProperty(NodeProperties.Generic.LANG);
	}

	public Name getName() {
		return (Name) this.getParentNode(GENgraphRelTypes.HAS_NAME_FORM);
	}

	public List<NamePart> getNameParts() {
		return this.getNodesByRelationship(NamePart.class, GENgraphRelTypes.HAS_NAME_PART);
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	public void setFullText(final String fullText) {
		this.setProperty(NodeProperties.Conclusion.FULL_TEXT, fullText);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.NameForm gedcomXNameForm = (org.gedcomx.conclusion.NameForm) gedcomXObject;

		this.setLang(gedcomXNameForm.getLang());
		this.setFullText(gedcomXNameForm.getFullText());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.NameForm gedcomXNameForm = (org.gedcomx.conclusion.NameForm) gedcomXObject;

		for (final org.gedcomx.conclusion.NamePart namePart : gedcomXNameForm.getParts()) {
			this.addNameParts(new NamePart(namePart));
		}
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		return;
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		return;
	}

}
