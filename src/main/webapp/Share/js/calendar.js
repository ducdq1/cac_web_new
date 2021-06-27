/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var calendar = null;

Date.prototype.addDays = function(day) {
    var ms = this.getTime() + day * 24 * 60 * 60 * 1000;
    var newDate = new Date(ms);
    return newDate;
}

viewCalendar = function(calendarId) {
    var wgt = zk.Widget.$("$" + calendar.calendarView.component);
    zAu.send(new zk.Event(wgt, 'onView', calendarId));
}

createNewCalendar = function(day, hour, minute) {
    day = day - 1;
    if (day < 0) {
        day = 6;
    }
    var date = calendar.calendarView.date.addDays(day);
    date.setHours(hour);
    date.setMinutes(minute);
    var wgt = zk.Widget.$("$" + calendar.calendarView.component);
    zAu.send(new zk.Event(wgt, 'onCreateCalendar', date.getTime()));
};

createNewCalendarForMonth = function(dateOfMonth, month, year) {
    var date = new Date();
    date.setYear(year);
    date.setMonth(month);
    date.setDate(dateOfMonth);
    date.setHours(8);
    date.setMinutes(0);
    var wgt = zk.Widget.$("$" + calendar.calendarView.component);
    zAu.send(new zk.Event(wgt, 'onCreateCalendar', date.getTime()));
};

getString = function(obj) {
    var ret = "";
    if (obj.constructor == String) {
        ret = obj;
    } else if (obj.constructor == Array) {
        ret = obj.join("");
    }

    return sd.validator.trim(ret);
}

convertStringToTime = function(/*String date from DataGrid: yyyy-MM-ddThh:mm:ss*/dgDate) {
    try {
        var date = new Date(dgDate);
        return date;
    } catch (e) {
        //page.alert("Thông báo","function convertStringToDate need parameter format: yyyy-MM-ddThh:mm:ss", "warning");
        return undefined;
    }
};

convertStringToTimeFormat = function(/*String date from DataGrid: yyyy-MM-ddThh:mm:ss*/dgDate) {
    try {
        var date = new Date(dgDate);
        var hour = date.getHours()>=10?date.getHours():"0"+date.getHours();
        var minute = date.getMinutes()>=10?date.getMinutes():"0"+date.getMinutes();
        var str = hour+":"+minute;
        //var str = (time[0].length<2 ? '0'+time[0] : time[0]) + "g" + (time[1].length<2 ? '0'+time[1] : time[1]);
        return str;
    } catch (e) {
        return "";
    }
}

convertStringToDateFormat = function(/*String date from DataGrid: yyyy-MM-ddThh:mm:ss*/dgDate) {
    try {
        var date = new Date(dgDate);
        var year = date.getFullYear();
        var month = date.getMonth()+1;
        var day = date.getDate();
        month = month >=10?month:"0"+month;
        day = day>=10?day:"0"+day;
        var str = day+"/"+month+"/"+year;
        //var str = (time[0].length<2 ? '0'+time[0] : time[0]) + "g" + (time[1].length<2 ? '0'+time[1] : time[1]);
        return str;
    } catch (e) {
        return "";
    }
}

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

onMouseWheel = function() {
    var e = window.event || e; // old IE support
    var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));

    if (delta > 0) {
        var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
        if (scrollTop == 0) {
            if (calendar.calendarView.start > 0) {
                calendar.calendarView.start--;
                calendar.generateWeekCalendar();
                calendar.rendCalendar();
                return false;
            } else {
                calendar.calendarView.start = 7;
                calendar.generateWeekCalendar();
                calendar.previousWeek();
            }
        } else {
            return true;
        }
    } else {
        var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
        var clientHeight = document.body.scrollHeight - document.body.clientHeight;
        if (scrollTop >= clientHeight) {
            calendar.calendarView.start = 7;
            calendar.nextWeek();
            calendar.generateWeekCalendar();
            document.body.scrollTop = 0;
            document.documentElement.scrollTop = 0;
            return false;
        } else {
            //calendar.calendarView.start++;
            //calendar.generateWeekCalendar();
            return true;
        }
    }
};

