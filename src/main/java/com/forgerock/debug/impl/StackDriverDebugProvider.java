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
package com.forgerock.debug.impl;

import com.forgerock.debug.file.impl.StackDriverDebugFile;
import com.sun.identity.shared.debug.file.DebugFile;
import com.sun.identity.shared.debug.file.DebugFileProvider;
import com.sun.identity.shared.debug.impl.DebugProviderImpl;

public class StackDriverDebugProvider extends DebugProviderImpl {
	
    public StackDriverDebugProvider() {
        super(new DebugFileProvider() {
            @Override
            public DebugFile getInstance(String debugName) {
                return StackDriverDebugFile.getInstance();
            }

            @Override
            public DebugFile getStdOutDebugFile() {
                return StackDriverDebugFile.getInstance();
            }
        });
    }

}
