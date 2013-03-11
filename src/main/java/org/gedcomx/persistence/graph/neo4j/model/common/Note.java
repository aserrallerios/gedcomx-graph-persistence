package org.gedcomx.persistence.graph.neo4j.model.common;

import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.model.GENgraphNode;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.RelTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

public class Note extends GENgraphNode {

	protected Note(final Node node) throws WrongNodeType {
		super(NodeTypes.NOTE, node);
	}

	public Note(final org.gedcomx.common.Note gedcomXNote) throws MissingFieldException {
		super(NodeTypes.NOTE, gedcomXNote);
	}

	public Note(final String text) {
		super(NodeTypes.NOTE, new Object[] { text });
	}

	@Override
	protected void deleteAllReferences() {
		this.deleteReferencedNode(Attribution.class, RelTypes.ATTRIBUTION);
	}

	public Attribution getAttribution() {
		return this.getNodeByRelationship(Attribution.class, RelTypes.ATTRIBUTION);
	}

	@Override
	protected org.gedcomx.common.Note getGedcomX() {
		final org.gedcomx.common.Note gedcomXNote = new org.gedcomx.common.Note();

		gedcomXNote.setText(this.getText());
		gedcomXNote.setSubject(this.getSubject());
		gedcomXNote.setLang(this.getLang());
		gedcomXNote.setAttribution(this.getAttribution().getGedcomX());

		return gedcomXNote;
	}

	public String getLang() {
		return (String) this.getProperty(NodeProperties.Generic.LANG);
	}

	public GENgraphNode getParentNode() {
		// TODO
		return this.getNodeByRelationship(GENgraphNode.class, RelTypes.HAS_NOTE, Direction.INCOMING);
	}

	public String getSubject() {
		return (String) this.getProperty(NodeProperties.Generic.SUBJECT);
	}

	public String getText() {
		return (String) this.getProperty(NodeProperties.Generic.TEXT);
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	public void setAttribution(final Attribution attribution) {
		this.createRelationship(RelTypes.ATTRIBUTION, attribution);
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.common.Note gedcomXNote = (org.gedcomx.common.Note) gedcomXObject;

		this.setLang(gedcomXNote.getLang());
		this.setSubject(gedcomXNote.getSubject());
		this.setText(gedcomXNote.getText());

	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		final org.gedcomx.common.Note gedcomXNote = (org.gedcomx.common.Note) gedcomXObject;

		this.setAttribution(this.createNode(Attribution.class, gedcomXNote.getAttribution()));
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setText((String) properties[0]);
	}

	public void setSubject(final String subject) {
		this.setProperty(NodeProperties.Generic.SUBJECT, subject);
	}

	public void setText(final String text) {
		this.setProperty(NodeProperties.Generic.TEXT, text);
	}

	@Override
	protected void validateGedcomXObject(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.common.Note gedcomXNote = (org.gedcomx.common.Note) gedcomXObject;

		if ((gedcomXNote.getText() == null) || gedcomXNote.getText().isEmpty()) {
			throw new MissingRequiredPropertyException(Note.class, gedcomXNote.getId(), NodeProperties.Generic.TEXT);
		}
	}

	@Override
	protected void validateUnderlyingNode() throws WrongNodeType {
		if ((this.getText() == null) || this.getText().isEmpty()) {
			throw new WrongNodeType();
		}
	}

}
