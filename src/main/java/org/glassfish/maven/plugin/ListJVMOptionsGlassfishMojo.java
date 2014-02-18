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

package org.glassfish.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.glassfish.maven.plugin.command.ListJVMOptionsCommand;

/**
 *
 * @author chris
 */
@Mojo(name = "list-jvm-options")
public class ListJVMOptionsGlassfishMojo extends AbstractGlassfishMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        postConfig();

        new ListJVMOptionsCommand(this).execute();
    }

}
