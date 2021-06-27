/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var menu = new Object();

getImageURLOfFunction = function(url) {
    var urlImg = "Share/img/menu/file_list_to_do.png";
    if (url == "/Pages/admin/application/app.zul") {
        urlImg = "Share/img/menu/application.png";
    } else if (url == "/Pages/admin/user/userMan.zul") {
        urlImg = "Share/img/menu/task_assign.png";
    } else if (url == "/Pages/admin/role/roleManage.zul") {
        urlImg = "Share/img/menu/role.png";
    } else if (url == "/Pages/admin/department/dept.zul") {
        urlImg = "Share/img/menu/department.png";
    } else if (url == "/Pages/admin/flow/flowManager.zul") {
        urlImg = "Share/img/menu/flow.png";
    } else if (url == "/Pages/admin/outsideOffice/index.zul") {
        urlImg = "Share/img/menu/department.png";
    } else if (url == "/Pages/admin/category/catManage.zul") {
        urlImg = "Share/img/menu/procedure.png";
    } else if (url == "/Pages/document/template/templateMan.zul") {
        urlImg = "Share/img/menu/approve2.png";
    } else if (url == "/Pages/document/docIn/index.zul") {
        urlImg = "Share/img/menu/files.png";
    } else if (url == "/Pages/document/docOut/docDraftMan.zul") {
        urlImg = "Share/img/menu/register.png";
    }
    return urlImg;
};

gotoMenu = function(menuId, url) {
    try {
        var currentMenu = localStorage.menuId;
        var menu;
        if (currentMenu != null) {
            menu = document.getElementById("img" + currentMenu);
            if (menu != null)
                if (menu.style.display != "none") {
                    menu.setAttribute("class", "");
                }
        }
        localStorage.menuId = menuId;
        var selectMenu = document.getElementById("img" + menuId);
        selectMenu.setAttribute("class", "selected");
        localStorage.menuId = menuId;
    } catch (e) {
        console.log(e);
    }
    var wgt = zk.Widget.$("mainContent");
    data = {
        url: url
    };
    zAu.send(new zk.Event(wgt, 'onLoadPage', url));
};

selectTab = function(name, menuId, url) {
    //
    // hide menu of previous tab
    //
    try {
        var hideItem = localStorage.currentItem;
        if (hideItem != null) {
            var hideTab = document.getElementsByName("li" + hideItem);
            hideTab[0].setAttribute("class", "");
            var hideItems = document.getElementsByName("group" + hideItem);
            var i = 0;
            for (i = 0; i < hideItems.length; i++) {
                console.log("set menu :"+i);
                hideItems[i].setAttribute("style", "display:none");
            }
        }
    } catch (e) {
        console.log("when selectab have error : "+e);
    }
    //
    // display menu of tab
    //
    var items = document.getElementsByName("group" + name);
    if (items == null || items.length == 0) {
        name = "0";
        items = document.getElementsByName("group" + name);
    }
    var i = 0;
    for (i = 0; i < items.length; i++) {
        items[i].setAttribute("style", "display:''");
    }
    var displayTab = document.getElementsByName("li" + name);
    displayTab[0].setAttribute("class", "selected");
    localStorage.currentItem = name;

    if (menuId != null) {
        gotoMenu(menuId, url);
    }
};

