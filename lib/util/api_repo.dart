import 'dart:io';

import 'package:dio/adapter.dart';
import 'package:dio/dio.dart';

class ApiRepo{

  Future<Response> getApiCall(String url) async {
    Dio dio =  Dio();
    (dio.httpClientAdapter as DefaultHttpClientAdapter).onHttpClientCreate =
        (HttpClient client) {
      client.badCertificateCallback =
          (X509Certificate cert, String host, int port) => true;
      return client;
    };
    Response response = await dio.get(url);
    print(response.data.toString());

    return response;
  }
}