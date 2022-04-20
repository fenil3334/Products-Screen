import 'dart:convert';

import 'package:get/get.dart';
import 'package:products_screen_usinggetx/model/data_model.dart';
import 'package:products_screen_usinggetx/util/api_repo.dart';
import 'package:products_screen_usinggetx/util/utils.dart';

class HomeController extends GetxController{
  RxList<DataModel> dataArrayList = <DataModel>[].obs;
  RxInt selectIndex=0.obs;
  
  @override
  void onInit() {
    super.onInit();

    getData();
    getSubCategoryData();
  }
  
  
  
  
  getData() async {

    ApiRepo apiRepo=ApiRepo();
    var jsondata=await apiRepo.getApiCall('http://myjson.dit.upm.es/api/bins/2tfp');


    var getUsersData = jsondata.data as List;
    dataArrayList.value = getUsersData.map((i) => DataModel.fromJson(i)).toList();
    dataArrayList.refresh();

    print('----length------${dataArrayList.length}');
    update();
  }


  getSubCategoryData() async {
    ApiRepo apiRepo=ApiRepo();
    var jsondata=await apiRepo.getApiCall('http://myjson.dit.upm.es/api/bins/2tfp');


    var getUsersData = jsondata.data as List;
    update();
    Utils.removeLoader();
  }
}