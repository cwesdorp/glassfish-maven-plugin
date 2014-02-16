/*******************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2007-2008 maven-glassfish-plugin developers. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by the copyright holder in the GPL Version 2 section of the
 * License file that accompanied this code.  If applicable, add the following
 * below the License Header, with the fields enclosed by brackets [] replaced
 * by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 ******************************************************************************/

package org.glassfish.maven.plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;


/**
 *
 * @author <a href="mailto:dave.whitla@ocean.net.au">Dave Whitla</a>
 */
public abstract class AbstractGlassfishMojo extends AbstractMojo {

    /**
     * The directory into which domains are deployed. Default value is ${glassfishDirectory}/domains.
     */
    @Parameter
    protected File domainDirectory;

    /**
     * Container for domain configuration parameters.
     */
    @Parameter(required = true)
    protected Domain domain;

    /**
     * The root directory of the Glassfish installation to be used.
     */
    @Parameter(required = true)
    private File glassfishDirectory;

    /**
     * Debug Glassfish output.
     */
    @Parameter(defaultValue = "false")
    private boolean debug;

    /**
     * Force component deployment.
     */
    @Parameter(defaultValue = "false")
    private boolean force;

    /**
     * Echo Glassfish asadmin commands.
     */
    @Parameter(defaultValue = "false")
    private boolean echo;

    /**
     * Terse Glassfish output.
     */
    @Parameter(defaultValue = "true")
    private boolean terse = true;

    /**
     * Automatically create the domain if it does not already exist.
     */
    @Parameter(defaultValue = "true")
    private boolean autoCreate;

    /**
     * The asadmin user to create for domain administration.
     */
    @Parameter(defaultValue = "${user.name}")
    private String user;

    /**
     * Location of the asadmin style password file (if you do not want to provide the password in your POM).
     */
    @Parameter
    private String passwordFile;

    /**
     * The admin password to use for this domain (if you would rather not use an asadmin style password file).
     */
    @Parameter(defaultValue = "${glassfish.adminPassword}")
    private String adminPassword;

    protected String getPrefix() {
        return "glassfish";
    }

    public File getGlassfishDirectory() {
        return glassfishDirectory;
    }

    public void setGlassfishDirectory(File glassfishDirectory) {
        this.glassfishDirectory = glassfishDirectory;
    }

    public File getDomainDirectory() {
        return domainDirectory;
    }

    public void setDomainDirectory(File domainDirectory) {
        this.domainDirectory = domainDirectory;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isEcho() {
        return echo;
    }

    public void setEcho(boolean echo) {
        this.echo = echo;
    }

    public boolean isTerse() {
        return terse;
    }

    public void setTerse(boolean terse) {
        this.terse = terse;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getPasswordFile() {
        return passwordFile;
    }

    public void setPasswordFile(String passwordFile) {
        this.passwordFile = passwordFile;
    }

    protected void postConfig() throws MojoFailureException {
        List<String> configErrors = getConfigErrors();
        if (!configErrors.isEmpty()) {
            throw new MojoFailureException(configErrors.get(0));
        }
        if (adminPassword != null && adminPassword.length() > 0) {
            // create temporary passfile
            try {
                File tmpPassFile = File.createTempFile("mgfp", null);
                tmpPassFile.deleteOnExit();
                passwordFile = tmpPassFile.getAbsolutePath();
                PrintWriter fileWriter = new PrintWriter(new FileWriter(tmpPassFile));
                fileWriter.println("AS_ADMIN_PASSWORD=" + adminPassword);
//                fileWriter.println("AS_ADMIN_USERPASSWORD=" + adminPassword);
//                fileWriter.println("AS_ADMIN_MASTERPASSWORD=" + adminPassword);
                fileWriter.close();
            } catch (IOException e) {
                throw new MojoFailureException("Unable to create temporary asadmin password file in "
                                + System.getProperty("java.io.tmpdir"));
            }
        }
        // todo: this should be left to asadmin to decide
        if (domainDirectory == null) {
            domainDirectory = new File(glassfishDirectory, "glassfish/domains");
        }

        if (domain.getDirectory() == null) {
            domain.setDirectory(domainDirectory);
        }

    }

    protected List<String> getConfigErrors() {
        List<String> errors = new ArrayList<String>();
        // adminPort or basePort are required
        // passfile or adminPassword are required
        if (adminPassword == null && passwordFile == null) {
            String error = "inside the definition for plugin: 'maven-glassfish-plugin' specify the following:\n\n"
                    + "<configuration>\n  ...\n  <passwordFile>VALUE</passwordFile>\n  ...\n   OR\n  ...\n  "
                    + "<adminPassword>VALUE</adminPassword>\n  ...\n</configuration>\n";
            errors.add(error);
        }
        return errors;
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }
}
