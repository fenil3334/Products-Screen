
import 'package:flutter/material.dart';

class Utils{
  static late BuildContext contextdialog;
  static void onLoading(BuildContext context) {
    contextdialog=context;
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        return Dialog(
          child: Container(
            height: 90,
            width: 90,
            child: Center(
              child: CircularProgressIndicator(),
            ),
          )
        );
      },
    );

  }

/*

  <service
  android:name=".tracking.LocationService"
  android:foregroundServiceType="location"
  android:exported="false">
*/


  static void removeLoader(){
    if(contextdialog!=null) {
      Navigator.pop(contextdialog);
    }
  }
}