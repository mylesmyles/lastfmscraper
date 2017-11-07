package net.mylesputnam.lastfm.scraper.db.requests.update;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.mylesputnam.lastfm.scraper.db.ScraperDatabaseConnector;

@Singleton
public class DbUpdateRequestScheduler {
	private final ExecutorService updateRequests;
	private final ScraperDatabaseConnector dbConnector;
	
	@Inject
	public DbUpdateRequestScheduler(ScraperDatabaseConnector dbConnector) {
		updateRequests = Executors.newFixedThreadPool(3);
		this.dbConnector = dbConnector;
	}
	
	public void scheduleUpdates(LastFmRecordUpdates recordUpdates) {
		updateRequests.execute(new UpdateTask(recordUpdates));
	}
	
	private class UpdateTask implements Runnable {
		private final LastFmRecordUpdates updates;
		public UpdateTask(LastFmRecordUpdates updates) {
			this.updates = updates;
		}
		
		@Override
		public void run() {
			Connection dbConnection = dbConnector.newDbConnection();
			updates.updateRecords(dbConnection);
		}
	}
}
