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

@NodeType(NodeTypes.NOTE)
public class Note extends NodeWrapper {

    protected Note(final Node node) throws UnknownNodeType,
            MissingFieldException {
        super(node);
    }

    public Note(final org.gedcomx.common.Note gedcomXNote)
            throws MissingFieldException {
        super(gedcomXNote);
    }

    public Note(final String text) throws MissingFieldException {
        super(new Object[] { text });
    }

    @Override
    protected void deleteAllReferences() {
        this.deleteReferencedNode(this.getAttribution());
    }

    public Attribution getAttribution() {
        return this.getNodeByRelationship(Attribution.class,
                RelationshipTypes.ATTRIBUTION);
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
        return (String) this.getProperty(GenericProperties.ID);
    }

    public String getLang() {
        return (String) this.getProperty(GenericProperties.LANG);
    }

    public NodeWrapper getParentNode() {
        return super.getParentNode(RelationshipTypes.HAS_NOTE);
    }

    public String getSubject() {
        return (String) this.getProperty(GenericProperties.SUBJECT);
    }

    public String getText() {
        return (String) this.getProperty(GenericProperties.TEXT);
    }

    @Override
    public void resolveReferences() {
        return;
    }

    public void setAttribution(final Attribution attribution) {
        this.createRelationship(RelationshipTypes.ATTRIBUTION, attribution);
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
    protected void setGedcomXRelations(final Object gedcomXObject)
            throws MissingFieldException {
        final org.gedcomx.common.Note gedcomXNote = (org.gedcomx.common.Note) gedcomXObject;

        if (gedcomXNote.getAttribution() != null) {
            this.setAttribution(new Attribution(gedcomXNote.getAttribution()));
        }
    }

    public void setId(final String id) {
        this.setProperty(GenericProperties.ID, id);
    }

    public void setLang(final String lang) {
        this.setProperty(GenericProperties.LANG, lang);
    }

    @Override
    protected void setRequiredProperties(final Object... properties) {
        this.setText((String) properties[0]);
    }

    public void setSubject(final String subject) {
        this.setProperty(GenericProperties.SUBJECT, subject);
    }

    public void setText(final String text) {
        this.setProperty(GenericProperties.TEXT, text);
    }

    @Override
    protected void validateUnderlyingNode()
            throws MissingRequiredPropertyException {
        if (Validation.nullOrEmpty(this.getText())) {
            throw new MissingRequiredPropertyException(
                    this.getAnnotatedNodeType(), this.getId(),
                    GenericProperties.TEXT.toString());
        }
    }

}
