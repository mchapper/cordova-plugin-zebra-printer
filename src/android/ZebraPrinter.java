package com.gumba.cordova.plugin.printer;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;


public class ZebraPrinter extends CordovaPlugin {
    
    public static final String PRINT_LABEL = "printLabel";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (PRINT_LABEL.equals(action)) {
            String macaddress = args.getString(0);
            String label = args.getString(1);
            JSONObject params = args.getJSONObject(2);

            Map<Integer, String> vars = new HashMap<Integer, String>();
            Iterator<String> keys = params.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                vars.put(Integer.parseInt(key), (String) params.get(key));
            }

            this.printLabel(macaddress, label, vars, callbackContext);
            return true;
        }
        return false;
    }

    private void printLabel(String macaddress, String label, Map<Integer, String> vars, CallbackContext callbackContext) {

        Connection connection =  new BluetoothConnection(macaddress);

        try {
                connection.open();

                com.zebra.sdk.printer.ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                                
                printer.printStoredFormat(label, vars, "utf8");
                
                connection.close();

        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (ZebraPrinterLanguageUnknownException e) {
            e.printStackTrace();
       } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                if(connection.isConnected()){
                    connection.close();
                }   
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
        }
    }
}