package neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.rest.graphdb.RestGraphDatabase;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateDBFactory {

	private static GraphDatabaseService graphDb = null;
	public static final String RESOURCES_CRAWL_DB = "/tmp/db";

	public static GraphDatabaseService createInMemoryDB() {
		if (null == graphDb) {
			graphDb = new RestGraphDatabase("app25869497.sb02.stations.graphenedb.com:24789", "app25869497", "RQMsA1NDgfePPrzZXPc7");
//			synchronized (GraphDatabaseService.class) {
//				if (null == graphDb) {
//					final Map<String, String> config = new HashMap<>();
//					config.put("neostore.nodestore.db.mapped_memory", "50M");
//					config.put("string_block_size", "60");
//					config.put("array_block_size", "300");
//					graphDb = new GraphDatabaseFactory()
//							.newEmbeddedDatabaseBuilder(RESOURCES_CRAWL_DB)
//							.setConfig(config).newGraphDatabase();
//
//					registerShutdownHook(graphDb);
//				}
//			}
		}
		return graphDb;
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}

	public static void clearDb() {
		try {
			if (graphDb != null) {
				graphDb.shutdown();
				graphDb = null;
			}
			FileUtils.deleteRecursively(new File(RESOURCES_CRAWL_DB));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

}
