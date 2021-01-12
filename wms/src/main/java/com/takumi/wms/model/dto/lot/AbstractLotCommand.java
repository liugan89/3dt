package com.takumi.wms.model.dto.lot;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractLotCommand extends AbstractCommand implements LotCommand {
    private String lotId;

    public String getLotId() {
        return this.lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    private Long version;

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    public static abstract class AbstractCreateOrMergePatchLot extends AbstractLotCommand implements CreateOrMergePatchLot {
        private java.math.BigDecimal quantity;

        public java.math.BigDecimal getQuantity() {
            return this.quantity;
        }

        public void setQuantity(java.math.BigDecimal quantity) {
            this.quantity = quantity;
        }

        private java.sql.Timestamp expirationDate;

        public java.sql.Timestamp getExpirationDate() {
            return this.expirationDate;
        }

        public void setExpirationDate(java.sql.Timestamp expirationDate) {
            this.expirationDate = expirationDate;
        }

        private Boolean active;

        public Boolean getActive() {
            return this.active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }

    }

    public static abstract class AbstractCreateLot extends AbstractCreateOrMergePatchLot implements CreateLot {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static abstract class AbstractMergePatchLot extends AbstractCreateOrMergePatchLot implements MergePatchLot {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }

        private Boolean isPropertyQuantityRemoved;

        public Boolean getIsPropertyQuantityRemoved() {
            return this.isPropertyQuantityRemoved;
        }

        public void setIsPropertyQuantityRemoved(Boolean removed) {
            this.isPropertyQuantityRemoved = removed;
        }

        private Boolean isPropertyExpirationDateRemoved;

        public Boolean getIsPropertyExpirationDateRemoved() {
            return this.isPropertyExpirationDateRemoved;
        }

        public void setIsPropertyExpirationDateRemoved(Boolean removed) {
            this.isPropertyExpirationDateRemoved = removed;
        }

        private Boolean isPropertyActiveRemoved;

        public Boolean getIsPropertyActiveRemoved() {
            return this.isPropertyActiveRemoved;
        }

        public void setIsPropertyActiveRemoved(Boolean removed) {
            this.isPropertyActiveRemoved = removed;
        }

    }

    public static class SimpleCreateLot extends AbstractCreateLot {
    }


    public static class SimpleMergePatchLot extends AbstractMergePatchLot {
    }


    public static class SimpleDeleteLot extends AbstractLotCommand implements DeleteLot {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_DELETE;
        }
    }


}

