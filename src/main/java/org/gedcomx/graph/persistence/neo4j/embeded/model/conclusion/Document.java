package org.gedcomx.graph.persistence.neo4j.embeded.model.conclusion;

import org.gedcomx.common.URI;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingFieldException;
import org.gedcomx.graph.persistence.neo4j.embeded.exception.MissingRequiredPropertyException;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraph;
import org.gedcomx.graph.persistence.neo4j.embeded.model.GENgraphTopLevelNode;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeProperties;
import org.gedcomx.graph.persistence.neo4j.embeded.utils.NodeTypes;

public class Document extends ConclusionSubnode implements GENgraphTopLevelNode {

	protected Document(final GENgraph graph, final org.gedcomx.conclusion.Document gedcomXDocument) throws MissingFieldException {
		super(graph, NodeTypes.DOCUMENT, gedcomXDocument);
	}

	@Override
	protected void checkRequiredProperties(final Object gedcomXObject) throws MissingFieldException {
		final org.gedcomx.conclusion.Document gedcomXDocument = (org.gedcomx.conclusion.Document) gedcomXObject;
		if ((gedcomXDocument.getText() == null) || gedcomXDocument.getText().isEmpty()) {
			throw new MissingRequiredPropertyException(Document.class, NodeProperties.Conclusion.TEXT);
		}
	}

	public String getText() {
		return (String) this.getProperty(NodeProperties.Conclusion.TEXT);
	}

	public URI getType() {
		return new URI((String) this.getProperty(NodeProperties.Generic.TYPE));
	}

	@Override
	protected void setGedcomXProperties(final Object gedcomXObject) {
		final org.gedcomx.conclusion.Document gedcomXDocument = (org.gedcomx.conclusion.Document) gedcomXObject;
		this.setType(gedcomXDocument.getType());
		this.setText(gedcomXDocument.getText());
	}

	public void setText(final String text) {
		this.setProperty(NodeProperties.Conclusion.TEXT, text);
	}

	public void setType(final URI type) {
		this.setProperty(NodeProperties.Generic.TYPE, type.toString());
	}

}