calendarWeek = function() {
    this.previousWeek = function() {
        this.calendarView.date = this.calendarView.date.addDays(-7);
        this.updateInfo();
        this.getCalendar();
    }

    this.nextWeek = function() {
        this.calendarView.date = this.calendarView.date.addDays(7);
        this.updateInfo();
        this.getCalendar();
    }

    this.updateInfo = function() {
        var startDate = this.calendarView.date;
        var endDate = startDate.addDays(6);
        //dijit.byId("searchForm.startTime").setValue(startDate);
        //dijit.byId("searchForm.endTime").setValue(endDate);
        document.getElementById("displayWeek").innerHTML = startDate.getDate() + "/" + (startDate.getMonth() + 1) + "-" + endDate.getDate() + "/" + (endDate.getMonth() + 1) + "  Năm " + endDate.getFullYear();
        //onSearch();
    }

    this.generateWeekCalendar = function() {
        var i = 0;
        var td = document.getElementById("t7");
        td.innerHTML = "";
        for (i = this.calendarView.start; i < 24; i++) {
            var text = "";
            if (i <= 12) {
                text = i + " am";
            } else {
                text = (i - 12) + " pm";
            }
            var div = document.createElement("div");
            div.innerHTML = text;
            div.setAttribute("class", "timeLineDiv");
            td.appendChild(div);
        }
        var i = 1;
        var j = 0;
        for (i = 0; i <= 6; i++) {
            td = document.getElementById("t" + i);
            td.innerHTML = "";
            var div = document.createElement("div");
            div.setAttribute("style", "display:block;position:relative");
            div.setAttribute("id", "div" + i);
            td.appendChild(div);

            for (j = this.calendarView.start; j < 24; j++) {
                var divOdd = document.createElement("div");
                divOdd.setAttribute("class", "oddDiv");
                divOdd.setAttribute("ondblclick", "createNewCalendar(" + i + "," + j + "," + "0);");
                var divEven = document.createElement("div");
                divEven.setAttribute("class", "evenDiv");
                divEven.setAttribute("ondblclick", "createNewCalendar(" + i + "," + j + "," + "30);");
                td.appendChild(divOdd);
                td.appendChild(divEven);
            }
        }
    }

    this.getPosition = function(date) {
        if (date == null)
            return null;
        var hour = date.getHours();
        var minute = date.getMinutes();
        var pos = (hour - this.calendarView.start) * 42 + (minute / 30) * 21;
//        if(pos < 0)
//            pos = -20;
        return pos;
    }

    this.updateCalendarDisplay = function(object) {
        var updateDiv = document.getElementById("cal" + object.calendarId);
        var top = object.top;
        var height = object.height;
        var width = object.width;
        var left = object.left;
        var style = "top:" + top + "px;left:" + left + "%;height:" + height + "px;width:" + width + "%";
        updateDiv.setAttribute("style", style);
    }

    this.rendCalendar = function() {
        for (var i = 0; i < this.lstDateCalendar.length; i++) {
            var date = convertStringToTime(this.lstDateCalendar[i].date);
            var divContainer = document.getElementById("div" + date.getDay());
            var colWidth = divContainer.clientWidth;
            divContainer.innerHTML = "";
            var tdContainer = document.getElementById("t0" + date.getDay());
            tdContainer.innerHTML = "";
            if (this.lstDateCalendar[i].lstCalendar == null || this.lstDateCalendar[i].lstCalendar.length == 0) {

            } else {
                for (var j = 0; j < this.lstDateCalendar[i].lstCalendar.length; j++) {
                    var div = document.createElement("div");
                    //alert(headerDiv.innerHTML);
                    //div.innerHTML = this.lstDateCalendar[i].lstCalendar[j].title;
                    var timeStart = convertStringToTime(this.lstDateCalendar[i].lstCalendar[j].timeStart);
                    var timeEnd = convertStringToTime(this.lstDateCalendar[i].lstCalendar[j].timeEnd);
                    var top = this.getPosition(timeStart);
                    var left = 1;
                    var bottom = this.getPosition(timeEnd);
                    var headerDiv = document.createElement("div");
                    if(timeEnd.getDate() != timeStart.getDate() 
                            || timeEnd.getMonth() != timeStart.getMonth() 
                            || timeEnd.getYear() != timeStart.getYear()){
                        top = -1;
                        bottom = 0;
                        headerDiv.innerHTML = convertStringToDateFormat(this.lstDateCalendar[i].lstCalendar[j].timeStart) + "-" + convertStringToDateFormat(this.lstDateCalendar[i].lstCalendar[j].timeEnd);                                                
                    } else {
                        headerDiv.innerHTML = convertStringToTimeFormat(this.lstDateCalendar[i].lstCalendar[j].timeStart) + "-" + convertStringToTimeFormat(this.lstDateCalendar[i].lstCalendar[j].timeEnd);                        
                    }
                    
                    div.appendChild(headerDiv);
                    var height = 0;
                    var width = 95;
                    if(top<0 && bottom>0){
                        top = 0;
                    }
                    if (bottom == null) {
                        height = 41;
                    } else {
                        height = bottom - top;
                    }
                    
                    if(height <=0){
                        height = 3;
                    }
                    

                    for (var h = j - 1; h >= 0; h--) {
                        if(this.lstDateCalendar[i].lstCalendar[h].top<0)
                            continue;
                        if (this.lstDateCalendar[i].lstCalendar[h].bottom >= top) {
                            left += 5;
                            if(left>colWidth-10){
                                left = colWidth-5;
                            }
                            width -= 5;                            
//                            this.lstDateCalendar[i].lstCalendar[h].width -= 10;
//                            if(this.lstDateCalendar[i].lstCalendar[h].width <20){
//                                this.lstDateCalendar[i].lstCalendar[h].width = 20;
//                            }
//                            this.updateCalendarDisplay(this.lstDateCalendar[i].lstCalendar[h]);
                            //break;
                        }
                    }
                    if(width<0){
                        width=0;
                    }
                    this.lstDateCalendar[i].lstCalendar[j].left = left;
                    this.lstDateCalendar[i].lstCalendar[j].top = top;
                    this.lstDateCalendar[i].lstCalendar[j].bottom = bottom;
                    this.lstDateCalendar[i].lstCalendar[j].height = height;
                    this.lstDateCalendar[i].lstCalendar[j].width = width;
                    var style = "top:" + top + "px;left:" + left + "%;height:" + height + "px;width:" + width + "%";
                    div.setAttribute("style", style);
                    //div.setAttribute("height", height+"px");
                    div.setAttribute("class", "divCalendarWeek");
                    div.setAttribute("id", "cal" + this.lstDateCalendar[i].lstCalendar[j].calendarId);
                    div.setAttribute("onclick", "viewCalendar(" + this.lstDateCalendar[i].lstCalendar[j].calendarId + ")");
                    var status = parseInt(this.lstDateCalendar[i].lstCalendar[j].status.toString());
                    if (status == 2) {
                        div.setAttribute("class", "divCalendarWeekApprove");
                    } else if (status > 2) {
                        div.setAttribute("class", "divCalendarWeekReject");
                    } else {
                        div.setAttribute("class", "divCalendarWeek");
                    }
                    var contentDiv = document.createElement("div");
                    contentDiv.innerHTML = escapeHtml_(this.lstDateCalendar[i].lstCalendar[j].title);
                    div.setAttribute("title",escapeHtml_(this.lstDateCalendar[i].lstCalendar[j].title));
                    div.appendChild(contentDiv);
                    if(top<0){
                        console.log(div.innerHTML);
                        div.setAttribute("style","position:relative;margin:1px");
                        console.log(div.getAttribute("style"));
                        var containerTd = document.getElementById("t0" + date.getDay());
                        containerTd.appendChild(div);
                    } else {
                        divContainer.appendChild(div);
                    }
                }
            }
        }
    }

    this.loadCalendar = function() {
        //clearCalendar();
        this.rendCalendar();
    };

    this.initCalendar = function(component) {
        var date = new Date();
        var day = parseInt(date.getDay());
        if (day >= 1) {
            var adding = -1 * day + 1;
            date = date.addDays(adding);
        } else {
            date = date.addDays(-6);
        }
        this.calendarView = new Object();
        this.calendarView.component = component;
        this.calendarView.date = date;
        this.calendarView.start = 7;
        this.generateWeekCalendar();
        this.updateInfo();

        this.getCalendar();
    };

    this.getCalendar = function() {
        var startDate = this.calendarView.date;
        var endDate = this.calendarView.date.addDays(6);
        var text = document.getElementById("searchFullText").value;
        var args = {
            fromDate: startDate.getTime(),
            toDate: endDate.getTime(),
            searchText: text
        };
        var wgt = zk.Widget.$("$" + this.calendarView.component);
        zAu.send(new zk.Event(wgt, 'onSearch', args));
    };

    this.initMouseWheel = function() {
        var div = document.getElementById("weekCalendarDiv");
        if (div.addEventListener) {
            // IE9, Chrome, Safari, Opera
            div.addEventListener("mousewheel", onMouseWheel, false);
            // Firefox
            div.addEventListener("DOMMouseScroll", onMouseWheel, false);
        }
        else
            div.attachEvent("onmousewheel", onMouseWheel);

    };
    
    this.onMouseMove = function(){
        
    }

};

