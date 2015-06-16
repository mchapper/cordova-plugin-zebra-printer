var zebraPrinter = {
    print: function(macaddress, label, params, successCallback, errorCallback) {
        cordova.exec(successCallback,
            errorCallback,
            "ZebraPrinter",
            "printLabel",
            [macaddress, label, params]);
    }
}
module.exports = zebraPrinter;