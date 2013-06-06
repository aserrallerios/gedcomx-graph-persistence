package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exceptions.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.model.constants.RelationshipTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.TEXT_VALUE)
public class TextValue extends NodeWrapper {

    protected TextValue(final Node node) throws UnknownNodeType,
            MissingFieldException {
        super(node);
    }

    protected TextValue(final org.gedcomx.common.TextValue gedcomXTextValue)
            throws MissingFieldException {
        super(gedcomXTextValue);
    }

    protected TextValue(final String value) throws MissingFieldException {
        super(new Object[] { value });
    }

    @Override
    protected void deleteAllReferences() {
        return;
    }

    @Override
    public org.gedcomx.common.TextValue getGedcomX() {
        final org.gedcomx.common.TextValue textValue = new org.gedcomx.common.TextValue();

        textValue.setLang(this.getLang());
        textValue.setValue(this.getValue());

        return textValue;
    }

    public String getLang() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                GenericProperties.LANG);
    }

    public NodeWrapper getParentNode() {
        return NodeWrapper.nodeWrapperOperations.getParentNode(this,
                RelationshipTypes.HAS_NAME);
    }

    public String getValue() {
        return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
                GenericProperties.VALUE);
    }

    @Override
    public void resolveReferences() {
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
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                GenericProperties.LANG, lang);
    }

    @Override
    protected void setRequiredProperties(final Object... properties) {
        this.setValue((String) properties[0]);
    }

    public void setValue(final String value) {
        NodeWrapper.nodeWrapperOperations.setProperty(this,
                GenericProperties.VALUE, value);
    }

    @Override
    protected void validateUnderlyingNode()
            throws MissingRequiredPropertyException {
        if (Validation.nullOrEmpty(this.getValue())) {
            throw new MissingRequiredPropertyException(
                    NodeWrapper.nodeWrapperOperations
                            .getAnnotatedNodeType(this),
                    GenericProperties.VALUE.toString());
        }
    }
}
