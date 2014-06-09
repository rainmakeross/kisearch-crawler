package neo4j;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.rest.graphdb.RestGraphDatabase;

public class CreateDBFactory {

	private static GraphDatabaseService graphDb = null;
	public static final String RESOURCES_CRAWL_DB = "/tmp/db";

	public static GraphDatabaseService createInMemoryDB() {
		if (null == graphDb) {
			
			String neoUrl = System.getProperty("neo4j.url");
			String neoDb = System.getProperty("neo4j.db");
			
			synchronized (GraphDatabaseService.class) {
//				graphDb = new RestGraphDatabase(neoUrl + neoDb);
				graphDb = createInMemoryDB();
				
				registerShutdownHook(graphDb);
			}
		}
		return graphDb;
	}
	
	private GraphDatabaseService createInMemory() {
		
		GraphDatabaseService graphDb = new GraphDatabaseFactory()
	    .newEmbeddedDatabaseBuilder( RESOURCES_CRAWL_DB )
	    .setConfig( GraphDatabaseSettings.nodestore_mapped_memory_size, "10M" )
	    .setConfig( GraphDatabaseSettings.string_block_size, "60" )
	    .setConfig( GraphDatabaseSettings.array_block_size, "300" )
	    .newGraphDatabase();

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
