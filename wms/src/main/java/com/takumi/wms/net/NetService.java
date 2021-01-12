package com.takumi.wms.net;

import com.takumi.wms.BuildConfig;

/**
 * 网络中要用到的一些参数
 */
public class NetService {
    //baseUrl
    public static String BaseURL = BuildConfig.API_HOST;

    //接口路径
    public static final String Products = "Products";
    public static final String InOuts = "InOuts";
    public static final String _commands = "_commands";
    public static final String Complete = "Complete";
    public static final String AddLine = "AddLine";
    public static final String queries = "queries";
    public static final String productAtts = "productAtts";
    public static final String inoutlines = "inoutlines";
    public static final String Warehouses = "Warehouses";
    public static final String Locators = "Locators";
    public static final String Lots = "Lots";
    public static final String InventoryAttribute = "InventoryAttribute";
    public static final String Movements = "Movements";
    public static final String mandatoryAtts = "mandatoryAtts";
    public static final String lines = "lines";
    public static final String inout = "inout";
    public static final String movement = "movement";
    public static final String DocumentAction = "DocumentAction";
    public static final String Shipments = "Shipments";
    public static final String ShipmentReceipt = "ShipmentReceipt";
    public static final String productAttribute = "productAttribute";
    public static final String inventoryAtt = "inventoryAtt";
    public static final String statusItem = "statusItem";
    public static final String document = "document";
    public static final String all = "all";
    public static final String serialNumber = "serialNumber";
    public static final String shipment = "shipment";
    public static final String ReceiveItem = "ReceiveItem";
    public static final String ConfirmAllItemsReceived = "ConfirmAllItemsReceived";
    public static final String api = "api";
    public static final String fuzzyQuery = "fuzzyQuery";
    public static final String product = "product";
    public static final String status = "status";
    public static final String noticeDocument = "noticeDocument";
    public static final String AddItemAndReceipt = "AddItemAndReceipt";
    public static final String receipt = "receipt";
    public static final String OrderShipGroupService = "OrderShipGroupService";
    public static final String CreateSOShipment = "CreateSOShipment";
    public static final String ConfirmAllItemsIssued = "ConfirmAllItemsIssued";
    public static final String AddItemAndIssuance = "AddItemAndIssuance";
    public static final String shipmentIssuance = "shipmentIssuance";
    public static final String issuance = "issuance";
    public static final String issuances = "issuances";
    public static final String Ship = "Ship";
    public static final String orderGroup = "orderGroup";
    public static final String getVersion = "getVersion";
    public static final String InOutNotices = "InOutNotices";


    //java方法名
    public static final String getProducts = "getProducts";
    public static final String getInOutDocuments = "getInOutDocuments";
    public static final String getAttrSet = "getAttrSet";
    public static final String getLineInfo = "getLineInfo";
    public static final String getInOutDocument = "getInOutDocument";
    public static final String getWarehouses = "getWarehouses";
    public static final String getLocators = "getLocators";
    public static final String CommitInOut = "CommitInOut";
    public static final String newInOut = "newInOut";
    public static final String AddLineFun = "AddLineFun";
    public static final String DeleteInOutLine = "DeleteInOutLine";
    public static final String getLots = "getLots";
    public static final String addLot = "addLot";
    public static final String getMovements = "getMovements";
    public static final String createNewMovement = "createNewMovement";
    public static final String getMandatoryAtts = "getMandatoryAtts";
    public static final String AddMovementLine = "AddMovementLine";
    public static final String DeleteMovementLine = "DeleteMovementLine";
    public static final String CommitMovement = "CommitMovement";
    public static final String getShipments = "getShipments";
    public static final String AddLineImage = "AddLineImage";
    public static final String AddShipmentImage = "AddShipmentImage";
    public static final String AddInOutDocImage = "AddInOutDocImage";
    public static final String getProductAtt = "getProductAtt";
    public static final String getOutInventoryAtt = "getOutInventoryAtt";
    public static final String getStatusItem = "getStatusItem";
    public static final String getProduct = "getProduct";
    public static final String getShipment = "getShipment";
    public static final String CommitShipmentItem = "CommitShipmentItem";
    public static final String CommitShipment = "CommitShipment";
    public static final String CommitShipmentOut = "CommitShipmentOut";
    public static final String getShipmentVersion = "getShipmentVersion";
    public static final String getInOutDocumentVersion = "getInOutDocumentVersion";
    public static final String getBlurId = "getBlurId";
    public static final String getContractByNotice = "getContractByNotice";
    public static final String RelateNotice = "RelateNotice";
    public static final String createPOShipment = "createPOShipment";
    public static final String login = "login";
    public static final String AddShipmentItem = "AddShipmentItem";
    public static final String getDocumentLineById = "getDocumentLineById";
    public static final String createSOShipment2 = "createSOShipment2";
    public static final String CommitShipmentItemOut = "CommitShipmentItemOut";
    public static final String changeShipmentStatus = "changeShipmentStatus";
    public static final String getOrderShipGroup = "getOrderShipGroup";
    public static final String getNoticeVersion = "getNoticeVersion";
    public static final String completeNotice = "completeNotice";
    public static final String getNoticeInfo = "getNoticeInfo";
    public static final String getInOutNotices = "getInOutNotices";
    public static final String getNoticeInf = "getNoticeInf";
    public static final String createSOShipment = "createSOShipment";
    public static final String getOutShipment = "getOutShipment";
    public static final String getInfor = "getInfor";
    public static final String DeleteShipmentOutLineItem = "DeleteShipmentOutLineItem";
    public static final String deleteShipmentInItem = "deleteShipmentInItem";
    public static final String getCheckProduct = "getCheckProduct";
    public static final String getProductName = "getProductName";
    public static final String shipmentPush = "shipmentPush";
    public static final String getProductionList = "getProductionList";
    public static final String createListProduction = "createListProduction";
}
