/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function initPlugin() {
    if (!(VtPlugin.initPlugin()) || VtPlugin.getVersion() !== '1.1.0.0') {
        alert('Ban can cai dat plugin ViettelCASigner');
        window.open("/Share/js/ca/ViettelCASigner.msi");
        return false;
    }
    return true;
}

//function uploadCert() {
//    var wgt = zk.Widget.$("caIntegrationForm");
//    if (!initPlugin())
//        return false;
//    var base64Cert = new String(VtPlugin.getCert());
//    base64Cert = base64Cert.replace(/\n|\r/g, "");
//    zAu.send(new zk.Event(wgt, 'onUploadCert', base64Cert));
//}
function uploadCert() {
    var wgt = zk.Widget.$('$caCRUDWindow');
    if (!initPlugin())
        return false;
    var base64Cert = new String(VtPlugin.getCert());
    base64Cert = base64Cert.replace(/\n|\r/g, "");
    zAu.send(new zk.Event(wgt, 'onUploadCert', base64Cert));
}

function signAndSubmit() {
    //var wgt = zk.Widget.$("caIntegrationForm");
    var wgt = zk.Widget.$('$caCRUDWindow');
    //var base64Hash = document.getElementById('base64Hash').value;
    //var certSerial = document.getElementById('certSerial').value;
    var base64Hash = zk.Widget.$("$txtBase64Hash").getValue();
    var certSerial = zk.Widget.$("$txtCertSerial").getValue();
    var base64Signature = new String(VtPlugin.signHash(base64Hash, certSerial));
    console.log('base64: ' + base64Hash);
    console.log('certSerial: ' + certSerial);
    base64Signature = base64Signature.replace(/\n|\r/g, "");
    zAu.send(new zk.Event(wgt, 'onSign', base64Signature));
}

