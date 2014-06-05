package neo4j;

import org.graphstream.algorithm.PageRank;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

public class PageRankTask extends SearchTask implements Callable<TaskResponse> {

	public PageRankTask(final String... terms) {
		super(terms);
	}

	@Override
	protected ExecutionResult executeQuery(final String... words) {
		final ExecutionEngine engine = new ExecutionEngine(graphDb);
		final StringBuilder bob = new StringBuilder(
				"START page=node(*) MATCH (page)-[:CONTAINS]->words ");
		bob.append(", (page)-[:LINK_TO]->related ");
		bob.append("WHERE words.word in [");
		bob.append(formatArray(words));
		bob.append("] ");
		bob.append("RETURN DISTINCT page, related");

		return engine.execute(bob.toString());
	}

	public TaskResponse call() {
		final ExecutionResult result = executeQuery(searchTerms);
		final Map<String, Double> returnMap = convertToUrlTotalWords(result);

		final TaskResponse response = new TaskResponse();
		response.taskClazz = this.getClass();
//		response.resultMap = NormalizationFunctions.normalizeMap(returnMap,
//				true);
		
		response.resultMap = returnMap;
		return response;
	}

	private Map<String, Double> convertToUrlTotalWords(
			final ExecutionResult result) {
		final Map<String, Double> uniqueUrls = new HashMap<>();

		final Graph g = new SingleGraph("rank", false, true);
		final Iterator<Node> pageIterator = result.columnAs("related");

		while (pageIterator.hasNext()) {
			final Node node = pageIterator.next();
			final Iterator<Relationship> relationshipIterator = node
					.getRelationships().iterator();
			while (relationshipIterator.hasNext()) {

				final Relationship relationship = relationshipIterator.next();
				final String source = relationship.getProperty(
						NodeConstants.SOURCE).toString();
				uniqueUrls.put(source, 0.0);
				final String destination = relationship.getProperty(
						NodeConstants.DESTINATION).toString();
				g.addEdge(String.valueOf(node.getId()), source, destination,
						true);

			}
		}

		computeAndSetPageRankScores(uniqueUrls, g);
		return uniqueUrls;
	}

	/**
	 * Compute score
	 *
	 * @param uniqueUrls
	 *            urls
	 * @param graph
	 *            the graph of all links
	 */
	private void computeAndSetPageRankScores(
			final Map<String, Double> uniqueUrls, final Graph graph) {
		final PageRank pr = new PageRank();
		pr.init(graph);
		pr.compute();

		for (final Map.Entry<String, Double> entry : uniqueUrls.entrySet()) {
			final double score = 100 * pr
					.getRank(graph.getNode(entry.getKey()));
			entry.setValue(score);
		}
	}

}
