package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exception.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exception.WrongNodeType;
import org.gedcomx.persistence.graph.neo4j.utils.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.utils.ValidationTools;
import org.gedcomx.types.DocumentType;
import org.neo4j.graphdb.Node;

@NodeType("DOCUMENT")
public class Document extends Conclusion {

	protected Document(final Node node) throws MissingFieldException, WrongNodeType {
		super(node);
	}

	public Document(final org.gedcomx.conclusion.Document gedcomXDocument) throws MissingFieldException {
		super(gedcomXDocument);
	}

	public Document(final String text) throws MissingFieldException {
		super(text);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		return;
	}

	@Override
	protected org.gedcomx.conclusion.Document getGedcomX() {
		final org.gedcomx.conclusion.Document gedcomXDocument = new org.gedcomx.conclusion.Document();

		gedcomXDocument.setText(this.getText());
		gedcomXDocument.setType(this.getType());
		gedcomXDocument.setKnownType(this.getKnownType());

		return gedcomXDocument;
	}

	public DocumentType getKnownType() {
		return DocumentType.fromQNameURI(this.getType());
	}

	public String getText() {
		return (String) this.getProperty(NodeProperties.Conclusion.TEXT);
	}

	public URI getType() {
		return new URI((String) this.getProperty(NodeProperties.Generic.TYPE));
	}

	@Override
	protected void resolveReferences() {
		return;
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Document gedcomXDocument = (org.gedcomx.conclusion.Document) gedcomXObject;
		this.setType(gedcomXDocument.getType());
		this.setText(gedcomXDocument.getText());
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) throws MissingFieldException {
		return;
	}

	public void setKnownType(final DocumentType type) {
		this.setType(type.toQNameURI());
	}

	@Override
	protected void setRequiredProperties(final Object... properties) throws MissingFieldException {
		this.setText((String) properties[0]);
	}

	public void setText(final String text) {
		this.setProperty(NodeProperties.Conclusion.TEXT, text);
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

	@Override
	protected void validateUnderlyingNode() throws MissingFieldException {
		if (ValidationTools.nullOrEmpty(this.getText())) {
			throw new MissingRequiredPropertyException(Document.class, NodeProperties.Conclusion.TEXT);
		}
	}
}
