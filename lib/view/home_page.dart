
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:products_screen_usinggetx/controller/home_controller.dart';
import 'package:products_screen_usinggetx/util/utils.dart';

class HomePage extends StatelessWidget {
   HomePage({Key? key}) : super(key: key);


  HomeController homeController=Get.put(HomeController());

  @override
  Widget build(BuildContext context) {

    return Material(

      child: SafeArea(
        child: Container(
          color: Colors.white,

          child: Column(
            children: [
              Container(
                width: double.infinity,
                height: 48,
                color: Colors.green,
                child: const Align(
                  alignment: Alignment.center,
                  child: Text('Products',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 17,
                  ),),
                ),
              ),



              tabListview(),


              SizedBox(height: 20,),

              Expanded(child: Container(
                child: Obx(()=>
                    Column(
                      children: [
                        Text((homeController.selectIndex.value+1).toString(),
                        style: TextStyle(
                          color: Colors.black,
                          fontSize: 15
                        ),)
                      ],
                    )
                ),
              ),
              )

            ],
          ),
        ),
      ),
    );
  }



  Widget tabListview(){
    return Obx((){
      return Container(
        height: 40,
        child: ListView.builder(
          shrinkWrap: true,
            scrollDirection: Axis.horizontal,
          itemCount: homeController.dataArrayList.length,
            itemBuilder: (context,i){
            return InkWell(
              onTap: (){
                homeController.selectIndex.value=i;
                Utils.onLoading(context);
                homeController.getSubCategoryData();
              },
              child: Obx(()=>
                 Container(
                   padding: EdgeInsets.only(left: 14,right: 14),
                  color: homeController.selectIndex.value==i ? Colors.green : Colors.green[300],

                  child: Align(
                    alignment: Alignment.center,
                    child: Text(homeController.dataArrayList[i].title.toString(),
                    maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    style: TextStyle(
                      color: Colors.white
                    ),),
                  ),
                ),
              ),
            );
            }),
      );
    });
  }
}
