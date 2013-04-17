package org.gedcomx.persistence.graph.neo4j.model;

import org.codehaus.enunciate.XmlQNameEnumUtil;
import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.UnknownNodeType;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.neo4j.graphdb.Node;

@NodeType(NodeTypes.QUALIFIER)
public class Qualifier extends NodeWrapper {

    public Qualifier() throws MissingFieldException, UnknownNodeType {
        super();
    }

    protected Qualifier(final Node node) throws MissingFieldException,
            UnknownNodeType {
        super(node);
    }

    public Qualifier(final org.gedcomx.common.Qualifier gedcomXQualifier)
            throws MissingFieldException, UnknownNodeType {
        super(gedcomXQualifier);
    }

    @Override
    protected void deleteAllReferences() {
        return;
    }

    @Override
    public org.gedcomx.common.Qualifier getGedcomX() {
        final org.gedcomx.common.Qualifier gedcomXQualifier = new org.gedcomx.common.Qualifier();
        gedcomXQualifier.setName(this.getName());
        gedcomXQualifier.setValue(this.getValue());

        return gedcomXQualifier;
    }

    public URI getName() {
        return new URI((String) this.getProperty(GenericProperties.NAME));
    }

    public <E extends Enum> E getName(final Class<E> vocabulary) {
        final URI name = this.getName();
        return name == null ? null : (E) XmlQNameEnumUtil.fromURI(name.toURI(),
                vocabulary);
    }

    public String getValue() {
        return (String) this.getProperty(GenericProperties.VALUE);
    }

    @Override
    public void resolveReferences() {
        return;
    }

    @Override
    protected void setGedcomXProperties(final Object gedcomXObject) {
        final org.gedcomx.common.Qualifier gedcomXQualifier = (org.gedcomx.common.Qualifier) gedcomXObject;

        this.setName(gedcomXQualifier.getName());
        this.setValue(gedcomXQualifier.getValue());
    }

    @Override
    protected void setGedcomXRelations(final Object gedcomXObject)
            throws MissingFieldException {
        return;
    }

    public void setName(final Enum<?> element) {
        this.setProperty(GenericProperties.NAME, element == null ? null
                : new URI(XmlQNameEnumUtil.toURI(element).toString()));
    }

    @Deprecated
    public void setName(final URI name) {
        this.setProperty(GenericProperties.NAME, name);
    }

    @Override
    protected void setRequiredProperties(final Object... properties)
            throws MissingFieldException {
        return;
    }

    public void setValue(final String value) {
        this.setProperty(GenericProperties.VALUE, value);
    }

    @Override
    protected void validateUnderlyingNode() throws MissingFieldException {
        return;
    }

}
