<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<link rel="shortcut icon" href="images/favicon.ico"
			type="image/x-icon" />
		<link rel="apple-touch-icon" type="image/png"
			href="images/guacamole-logo-144.png" />
		<link rel="stylesheet" type="text/css" href="styles/login.css" />
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densitydpi=device-dpi" />
		<title>云景云平台</title>
		<SCRIPT type="text/javascript" src="../js/jquery-1.7.2.min.js"></SCRIPT>
	</head>
	<body>
		<div id="login-ui" style="display: none">
			<div id="login-dialog-middle">

				<div id="login-dialog">
					<p>
						没有登录，请先登录
					</p>
				</div>

			</div>
		</div>

		<!-- Connection list UI -->
		<div id="connection-list-ui" style="display: none">

			<div id="logout-panel">
				<button id="logout">
					Logout
				</button>
			</div>

			<h1>
				<img class="logo" src="images/guacamole-logo-64.png" alt="" />
				Available Connections
			</h1>

			<table class="connections">
				<thead>
					<tr>
						<th class="protocol">
						</th>
						<th class="name">
							Name
						</th>
					</tr>
				</thead>
				<tbody id="connections-tbody">
				</tbody>
			</table>

		</div>

		<div id="version-dialog">
			Guacamole 0.6.2
		</div>

		<script type="text/javascript" src="scripts/connections.js"></script>

		<!-- Init -->
		<script type="text/javascript"> /* <![CDATA[ */

            // Constructs the URL for a client which connects to the connection
            // with the given id.
            function getClientURL(id) {
           	  return "client.jsp?id=" + encodeURIComponent(id)+"&hostname=${param.hostname}"+"&vmName=${param.vmName}";
            }

            // Resets the interface such that the login UI is displayed if
            // the user is not authenticated (or authentication fails) and
            // the connection list UI (or the client for the only available
            // connection, if there is only one) is displayed if the user is
            // authenticated.
            function resetUI() {

                // Get parameters from query string
                var parameters = window.location.search.substring(1);

                var configs;
                try {
                    configs = getConfigList(parameters);
                }
                catch (e) {

                    // Show login UI if unable to get configs
                    loginUI.style.display = "";
                    connectionListUI.style.display = "none";

                    return;

                }

                // If only one connection, redirect to that.
                if (configs.length == 1) {
                    window.location.href = getClientURL(configs[0].id);
                    return;
                }

                // Remove all rows from connections list
                var tbody = document.getElementById("connections-tbody");
                tbody.innerHTML = "";
                
                // Add one row per connection
                for (var i=0; i<configs.length; i++) {

                    // Create row and cells
                    var tr = document.createElement("tr");
                    var protocol = document.createElement("td");
                    var id = document.createElement("td");

                    var protocolIcon = document.createElement("div");
                    protocolIcon.className = "protocol icon " + configs[i].protocol;

                    // Set CSS
                    protocol.className = "protocol";
                    id.className = "name";

                    // Create link to client
                    var clientLink = document.createElement("a");
                    clientLink.setAttribute("href", getClientURL(configs[i].id));

                    // Set cell contents
                    protocol.appendChild(protocolIcon);
                    //protocol.textContent   = configs[i].protocol;
                    clientLink.textContent = configs[i].id;
                    id.appendChild(clientLink);

                    // Add cells
                    tr.appendChild(protocol);
                    tr.appendChild(id);

                    // Add row
                    tbody.appendChild(tr);

                }

                // If configs could be retrieved, display list
                loginUI.style.display = "none";
                connectionListUI.style.display = "";

            }

            var loginUI = document.getElementById("login-ui");
            var connectionListUI = document.getElementById("connection-list-ui");
            var logout = document.getElementById("logout");

            logout.onclick = function() {
                window.location.href = "vnc/logout";
            };

            resetUI();

            /* ]]> */ </script>

	</body>

</html>