menu.drawOfficeMenu = function(tabId, contentId, menu) {
	
    var headerTab = document.getElementById(tabId);
    var contentMenu = document.getElementById(contentId);
    var i = 0;
    var countMenu = 0;
    for (i = 0; i < menu.lstMenu.length; i++) {
        try {
            var item = menu.lstMenu[i];
            if (!item)
                continue;
            var tabItem = document.createElement("li");
            tabItem.innerHTML = item.menuName;
            tabItem.setAttribute("name", "li" + i);
            var shortUrl = "";
            var shortMenu = null;
            headerTab.appendChild(tabItem);
            var j = 0;
            for (j = 0; j < item.lstMenu.length; j++) {
                var groupItem = item.lstMenu[j];
                var divGroup = document.createElement("div");
                divGroup.setAttribute("class", "group");
                divGroup.setAttribute("name", "group" + i);
                divGroup.setAttribute("id", "menu" + groupItem.menuId);
                divGroup.setAttribute("style", "display:none");
                if (groupItem.lstMenu == null || groupItem.lstMenu.length <= 1) {
                    var url = getImageURLOfFunction(groupItem.menuUrl);
                    var img = document.createElement("img");
                    img.setAttribute("id", "img"+groupItem.menuId);
                    img.setAttribute("src", url);
                    //img.setAttribute("style", "width:55px;height:55px");
                    divGroup.appendChild(img);
                    divGroup.setAttribute("onclick", "gotoMenu(" + groupItem.menuId + ",'" + groupItem.menuUrl + "');");
                    if (shortUrl === "") {
                        shortUrl = groupItem.menuUrl;
                        shortMenu = groupItem.menuId;
                    }
//                    divGroup.onclick = new function(){
//                        gotoMenu(groupItem.menuUrl);
//                    };

                } else {
                    var h = 0;
                    for (h = 0; h < groupItem.lstMenu.length; h++) {
                        var divItem = document.createElement("div");
                        divItem.setAttribute("class", "item");
                        var url = getImageURLOfFunction(groupItem.lstMenu[h].menuUrl);
                        var img = document.createElement("img");
                        img.setAttribute("src", url);
                        img.setAttribute("id", "img"+groupItem.menuId);
                        divItem.appendChild(img);
                        var br = document.createElement("br");
                        divItem.appendChild(br);
                        var divTitle = document.createElement("div")
                        divTitle.innerHTML = groupItem.lstMenu[h].menuName;
                        divItem.appendChild(divTitle);
                        divItem.setAttribute("onclick", "gotoMenu(" + groupItem.menuId + ",'" + groupItem.menuUrl + "');");
                        divGroup.appendChild(divItem);
                        if (shortUrl === "") {
                            shortUrl = groupItem.menuUrl;
                            shortMenu = groupItem.menuId;
                        }
                    }
                }
                var bottomTitle = document.createElement("div");
                if (groupItem.lstMenu != null && groupItem.lstMenu.length > 1) {
                    bottomTitle.setAttribute("class", "title");
                } else {
                    bottomTitle.setAttribute("class", "function");
                }

                var title = groupItem.menuName;
                bottomTitle.innerHTML = title;
                divGroup.appendChild(bottomTitle);
                //divGroup.setAttribute("title",groupItem[1]);
                contentMenu.appendChild(divGroup);
            }
            
            tabItem.setAttribute("onclick", "selectTab(" + i + "," + shortMenu + ",'" + shortUrl + "')");

            countMenu = countMenu + 1;
        } catch (e) {
        }
    }

    var item = localStorage.currentItem;
    if (item != null) {
        selectTab(item);
    } else {
        selectTab('0');
    }
};
// hiển thị thông báo nội bộ
createNotifysAlert = function(){
   
    var lstNotify = page.lstNotifyAlert;
    var home = document.getElementById("homeAlertDiv");
    var homeParent = document.getElementById("homeParentDiv");
    if (lstNotify == null || lstNotify.length == 0) {
        homeParent.style.display = "none";
        return;
    }
    for (var i = 0; i < lstNotify.length; i++) {
        var notify = lstNotify[i];
        var notifyDiv = document.createElement("div");
        notifyDiv.setAttribute("class", "notifyItemAlert");
        var titleDiv = document.createElement("div");
        var title = notify.title;
        titleDiv.setAttribute("class", "notifyItemAlert");
        titleDiv.innerHTML = escapeHtml_(title);
        notifyDiv.appendChild(titleDiv);

        notifyDiv.setAttribute("onclick", "viewNotifyAlert(" + notify.notifyId + "," + notify.objectType + ");");
        home.appendChild(notifyDiv);
    }
    viewNotifyAlert = function(notifyId, objectType) {
        var param = {
            notifyId: notifyId,
            objectType: objectType
        };
        var wgt = zk.Widget.$("$homeWnd");
        zAu.send(new zk.Event(wgt, 'onOpenViewAlert', param));

    }
};