calendarMonth = function() {
    this.previousMonth = function() {
        this.calendarView.month = this.calendarView.month - 1;
        if (this.calendarView.month < 0) {
            this.calendarView.month = this.calendarView.month + 12;
            this.calendarView.year -= 1;
        }
        this.generateMonthCalendar(this.calendarView.month, this.calendarView.year);
        this.getCalendar();
    };

    this.nextMonth = function() {
        this.calendarView.month = this.calendarView.month + 1;
        if (this.calendarView.month > 11) {
            this.calendarView.month = this.calendarView.month - 12;
            this.calendarView.year += 1;
        }
        this.generateMonthCalendar(this.calendarView.month, this.calendarView.year);
        this.getCalendar();
    }

    this.generateMonthCalendar = function(month, year) {
        this.calendarView.month = month;
        this.calendarView.year = year;
        document.getElementById("displayMonth").innerHTML = "Tháng " + (month + 1) + " Năm " + year;
        var table = document.getElementById("calendarMonth").getElementsByTagName("tbody")[0];
        var rows = table.getElementsByTagName("tr");
        //
        // remove old month date
        //
        var i = 0;
        for (i = rows.length - 1; i > 0; i--) {
            table.removeChild(rows[i]);
        }
        var startDate = new Date(year, month, 1);
        if (startDate.getDay() > 1) {
            startDate = startDate.addDays(-1 * startDate.getDay() + 1);
        } else if (startDate.getDay() === 0) {
            startDate = startDate.addDays(-6);
        }
        var rowIndex = 0;
        var runDate = startDate;
        do {
            var rowHeader = document.createElement("tr");
            rowHeader.setAttribute("class", "monthHeader");
            var rowContent = document.createElement("tr");
            rowContent.setAttribute("class", "monthContent");
            var i = 0;
            for (i = 0; i < 7; i++) {
                runDate = startDate.addDays(rowIndex * 7 + i);
                var tdHeader = document.createElement("td");
                if (runDate.getDate() == 1) {
                    tdHeader.innerHTML = (runDate.getDate()) + "/" + (runDate.getMonth() + 1);
                } else {
                    tdHeader.innerHTML = (runDate.getDate());
                }
                var tdBody = document.createElement("td");
                var colId = runDate.getDate() + "_" + runDate.getMonth() + "_" + runDate.getFullYear();
                tdBody.setAttribute("id", colId);
                tdBody.setAttribute("ondblclick","createNewCalendarForMonth("+runDate.getDate()+","+runDate.getMonth()+","+runDate.getFullYear()+");");
                rowHeader.appendChild(tdHeader);
                rowContent.appendChild(tdBody);
                if (runDate.getMonth() != this.calendarView.month) {
                    tdBody.setAttribute("class", "noActive");
                }
            }
            table.appendChild(rowHeader);
            table.appendChild(rowContent);
            rowIndex++;
            if (runDate.getFullYear() > this.calendarView.year) {
                break;
            } else if (runDate.getMonth() > this.calendarView.month) {
                break;
            }

        } while (true);
    }

    this.rendCalendar = function() {
        for (var i = 0; i < this.lstDateCalendar.length; i++) {
            var date = convertStringToTime(this.lstDateCalendar[i].date);
            var colId = date.getDate() + "_" + date.getMonth() + "_" + date.getFullYear();
            var col = document.getElementById(colId);
            if(col == null)
                continue;
            col.innerHTML = "";
            if (this.lstDateCalendar[i].lstCalendar == null || this.lstDateCalendar[i].lstCalendar.length == 0) {

            } else {
                for (var j = 0; j < this.lstDateCalendar[i].lstCalendar.length; j++) {
                    var div = document.createElement("div");
                    div.innerHTML = escapeHtml_(this.lstDateCalendar[i].lstCalendar[j].title);
                    //div.setAttribute("class", "divCalendarMonth");
                    div.setAttribute("onclick", "viewCalendar(" + this.lstDateCalendar[i].lstCalendar[j].calendarId + ")");
                    var status = parseInt(this.lstDateCalendar[i].lstCalendar[j].status.toString());
                    if (status == 2) {
                        div.setAttribute("class", "divCalendarMonthApprove");
                    } else if (status > 2) {
                        div.setAttribute("class", "divCalendarMonthReject");
                    } else {
                        div.setAttribute("class", "divCalendarMonth");
                    }
                    col.appendChild(div);
                }
            }
        }
    };

    this.loadCalendar = function() {
        this.rendCalendar();
    };

    this.getCalendar = function() {
        var startDate = new Date(this.calendarView.year, this.calendarView.month, 1);
        var endDate = null;
        if(this.calendarView.month == 11){
            endDate = new Date(this.calendarView.year+1, 0, 1);
        } else {
            endDate = new Date(this.calendarView.year, (this.calendarView.month + 1), 1);
        }
        var text = document.getElementById("searchFullText").value;
        var args = {
            fromDate: startDate.getTime(),
            toDate: endDate.getTime(),
            searchText: text
        };
        var wgt = zk.Widget.$("$" + this.calendarView.component);
        zAu.send(new zk.Event(wgt, 'onSearch', args));
    }

    this.onMouseWheel = function() {
        if (event.wheelDelta > 0) {
            var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
            if (scrollTop == 0) {
                this.previousMonth();
                document.body.scrollTop = 0;
                document.documentElement.scrollTop = 0;
            } else {
                return true;
            }

        } else {
            //alert("herre");
            var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
            var clientHeight = document.body.scrollHeight - document.body.clientHeight;
            //alert(scrollTop+"-"+clientHeight);
            if (scrollTop >= clientHeight) {
                this.nextMonth();
                document.body.scrollTop = 0;
                document.documentElement.scrollTop = 0;
            } else {
                return true;
            }
        }
    }

    this.initCalendar = function(component) {
        var date = new Date();
        this.calendarView = new Object();
        this.calendarView.component = component;
        this.generateMonthCalendar(date.getMonth(), date.getFullYear());
        this.getCalendar();
//        var startDate = new Date(calendar.year, calendar.month, 1);
//        var endDate = new Date(calendar.year, (calendar.month + 1), 1);
//        dijit.byId("searchForm.startTime").setValue(startDate);
//        dijit.byId("searchForm.endTime").setValue(endDate);
//
//        onSearch();
    }

};

