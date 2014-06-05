package neo4j;

import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;

public abstract class SearchTask {

	protected String searchTerms;
	protected static GraphDatabaseService graphDb;

	public SearchTask(final String... terms) {
		searchTerms = formatArray(terms).toString();
		
		graphDb = CreateDBFactory.createInMemoryDB();
	}

	protected abstract ExecutionResult executeQuery(final String... words);

	protected String formatArray(String[] words) {
		StringBuilder s = new StringBuilder();
		
		for (String word : words) {
			s.append(word);
		}
		return s.toString();
	}

}
