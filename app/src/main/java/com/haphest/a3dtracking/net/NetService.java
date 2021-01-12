package com.haphest.a3dtracking.net;

import com.haphest.a3dtracking.BuildConfig;

/**
 * 网络中要用到的一些参数
 */
public class NetService {
    //baseUrl
    public static String BaseURL = BuildConfig.API_HOST;

    public static final String getProducts = "Products";
    public static final String getProductBlurId = "queries/search/productIds";
    public static final String getAttributeBlurId = "queries/inventoryItems/getAvailableAttributeIdsByProductId";
    public static final String getAttributeValueBlurId = "queries/inventoryItems/getAttributeValuesByProductId";
    public static final String getShipGroupBlurId = "queries/orderShipGroups/getFirstOrderShipGroupByShipGroupSeqId";
    public static final String getAllShipGroupBlurId = "getShipGroupBlurIdqueries/search/allCreatedOrderShipGroupSeqIds";
    public static final String getSummarize = "queries/inventoryItems/summarizeByProductAndOneAttribute";
    public static final String getProduct = "queries/products/@";
    public static final String getInOutDocuments = "InOuts";
    public static final String getAttrSet = "queries/attributeSets/@";
    public static final String getInOutLineInfo = "queries/documents/getByInOutId";
    public static final String getMovementLineInfo = "queries/documents/getByMovementId";
    public static final String Movements = "Movements/@";
    public static final String InOuts = "InOuts/@";
    public static final String getWarehouses = "queries/permissions/getPermittedWarehouseIds";
    public static final String getLocators = "Locators";
    public static final String CommitInOut = "InOuts/@/_commands/Complete";
    public static final String newInOut = "InOuts/@";
    public static final String AddLineFun = "InOuts/@/_commands/AddLine";
    public static final String DeleteInOutLine = "InOuts/@";
    public static final String getLots = "Lots";
    public static final String addLot = "Lots/@";
    public static final String getMovements = "Movements";
    public static final String createNewMovement = "Movements/@";
    public static final String getInOutMandatoryAtts = "queries/inOutSummaries/getByInOutId";
    public static final String getMovementMandatoryAtts = "queries/inOutSummaries/getByMovementId";
    public static final String getShipmentInMandatoryAtts = "queries/inOutSummaries/getSerialNumberShipmentItemAndReceiptSummaryByShipmentId";
    public static final String getShipmentOutMandatoryAtts = "queries/inOutSummaries/getIssuanceSummaryByShipmentId";
    public static final String AddMovementLine = "Movements/@/_commands/AddLine";
    public static final String DeleteMovementLine = "Movements/@";
    public static final String CommitMovement = "Movements/@/_commands/DocumentAction";
    public static final String getShipments = "Shipments";
    public static final String AddLineImage = "InOuts/@";
    public static final String AddShipmentImage = "Shipments/@";
    public static final String AddInOutDocImage = "InOuts/@";
    public static final String getOutInventoryAtt = "queries/inventoryItems/getFirstInventoryItem";
    public static final String getStatusItem = "queries/statusItems/getDamageStatusItems";
    public static final String CommitShipmentItem = "Shipments/@/_commands/ReceiveItem";
    public static final String CommitShipment = "Shipments/@/_commands/ConfirmAllItemsReceived";
    public static final String CommitShipmentOut = "Shipments/@/_commands/ConfirmAllItemsIssued";
    public static final String getShipmentVersion = "Shipments/@";
    public static final String getInOutDocumentVersion = "Movements/@";
    public static final String getContractByNotice = "queries/noticeDocument/@";
    public static final String getContractByOrderShipGroupSeqId = "queries/search/getOrderIdsByOrderShipGroupSeqId";
    public static final String RelateNotice = "Shipments/@";
    public static final String createPOShipment = "Shipments";
    public static final String login = "login";
    public static final String AddShipmentItem = "Shipments/@/_commands/ReceiveItem";
    public static final String getIncomingShipmentDocumentLineById = "queries/documents/getLineByShipmentReceiptId";
    public static final String getOutgoingShipmentDocumentLineById = "queries/documents/getLineByShipmentItemIssuanceId";
    public static final String createSOShipment2 = "OrderShipGroupService/CreateSOShipment";
    public static final String CommitShipmentItemOut = "Shipments/@/_commands/IssueItem";
    public static final String changeShipmentStatus = "OrderShipGroupService/ShipPOShipment";
    public static final String getOrderShipGroup = "queries/orderShipGroups/summarizeShipmentReceiptOrIssuanceProductByPrimaryOrderShipGroupId";
    public static final String getNoticeVersion = "queries/orderShipGroups/getFirstOrderShipGroupVersionByShipGroupSeqId";
    public static final String completeNotice = "Orders/@/OrderShipGroups/@/_commands/OrderShipGroupAction";
    public static final String getNoticeInfo = "queries/orderShipGroups/getCarrierInformationByNoticeId";
    public static final String getInOutNotices = "InOutNotices";
    public static final String getNoticeInf = "queries/orderShipGroups/getInOutNoticeByNoticeId";
    public static final String createSOShipment = "OrderShipGroupService/CreateSOShipment";
    public static final String getInShipment = "queries/shipments/getIncomingShipmentById";
    public static final String getOutShipment = "queries/shipments/getOutgoingShipmentById";
    public static final String DeleteShipmentOutLineItem = "Shipments/@/ItemIssuances/@";
    public static final String deleteShipmentInItem = "Shipments/@";
    public static final String GetLabel = "queries/ui/getLabelMapByLanguage";

