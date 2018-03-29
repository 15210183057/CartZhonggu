package jiekou;

/**
 * Created by 123456 on 2018/2/5.
 */

public class getInterface {
    public static  String host="http://mkerp.zgcw.cn";
    //登陆接口http://test.zgcw.cn/api/api_user/userlogin?username=wangsf&pass=wangsf&json=1
//    {"status":1,"user":{"id":"2","username":"admin","password":"8940249fbb327af9b5ee2836425cdbc2","headimg":"20170918\\732f97fa0c543a3d1150d7b1ee302631.jpg","loginip":"127.0.0.1","logintime":"1478751964","groupid":"0","status":"1"},"groups":[0]}
    public static String loginUser=host+"/api/api_user/userlogin?";
    //待补充车源
//    http://test.zgcw.cn/api/api_car_select/getCarList
// 参数格式：json=1&pagesize=10&where=blu=1 and groupid in(5,3,2) and status = 1
//    mkerp.zgcw.cn/api/api_car/getMylist?userid=16&page=1&merchantid=72&makeup=1
    public static String getBuCartList=host+"/api/api_car/getMylist";
    //获取驾驶证 参数imgdata
    public static String getJaShiZheng="http://open.zgcw.cn/api/vehicle/getVinByDriving";
    //获取vin码 参数imgdata
    public static String getVin="http://open.zgcw.cn/api/vehicle/getVinByImage";
    //获取品牌
    public static String getBrand=host+"/api/api_car/getBrand";
    //获取车系
    public static String getSeries=host+"/api/api_car/getSeries?";
    //获取车型
    public static String getModels=host+"/api/api_car/getModels?";
    //获取区域
    public static String getA =host+"/api/api_merchant/getlist";
    //上传三张图片
    public static String UpdateImag="http://open.zgcw.cn/api/vehicle/upfile";
    //上传车辆补录信息
    public static String UpCartData=host+"/api/api_car/addcar";
    public static String getList="http://mkerp.zgcw.cn/api/api_car/getmylist";
    //获取价格
    public static String getPrice="http://open.zgcw.cn/api/vehicle/getVinByStr";
    //上传商户信息http://mkerp.zgcw.cn/api/api_distributor/AddMerchant?groupid=2&name=%E7%8E%8B%E6%8C%AF%E6%9D%B0
    public static String UpdateCartName="http://mkerp.zgcw.cn/api/api_distributor/AddMerchant";

    //点击item
//    http://mkerp.zgcw.cn/api/api_car/getInfo?id=1695&json=1&makeup=1
    public static String CartItem=host+"/api/api_car/getInfo";
    //上传补录车辆信息接口,9张大图
//    http://mkerp.zgcw.cn/api/api_car/addcar?vin=121456789014725833&merchant_code=244&groupid=8,9,10,2&userid=16&brandid=2&seriesid=2446&regDate=2018-03-28&mileage=1&target_price=6&zhengqian45=http://mkusecar-carpics.oss-cn-shanghai.aliyuncs.com/2018-03-28/c0778c897ba7cf690f71520d760f647e.jpg&zhengqian=http://mkusecar-carpics.oss-cn-shanghai.aliyuncs.com/2018-03-28/58dc6e7a1ac2f372a49277eb38a32796.jpg&zhenghou=http://mkusecar-carpics.oss-cn-shanghai.aliyuncs.com/2018-03-28/c0778c897ba7cf690f71520d760f647e.jpg&picone=%27http://mkusecar-carpics.oss-cn-shanghai.aliyuncs.com/2018-03-28/c0778c897ba7cf690f71520d760f647e.jpg%27&pictwo=%27http://mkusecar-carpics.oss-cn-shanghai.aliyuncs.com/2018-03-28/c0778c897ba7cf690f71520d760f647e.jpg%27&picthree=%27http://mkusecar-carpics.oss-cn-shanghai.aliyuncs.com/2018-03-28/c0778c897ba7cf690f71520d760f647e.jpg%27&picfour=%27http://mkusecar-carpics.oss-cn-shanghai.aliyuncs.com/2018-03-28/c0778c897ba7cf690f71520d760f647e.jpg%27&picfive=%27http://mkusecar-carpics.oss-cn-shanghai.aliyuncs.com/2018-03-28/c0778c897ba7cf690f71520d760f647e.jpg%27&picsix=%27http://mkusecar-carpics.oss-cn-shanghai.aliyuncs.com/2018-03-28/c0778c897ba7cf690f71520d760f647e.jpg%27&modelid=29702&carName=2015%E6%AC%BE%E6%8B%89%E5%85%B1%E8%BE%BETaraf6.0L%E6%A0%87%E5%87%86%E5%9E%8B&pid%20=0&tel=12345&name=He&isDaTing=1&transterstatus=1
    public static String UpdateBuCartItem=host+"/api/api_car/addcar";
    //补录车辆修改信息接口
////    http://mkerp.zgcw.cn/api/api_car/addcar?id=5259&merchant_code=244&groupid=8,9,10,2&vendorId=2&brandId=2446&regDate=2018-03-01&mileage=1.00&target_price=6.00&zhengqian45=1.jpg&zhengqian=2.jpg&zhenghou=3.jpg&picone=%274.jpg%27&pictwo=%275.jpg%27&picthree=%276.jpg%27&picfour=%277.jpg%27&picfive=%278.jpg%27&picsix=%279.jpg%27&carStyleId=29702&carName=2015%E6%AC%BE%E6%8B%89%E5%85%B1%E8%BE%BETaraf6.0L%E6%A0%87%E5%87%86%E5%9E%8B&pid=248&tel=12345&name=He&isDaTing=1&transterstatus=1
//    public static String SetBuCartItem=host+"/api/api_car/addcar";
}
