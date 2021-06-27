/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var toolBar = new Object();
toolBar.selection = null;
toolBar.widget = null;
escapeHtml_ = function(str) {
    var result = "";
    if (str != null && str != "") {
        result = str.toString().replace(/&/g, '&amp;');
        result = result.replace(/</g, '&lt;');
        result = result.replace(/>/g, '&gt;');
        result = result.replace(/'/g, '&39;');
        result = result.replace(/"/g, '&quot;');
    }
    return result;
}
addDay = function(date, day) {
    var time = date.getTime();
    time = time + day * 24 * 60 * 60 * 1000;
    var newDate = new Date();
    newDate.setTime(time);
    return newDate;
};

addToolbar = function(imgUrl, title, action) {
    var img = document.createElement("img");
    img.setAttribute("src", imgUrl);
    img.setAttribute("class", "img-cal");
    img.setAttribute("title", title);
    action= escapeHtml_(action);
    img.setAttribute("onclick", "sendAction('" + action + "')");
    var bar = document.getElementById("toolBar");
    bar.appendChild(img);
};
addToolbarButton = function(imgUrl, title, action) {
    var img = document.createElement("button");
    img.setAttribute("class", "btnAction");
    img.setAttribute("title", title);
//    img.innerHTML=title;
    img.setAttribute("style", "background-color:#2b6fc2;background-image: url(Share/img/add.png);background-repeat:no-repeat;padding-left:24px;cursor: pointer;");
    action= escapeHtml_(action);
    img.setAttribute("onclick", "sendAction('" + action + "')");
    var bar = document.getElementById("toolBar");
    
    bar.appendChild(img);
};



addPrintToolbar = function(){
    var img = document.createElement("img");
    img.setAttribute("src", "Share/img/icon/print.gif");
    img.setAttribute("title", "In ấn");
    img.setAttribute("class", "img-cal");
    img.setAttribute("onclick", "printPage();");
    var bar = document.getElementById("toolBar");
    bar.appendChild(img);
    
}

printPage = function(){
    document.getElementById("topDiv").style.display="none";
    document.getElementById("toolBar").style.display="none";;
    window.print();
    document.getElementById("topDiv").style.display="";
    document.getElementById("toolBar").style.display="";;
}

toolBarCheckSearch = function(event) {
    if (event.keyCode === 13) {
        toolBarSearch();
    }
};

sendAction = function(action) {
    var wgt = zk.Widget.$("$" + toolBar.widget);
    zAu.send(new zk.Event(wgt, action, null));
};

toolBarSearch = function() {
    if (toolBar.widget !== null) {
        var text = document.getElementById("searchFullText").value;
        var data = {
            searchText: text
        };
        var wgt = zk.Widget.$("$" + toolBar.widget);
        zAu.send(new zk.Event(wgt, 'onSearchFullText', data));
    }
};

changeWorkingDay = function(type) {
    try {
        if (toolBar.selection !== null) {
            if (document.getElementById("workingTime" + toolBar.selection) !== null)
            {
                document.getElementById("workingTime" + toolBar.selection).style.color="";
            }
        }
        toolBar.selection = type;
        var obj = document.getElementById("workingTime" + toolBar.selection);
        if (obj !== null)
        { //obj.setAttribute("style", "background-color:lightcoral");
                obj.style.color="lightcoral";
        }
    }
    catch (en) {
        console.log(en);
    }

    var fromDate = new Date();
    var toDate = new Date();
    var year = fromDate.getFullYear();
    var month = fromDate.getMonth();
    var day = fromDate.getDay();

    switch (type) {
        case 0:
            break;
        case 1:
            break;
        case 2:    // HOM QUA
            fromDate = addDay(new Date(), -1);
            toDate = addDay(new Date(), -1);
            break;
        case 3:    // TUAN NAY
            fromDate = addDay(new Date(), -1 * day);
            toDate = addDay(new Date(), 6 - day);
            break;
        case 4:    // TUAN TRUOC 
            fromDate = addDay(new Date(), -1 * (day + 7));
            toDate = addDay(new Date(), -1 - day);
            break;
        case 5:     // THANG NAY
            fromDate = new Date(year, month, 1, 0, 0, 0, 0);
            month = month + 1;
            if (month === 12) {
                month = 0;
                year = year + 1;
            }
            toDate = new Date(year, month, 1, 0, 0, 0, 0);
            toDate = addDay(toDate, -1);
            break;
        case 6:     // THANG TRUOC
            toDate = new Date(year, month, 1, 0, 0, 0, 0);
            month = month - 1;
            if (month < 0) {
                month = 11;
                year = year - 1;
            }
            fromDate = new Date(year, month, 1, 0, 0, 0, 0);
            toDate = addDay(toDate, -1);
            break;
        case 7:     // NAM NAY
            toDate = new Date(year, 11, 31, 0, 0, 0, 0);
            fromDate = new Date(year, 0, 1, 0, 0, 0, 0);
            break;
        case 8:     // NAM TRUOC
            toDate = new Date(year - 1, 11, 31, 0, 0, 0, 0);
            fromDate = new Date(year - 1, 0, 1, 0, 0, 0, 0);
            break;
        default:
            fromDate = addDay(new Date(), -1 * day);
            toDate = addDay(new Date(), 6 - day);
            break;

    }
    //var text = document.getElementById("searchFullText").value;
    var data = {
        //searchText: text,
        fromDate: fromDate.getTime(),
        toDate: toDate.getTime()
    };

    if (toolBar.widget !== null) {
//        var main = zk.Widget.$("bodyContent");
//        if(main == null){
//            alert("main is null")
//        }
        var wgt = zk.Widget.$("$" + toolBar.widget);
        if (wgt != null) {
            zAu.send(new zk.Event(wgt, 'onChangeTime', data, {toServer: true}));
        } else {
            console.log("toolbar.js: can't find widget");
        }

    }

};
