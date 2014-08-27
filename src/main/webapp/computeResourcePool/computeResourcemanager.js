$(function() {
			$("#s1").live('dblclick', function() {
				var alloptions = $("#s1 option");
				var so = $("#s1 option:selected");
				if (so === null || so == "undefined") {
					return;
				}
				var name = '';
				var names = new Array();
				for (var i = 0; i < so.length; i++) {
					name = so.eq(i).html();
					if (computeResourcesHasVm[name]) {
						names.push(name);
						continue;
					}
					try {
						var a = (so.get(so.length - 1).index == alloptions.length
								- 1) ? so.prev().attr("selected", true) : so
								.next().attr("selected", true);
					} catch (e) {
						return;
					}
					$("#s2").append(so);
				}
				if (names.length > 0) {
					alert(names + ' 下存在虚拟机');
				}
			});

			$("#s2").live('dblclick', function() {
				var alloptions = $("#s2 option");
				var so = $("#s2 option:selected");
				if (so === null || so == "undefined") {
					return;
				}
				try {
					var a = (so.get(so.length - 1).index == alloptions.length
							- 1) ? so.prev().attr("selected", true) : so.next()
							.attr("selected", true);
				} catch (e) {
					return;
				}
				$("#s1").append(so);
			});

			$("#add").live('click', function() {
				var alloptions = $("#s1 option");
				var so = $("#s1 option:selected");
				if (so.length < 1) {
					return;
				}
				var name = '';
				var names = new Array();
				for (var i = 0; i < so.length; i++) {
					name = so.eq(i).html();
					if (computeResourcesHasVm[name]) {
						names.push(name);
						continue;
					}
					var a = (so.get(so.length - 1).index == alloptions.length
							- 1) ? so.prev().attr("selected", true) : so.next()
							.attr("selected", true);
					$("#s2").append(so.eq(i));
				}
				if (names.length > 0) {
					alert(names + ' 下存在虚拟机');
				}
			});

			$("#remove").live('click', function() {
				var alloptions = $("#s2 option");
				var so = $("#s2 option:selected");
				if (so.length < 1) {
					return;
				}
				var a = (so.get(so.length - 1).index == alloptions.length - 1)
						? so.prev().attr("selected", true)
						: so.next().attr("selected", true);
				$("#s1").append(so);
			});
		});