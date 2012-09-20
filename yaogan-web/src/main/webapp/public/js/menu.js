	var json_str = {};
	function createMenu (loginUrl){
		var w_left = $("#left").width();
		resize_fu();
		$("#tool").click(function (){
			$("#left").toggle();//有什么用呢这行？
			if($("#left").width() == w_left){
				$("#left").width(0);
				$("#bar_img").attr("src","img/bar_fan.gif");
			}else{
				$("#left").width(w_left);
				$("#bar_img").attr("src","img/bar.gif");
			}
			resize_fu();
		});
		creat_main_menu();
		creat_menu();
		if(!!loginUrl){
			$("#mainiframe").attr("src",loginUrl).one("load",function (){
				creat_left_menu($(".menu li.on").attr("id"));
			});
		}
		else{
			creat_left_menu($(".menu li.on").attr("id"));
		}
			
		resize_fu();
	}
	function resize_fu(){
		$("#main").css("width",(parseInt($("#content").css("width"),10) - parseInt($("#left").css("width"),10) - parseInt($("#tool").css("width"),10)) + "px");
		var h = parseInt($(window).height(),10) - parseInt($("#top").css("height"),10);
		$("#content").css("height",h - 10);
		$("#content").css("min-height",h - 10);
		$("#tool").css("height",h);
		$("#mainiframe").css("height",h - 10);
	}
	function creat_menu(){
		var span = '<span></span>';
		$('.menu li a').wrapInner(span);	
		$('.menu li').click(function (){
			$(this).siblings().removeClass('on');
			$(this).addClass('on');
			creat_left_menu($(".menu li.on").attr("id"));
		}).mouseover(function(){
			$(this).addClass('selected').siblings().removeClass('selected');
			return false;
		}).mouseout(function (){
			$(this).removeClass('selected');
		});
	};
	
	function creat_main_menu (){
		for(var ii=0; ii<json_str.length; ii++){
			if(ii==0){
				$("#menu_ul").append("<li class='on' id='" + json_str[ii].d + "'><a>" + json_str[ii].n+ "</a></li>");	
			}else{
				$("#menu_ul").append("<li id='" + json_str[ii].d + "'><a>" + json_str[ii].n+ "</a></li>");
			}
		}
	}
	function creat_left_menu(main_code){
		for(var ii=0; ii<json_str.length; ii++){
			if(json_str[ii].d == main_code){
				$("#left_menu li").remove();
				var o = json_str[ii].c;
				if(o){
					for(var jj=0; jj<o.length; jj++){
						var menu_str = [];
						menu_str.push("<li><span class='m_n'><span class='icon_left'><a>");
						menu_str.push(o[jj].n);
						menu_str.push("</a><img class='on_off' src='img/butt-1.gif' style='margin-bottom:3px;margin-left:6px;'/></span></span><ul class='ul_sub'>");
						var oo = o[jj].c;
						if(oo){
							for(var nn=0; nn<oo.length; nn++){
								menu_str.push("<li id='" + oo[nn].d + "' onclick='getIframePage(" + oo[nn].d + ",\"" + oo[nn].u + "\")'><a>");
								menu_str.push(oo[nn].n);
								menu_str.push("</a></li>");
							}
						}
						menu_str.push("</ul></li>");
						$("#left_menu").append(menu_str.join(""));
					}
				}
			}
		}
		$("li span.m_n").click(function (){
			if($(this).next().find("li").length == 0){
				return false;
			}
			$("ul.ul_sub").slideUp();
			$("img.on_off").attr("src","img/butt-1.gif");
			$(this).parent().find("ul").slideDown();
			$(this).find("img").attr("src","img/butt.gif");
		});
		if($("ul.ul_sub:first").find("li").length != 0){
			$("ul.ul_sub:first").slideDown();
			$("ul.ul_sub:first").prev().find("img.on_off").attr("src", "img/butt.gif");
			$("ul.ul_sub:first li:first").trigger("click");
		}
	}
	function getIframePage (id, url){
		showLoading();
		$(".ul_sub li").removeClass("at");
		$("#" + id).addClass("at");
		$("#mainiframe").attr("src",url).one("load",function (){removeLoading();});
	}
	function showLoading(){}
	function removeLoading(){}