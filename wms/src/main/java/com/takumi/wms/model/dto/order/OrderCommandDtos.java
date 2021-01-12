package com.takumi.wms.model.dto.order;


public class OrderCommandDtos {
    private OrderCommandDtos() {
    }

    public static class OrderShipGroupActionRequestContent {

        private String commandType = "OrderShipGroupAction";

        public String getCommandType() {
            return "OrderShipGroupAction";
        }

        /**
         * Value
         */
        private String value;

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Order Ship Group Id
         */
        private OrderShipGroupId orderShipGroupId;

        public OrderShipGroupId getOrderShipGroupId() {
            return this.orderShipGroupId;
        }

        public void setOrderShipGroupId(OrderShipGroupId orderShipGroupId) {
            this.orderShipGroupId = orderShipGroupId;
        }

        /**
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        /**
         * Command Id
         */
        private String commandId;

        public String getCommandId() {
            return this.commandId;
        }

        public void setCommandId(String commandId) {
            this.commandId = commandId;
        }

        /**
         * Requester Id
         */
        private String requesterId;

        public String getRequesterId() {
            return this.requesterId;
        }

        public void setRequesterId(String requesterId) {
            this.requesterId = requesterId;
        }

    }

}

