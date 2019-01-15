package com.forgerock.debug.file.impl;

import static org.forgerock.openam.utils.Time.newDate;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.forgerock.http.util.Json;

import com.sun.identity.shared.debug.DebugConstants;
import com.sun.identity.shared.debug.file.DebugFile;

/**
 * Debug file dedicated to std out
 */
public class StackDriverDebugFile implements DebugFile {

	private static final StackDriverDebugFile INSTANCE = new StackDriverDebugFile();

	private static final SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");

	private PrintWriter stdoutWriter = new PrintWriter(System.out, true);

	private static final int LOGGER = 0;
	private static final int THREAD_NAME = 5;
	private static final int TRANSACTION_ID = 6;

	private StackDriverDebugFile() {
	}

	/**
	 * Get StackDriver debug file
	 *
	 * @return StackDriver debug file
	 */
	public static StackDriverDebugFile getInstance() {
		return INSTANCE;
	}
	
	/**
	 * For testing enable the use of a different PrintWriter
	 * @param pw
	 */
	public void setPrintWriter(PrintWriter pw) {
		this.stdoutWriter = pw;
	}

	/**
	 * Converts a passed prefix into a JSON object for printing to stdout
	 * 
	 * @param prefix
	 * @param msg
	 * @param t
	 * @throws IOException
	 */
	public void writeIt(String prefix, String msg, Throwable t) throws IOException {
		
		if (prefix.split(":").length < 7) {
			writeIt("Unable to parse log entry: " + prefix + ": " + msg);
		} else {
			String[] prefixParts = prefix.split(":", 7);
			LogEntry logEntry = new LogEntry();

			logEntry.setLogger(prefixParts[LOGGER]);

			// Rejoin the elements of the timestamp and format them for StackDriver
			// consumption
			String timeStamp = String.join(":", Arrays.copyOfRange(prefixParts, 1, 5));
			logEntry.setTime(reformatTimeStamp(timeStamp));
			logEntry.setThreadName(prefixParts[THREAD_NAME].trim());
			logEntry.setTransactionId(prefixParts[TRANSACTION_ID].trim());

			String severity;
			String message;
			/*
			 * DEBUG messages are not prefixed with a severity
			 */
			if (msg.startsWith("WARNING: ") || msg.startsWith("ERROR: ")) {
				String[] parts = msg.split(":", 2);
				severity = parts[0];
				message = parts[1].trim();
			} else {
				severity = "DEBUG";
				message = msg;
			}
			logEntry.setSeverity(severity);
			logEntry.setMessage(message);

			if (t != null) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				t.printStackTrace(pw);
				logEntry.setStackTrace(sw.toString());
				sw.close();
				pw.close();
			} else {
				logEntry.setStackTrace("");
			}
			writeIt(new String(Json.writeJson(logEntry)));
		}
	}

	/**
	 * Converts the timestamp from internal format to ISO8601 for StackDriver
	 * 
	 * @param timeStamp
	 * @return
	 */
	private String reformatTimeStamp(String timeStamp) {
		try {
			return ISO8601.format(DebugConstants.DEBUG_DATE_FORMAT.parse(timeStamp));
		} catch (ParseException e) {
			return timeStamp;
		}
	}

	public void writeIt(String log) throws IOException {
		stdoutWriter.println(log);
	}

	/**
	 * Printing error directly into the stdout. A log header will be generated
	 *
	 * @param debugName debug name
	 * @param message   the error message
	 * @param ex        the exception (can be null)
	 */
	public static void printError(String debugName, String message, Throwable ex) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss:SSS a zzz");
		String prefix = debugName + ":" + dateFormat.format(newDate()) + ": " + Thread.currentThread().toString()
				+ "\n";

		System.err.println(prefix + message);
		if (ex != null) {
			System.err.println(prefix + message);
			ex.printStackTrace(System.err);
		}
	}

	static void printError(String log) {
		System.err.println(log);
	}
}
