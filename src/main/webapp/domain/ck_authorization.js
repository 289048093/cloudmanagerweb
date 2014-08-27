var domainId;					//域Id
var menuId;						//当前操作菜单Id,用以暂存在权限容器中
var oldMenuArr = new Array(); 	//最初的菜单状态保存容器,用以比较某菜单是否有作更改
var oldRightsArr = new Array();	//最初的权限保存容器,用以比较某权限是否有作更改
var rightsArr = new Array();	//最后的权限保存容器
var zTreeObj;  

// 菜单类
function Menu(id,checked) {
	this.id = id;
	this.checked = checked;
}

// 权限类
function Rights(id,menuId,checked) {
	this.id = id;
	this.menuId = menuId;
	this.checked = checked;
}

function getMenu(domainIdParam) {
	domainId = domainIdParam;
	$.ajax({
        url : "domainManager/domain!queryMenuJsonTree.action",  
        type : "post",  
        dataType : "json",  
        data:{"cloudContext.vo.id":domainId},
        success : initZtree  
    }); 
}

//ztree 配置
var setting = {  
    check : {  
        enable : true  
    },  
    data : {  
        simpleData : {  
            enable : true  
        }  
    },
    callback : {
			onClick : queryRights,
			onCheck : checkedModule
		}
};  

//树目录生成函数
function initZtree(json) {
    var data = (json.cloudContext.params.menu);  
    var zNodes = eval("(" + data + ")");  
    //保存最初状态的菜单状态
    for(var menu in zNodes){
    	oldMenuArr.push(new Menu(zNodes[menu].id,zNodes[menu].checked));
    }
    zTreeObj = $.fn.zTree.init($('#kpiTree'), setting, zNodes);  
}  

// 选中模块的时候
function checkedModule(event, treeId, treeNode) {
	if(menuId==treeNode.id){
		if(treeNode.checked){
			$(".ck_rights_checkbox_item").each(function(){
				this.checked = true;
			});
		}else{
			$(".ck_rights_checkbox_item").each(function(){
				this.checked = false;
			});
		}
		//更新权限
		saveRightsLocalBeforeExit();
	}
}
  
//单击查询具体权限
function queryRights(event, treeId, treeNode, clickFlag) {
	//保存上次点出的权限
	saveRightsLocalBeforeExit();
	
	menuId = treeNode.id;	//保存当前操作菜单
	$.ajax({
        type : "POST", 
        url : "domainManager/domain!queryRightsByMenu.action",
        data:{
        	"cloudContext.params.menuId":menuId,
        	"cloudContext.params.domainId":domainId
        	},
        dataType : "json", 
        success : showRights
    }); 
}

//展示具体权限
function showRights(json) {
    var data = json.cloudContext.params.rights;
    
    //保存最初状态的权限状态 
    for(var rights in data){
    	var containRightsFlag = false;
    	for(var i=0; i<oldRightsArr.length; i++){
    		if(oldRightsArr[i].id==data[rights].id){
    			containRightsFlag = true;
    			break;
    		}
    	}
    	if(!containRightsFlag){
    		oldRightsArr.push(new Rights(data[rights].id,menuId,data[rights].hasRights)); 
    	}
    }
    
    var rightHtml = "<table width='100%' border='1' cellspacing='0' cellpadding='0'><thead><th width='20%'>操作</th><th  width='30%'>名称</th><th  width='50%'>描述</th></thead><tbody>";
    var tmpHtml = null;
    var hasDataFlag = false;
    for(var p in data){
    	hasDataFlag = true;
    	if(data[p].hasRights){
    		tmpHtml = "<input checked type='checkbox' id='"+data[p].id+"' class='ck_rights_checkbox_item' />";
    	}else{
    		tmpHtml = "<input type='checkbox' id='"+data[p].id+"' class='ck_rights_checkbox_item' />";
    	}
    	rightHtml += "<tr>" +
    				 "<td>"+tmpHtml+"</td>"+
    				 "<td>"+data[p].name+"</td>"+
    				 "<td>"+data[p].desc+"</td>"+
    				 "</tr>";
    	//不管用户是否
    }
    if(!hasDataFlag){
    	rightHtml +="<tr><td colspan='3'>无权限</td></tr>";
    }
    rightHtml+="</tbody></table>"
    $(".ck_rights_wrapper").html(rightHtml);
}

