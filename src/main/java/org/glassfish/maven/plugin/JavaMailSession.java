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

import java.util.Set;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Configuration for a Java Mail Session.
 *
 * The parameters for changing stores and transmission protocol are not yet implemented.
 *
 * @author chris.
 */
public class JavaMailSession {

    @Parameter(required = true)
    private String name;

    @Parameter(required = true)
    private String mailHost;

    @Parameter(required = true)
    private String mailUser;

    @Parameter(required = true)
    private String fromAddress;

    @Parameter(defaultValue = "true")
    private boolean enabled = true;

    @Parameter(defaultValue = "false")
    private boolean debug;

    @Parameter
    private String description;

    @Parameter
    private Set<Property> properties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

}
