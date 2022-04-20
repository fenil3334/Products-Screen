import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:products_screen_usinggetx/view/home_page.dart';



void main() {
  runApp(const MyApp());
}


class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: 'Product Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primaryColor: Colors.blue
      ),
      home: HomePage(),
    );
  }
}
