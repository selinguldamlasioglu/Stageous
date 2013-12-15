package gigs.db.bigquery;

import java.io.IOException;
import java.util.List;

import com.google.api.services.bigquery.model.*;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.Bigquery.Jobs.Insert;

@SuppressWarnings("all")

public class BigQueryWrapper { 
	//ASYNCHRONOUS!
	 /**
	   * Creates a Query Job for a particular query on a dataset
	   *
	   * @param bigquery  an authorized BigQuery client
	   * @param projectId a String containing the current Project ID
	   * @param querySql  the actual query string
	   * @return a reference to the inserted query Job
	   * @throws IOException
	   */
	  public static JobReference startQuery(Bigquery bigquery, String projectId,
	                                        String querySql) throws IOException {
	    System.out.format("\nInserting Query Job: %s\n", querySql);

	    Job job = new Job();
	    JobConfiguration config = new JobConfiguration();
	    JobConfigurationQuery queryConfig = new JobConfigurationQuery();
	    config.setQuery(queryConfig);

	    job.setConfiguration(config);
	    queryConfig.setQuery(querySql);

	    Insert insert = bigquery.jobs().insert(projectId, job);
	    insert.setProjectId(projectId);
	    JobReference jobId = insert.execute().getJobReference();

	    System.out.format("\nJob ID of Query Job is: %s\n", jobId.getJobId());

	    return jobId;
	  }

	  /**
	   * Polls the status of a BigQuery job, returns Job reference if "Done"
	   *
	   * @param bigquery  an authorized BigQuery client
	   * @param projectId a string containing the current project ID
	   * @param jobId     a reference to an inserted query Job
	   * @return a reference to the completed Job
	   * @throws IOException
	   * @throws InterruptedException
	   */
	  public static Job checkQueryResults(Bigquery bigquery, String projectId, JobReference jobId)
	      throws IOException, InterruptedException {
	    // Variables to keep track of total query time
	    long startTime = System.currentTimeMillis();
	    long elapsedTime;

	    while (true) {
	      Job pollJob = bigquery.jobs().get(projectId, jobId.getJobId()).execute();
	      elapsedTime = System.currentTimeMillis() - startTime;
	      System.out.format("Job status (%dms) %s: %s\n", elapsedTime,
	          jobId.getJobId(), pollJob.getStatus().getState());
	      if (pollJob.getStatus().getState().equals("DONE")) {
	        return pollJob;
	      }
	      // Pause execution for one second before polling job status again, to
	      // reduce unnecessary calls to the BigQUery API and lower overall
	      // application bandwidth.
	      Thread.sleep(1000);
	    }
	  }

	  /**
	   * Makes an API call to the BigQuery API
	   *
	   * @param bigquery     an authorized BigQuery client
	   * @param projectId    a string containing the current project ID
	   * @param completedJob to the completed Job
	   * @throws IOException
	   */
	  private static void displayQueryResults(Bigquery bigquery,
	                                          String projectId, Job completedJob) throws IOException {
	    GetQueryResultsResponse queryResult = bigquery.jobs()
	        .getQueryResults(
	            projectId, completedJob
	            .getJobReference()
	            .getJobId()
	        ).execute();
	    List<TableRow> rows = queryResult.getRows();
	    System.out.print("\nQuery Results:\n------------\n");
	    for (TableRow row : rows) {
	      for (TableCell field : row.getF()) {
	    	  //row.getF().getClass().cast(field);
	    	  System.out.printf("%-20s", field.getV());
	      }
	      System.out.println();
	    }
	  }
	  
}
