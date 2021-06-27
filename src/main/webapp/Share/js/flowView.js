/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
drawFlowView = function(id) {
    this.maxHeight = 0;
    this.maxWidth = 0;
    this.updateNewSize = false;
    this.id = id;
    this.lstProcess = null;
    this.setProcess = function(lstProcess) {
        this.lstProcess = lstProcess;
        for (var i = 0; i < this.lstProcess.length; i++) {
            if (this.lstProcess[i].sendGroupId === undefined) {
                this.lstProcess[i].sendGroupId = null;
            }
            if (this.lstProcess[i].sendGroup === undefined) {
                this.lstProcess[i].sendGroup = "";
            }
            if (this.lstProcess[i].sendUserId === undefined) {
                this.lstProcess[i].sendUserId = null;
            }
            if (this.lstProcess[i].sendUser === undefined) {
                this.lstProcess[i].sendUser = "";
            }
            if (this.lstProcess[i].receiveGroupId === undefined) {
                this.lstProcess[i].receiveGroupId = null;
            }
            if (this.lstProcess[i].receiveGroup === undefined) {
                this.lstProcess[i].receiveGroup = "";
            }
            if (this.lstProcess[i].receiveUserId === undefined) {
                this.lstProcess[i].receiveUserId = null;
            }
            if (this.lstProcess[i].receiveUser === undefined) {
                this.lstProcess[i].receiveUser = "";
            }
        }
    }
    
    this.resize = function(width, height) {
        this.fixedWidth = width;
        this.fixedHeight = height;
        this.drawProcess();
    };

    this.drawProcess = function() {
        var canvas = document.getElementById(this.id);
        canvas.width = this.fixedWidth;
        canvas.height = this.fixedHeight;
        var nodeWidth = 80;
        var nodeHeight = 80;

        //canvas.setAttribute("width", 800);
        //context.globalAlpha = 0.8;
        var context = canvas.getContext("2d");
        context.font = "bold 10pt Times New Roman";

        if (this.lstProcess == null || this.lstProcess.length == 0) {
            return;
        }
        //
        // process is first item because list is ordered by time and processid
        //
        var lstActor = [];
        var lstHeight = [10];
        var firstActor = {
            deptId: this.lstProcess[0].sendGroupId,
            deptName: this.lstProcess[0].sendGroup,
            userId: this.lstProcess[0].sendUserId,
            userName: this.lstProcess[0].sendUser,
            level: 0,
            x: 10,
            y: 50,
            status:this.lstProcess[0].status
        };
        
        var backupProcess = this.lstProcess.slice(0);

        if (firstActor.deptId === null) {
            firstActor.deptId = this.lstProcess[0].receiveGroupId;
            firstActor.deptName = this.lstProcess[0].receiveGroup;
            firstActor.userId = this.lstProcess[0].receiveUserId;
            firstActor.userName = this.lstProcess[0].receiveUser;
            this.lstProcess.splice(0, 1);
        }

        var widthStep = 150;
        var heightStep = 150;
        lstActor.push(firstActor);
        var i = 0;
        console.log("before while");
        while (i < lstActor.length) {
            console.log("index of actor" + i);
            var drawItem = lstActor[i];
            //
            // Ve item
            //
            this.drawActor(context, lstActor[i]);
            //
            // Nap them cac actor
            //
            for (var j = 0; j < this.lstProcess.length; j++) {
                var item = this.lstProcess[j];
                console.log("index of process" + j);
                console.log(item.sendGroupId + "==" + drawItem.deptId);
                if (item.sendGroupId === drawItem.deptId) {
                    console.log("equals dept");
                    console.log(item.sendUserId + "==" + drawItem.userId);
                    if (item.sendUserId !== drawItem.userId) {
                        console.log("not equals user continue");
                        continue;
                    }
                    if (lstHeight.length <= drawItem.level + 1) {
                        lstHeight.push(50);
                    }
                    console.log("push new");
                    var newActor = {
                        deptId: item.receiveGroupId,
                        deptName: item.receiveGroup,
                        userId: item.receiveUserId,
                        userName: item.receiveUser,
                        level: lstActor[i].level + 1,
                        x: lstActor[i].x + widthStep,
                        y: lstHeight[lstActor[i].level + 1],
                        status: item.status
                    };
                    // 
                    //
                    var x1 = lstActor[i].x + nodeWidth / 2;
                    var y1 = lstActor[i].y + nodeHeight / 2; 
                    var x2 = newActor.x + nodeWidth / 2;
                    var y2 = newActor.y + nodeHeight / 2;
                    this.drawArrow(context, x1,y1,x2,y2, item.actionName, item.actionType, item.processType);
                    lstHeight[lstActor[i].level + 1] += heightStep;
                    lstActor.push(newActor);
                    this.lstProcess.splice(j, 1);
                    j--;
                }
            }
            i++;
        }
        
        this.lstProcess = backupProcess;
        
        if(this.updateNewSize){
            var canvas = document.getElementById(this.id);
//            var style = "width:"+this.maxWidth+"px;"+"height:"+this.maxHeight+"px";
//            canvas.setAttribute("style",style);
            canvas.style.width = this.maxWidth+"px";
            canvas.style.height = this.maxHeight+"px";
            this.updateNewSize = false;
            this.resize(this.maxWidth,this.maxHeight);
        }
    };

    this.findRootItem = function() {
        for (var i = 0; i < this.lstProcess.length; i++) {
            if (this.lstProcess[i].sendGroupId === undefined && this.lstProcess[i].sendUserId === undefined) {
                var firstActor = {
                    deptId: this.lstProcess[i].sendGroupId,
                    deptName: this.lstProcess[i].sendGroup,
                    userId: this.lstProcess[i].sendUserId,
                    userName: this.lstProcess[i].sendUser,
                    level: 0,
                    x: 10,
                    y: 10
                }
                this.lstProcess.splice(i, 1);
                return firstActor;
            }
        }
        //
        // Ko co item nao thoa man
        //
    }

    drawRect = function(context, left,top, right, bottom) {
        var radius = 10;
        context.moveTo(left + radius, top);

        context.lineTo(right - radius, top);
        //context.moveTo(node.x+nodeWidth-3,node.y+3);
        context.arc(right - radius, top + radius, radius, 3 * Math.PI / 2, 2 * Math.PI);

        context.lineTo(right, bottom - radius);
        //context.moveTo(node.x+nodeWidth-3,node.y+nodeHeight-3);
        context.arc(right - radius, bottom - radius, radius, 0, Math.PI / 2);

        context.lineTo(left + radius, bottom);
        //context.moveTo(node.x+3,node.y+nodeHeight-3);
        context.arc(left + radius, bottom - radius, radius, Math.PI / 2, Math.PI);

        context.lineTo(left, top + radius);
        //context.moveTo(node.x+3,node.y+3);
        context.arc(left + radius, top + radius, radius, Math.PI, 3 * Math.PI / 2);
        context.stroke();
    };

    this.drawActor = function(context, item) {
        var imgUrl = "";
        var name = "";
        var imgWidth = 60;
        var imgHeight = 60;
        context.beginPath();
        if (item.userName === "") {
            imgUrl = "/Share/img/menu/department.png";
            name = item.deptName;
        } else {
            imgUrl = "/Share/avatar/" + item.userId;
            name = item.userName;
        }

        var img = new Image();
        img.onload = function() {
            if(item.status == 5 || item.status == 13){
                context.strokeStyle = "rgb(0,255,0,0)";
                context.fillStyle = "rgba(0,255,0,0.3)";
            } else if(item.status == 4 || item.status == 6 
                    || item.status == 9 || item.status == 3 
                    || item.status == 14 ){
                context.strokeStyle = "rgb(255,0,0)";
                context.fillStyle = "rgba(255,0,0,0.3)";                
            } else {
                context.strokeStyle = "rgb(0,0,255)";
                context.fillStyle = "rgba(0,0,255,0.3)";
            }
            drawRect(context,item.x,item.y,item.x+imgWidth,item.y+imgHeight);
            context.drawImage(img, item.x, item.y, imgWidth, imgHeight);
        };
        img.onerror = function() {
            img.src = "/Share/avatar/default-avatar.png";
        }
        img.src = imgUrl;
        var textWidth = context.measureText(name).width;
        context.fillText(name, item.x + (imgWidth / 2) - (textWidth / 2), item.y + imgHeight+10);

        context.closePath();
        
        if(item.x + (imgWidth/2)>this.maxWidth){
            this.maxWidth = item.x + (imgWidth/2);
            this.updateNewSize = true;
        } 
        
        if( item.y + imgHeight+10 > this.maxHeight){
            this.maxHeight = item.y + imgHeight+10;
            this.updateNewSize = true;            
        }

    };

    this.drawArrow = function(context, x1, y1, x2, y2, action, type, processType) {
        context.beginPath();
        if (type == 2) {
            context.setLineDash([5, 2]);
        }
        if(processType == 0){
            //context.strokeStyle = "rgb(0,0,255)";
        } else if(processType == 1){
            context.strokeStyle = "rgb(0,0,255)";            
        } else {
            context.strokeStyle = "rgb(0,255,0)";
        }
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        
        var xc = (x1 + x2) / 2;
        var yc = (y1 + y2) /2 ;
        
        var arg = Math.atan((y1 - y2) / (x2 - x1));

        var arg1 = arg + Math.PI / 6;
        var arg2 = arg - Math.PI / 6;
        var anchorLenght = 10;

        var arrowX1;
        var arrowY1;
        var arrowX2;
        var arrowY2;
        if (x2 >= x1) {
            arrowX1 = xc - Math.cos(arg1) * anchorLenght;
            arrowY1 = yc + Math.sin(arg1) * anchorLenght;
            arrowX2 = xc - Math.cos(arg2) * anchorLenght;
            arrowY2 = yc + Math.sin(arg2) * anchorLenght;
        } else {
            arrowX1 = xc + Math.cos(arg1) * anchorLenght;
            arrowY1 = yc - Math.sin(arg1) * anchorLenght;
            arrowX2 = xc + Math.cos(arg2) * anchorLenght;
            arrowY2 = yc - Math.sin(arg2) * anchorLenght;
        }

        context.moveTo(xc, yc);
        context.lineTo(arrowX1, arrowY1);
        context.moveTo(xc, yc);
        context.lineTo(arrowX2, arrowY2);
        
        context.stroke();
        if (type == 2) {
            context.setLineDash([0, 0]);
        }
        context.closePath();
        xc = (x1 + x2) / 2;
        yc = (y1 + y2) / 2;
        if (action === undefined) {
            action = "Chuyển";
        }
        var textLength = context.measureText(action).width;
        xc = xc - textLength / 2;
        context.fillText(action, xc, yc);
    }

    this.checkIEVersion = function() {
        var bReturn = false;

        var BrowserDetect = {
            init: function() {
                this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
                this.version = this.searchVersion(navigator.userAgent)
                        || this.searchVersion(navigator.appVersion)
                        || "an unknown version";
                this.OS = this.searchString(this.dataOS) || "an unknown OS";
            },
            searchString: function(data) {
                for (var i = 0; i < data.length; i++) {
                    var dataString = data[i].string;
                    var dataProp = data[i].prop;
                    this.versionSearchString = data[i].versionSearch || data[i].identity;
                    if (dataString) {
                        if (dataString.indexOf(data[i].subString) != -1)
                            return data[i].identity;
                    }
                    else if (dataProp)
                        return data[i].identity;
                }
            },
            searchVersion: function(dataString) {
                var index = dataString.indexOf(this.versionSearchString);
                if (index == -1)
                    return;
                return parseFloat(dataString.substring(index + this.versionSearchString.length + 1));
            },
            dataBrowser: [
                {
                    string: navigator.userAgent,
                    subString: "Chrome",
                    identity: "Chrome"
                },
                {string: navigator.userAgent,
                    subString: "OmniWeb",
                    versionSearch: "OmniWeb/",
                    identity: "OmniWeb"
                },
                {
                    string: navigator.vendor,
                    subString: "Apple",
                    identity: "Safari",
                    versionSearch: "Version"
                },
                {
                    prop: window.opera,
                    identity: "Opera",
                    versionSearch: "Version"
                },
                {
                    string: navigator.vendor,
                    subString: "iCab",
                    identity: "iCab"
                },
                {
                    string: navigator.vendor,
                    subString: "KDE",
                    identity: "Konqueror"
                },
                {
                    string: navigator.userAgent,
                    subString: "Firefox",
                    identity: "Firefox"
                },
                {
                    string: navigator.vendor,
                    subString: "Camino",
                    identity: "Camino"
                },
                {// for newer Netscapes (6+)
                    string: navigator.userAgent,
                    subString: "Netscape",
                    identity: "Netscape"
                },
                {
                    string: navigator.userAgent,
                    subString: "MSIE",
                    identity: "Explorer",
                    versionSearch: "MSIE"
                },
                {
                    string: navigator.userAgent,
                    subString: "Gecko",
                    identity: "Mozilla",
                    versionSearch: "rv"
                },
                {// for older Netscapes (4-)
                    string: navigator.userAgent,
                    subString: "Mozilla",
                    identity: "Netscape",
                    versionSearch: "Mozilla"
                }
            ],
            dataOS: [
                {
                    string: navigator.platform,
                    subString: "Win",
                    identity: "Windows"
                },
                {
                    string: navigator.platform,
                    subString: "Mac",
                    identity: "Mac"
                },
                {
                    string: navigator.userAgent,
                    subString: "iPhone",
                    identity: "iPhone/iPod"
                },
                {
                    string: navigator.platform,
                    subString: "Linux",
                    identity: "Linux"
                }
            ]

        };

        BrowserDetect.init();
        if (BrowserDetect.browser == "Firefox") {
            if (BrowserDetect.version > 9) {
                bReturn = true;
            }
        } else if (BrowserDetect.browser == "Chrome") {
            if (BrowserDetect.version > 9) {
                bReturn = true;
            }
        } else if (BrowserDetect.browser == "Explorer") {
            if (BrowserDetect.version > 9) {
                bReturn = true;
            }
        }
        //            alert(BrowserDetect.browser +"-"+BrowserDetect.version);

        return bReturn;
    };


};

