package com.takumi.wms.model.dto.lot;


import com.takumi.wms.model.dto.Command;

public interface LotCommand extends Command
{
    String getLotId();

    void setLotId(String lotId);

    Long getVersion();

    void setVersion(Long version);


    interface CreateOrMergePatchLot extends LotCommand
    {
        java.math.BigDecimal getQuantity();

        void setQuantity(java.math.BigDecimal quantity);

        java.sql.Timestamp getExpirationDate();

        void setExpirationDate(java.sql.Timestamp expirationDate);

        Boolean getActive();

        void setActive(Boolean active);

    }

    interface CreateLot extends CreateOrMergePatchLot
    {
    }

    interface MergePatchLot extends CreateOrMergePatchLot
    {
        Boolean getIsPropertyQuantityRemoved();

        void setIsPropertyQuantityRemoved(Boolean removed);

        Boolean getIsPropertyExpirationDateRemoved();

        void setIsPropertyExpirationDateRemoved(Boolean removed);

        Boolean getIsPropertyActiveRemoved();

        void setIsPropertyActiveRemoved(Boolean removed);

    }

	interface DeleteLot extends LotCommand
	{
	}

}

