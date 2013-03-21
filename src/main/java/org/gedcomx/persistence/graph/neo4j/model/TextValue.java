package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.neo4j.graphdb.Node;

@NodeType("TEXT_VALUE")
public class TextValue extends NodeWrapper {

	public TextValue(final Node node) throws UnknownNodeType, MissingFieldException {
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
		return (String) this.getProperty(GenericProperties.LANG);
	}

	public NodeWrapper getParentNode() {
		return super.getParentNode(RelTypes.HAS_NAME);
	}

	public String getValue() {
		return (String) this.getProperty(GenericProperties.VALUE);
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
		this.setProperty(GenericProperties.LANG, lang);
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setValue((String) properties[0]);
	}

	public void setValue(final String value) {
		this.setProperty(GenericProperties.VALUE, value);
	}

	@Override
	protected void validateUnderlyingNode() throws MissingRequiredPropertyException {
		if (ValidationTools.nullOrEmpty(this.getValue())) {
			throw new MissingRequiredPropertyException(this.getAnnotatedNodeType(), GenericProperties.VALUE.toString());
		}
	}
}
