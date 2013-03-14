package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphRelTypes;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("TEXT_VALUE")
public class TextValue extends NodeWrapper {

	public TextValue(final Node node) throws WrongNodeType, MissingFieldException {
		super(node);
	}

	public TextValue(final org.gedcomx.common.TextValue gedcomXTextValue) throws MissingFieldException {
		super(gedcomXTextValue);
	}

	public TextValue(final String value) throws MissingFieldException {
		super(new Object[] { value });
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

	public NodeWrapper getParentNode() {
		return super.getParentNode(GENgraphRelTypes.HAS_NAME);
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
	protected void validateUnderlyingNode() throws MissingRequiredPropertyException {
		if (ValidationTools.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(TextValue.class, NodeProperties.Generic.VALUE);
		}
	}
}
