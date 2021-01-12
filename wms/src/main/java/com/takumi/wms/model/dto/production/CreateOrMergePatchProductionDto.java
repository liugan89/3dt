package com.takumi.wms.model.dto.production;

import java.util.Date;

public class CreateOrMergePatchProductionDto extends AbstractProductionCommandDto {
    /**
     * Description
     */
    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Movement Date
     */
    private Date movementDate;

    public Date getMovementDate() {
        return this.movementDate;
    }

    public void setMovementDate(Date movementDate) {
        this.movementDate = movementDate;
    }

    /**
     * Facility Id
     */
    private String facilityId;

    public String getFacilityId()
    {
        return this.facilityId;
    }

    public void setFacilityId(String facilityId)
    {
        this.facilityId = facilityId;
    }

    /**
     * Active
     */
    private Boolean active;

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    private String documentAction;

    public String getDocumentAction() {
        return this.documentAction;
    }

    public void setDocumentAction(String documentAction) {
        this.documentAction = documentAction;
    }

    private CreateOrMergePatchProductionLineDto[] productionLines;

    public CreateOrMergePatchProductionLineDto[] getProductionLines() {
        return this.productionLines;
    }

    public void setProductionLines(CreateOrMergePatchProductionLineDto[] productionLines) {
        this.productionLines = productionLines;
    }

    private Boolean isPropertyDescriptionRemoved;

    public Boolean getIsPropertyDescriptionRemoved() {
        return this.isPropertyDescriptionRemoved;
    }

    public void setIsPropertyDescriptionRemoved(Boolean removed) {
        this.isPropertyDescriptionRemoved = removed;
    }

    private Boolean isPropertyMovementDateRemoved;

    public Boolean getIsPropertyMovementDateRemoved() {
        return this.isPropertyMovementDateRemoved;
    }

    public void setIsPropertyMovementDateRemoved(Boolean removed) {
        this.isPropertyMovementDateRemoved = removed;
    }

    private Boolean isPropertyActiveRemoved;

    public Boolean getIsPropertyActiveRemoved() {
        return this.isPropertyActiveRemoved;
    }

    public void setIsPropertyActiveRemoved(Boolean removed) {
        this.isPropertyActiveRemoved = removed;
    }

    public static class CreateProductionDto extends CreateOrMergePatchProductionDto {
        public CreateProductionDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
    }

    public static class MergePatchProductionDto extends CreateOrMergePatchProductionDto {
        public MergePatchProductionDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
    }

}

