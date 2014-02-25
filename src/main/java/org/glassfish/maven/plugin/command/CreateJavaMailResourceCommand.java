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
import java.util.List;
import org.glassfish.maven.plugin.AbstractGlassfishMojo;
import org.glassfish.maven.plugin.Domain;
import org.glassfish.maven.plugin.JavaMailSession;

/**
 *
 * @author chris
 */
public class CreateJavaMailResourceCommand extends AbstractInteractiveAsadminCommand {

    private final Domain domain;
    private final JavaMailSession jms;

    public CreateJavaMailResourceCommand(AbstractGlassfishMojo sharedContext, Domain domain, JavaMailSession jms) {
        super(sharedContext);
        this.domain = domain;
        this.jms = jms;
    }

    @Override
    protected String getName() {
        return "create-javamail-resource";
    }

    @Override
    protected List<String> getParameters() {
        List<String> parameters = super.getParameters();
        parameters.addAll(Arrays.asList(
                "--port", String.valueOf(domain.getAdminPort()),
                "--mailhost", jms.getMailHost(),
                "--mailuser", jms.getMailUser(),
                "--fromaddress", jms.getFromAddress()
        ));
        addOptionalParameter(parameters, "--debug", Boolean.toString(jms.isDebug()));
        addOptionalParameter(parameters, "--enabled", Boolean.toString(jms.isEnabled()));
        addOptionalParameter(parameters, "--description", jms.getDescription());
        addProperties(parameters, jms.getProperties());
        parameters.add(jms.getName());
        return parameters;
    }

    @Override
    protected String getErrorMessage() {
        return "Unable to create java mail resource.";
    }

}
