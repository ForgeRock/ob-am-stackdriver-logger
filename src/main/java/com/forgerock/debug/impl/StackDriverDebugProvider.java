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
