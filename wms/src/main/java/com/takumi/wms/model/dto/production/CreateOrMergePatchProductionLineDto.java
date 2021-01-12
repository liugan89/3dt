package com.takumi.wms.model.dto.production;


public class CreateOrMergePatchProductionLineDto extends AbstractProductionLineCommandDto
{
    /**
     * Processing Charge
     */
    private java.math.BigDecimal processingCharge;

    public java.math.BigDecimal getProcessingCharge()
    {
        return this.processingCharge;
    }

    public void setProcessingCharge(java.math.BigDecimal processingCharge)
    {
        this.processingCharge = processingCharge;
    }

    /**
     * Active
     */
    private Boolean active;

    public Boolean getActive()
    {
        return this.active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    private CreateOrMergePatchProductionLineInputDto[] productionLineInputs = new CreateOrMergePatchProductionLineInputDto[0];

    public CreateOrMergePatchProductionLineInputDto[] getProductionLineInputs()
    {
        return this.productionLineInputs;
    }

    public void setProductionLineInputs(CreateOrMergePatchProductionLineInputDto[] productionLineInputs)
    {
        this.productionLineInputs = productionLineInputs;
    }

    private CreateOrMergePatchProductionLineOutputDto[] productionLineOutputs = new CreateOrMergePatchProductionLineOutputDto[0];

    public CreateOrMergePatchProductionLineOutputDto[] getProductionLineOutputs()
    {
        return this.productionLineOutputs;
    }

    public void setProductionLineOutputs(CreateOrMergePatchProductionLineOutputDto[] productionLineOutputs)
    {
        this.productionLineOutputs = productionLineOutputs;
    }

    private Boolean isPropertyProcessingChargeRemoved;

    public Boolean getIsPropertyProcessingChargeRemoved()
    {
        return this.isPropertyProcessingChargeRemoved;
    }

    public void setIsPropertyProcessingChargeRemoved(Boolean removed)
    {
        this.isPropertyProcessingChargeRemoved = removed;
    }

    private Boolean isPropertyActiveRemoved;

    public Boolean getIsPropertyActiveRemoved()
    {
        return this.isPropertyActiveRemoved;
    }

    public void setIsPropertyActiveRemoved(Boolean removed)
    {
        this.isPropertyActiveRemoved = removed;
    }

    public static class CreateProductionLineDto extends CreateOrMergePatchProductionLineDto
    {
        public CreateProductionLineDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
    }

    public static class MergePatchProductionLineDto extends CreateOrMergePatchProductionLineDto
    {
        public MergePatchProductionLineDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
    }

}

