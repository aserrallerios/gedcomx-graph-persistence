package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.common.URI;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.model.constants.ConclusionProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.utils.Validation;
import org.gedcomx.types.DocumentType;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

@NodeType(NodeTypes.DOCUMENT)
public class Document extends Conclusion {

	protected Document(final Node node) {
		super(node);
	}

	@Inject
	protected Document(
			final @Assisted org.gedcomx.conclusion.Document gedcomXDocument) {
		super(gedcomXDocument);
	}

	protected Document(final String text) {
		super(text);
	}

	@Override
	protected void deleteAllConcreteReferences() {
		return;
	}

	@Override
	public org.gedcomx.conclusion.Document getGedcomX() {
		final org.gedcomx.conclusion.Document gedcomXDocument = new org.gedcomx.conclusion.Document();

		gedcomXDocument.setText(this.getText());
		gedcomXDocument.setType(this.getType());
		gedcomXDocument.setKnownType(this.getKnownType());

		return gedcomXDocument;
	}

	public DocumentType getKnownType() {
		return DocumentType.fromQNameURI(this.getType());
	}

	@Override
	public NodeWrapper getParentNode() {
		return null;
	}

	public String getText() {
		return (String) NodeWrapper.nodeWrapperOperations.getProperty(this,
				ConclusionProperties.TEXT);
	}

	@Deprecated
	public URI getType() {
		return new URI((String) NodeWrapper.nodeWrapperOperations.getProperty(
				this, GenericProperties.TYPE));
	}

	@Override
	protected void resolveConcreteReferences() {
		return;
	}

	@Override
	protected void setGedcomXConcreteProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Document gedcomXDocument = (org.gedcomx.conclusion.Document) gedcomXObject;
		this.setType(gedcomXDocument.getType());
		this.setText(gedcomXDocument.getText());
	}

	@Override
	protected void setGedcomXConcreteRelations(final Object gedcomXObject) {
		return;
	}

	public void setKnownType(final DocumentType type) {
		this.setType(type.toQNameURI());
	}

	@Override
	protected void setRequiredProperties(final Object... properties) {
		this.setText((String) properties[0]);
	}

	public void setText(final String text) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				ConclusionProperties.TEXT, text);
	}

	@Deprecated
	public void setType(final URI type) {
		NodeWrapper.nodeWrapperOperations.setProperty(this,
				GenericProperties.TYPE, type);
	}

	@Override
	protected void validateUnderlyingNode() {
		if (Validation.nullOrEmpty(this.getText())) {
			throw new MissingRequiredPropertyException(
					NodeWrapper.nodeWrapperOperations
							.getAnnotatedNodeType(this),
					ConclusionProperties.TEXT.toString());
		}
	}
}
