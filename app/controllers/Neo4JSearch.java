package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import neo4j.PageRankTask;
import neo4j.TaskResponse;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * Neo4JSearch extends Play Controller to invoke the search service.
 * 
 * @author kong on 08/06/2014
 *
 */
public class Neo4JSearch extends Controller {

	public static Result search() throws InterruptedException {

		final ExecutorService executorService = Executors.newFixedThreadPool(4);
		final String[] searchTerms = { "java", "spring" };

		List<Callable<TaskResponse>> tasks = new ArrayList<>();
		// tasks.add(new WordFrequencyTask(searchTerms));
		// tasks.add(new DocumentLocationTask(searchTerms));
		tasks.add(new PageRankTask(searchTerms));
		// tasks.add(new NeuralNetworkTask(searchTerms));

		final List<Future<TaskResponse>> results = executorService
				.invokeAll(tasks);

		return ok(index.render("Your new application is ready."));
	}

}