createNotifys = function() {
//    objects = objects.replace(/\'/g,"");
//    console.log(objects);
//    objects = objects.replace(/\n/g, "<br/>");
//    objects = objects.replace(/\r/g, "");
    var lstNotify = page.lstNotify;
//    if (homePage.lstNotify == null) {
//        homePage.lstNotify = lstNotify;
//    } else {
//        homePage.lstNotify.concat(lstNotify);
//    }
    var home = document.getElementById("homeContentDiv");
    if (lstNotify == null || lstNotify.length == 0) {
        var notifyDiv = document.createElement("div");
        notifyDiv.innerHTML = "Không có thông báo nào";
        notifyDiv.setAttribute("style", "border:1px solid gray; background-color: rgb(167, 190, 196); margin-bottom:5px; text-align:center; padding:10px; height:50px;color:white")
        home.appendChild(notifyDiv);
        document.getElementById("btnNext").style.display = "none";
        console.log("menu.js : No notify");
        return;
    }
    document.getElementById("btnNext").style.display = "";
    for (var i = 0; i < lstNotify.length; i++) {
        var notify = lstNotify[i];
        var notifyDiv = document.createElement("div");
        var signDiv = document.createElement("div");
        signDiv.setAttribute("class", "signOfNotifyItem");
        var color = "rgb(167, 190, 196)";
        if (notify.objectType == 1 || notify.objectType == 300) {
            //
            // Van ban den
            //
            color = "rgb(115, 195, 126)";
        } else if (notify.objectType == 2 || notify.objectType == 600) {
            //
            // Van ban du thao
            //
            color = "rgb(116, 116, 116)";
        } else if (notify.objectType == 30) {
            //
            // Ho so
            //
            color = "rgb(230, 142, 138)";
        } else if (notify.objectType == 9) {
            //
            // Lich
            //
            color = "rgb(83, 143, 215)";
        }
        signDiv.setAttribute("style", "background-color:" + color);
        notifyDiv.appendChild(signDiv);
        notifyDiv.setAttribute("class", "notifyItem");
        var img = document.createElement("img");
        img.setAttribute("src", "Share/avatar/" + notify.sendUserId);
        img.setAttribute("width", "60px");
        img.setAttribute("height", "60px");
        img.setAttribute("style", "float:left;");
        notifyDiv.appendChild(img);
        var titleDiv = document.createElement("div");
        var datemlsc = Date.parse(notify.sendTime);
        var date = new Date(datemlsc);
        var dateStr = date.getHours() + "h" + date.getMinutes() + " ngày " + date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
        var title = notify.sendUserName + " gửi cho bạn vào lúc " + dateStr;
        titleDiv.setAttribute("class", "titleItems");
        titleDiv.innerHTML = escapeHtml_(title);
        var contentDiv = document.createElement("div");
        contentDiv.setAttribute("class", "contentItems");
        var content = escapeHtml_(notify.content);
        content = content.replace(/\n/g, "<br/>");
        contentDiv.innerHTML = content;
        notifyDiv.appendChild(titleDiv);
        notifyDiv.appendChild(contentDiv);

        notifyDiv.setAttribute("onclick", "viewNotify(" + notify.objectId + "," + notify.objectType + ");");
        home.appendChild(notifyDiv);

    }

    viewNotify = function(objectId, objectType) {
        var param = {
            objectId: objectId,
            objectType: objectType
        };
        var wgt = zk.Widget.$("$homeWnd");
        zAu.send(new zk.Event(wgt, 'onOpenView', param));

    }
};
