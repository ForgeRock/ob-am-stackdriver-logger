/**
 *
 * The contents of this file are subject to the terms of the Common Development and
 *  Distribution License (the License). You may not use this file except in compliance with the
 *  License.
 *
 *  You can obtain a copy of the License at https://forgerock.org/cddlv1-0/. See the License for the
 *  specific language governing permission and limitations under the License.
 *
 *  When distributing Covered Software, include this CDDL Header Notice in each file and include
 *  the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 *  Header, with the fields enclosed by brackets [] replaced by your own identifying
 *  information: "Portions copyright [year] [name of copyright owner]".
 *
 *  Copyright 2019 ForgeRock AS.
 */

package com.sun.identity.shared.debug.file.impl.stackdriver;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.forgerock.debug.file.impl.StackDriverDebugFile;
import com.sun.identity.shared.debug.file.DebugFile;

/**
 * Unit test for simple App.
 */
public class StackDriverDebugFileTest 
{
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	private final static String LOGGER = "amAuthInternal";
	private final static String TIMESTAMP = "01/10/2019 04:51:41:583 PM GMT";
	private final static String THREAD = "Thread[localhost-startStop-1,5,main]";
	private final static String TRANSACTION = "TransactionId[0555ee69-744d-4c54-8b19-0a399af8f8b2-0]";
	private final static String PREFIX = "amAuthInternal:01/10/2019 04:51:41:583 PM GMT: Thread[localhost-startStop-1,5,main]: TransactionId[0555ee69-744d-4c54-8b19-0a399af8f8b2-0]";
	private final static String MESSAGE = "WARNING: getSSOToken: setProperty exception ";
	private final static String JSON = "{\"logger\":\"amAuthInternal\",\"time\":\"2019-01-10T16:51:41.583Z\"," +
	                                   "\"threadName\":\"Thread[localhost-startStop-1,5,main]\"," +
			                           "\"transactionId\":\"TransactionId[0555ee69-744d-4c54-8b19-0a399af8f8b2-0]\"," +
	                                   "\"severity\":\"WARNING\",\"message\":\"getSSOToken: setProperty exception\"," +
			                           "\"stackTrace\":\"\"}\n";

	//amAuthInternal:01/10/2019 04:51:41:583 PM GMT: Thread[localhost-startStop-1,5,main]: TransactionId[0555ee69-744d-4c54-8b19-0a399af8f8b2-0] WARNING: getSSOToken: setProperty exception :

	private PrintWriter pw = new PrintWriter(outContent);

	@Test
    public void testGetInstance()
    {
        DebugFile file = StackDriverDebugFile.getInstance();
        assertEquals( file.getClass(), StackDriverDebugFile.class);
    }
    
	@Test
    public void testWriteItHandlesBadFormat() throws IOException{
    	StackDriverDebugFile file = StackDriverDebugFile.getInstance();
    	file.setPrintWriter(pw);
    	file.writeIt("foo:bar", "message", null);
    	pw.flush();
    	assertEquals("Unable to parse log entry: foo:bar: message\n",outContent.toString());
    }
	
	@Test
    public void testWriteIt() throws IOException{
		StackDriverDebugFile file = StackDriverDebugFile.getInstance();
    	file.setPrintWriter(pw);
    	file.writeIt(PREFIX, MESSAGE, null);
    	pw.flush();
    	assertEquals(JSON,outContent.toString());
    }
}
