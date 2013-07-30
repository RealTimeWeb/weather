package realtimeweb.weatherservice.structured;

import java.util.ArrayList;
import java.util.HashMap;
import realtimeweb.weatherservice.domain.Report;
/**
 * A listener for the getReport method. On success, passes the data into the getReportCompleted method. On failure, passes the exception to the getReportFailed method.
 */
public interface StructuredGetReportListener {
	/**
	 * 
	 * @param data The method that should be overridden to handle the data if the method was successful.
	 */
	public abstract void getReportCompleted(HashMap<String, Object> data);
	/**
	 * 
	 * @param error The method that should be overridden to handle an exception that occurred while getting the SearchResponse.
	 */
	public abstract void getReportFailed(Exception error);
}