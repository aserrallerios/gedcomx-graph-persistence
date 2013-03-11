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

public class TextValue extends GENgraphNode {

	public TextValue(final Node node) throws WrongNodeType {
		super(NodeTypes.TEXT_VALUE, node);
	}

	public TextValue(final org.gedcomx.common.TextValue gedcomXTextValue) throws MissingFieldException {
		super(NodeTypes.TEXT_VALUE, gedcomXTextValue);
	}

	public TextValue(final String value) {
		super(NodeTypes.TEXT_VALUE, new Object[] { value });
	}

	@Override
	protected void deleteAllReferences() {
		return;
	}

	@Override
	protected org.gedcomx.common.TextValue getGedcomX() {
		final org.gedcomx.common.TextValue textValue = new org.gedcomx.common.TextValue();

		textValue.setLang(this.getLang());
		textValue.setValue(this.getValue());

		return textValue;
	}

	public String getLang() {
		return (String) this.getProperty(NodeProperties.Generic.LANG);
	}

	public GENgraphNode getParentNode() {
		// TODO
		return this.getNodeByRelationship(GENgraphNode.class, RelTypes.HAS_NAME, Direction.INCOMING);
	}

	public String getValue() {
		return (String) this.getProperty(NodeProperties.Generic.VALUE);
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.common.TextValue gedcomXName = (org.gedcomx.common.TextValue) gedcomXObject;
		this.setLang(gedcomXName.getLang());
		this.setValue(gedcomXName.getValue());
	}

	@Override
	protected void setGedcomXRelations(final Object gedcomXObject) {
		return;
	}

	public void setLang(final String lang) {
		this.setProperty(NodeProperties.Generic.LANG, lang);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setValue((String) properties[0]);
	}

	public void setValue(final String value) {
		this.setProperty(NodeProperties.Generic.VALUE, value);
	}

	@Override
	protected void validateGedcomXObject(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.common.TextValue gedcomXTextValue = (org.gedcomx.common.TextValue) gedcomXObject;
		if ((gedcomXTextValue.getValue() == null) || gedcomXTextValue.getValue().isEmpty()) {
			throw new MissingRequiredPropertyException(TextValue.class, NodeProperties.Generic.VALUE);
		}
	}

	@Override
	protected void validateUnderlyingNode() throws WrongNodeType {
		if ((this.getValue() == null) || this.getValue().isEmpty()) {
			throw new WrongNodeType();
		}
	}
}
