/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//service ky so
var webSocketSigner;
var GET_CERT = "GET_CERT";
var GET_CERT_WITH_CHAIN = "GET_CERT_WITH_CHAIN";
var ws_host="ws://localhost:60000/ViettelCASignerService";
var connected=false;
function initPlugin() {
	//var x=LinkCheck("c:\\Windows\\Web\\Wallpaper\\img1.jpg");
	
	if (!(VtPlugin.initPlugin()) || VtPlugin.getVersion() !== '1.1.0.0') {
		alert('Ban can cai dat plugin ViettelCASigner');
		var pathname = window.location.pathname;
		pathname = pathname.replace("index.zul", "");
		var url = pathname + "Share/js/ca/ViettelCASigner.msi"
		window.open(url);
		return false;
	}
	return true;
}

function LinkCheck(url)
{
    var http = new XMLHttpRequest();
    http.open('HEAD', url, false);
    http.send();
    return http.status!=404;
}

function isFirefoxBrower() {
	if (navigator.userAgent.indexOf("Firefox") <= 0) {
		// alert('Chữ ký số chỉ được hỗ trợ trên trình duyệt firefox. Vui lòng
		// sử dụng trình duyệt Firefox để thực hiện ký số.');
		return false;
	}
	return true;
}

function uploadCert() {
	var wgt = zk.Widget.$('$businessWindow');
	try {
		if (!isFirefoxBrower()) {
			chromeUploadCertRegister(wgt);
		} else {

			if (!initPlugin()) {
				alert("Ký số không thành công!");
				return false;
			}
			var base64Cert = new String(VtPlugin.getCert());
			if (base64Cert !== '') {
				base64Cert = base64Cert.replace(/\n|\r/g, "");
				zAu.send(new zk.Event(wgt, 'onUploadCert', base64Cert));
			} else {
				alert("Ký số không thành công!");
				return false;
			}
		}
	} catch (e) {
		alert("Ký số không thành công!");
		return false;
	}
}

function uploadCertRegister() {
	var wgt = zk.Widget.$('$caCRUDWindow');
	if (!isFirefoxBrower()) {
		chromeUploadCertRegister(wgt);
	} else {
		if (!initPlugin())
			return false;
		var base64Cert = new String(VtPlugin.getCert());
		base64Cert = base64Cert.replace(/\n|\r/g, "");
		zAu.send(new zk.Event(wgt, 'onUploadCert', base64Cert));
	}
}

function uploadCertOfFile(fileId) {
	try {
		var wgt_listall = zk.Widget.$('$businessWindow');
		 
		if (!isFirefoxBrower()) {
			 ChromeUploadCertFinalFile(fileId,wgt_listall);
		} else {			
			if (!initPlugin()) {
				alert("Ký số không thành công !");
				return false;
			}
			
			var certChain = new String(VtPlugin.getCertChain());
			if (certChain !== '') {
				certChain = certChain.replace(/\n|\r/g, "");
				var data = {
					certChain : certChain,
					fileId : fileId
				};
				zAu.send(new zk.Event(wgt_listall, 'onUploadCert', data));
			} else {
				alert("Ký số không thành công !");
				return false;
			}
		}

		

	} catch (e) {
		alert("Ký số không thành công !"+e);
		return false;
	}

}

function signAndSubmit() {
	try {
		// var wgt = zk.Widget.$("caIntegrationForm");
		var wgt = zk.Widget.$('$businessWindow');
		// var base64Hash = document.getElementById('base64Hash').value;
		// var certSerial = document.getElementById('certSerial').value;
		var base64Hash = zk.Widget.$('$txtBase64Hash').getValue();
		var certSerial = zk.Widget.$("$txtCertSerial").getValue();
		if (base64Hash !== '' && certSerial !== '') {
			var base64Signature = new String(VtPlugin.signHash(base64Hash,
					certSerial));
			console.log('base64: ' + base64Hash);
			console.log('certSerial: ' + certSerial);
			base64Signature = base64Signature.replace(/\n|\r/g, "");
			zAu.send(new zk.Event(wgt, 'onSign', base64Signature));
		} else {
			alert("Ký số không thành công!");
			return false;
		}
	} catch (e) {

		alert("Ký số không thành công !");
		return false;
	}
};

function signAndSubmitTemp(base64Hash, certSerial) {
	try {
		var wgt = zk.Widget.$('$businessWindow');

		if (base64Hash !== '' && certSerial !== '') {
			if (!isFirefoxBrower()) {
				ChromeSignHash(wgt, base64Hash, certSerial);
			} else {
				var base64Signature = new String(VtPlugin.signHash(base64Hash,
						certSerial));
				base64Signature = base64Signature.replace(/\n|\r/g, "");
				zAu.send(new zk.Event(wgt, 'onSign', base64Signature));
			}

		} else {
			alert("Ký số không thành công!");
			return false;
		}

	} catch (e) {

		alert("Ký số không thành công !");
		return false;
	}
};