//保存上次点出的权限
function saveRightsLocalBeforeExit(){
	$(".ck_rights_checkbox_item").each(function(){
		var hasAddedFlag = false;	
		//旧元素替换
		for(var i=0; i<rightsArr.length; i++){
			if(rightsArr[i].id==$(this).attr("id")){
				rightsArr[i].checked = this.checked;
				hasAddedFlag = true;
			}
		}
		//新元素添加
		if(!hasAddedFlag){
			rightsArr.push(new Rights($(this).attr("id"),menuId,this.checked));
		}
	});
}

//保存授权
$("#ck_authorization_confirm").live("click",function(){
	saveRightsLocalBeforeExit();
	//提取改变过的菜单项,并保存到 menuArr 容器中
	var nodes = zTreeObj.getNodes();
	var newMenuIds ="";
	var newMenuChecked ="";
	for(var i=0; i<nodes.length; i++){
		for(var menu in oldMenuArr){
			//如果菜单状态已改变，则保存到 menuArr 中
			if((oldMenuArr[menu].id==nodes[i].id)&&(oldMenuArr[menu].checked!=nodes[i].checked+"")){
				console.log("id:"+oldMenuArr[menu].id+" oldcheck:"+oldMenuArr[menu].checked+" newchecked:"+nodes[i].checked);
				newMenuIds += nodes[i].id+",";
				newMenuChecked += nodes[i].checked+",";
				break;
			}
		}
	}
	
	//提取改变过的权限项,并保存到 rightsArr 容器中
	var newRighgtsIds="";
	var newRighgtsMenus="";
	var newRighgtsChecked="";
	for(var i=0; i<rightsArr.length; i++){
		for(var j=0; j<oldRightsArr.length; j++){
			//如果权限状态已改变
			if((rightsArr[i].id==oldRightsArr[j].id)&&(rightsArr[i].checked!=oldRightsArr[j].checked)){
				newRighgtsIds += rightsArr[i].id+",";
				newRighgtsMenus += rightsArr[i].menuId+",";
				newRighgtsChecked += rightsArr[i].checked+",";
				break;
			}
		}
	}
	
	if(!newMenuIds&&!newRighgtsIds){
		return;
	}else{
		if(newMenuIds){
			newMenuIds = newMenuIds.substring(0,newMenuIds.lastIndexOf(","));
			newMenuChecked = newMenuChecked.substring(0,newMenuChecked.lastIndexOf(","));
		}
		if(newRighgtsIds){
			newRighgtsIds = newRighgtsIds.substring(0,newRighgtsIds.lastIndexOf(","));
			newRighgtsMenus = newRighgtsMenus.substring(0,newRighgtsMenus.lastIndexOf(","));
			newRighgtsChecked = newRighgtsChecked.substring(0,newRighgtsChecked.lastIndexOf(","));
		}
	}
	$.ajax({
        url : "domainManager/domain!saveAuthorization.action",  
        type : "post",  
        dataType : "json",  
        data:{
        	  "cloudContext.params.domainId":domainId,
        	  "cloudContext.params.menuId":newMenuIds,
        	  "cloudContext.params.menuChecked":newMenuChecked,
        	  "cloudContext.params.righgtsIds":newRighgtsIds,
        	  "cloudContext.params.righgtsMenus":newRighgtsMenus,
        	  "cloudContext.params.righgtsChecked":newRighgtsChecked
        	  },
        success : function(data){}  
    }); 
});
  
