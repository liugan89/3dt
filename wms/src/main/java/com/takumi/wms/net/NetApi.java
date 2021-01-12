package com.takumi.wms.net;

import com.takumi.wms.model.DocumentLine;
import com.takumi.wms.model.InOutNotice;
import com.takumi.wms.model.InventoryItemInfo;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.DocumentInfo;
import com.takumi.wms.model.Locator;
import com.takumi.wms.model.Lot;
import com.takumi.wms.model.MandatoryAtts;
import com.takumi.wms.model.InOutDocument;
import com.takumi.wms.model.OrderShipGroup;
import com.takumi.wms.model.OutInventoryAtt;
import com.takumi.wms.model.OutDocumentInfo;
import com.takumi.wms.model.Product;
import com.takumi.wms.model.Production;
import com.takumi.wms.model.ShipmentDetail;
import com.takumi.wms.model.StatusItem;
import com.takumi.wms.model.Warehouse;
import com.takumi.wms.model.dto.inout.CreateOrMergePatchInOutDto;
import com.takumi.wms.model.dto.inout.InOutCommandDtos;
import com.takumi.wms.model.dto.lot.CreateOrMergePatchLotDto;
import com.takumi.wms.model.dto.movement.CreateOrMergePatchMovementDto;
import com.takumi.wms.model.dto.movement.MovementCommandDtos;
import com.takumi.wms.model.dto.order.OrderCommandDtos;
import com.takumi.wms.model.dto.production.CreateOrMergePatchProductionDto;
import com.takumi.wms.model.dto.shipment.CreateOrMergePatchShipmentDto;
import com.takumi.wms.model.dto.shipment.OrderShipGroupServiceCommandDtos;
import com.takumi.wms.model.dto.shipment.ShipmentCommandDtos;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface NetApi {

    /**
     * 获得所有产品
     */
    @Headers("Content-Type:application/json;charset=utf-8x-www-form-urlencoded")
    @GET("{id}")
    Observable<Product> getProducts(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 根据产品ID获得产品详细信息
     */
    @GET("{id}")
    Observable<List<Product>> getProducts(@Path(value = "id", encoded = true) String id);

    /**
     * 根据产品ID获得产品详细信息
     */
    @GET("{id}")
    Observable<List<String>> getProductName(@Path(value = "id", encoded = true) String id);

    /**
     * 根据产品ID获得产品详细信息
     */
    @GET("{id}")
    Observable<Product> getProduct(@Path(value = "id", encoded = true) String id);

    /**
     * 根据条件获得相应单子
     */
    @GET("{id}")
    Observable<List<InOutDocument>> getInOutDocuments(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 根据条件获得相应移动单
     */
    @GET("{id}")
    Observable<List<InOutDocument>> getMovements(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 获得所有装运单
     */
    @GET("{id}")
    Observable<List<ShipmentDetail>> getShipments(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);


    /**
     * 获得所有装运单Version
     */
    @GET("{id}")
    Observable<ShipmentDetail> getShipmentVersion(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 获得Odd的Version
     */
    @GET("{id}")
    Observable<InOutDocument> getInOutDocumentVersion(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);


    /**
     * 根据条件获得相应装运单
     */
    @GET("{id}")
    Observable<DocumentInfo> getShipment(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 根据条件获得相应装运单
     */
    @GET("{id}")
    Observable<OutDocumentInfo> getOutShipment(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 根据通知单id获取合同号
     */
    @GET("{id}")
    Observable<List<String>> getContractByNotice(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);


    /**
     * 根据属性ID得到属性
     */
    @GET("{id}")
    Observable<InventoryAttribute> getAttrSet(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 根据属性ID得到属性
     */
    @GET("{id}")
    Observable<InventoryAttribute> getAttrSet(@Path(value = "id", encoded = true) String id);


    /**
     * 根据行项ID得到行项属性
     */
    @GET("{id}")
    Observable<DocumentInfo> getLineInfo(@Path(value = "id", encoded = true) String id);

    /**
     * 根据行项ID得到行项属性
     */
    @GET("{id}")
    Observable<List<StatusItem>> getStatusItem(@Path(value = "id", encoded = true) String id);

    /**
     * 根据条件获得相应单子
     */
    @GET("{id}")
    Observable<InOutDocument> getInOutDocument(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 得到所有仓库
     */
    @GET("{id}")
    Observable<List<Warehouse>> getWarehouses(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 得到仓库下的货位
     */
    @GET("{id}")
    Observable<List<Locator>> getLocators(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 根据条件获得相应单子
     */
    @GET("{id}")
    Observable<InOutDocument> getInOutDocument(@Path(value = "id", encoded = true) String id);

    /**
     * 获得批次
     */
    @GET("{id}")
    Observable<List<CreateOrMergePatchLotDto>> getLots(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 根据条件获得批次
     */
    @GET("{id}")
    Observable<List<Lot>> getLots(@Path(value = "id", encoded = true) String id);

    /**
     * 获得所有单子
     */
    @GET("{id}")
    Observable<List<InOutDocument>> getInOutDocuments(@Path(value = "id", encoded = true) String id);

    /**
     * 获得必填
     */
    @GET("{id}")
    Observable<MandatoryAtts> getMandatoryAtts(@Path(value = "id", encoded = true) String id);


    /**
     * 模糊查询
     */
    @GET("{id}")
    Observable<List<String>> getBlurId(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 模糊查询
     */
    @GET("{id}")
    Observable<List<String>> getBlurId(@Path(value = "id", encoded = true) String id);

    /**
     * 根据序列号获得库存属性
     */
    @GET("{id}")
    Observable<OutInventoryAtt> getOutInventoryAtt(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 装运单出库推送
     */
    @GET("{id}")
    Observable<Boolean> shipmentPush(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 生产单列表
     */
    @GET("{id}")
    Observable<List<Production>> getProductionList(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 生产单列表
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> createListProduction(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchProductionDto.CreateProductionDto o);

    /**
     * 根据装运单行项号获得接收项的实体
     */
    @GET("{id}")
    Observable<DocumentLine> getDocumentLineById(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 得到通知单
     */
    @GET("{id}")
    Observable<OrderShipGroup> getOrderShipGroup(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 得到通知单车辆司机信息
     */
    @GET("{id}")
    Observable<Map<String, String>> getNoticeInfo(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 得到盘点查询信息
     */
    @GET("{id}")
    Observable<InventoryItemInfo> getInfor(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 得到通知单列表
     */
    @GET("{id}")
    Observable<List<InOutNotice>> getInOutNotices(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);


    @GET("{id}")
    Observable<Response<ResponseBody>> getCheckProduct(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);


    /**
     * 根据通知单号得到通知单信息
     */
    @GET("{id}")
    Observable<OrderShipGroup> getNoticeInf(@Path(value = "id", encoded = true) String id);

    /**
     * 根据通知单号创建装运出库单
     */
    @POST("{id}")
    Observable<Response<ResponseBody>> createSOShipment(@Path(value = "id", encoded = true) String id, @Body OrderShipGroupServiceCommandDtos.CreateSOShipmentDto o);

    /**
     * 创建新的装运发货单
     */
    //@POST("{id}")
    //Observable<Response<ResponseBody>> createSOShipment2(@Path(value = "id", encoded = true) String id, @Body OrderShipGroupServiceCommandDtos.CreateSOShipmentDto o);


    /**
     * 根据通知单号创建装运出库单
     */
    @DELETE("{id}")
    Observable<Response<ResponseBody>> deleteShipmentInItem(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> mapw);

    /**
     * 根据序列号获得产品属性
     */
    @GET("{id}")
    Observable<Product> getProductAtt(@Path(value = "id", encoded = true) String id);

    /**
     * 提交单子
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> CommitInOut(@Path(value = "id", encoded = true) String id, @Body InOutCommandDtos.CompleteRequestContent o);


    /**
     * 提交单子
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> CommitMovement(@Path(value = "id", encoded = true) String id, @Body MovementCommandDtos.DocumentActionRequestContent o);


    /**
     * 创建新单子
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> newInOut(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchInOutDto o);

    /**
     * 创建新批次
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> addLot(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchLotDto o);

    /**
     * 创建新移动单
     */
    @POST("{id}")
    Observable<Response<ResponseBody>> createNewMovement(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchMovementDto.CreateMovementDto o);


    /**
     * 添加出入库行项
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> AddLineFun(@Path(value = "id", encoded = true) String id, @Body InOutCommandDtos.AddLineRequestContent o);

    /**
     * 添加出入库行项图片
     */
    @PATCH("{id}")
    Observable<Response<ResponseBody>> AddLineImage(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchInOutDto.MergePatchInOutDto o);


    /**
     * 添加装运单行项
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> AddShipmentItem(@Path(value = "id", encoded = true) String id, @Body ShipmentCommandDtos.ReceiveItemRequestContent o);


    /**
     * 添加装运单行项图片
     */
    @PATCH("{id}")
    Observable<Response<ResponseBody>> AddShipmentImage(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchShipmentDto.MergePatchShipmentDto o);


    /**
     * 添加出入库图片
     */
    @PATCH("{id}")
    Observable<Response<ResponseBody>> AddInOutDocImage(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchInOutDto.MergePatchInOutDto o);

    /**
     * 添加移动单行项
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> AddMovementLine(@Path(value = "id", encoded = true) String id, @Body MovementCommandDtos.AddLineRequestContent o);

    /**
     * 接收装运单行项
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> CommitShipmentItem(@Path(value = "id", encoded = true) String id, @Body ShipmentCommandDtos.ReceiveItemRequestContent o);


    /**
     * 确认提交已完成的装运收货单
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> CommitShipment(@Path(value = "id", encoded = true) String id, @Body ShipmentCommandDtos.ConfirmAllItemsReceivedRequestContent o);

    /**
     * 确认提交已完成的装运发货单
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> CommitShipmentOut(@Path(value = "id", encoded = true) String id, @Body ShipmentCommandDtos.ConfirmAllItemsIssuedRequestContent o);


    /**
     * 确认提交已完成的装运发货单
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> CommitShipmentItemOut(@Path(value = "id", encoded = true) String id, @Body ShipmentCommandDtos.IssueItemRequestContent o);


    /**
     * 删除出入库行项
     */
    @PATCH("{id}")
    Observable<Response<ResponseBody>> DeleteInOutLine(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchInOutDto.MergePatchInOutDto o);


    /**
     * 删除移动单行项
     */
    @PATCH("{id}")
    Observable<Response<ResponseBody>> DeleteMovementLine(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchMovementDto.MergePatchMovementDto o);


    /**
     * 删除装运单出库行项下的条目
     */
    @DELETE("{id}")
    Observable<Response<ResponseBody>> DeleteShipmentOutLineItem(@Path(value = "id", encoded = true) String id);


    /**
     * 装运单关联通知单
     */
    @PATCH("{id}")
    Observable<Response<ResponseBody>> RelateNotice(@Path(value = "id", encoded = true) String id, @Body CreateOrMergePatchShipmentDto.MergePatchShipmentDto o);


    /**
     * 创建新的装运收货单
     */
    @POST("{id}")
    Observable<Response<ResponseBody>> createPOShipment(@Path(value = "id", encoded = true) String id, @Body OrderShipGroupServiceCommandDtos.CreatePOShipmentDto o);


    /**
     * 登录
     */
    @POST("{id}")
    Observable<Response<ResponseBody>> login_release(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);


    /**
     * 登录
     */
    @POST("{id}")
    Observable<Response<ResponseBody>> login_debug(@Path(value = "id", encoded = true) String id, @QueryMap Map<String, String> map);

    /**
     * 将装运单的状态改为可装运
     */
    @POST("{id}")
    Observable<Response<ResponseBody>> changeShipmentStatus(@Path(value = "id", encoded = true) String id, @Body OrderShipGroupServiceCommandDtos.ShipPOShipmentDto o);

    /**
     * 完成提交通知单
     */
    @PUT("{id}")
    Observable<Response<ResponseBody>> completeNotice(@Path(value = "id", encoded = true) String id, @Body OrderCommandDtos.OrderShipGroupActionRequestContent o);

    /**
     * 得到通知单veriosn
     */
    @GET("{id}")
    Observable<Long> getNoticeVersion(@Path(value = "id", encoded = true) String id);

}
