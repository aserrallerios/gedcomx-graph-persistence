package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.NOTE)
public class Note extends NodeWrapper {

	protected Note(final Node node) {
		super(node);
	}

	protected Note(final org.gedcomx.common.Note gedcomXNote) {
		super(gedcomXNote);
	}

	protected Note(final String text) {
		super(new Object[] { text });
	}

	@Override
	protected void deleteAllReferences() {
		this.getAttribution().delete();
	}

	public Attribution getAttribution() {
		return NodeWrapper.nodeWrapperOperations.getNodeByRelationship(this,
				Attribution.class, RelationshipTypes.ATTRIBUTION);
	}

	@Override
	public org.gedcomx.common.Note getGedcomX() {
		final org.gedcomx.common.Note gedcomXNote = new org.gedcomx.common.Note();

		gedcomXNote.setText(this.getText());
		gedcomXNote.setSubject(this.getSubject());
		gedcomXNote.setLang(this.getLang());
		gedcomXNote.setAttribution(this.getAttribution().getGedcomX());

		return gedcomXNote;
	}

	public String getId() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.ID);
	}

	public String getLang() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.LANG);
	}

	@Override
	public NodeWrapper getParentNode() {
		return NodeWrapper.nodeWrapperOperations.getParentNode(this,
				RelationshipTypes.HAS_NOTE);
	}

	public String getSubject() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.SUBJECT);
	}

	public String getText() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				GenericProperties.TEXT);
	}

	@Override
	public void resolveReferences() {
		return;
	}

	public Attribution setAttribution() {
		return this.setAttribution(new Attribution());
	}

	private Attribution setAttribution(final Attribution attribution) {
		NodeWrapper.nodeWrapperOperations.createRelationship(this,
				RelationshipTypes.ATTRIBUTION, attribution);
		return attribution;
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.common.Note gedcomXNote = (org.gedcomx.common.Note) gedcomXObject;

		this.setId(gedcomXNote.getId());
		this.setLang(gedcomXNote.getLang());
		this.setSubject(gedcomXNote.getSubject());
		this.setText(gedcomXNote.getText());

	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.common.Note gedcomXNote = (org.gedcomx.common.Note) gedcomXObject;

		if (gedcomXNote.getAttribution() != null) {
			this.setAttribution(new Attribution(gedcomXNote.getAttribution()));
		}
	}

	public void setId(final String id) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.ID, id);
	}

	public void setLang(final String lang) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.LANG, lang);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setText((String) properties[0]);
	}

	public void setSubject(final String subject) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.SUBJECT, subject);
	}

	public void setText(final String text) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.TEXT, text);
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getText())) {
			throw new MissingRequiredPropertyException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					this.getId(), GenericProperties.TEXT.toString());
		}
	}

}
