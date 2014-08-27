/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.guacamole.net.auth.AuthenticationProvider;
import net.sourceforge.guacamole.net.auth.Credentials;
import net.sourceforge.guacamole.protocol.GuacamoleConfiguration;

/**
 * 根据当前登录的用户信息进行验证，VNC
 */
public class CloudKingGuacamoleAuthenticationProvider implements AuthenticationProvider {

    /**
     * 
     */
    public Map<String, GuacamoleConfiguration> getAuthorizedConfigurations(Credentials credentials) {
        if (credentials.getRequest().getSession().getAttribute("userLogin") == null) {
            return null;
        } else {
            String port = credentials.getRequest().getParameter("port");
            String hostname = credentials.getRequest().getParameter("hostname");
            Map<String, GuacamoleConfiguration> map = new HashMap<String, GuacamoleConfiguration>();
            GuacamoleConfiguration configuration = new GuacamoleConfiguration();
            configuration.setProtocol("vnc");
            configuration.setParameter("port", port);
            configuration.setParameter("hostname", hostname);
            map.put(credentials.getSession().getId(), configuration);
            return map;
        }
    }
}
