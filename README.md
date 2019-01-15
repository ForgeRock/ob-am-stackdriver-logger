# ob-am-stackdriver-logger
A StackDriver Log Provider for AM

## Local Testing Instructions

1. Install AM on a local tomcat.
1. Build with `mvn package`
1. Add jar file to `<TOMCAT_DIR>/webapps/openam/WEB-INF/lib`
1. Add the following to `<TOMCAT_DIR>/bin/setenv.sh`:

```
export CATALINA_OPTS="$CATALINA_OPTS -Dcom.iplanet.services.debug.level=warning \
-Dcom.sun.identity.util.debug.provider=com.forgerock.debug.impl.StackDriverDebugProvider"
```