drawCurrentProcess = function(canvasId) {
    /*
     * Khoi tao cac gia tri
     */
    var graph = new drawFlowView(canvasId);
    if (!graph.checkIEVersion())
        return;

    var canvas = document.getElementById(canvasId);
    var maxWidth = canvas.clientWidth;
    var maxHeight = canvas.clientHeight;

    graph.maxWidth = canvas.clientWidth;
    graph.maxHeight = canvas.clientHeight;

    document.getElementById(canvasId).onmousedown = function(e) {
        var dataFlow = document.getElementById(canvasId);
        var pos = findPos(dataFlow);
        var canvas_x = e.pageX - pos.x;
        var canvas_y = e.pageY - pos.y;
        //graph.clickInGraph(canvas_x, canvas_y);
        return false;
    };

    document.getElementById(canvasId).onmousemove = function(e) {
        var dataFlow = document.getElementById(canvasId);
        var pos = findPos(dataFlow);
        var canvas_x = e.pageX - pos.x;
        var canvas_y = e.pageY - pos.y;
        //console.log(canvas_x+":"+ canvas_y);
        //graph.whereIsActiveNode(canvas_x, canvas_y);
    };

    graph.resize(maxWidth, maxHeight);//mo rong de hien thi tooltip
    //graph.addNewNode("Văn thư", 2);
    //graph.addNewNode("Ban 1", 1);
    //page.loadNodeOfFlow();
    return graph;
};