    //RFID
    public static final String getShipmentBoxTypes = "ShipmentBoxTypes";
    public static final String getWritableTags = "queries/attributeSetInstances/getUsableBoxes";
    public static final String CreateDocument = "PackingService/CreateBoxesInDocument";
    public static final String CreateBox = "PackingService/CreateBoxAttributeSetInstances";
    public static final String GetPalletId = "queries/attributeSetInstances/getPalletIdPrefixes";
    public static final String getInventoryItems = "queries/inventoryItems/getInventoryItems";
    public static final String CreateBigBoxInOutDocument = "PackingService/CreateBigBoxInOutDocument";
    public static final String Facilities = "Facilities";
    public static final String CreateSOAndShipment = "OrderShipGroupService/CreateSOAndShipment";
    public static final String getShipmentRouteTemplates = "queries/shipmentRoutes/getShipmentRouteTemplates";
    public static final String CreateShipmentRouteSegments = "ShipmentRouteService/CreateShipmentRouteSegments";
    public static final String carrierGetRouteSegments = "queries/shipmentRoutes/carrierGetRouteSegments";
    public static final String destFacilityGetRouteSegments = "queries/shipmentRoutes/destFacilityGetRouteSegments";
    public static final String getShipmentBoxesForReclaim = "queries/shipmentRoutes/getShipmentBoxesForReclaim";
    public static final String getShipmentBoxesForJudgment = "queries/shipmentRoutes/getShipmentBoxesForJudgment";
    public static final String getAttributeSetInstances = "queries/attributeSetInstances/getAttributeSetInstances";
    public static final String CarrierReceive = "ShipmentRouteService/CarrierReceive";
    public static final String FacilityReceive = "ShipmentRouteService/FacilityReceive";
    public static final String RecyclingCenterReclaim = "ShipmentRouteService/RecyclingCenterReclaim";
    public static final String RecyclingCenterJudge = "ShipmentRouteService/RecyclingCenterJudge";
    public static final String getOwnerPartyId = "Facilities/@";
    public static final String getRebates = "queries/shipmentRoutes/getShipmentBoxLogs";
    public static final String getBindDevices = "ShipmentRouteSegments/@,@/ShipmentRouteSegmentDevices";
    public static final String bindDevices = "ShipmentRouteSegments/@,@";
    public static final String getProductions = "queries/manufacturing/getRunningProductionRuns";
    public static final String getRoutingTasks = "queries/manufacturing/getRoutingTasksByProductionRunId";
    public static final String productionContainerByTag = "ProductionContainers/@";
    public static final String clearContainer = "ProductionContainers/@/_commands/EmptyContainer";
    public static final String getProducedLotsByContainerId = "queries/manufacturing/getProducedLotsByContainerId";
    public static final String getProduce = "queries/manufacturing/getProductsToDeliver";
    public static final String getProduceLotId = "queries/attributeSetInstances/@";
    public static final String completeContainer = "ProductionContainers/@/_commands/CompleteTask";
    public static final String getLotTree = "queries/manufacturing/getProductLotComponents";
    public static final String moveBoxesToProduction = "ManufacturingService/MoveBoxesToProduction";
    public static final String getProductionRunMaterialItemsByContainerIds = "queries/manufacturing/getProductionRunMaterialItemsByContainerIds";
    public static final String returnFromProduction = "ManufacturingService/ReturnFromProduction";
    public static final String productionIn = "ManufacturingService/ProductionIn";
    public static final String ProductionStations = "ProductionStations/@";
    public static final String productionStationAddMaterialAssociation = "ManufacturingService/ProductionStationAddMaterialAssociation";
    public static final String ProductAssocs = "ProductAssocs";
    public static final String ProductionStationCompleteTask = "ManufacturingService/ProductionStationCompleteTask";

    public static String GetUrl(String base, String... ss) {
        String r = null;
        for (String s : ss) {
            base = base.replaceFirst("@", s);
        }
        return base;
    }

    public static void main(String[] args) {
        System.out.println(NetService.GetUrl(NetService.bindDevices, "1", "2"));
    }
}
