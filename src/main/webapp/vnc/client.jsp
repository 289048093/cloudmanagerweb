<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html>

<!--
    Guacamole - Clientless Remote Desktop
    Copyright (C) 2010  Michael Jumper

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
-->

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<link rel="shortcut icon" href="images/favicon.ico"
			type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="styles/client.css" />
		<link rel="stylesheet" type="text/css" href="styles/keyboard.css" />
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densitydpi=device-dpi" />
		<meta name="apple-mobile-web-app-capable" content="yes" />
		<SCRIPT type="text/javascript" src="../js/jquery-1.7.2.min.js"></SCRIPT>
		<title>云景云平台</title>
	</head>

	<body>
		<INPUT type="hidden" id="hostname" value="${param.hostname}" />
		<INPUT type="hidden" id="vmName" value="${param.vmName}" />

		<!-- Menu -->
		<div id="menu">

			<!-- Clipboard -->
			<button id="showClipboard">
				显示剪贴板
			</button>
			<div id="clipboardDiv">
				<h2>
					剪贴板
				</h2>
				<p>
					在Guacamole的文本复制和粘贴会显示在这里，改变这里的文本会影响到远程的受控机的剪贴板，并传递到远程桌面，可以通过下方输入框进行交互。
					<!-- Text copied/cut within Guacamole will appear here. Changes to the text will affect the remote clipboard, and will be pastable within the remote desktop. Use the textbox below as an interface between the client and server clipboards. -->
				</p>
				<textarea rows="10" cols="40" id="clipboard"></textarea>
			</div>

			<button id="showKeyboard">
				显示键盘
			</button>
			<button id="ctrlAltDelete">
				Ctrl-Alt-Delete
			</button>
			<button id="logout" style="display: none">
				关闭
			</button>
		</div>

		<!-- Touch-specific menu -->
		<div id="touchMenu">
			<img id="touchShowClipboard"
				src="images/menu-icons/tango/edit-paste.png" />
			<img id="touchShowKeyboard"
				src="images/menu-icons/tango/input-keyboard.png" />
			<img id="touchLogout"
				src="images/menu-icons/tango/system-log-out.png" />
		</div>

		<!-- Touch-specific clipboard -->
		<div id="touchClipboardDiv">
			<h2>
				Clipboard
			</h2>
			<p>
				Text copied/cut within Guacamole will appear here. Changes to the
				text will affect the remote clipboard, and will be pastable within
				the remote desktop. Use the textbox below as an interface between
				the client and server clipboards.
			</p>
			<textarea rows="10" cols="40" id="touchClipboard"></textarea>
		</div>

		<!-- Keyboard event target for platforms with native OSKs -->
		<textarea id="eventTarget"></textarea>

		<!-- Display -->
		<div id="display">

			<!-- Menu trigger -->
			<div id="menuControl"></div>

		</div>

		<!-- On-screen keyboard -->
		<div id="keyboardContainer"></div>

		<!-- Dimensional clone of viewport -->
		<div id="viewportClone" />

			<!-- Dialogs -->
			<div class="dialogOuter">
				<div class="dialogMiddle">

					<!-- Status Dialog -->
					<div id="statusDialog" class="dialog">
						<p id="statusText"></p>
						<div class="buttons">
							<button id="reconnect">
								关闭连接
							</button>
						</div>
					</div>

				</div>
			</div>

			<!-- guacamole-common-js scripts -->
			<script type="text/javascript" src="guacamole-common-js/keyboard.js"></script>
			<script type="text/javascript" src="guacamole-common-js/mouse.js"></script>
			<script type="text/javascript" src="guacamole-common-js/layer.js"></script>
			<script type="text/javascript" src="guacamole-common-js/tunnel.js"></script>
			<script type="text/javascript" src="guacamole-common-js/guacamole.js"></script>
			<script type="text/javascript"
				src="guacamole-common-js/oskeyboard.js"></script>

			<!-- guacamole-default-webapp scripts -->
			<script type="text/javascript" src="scripts/interface.js"></script>

			<!-- Init -->
			<script type="text/javascript"> /* <![CDATA[ */

            // Start connect after control returns from onload (allow browser
            // to consider the page loaded).
            window.onload = function() {
                window.setTimeout(function() {

                    var tunnel;

                    // If WebSocket available, try to use it.
                    if (window.WebSocket)
                        tunnel = new Guacamole.ChainedTunnel(
                            new Guacamole.WebSocketTunnel("websocket-tunnel"),
                            new Guacamole.HTTPTunnel("tunnel")
                        );

                    // If no WebSocket, then use HTTP.
                    else
                        tunnel = new Guacamole.HTTPTunnel("tunnel")

                    // Instantiate client
                    var guac = new Guacamole.Client(tunnel);

                    // Add client to UI
                    guac.getDisplay().className = "software-cursor";
                    GuacamoleUI.display.appendChild(guac.getDisplay());

                    // Tie UI to client
                    GuacamoleUI.attach(guac);

                    try {

                        // Get entire query string, and pass to connect().
                        // Normally, only the "id" parameter is required, but
                        // all parameters should be preserved and passed on for
                        // the sake of authentication.

                        var connect_string = window.location.search.substring(1);
                        guac.connect(connect_string);

                    }
                    catch (e) {
                        GuacamoleUI.showError(e.message);
                    }

                }, 0);
            };

        /* ]]> */ </script>
	</body>

</html>
