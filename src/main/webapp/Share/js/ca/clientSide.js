/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function initPlugin() {
    if (!(VtPlugin.initPlugin()) || VtPlugin.getVersion() != '1.1.0.0') {
        alert('Ban can cai dat plugin ViettelCASigner1');
        
        window.open("/Share/ca/ViettelCASigner.msi");
        return false;
    }
    return true;
}

function getFileName() {
    if (!initPlugin())
        return false;
    var filepath = document.getElementById("filepath");

    filepath.value = new String(VtPlugin.getFileName());
}
function uploadFile() {
    var wgt = zk.Widget.$("caIntegrationForm");
    var filepath = document.getElementById("filepath").value;
    areaId.innerHTML = "";
    var fileName = filepath.replace(/^.*[\\\/]/, '');
    var ext = fileName.substr(fileName.lastIndexOf('.') + 1).toLowerCase();
    var dataFile;
    if (ext === 'pdf') {
        dataFile = new String(VtPlugin.signPdf(filepath, "Ky file PDF", "Hanoi, Vietnam"));
    } else if (ext === 'xlsx' || ext.toLowerCase() === 'docx') {
        dataFile = new String(signOOXml(filepath));
    } else if (ext === 'xml') {
        dataFile = new String(signXml(filepath));
    } else {
        alert('file type is not supported');
        return false;
    }
    dataFile = dataFile.replace(/\n|\r/g, "");
    zAu.send(new zk.Event(wgt, 'onUpload', dataFile));
    //sendRequest(fileName, dataFile);
}

//function sendRequest(fileName, dataFile) {
//    var actionUrl = "http://localhost:8080/voffice/Pages/module/ca/integrateCA.zul";
//    var xhr = null;
//    if (window.XMLHttpRequest)
//    {// code for IE7+, Firefox, Chrome, Opera, Safari
//        xhr = new XMLHttpRequest();
//    }
//    else
//    {// code for IE6, IE5
//        xhr = new ActiveXObject("Microsoft.XMLHTTP");
//    }
//    xhr.onreadystatechange = function ()
//    {
//        if (xhr.readyState == 4) {
//            if (xhr.status == 200) {
//                areaId.innerHTML = xhr.responseText;
//            }
//        }
//
//        xhr.onerror = function () {
//            alert("Error! Upload failed. Can not connect to server.");
//        };
//        xhr.open('POST', actionUrl);
//        xhr.setRequestHeader("fileName", fileName);
//        dataFile = dataFile.replace(/\n|\r/g, "");
//        xhr.send(dataFile);
//        
//        console.log("Sent to server side!");
//    }
//}