function uploadCertFinalFile(fileId) {
	try {
		var wgt_listall = zk.Widget.$('$phamarcyAll');
		var certChain;
		if (!isFirefoxBrower()) {
			ChromeUploadCertFinalFile(fileId, wgt_listall);
		} else {
			
			if (!initPlugin()) {
				alert("Ký số không thành công !");
				return false;
			}
			certChain = new String(VtPlugin.getCertChain());

			if (certChain !== '') {
				certChain = certChain.replace(/\n|\r/g, "");
				var data = {
					fileId : fileId,
					certChain : certChain
				};
				zAu.send(new zk.Event(wgt_listall, 'onUploadCert', data));
			} else {
				// alert("Ký số không thành công3333 !");
				return false;
			}
		}

	} catch (e) {
		alert("Ký số không thành công !");
		return false;
	}

};

function signAndSubmitFinalFileCosmeticAll() {
	try {
		var wgt_listall = zk.Widget.$('$phamarcyAll');
		var base64Hash = zk.Widget.$("$txtBase64Hash").getValue();
		var certSerial = zk.Widget.$("$txtCertSerial").getValue();
		if (base64Hash !== '' && certSerial !== '') {
			if (!isFirefoxBrower()) {
				ChromeSignHash(wgt_listall, base64Hash, certSerial);
			} else {
				var base64Signature = new String(VtPlugin.signHash(base64Hash,
						certSerial));
				base64Signature = base64Signature.replace(/\n|\r/g, "");
				zAu.send(new zk.Event(wgt_listall, 'onSign', base64Signature));
			}
		} else {
			alert("Ký số không thành công!");
			return false;
		}
	} catch (e) {
		alert("Ký số không thành công!");
		return false;
	}
}

// -------------------WEBSOCKET FOR CHROME--------------------//
// DUCQ1
// khoi tao websocket
function initWebSocket() {
	
	 	
		webSocketSigner = new WebSocket(ws_host);
	 
	
	

}

// khoi tao websocket
function webSocketClosed() {	 
	if(connected){
		connected=false;		
	}else{	
	alert("Để ký số trên trình duyệt Chrome, bạn phải cài đặt và chạy chương trình hỗ trợ ký số.");
	var pathname = window.location.pathname;
	pathname = pathname.replace("index.zul", "");
	var url = pathname + "Share/js/ca/ViettelCASigner.msi"
	//window.open(url);
	}
}

// ducdq1:su dung websocket cho chrome
function chromeUploadCertRegister(wgt) {
	initWebSocket();
	webSocketSigner.onopen = function() {
		connected=true;
		webSocketSigner.send(GET_CERT);
	};
	webSocketSigner.onmessage = function(received_msg) {
		try {
			var cert = received_msg.data;
			if (cert.indexOf("ERROR:")==-1) {	
			var base64Cert = cert.replace(/\n|\r/g, "");
			zAu.send(new zk.Event(wgt, 'onUploadCert', base64Cert));
			}
		} catch (err) {
			alert('co loi xay ra');
		}
	};

	webSocketSigner.error = function() {
		alert('co loi xay ra');
	};

	webSocketSigner.onclose = function() {
		webSocketClosed();
	};
}

// ducdq1:su dung websocket cho chrome
function ChromeUploadCertFinalFile(fileId, wgt_listall) {
	initWebSocket();	 
	webSocketSigner.onopen = function() {	
		connected=true;
		webSocketSigner.send(GET_CERT_WITH_CHAIN);
	};
	webSocketSigner.onmessage = function(received_msg) {
		try {
			var certChain = received_msg.data
			
			if (certChain.indexOf("ERROR:")==-1) {
				
				certChain = certChain.replace(/\n|\r/g, "");
				var data = {
					fileId : fileId,
					certChain : certChain
				};
				
				zAu.send(new zk.Event(wgt_listall, 'onUploadCert', data));
			}

		} catch (err) {
			alert('co loi xay ra');
		}
	};

	webSocketSigner.error = function() {		 
		alert('co loi xay ra');
	};

	webSocketSigner.onclose = function() {		
		webSocketClosed();
	};
	
}

// ducdq1:su dung websocket cho chrome
function ChromeSignHash(wgt_listall, base64Hash, certSerial) {
	initWebSocket();
	webSocketSigner.onopen = function() {
		connected=true;
		webSocketSigner.send("BASE64_HASH=" + base64Hash + "SERIAL_NUMBER="
				+ certSerial);
	};
	webSocketSigner.onmessage = function(received_msg) {
		try {

			var base64Signature = received_msg.data;
			base64Signature = base64Signature.replace(/\n|\r/g, "");
			zAu.send(new zk.Event(wgt_listall, 'onSign', base64Signature));

		} catch (err) {
			alert('co loi xay ra');
		}
	};

	webSocketSigner.error = function() {
		alert('co loi xay ra');
	};

	webSocketSigner.onclose = function() {		
		webSocketClosed();
		 
	};
	
}