calendarList = function() {
    this.previousWeek = function() {
        this.calendarView.date = this.calendarView.date.addDays(-7);
        this.updateInfo();
        this.getCalendar();
    }

    this.nextWeek = function() {
        this.calendarView.date = this.calendarView.date.addDays(7);
        this.updateInfo();
        this.getCalendar();
    }

    this.updateInfo = function() {
        var startDate = this.calendarView.date;
        var endDate = startDate.addDays(6);
        //dijit.byId("searchForm.startTime").setValue(startDate);
        //dijit.byId("searchForm.endTime").setValue(endDate);
        document.getElementById("displayWeek").innerHTML = startDate.getDate() + "/" + (startDate.getMonth() + 1) + "-" + endDate.getDate() + "/" + (endDate.getMonth() + 1) + "  Năm " + endDate.getFullYear();
        //onSearch();
    }

    this.clearCalendar = function() {

        var table = document.getElementById("calendarTable");
        var rows = table.getElementsByTagName("tr");
        for (var i = rows.length - 1; i >= 1; i--) {
            table.removeChild(rows[i]);
        }
    };

    this.rendCalendar = function() {
        this.clearCalendar();
        var table = document.getElementById("calendarTable");
        for (var i = 0; i < this.lstDateCalendar.length; i++) {
            var rowOfDay = document.createElement("tr");
            var colOfRow = document.createElement("td");
            colOfRow.setAttribute("colspan", "7");
            colOfRow.setAttribute("align", "center");
            colOfRow.setAttribute("style", "font-weight:bold");
            colOfRow.setAttribute("class", "dayOfCalendar");
            colOfRow.innerHTML = escapeHtml_(this.lstDateCalendar[i].text);
            rowOfDay.appendChild(colOfRow);
            table.appendChild(rowOfDay);
            if (this.lstDateCalendar[i].lstCalendar == null
                    || this.lstDateCalendar[i].lstCalendar.length == 0) {
                var row = document.createElement("tr");
                var col = document.createElement("td");
                col.innerHTML = "Không có lịch trong ngày";
                col.setAttribute("colspan", "7");
                col.setAttribute("align", "center");
                row.appendChild(col);
                table.appendChild(row);
            } else {
                for (var j = 0; j < this.lstDateCalendar[i].lstCalendar.length; j++) {
                    var item = this.lstDateCalendar[i].lstCalendar[j];
                    var row = document.createElement("tr");
                    var colTime = document.createElement("td");
                    colTime.setAttribute("style", "font-weight:bold");
                    colTime.setAttribute("align", "center");
                    colTime.innerHTML = convertStringToTimeFormat(item.timeStart);

                    //userId
                    //allow edit for create by user
                    var colTitle = document.createElement("td");
                    colTitle.setAttribute("style", "font-weight:bold; color:#7b2e00;cursor:pointer");
                    colTitle.setAttribute("onclick", "viewCalendar("+item.calendarId+");");
                    var titleDiv = document.createElement("div");
                    titleDiv.innerHTML = escapeHtml_(item.title);
                    colTitle.appendChild(titleDiv);
                    var colLocation = document.createElement("td");
                    colLocation.innerHTML =escapeHtml_( item.locationName);
                    var colChiefMan = document.createElement("td");
                    colChiefMan.innerHTML = escapeHtml_(item.manChief);
                    var colParticipant = document.createElement("td");
                    colParticipant.innerHTML =escapeHtml_( item.manParticipant);


                    var colDept = document.createElement("td");
                    colDept.innerHTML =escapeHtml_( item.manPrepare);


                    var colStatus = document.createElement("td");
                    var strStatus = "";
                    if (item.status == null
                            || item.status == 0
                            || item.status == 1) {
                        strStatus = "Chờ duyệt";
                    } else if (item.status == 2) {
                        strStatus = "Đã duyệt";
                    } else if (item.status == 3) {
                        strStatus = "Từ chối";
                    } else if (item.status == 4) {
                        strStatus = "Đã hủy";
                    }
                    colStatus.innerHTML = strStatus;

                    row.appendChild(colTime);
                    row.appendChild(colTitle);
                    row.appendChild(colLocation);
                    row.appendChild(colChiefMan);
                    row.appendChild(colParticipant);
                    row.appendChild(colDept);
                    row.appendChild(colStatus);
                    table.appendChild(row);
                }
            }
        }
    };

    this.loadCalendar = function() {
        //clearCalendar();
        this.rendCalendar();
    };

    this.initCalendar = function(component) {
        var date = new Date();
        var day = parseInt(date.getDay());
        if (day >= 1) {
            var adding = -1 * day + 1;
            date = date.addDays(adding);
        } else {
            date = date.addDays(-6);
        }
        this.calendarView = new Object();
        this.calendarView.component = component;
        this.calendarView.date = date;
        this.calendarView.start = 7;
        this.updateInfo();

        this.getCalendar();
    };

    this.getCalendar = function() {
        var startDate = this.calendarView.date;
        var endDate = this.calendarView.date.addDays(6);
        var text = document.getElementById("searchFullText").value;
        var args = {
            fromDate: startDate.getTime(),
            toDate: endDate.getTime(),
            searchText: text
        };
        var wgt = zk.Widget.$("$" + this.calendarView.component);
        zAu.send(new zk.Event(wgt, 'onSearch', args));
    };

};

//initMouseWheel();
//initCalendar();