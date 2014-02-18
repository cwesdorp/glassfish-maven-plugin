/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the Licenses at http://opensource.org/licenses/CDDL-1.0 and
 * http://www.gnu.org/licenses/gpl-2.0.html. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 ******************************************************************************/

package org.glassfish.maven.plugin.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.glassfish.maven.plugin.AbstractGlassfishMojo;
import org.glassfish.maven.plugin.Domain;

/**
 * This is a helper command class that compares the current JVM options to the JVM options to set. Key/value properties
 * are compared on key and illegal combinations as -client and -server are detected. The found JVM options can then be
 * removed with the DeleteJVMOptionsCommand.
 *
 * @author chris
 */
public class FindReplacableJVMOptionsCommand extends ListJVMOptionsCommand {

    private static final String SEPERATOR = "=";
    private static final String XMX_OPTION = "-Xmx";

    private final Domain domain;
    private final Set<String> jvmOptions;

    private final Set<String> existingDomainOptions = new HashSet<String>();

    public FindReplacableJVMOptionsCommand(AbstractGlassfishMojo sharedContext, Domain domain, Set<String> jvmOptions) {
        super(sharedContext);
        this.domain = domain;
        this.jvmOptions = jvmOptions;
    }

    @Override
    public void execute(ProcessBuilder processBuilder) throws MojoExecutionException, MojoFailureException {
        existingDomainOptions.clear();

        // execute as normal
        super.execute(processBuilder);

        Map<String, String> userOptions = splitOptions(domain.getJVMOptions());
        Map<String, String> domainOps = splitOptions(existingDomainOptions);

        for (String userKey : userOptions.keySet()) {
            if ("-server".equals(userKey)) {
                jvmOptions.add("-client");

            } else if (domainOps.containsKey(userKey)) {
                String value = domainOps.get(userKey);
                jvmOptions.add(userKey + (value != null ? value : ""));
            }
        }
    }

    @Override
    protected void doReadOutput(BufferedReader reader) throws IOException {
        while (reader.ready()) {
            String s = reader.readLine();
            existingDomainOptions.add(s);
        }
    }

    /**
     * Split a set of given JVM options into key value pairs. E.g. -XX:MaxPermSize=192m is split on the '=' sign.
     *
     * @param options The options to process.
     * @return The options as key/value pairs.
     */
    protected Map<String, String> splitOptions(Set<String> options) {
        if (options == null || options.isEmpty()) {
            return Collections.EMPTY_MAP;
        }

        Map<String, String> result = new HashMap<String, String>();
        for (String option : options) {
            String[] keyValue = option.split(SEPERATOR);
            if (keyValue.length == 2) {
                result.put(keyValue[0], SEPERATOR + keyValue[1]);

            } else if (option.startsWith(XMX_OPTION)) {
                result.put(XMX_OPTION, option.substring(XMX_OPTION.length()));

            } else {
                result.put(option, null);
            }
        }
        return result;
    }

}
