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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.glassfish.maven.plugin.AbstractGlassfishMojo;
import org.glassfish.maven.plugin.Domain;

/**
 *
 * @author chris
 */
public abstract class AbstractJVMOptionsCommand extends AbstractInteractiveAsadminCommand {

    protected final Domain domain;
    private final Set<String> jvmOptions;

    public AbstractJVMOptionsCommand(AbstractGlassfishMojo sharedContext, Domain domain, Set<String> jvmOptions) {
        super(sharedContext);
        this.domain = domain;
        this.jvmOptions = jvmOptions;
    }

    @Override
    protected List<String> getParameters() {
        if (jvmOptions == null || jvmOptions.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        StringBuilder options = new StringBuilder();
        for (String option : jvmOptions) {
            if (options.length() > 0) {
                options.append(':');
            }
            options.append(escape(option, ";:", "\\\\"));
        }
        List<String> parameters = super.getParameters();
        parameters.addAll(Arrays.asList(
                "--port", String.valueOf(domain.getAdminPort()),
                options.insert(0, "\\").toString()
        ));
        return parameters;
    }

}
