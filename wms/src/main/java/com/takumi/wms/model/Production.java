package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/11/11/011.
 */

public class Production implements Parcelable {


    private String productionId;
    private String documentStatusId;
    private Date movementDate;
    private long version;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private List<ProductionLine> productionLines;

    public Production() {
    }

    protected Production(Parcel in) {
        productionId = in.readString();
        documentStatusId = in.readString();
        movementDate = new Date(in.readLong());
        version = in.readLong();
        createdBy = in.readString();
        createdAt = new Date(in.readLong());
        updatedBy = in.readString();
        updatedAt = new Date(in.readLong());
        productionLines = in.createTypedArrayList(ProductionLine.CREATOR);
    }

    public static final Creator<Production> CREATOR = new Creator<Production>() {
        @Override
        public Production createFromParcel(Parcel in) {
            return new Production(in);
        }

        @Override
        public Production[] newArray(int size) {
            return new Production[size];
        }
    };

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public String getDocumentStatusId() {
        return documentStatusId;
    }

    public void setDocumentStatusId(String documentStatusId) {
        this.documentStatusId = documentStatusId;
    }

    public Date getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(Date movementDate) {
        this.movementDate = movementDate;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ProductionLine> getProductionLines() {
        return productionLines;
    }

    public void setProductionLines(List<ProductionLine> productionLines) {
        this.productionLines = productionLines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productionId);
        parcel.writeString(documentStatusId);
        parcel.writeLong(movementDate == null ? 0 : movementDate.getTime());
        parcel.writeLong(version);
        parcel.writeString(createdBy);
        parcel.writeLong(createdAt == null ? 0 : createdAt.getTime());
        parcel.writeString(updatedBy);
        parcel.writeLong(updatedAt == null ? 0 : updatedAt.getTime());
        parcel.writeTypedList(productionLines);
    }

    public static class ProductionLine implements Parcelable {

        private String lineNumber;
        private boolean active;
        private long version;
        private String productionId;
        private String createdBy;
        private Date createdAt;
        private String updatedBy;
        private Date updatedAt;
        private List<ProductionLineInput> productionLineInputs;
        private List<ProductionLineOutput> productionLineOutputs;

        public ProductionLine() {
        }

        protected ProductionLine(Parcel in) {
            lineNumber = in.readString();
            active = in.readByte() != 0;
            version = in.readLong();
            productionId = in.readString();
            createdBy = in.readString();
            createdAt = new Date(in.readLong());
            updatedBy = in.readString();
            updatedAt = new Date(in.readLong());
            productionLineInputs = in.createTypedArrayList(ProductionLineInput.CREATOR);
            productionLineOutputs = in.createTypedArrayList(ProductionLineOutput.CREATOR);
        }

        public static final Creator<ProductionLine> CREATOR = new Creator<ProductionLine>() {
            @Override
            public ProductionLine createFromParcel(Parcel in) {
                return new ProductionLine(in);
            }

            @Override
            public ProductionLine[] newArray(int size) {
                return new ProductionLine[size];
            }
        };

        public String getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(String lineNumber) {
            this.lineNumber = lineNumber;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
        }

        public String getProductionId() {
            return productionId;
        }

        public void setProductionId(String productionId) {
            this.productionId = productionId;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<ProductionLineInput> getProductionLineInputs() {
            return productionLineInputs;
        }

        public void setProductionLineInputs(List<ProductionLineInput> productionLineInputs) {
            this.productionLineInputs = productionLineInputs;
        }

        public List<ProductionLineOutput> getProductionLineOutputs() {
            return productionLineOutputs;
        }

        public void setProductionLineOutputs(List<ProductionLineOutput> productionLineOutputs) {
            this.productionLineOutputs = productionLineOutputs;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(lineNumber);
            parcel.writeByte((byte) (active ? 1 : 0));
            parcel.writeLong(version);
            parcel.writeString(productionId);
            parcel.writeString(createdBy);
            parcel.writeLong(createdAt == null ? 0 : createdAt.getTime());
            parcel.writeString(updatedBy);
            parcel.writeLong(updatedAt == null ? 0 : updatedAt.getTime());
            parcel.writeTypedList(productionLineInputs);
            parcel.writeTypedList(productionLineOutputs);
        }

        public static class ProductionLineInput implements Parcelable {

            private String productionLineInputSeqId;
            private String locatorId;
            private String productId;
            private String attributeSetInstanceId;
            private double quantity;
            private long version;
            private String productionId;
            private String productionLineLineNumber;
            private String createdBy;
            private Date createdAt;
            private Map attributeSetInstance;
            private Product product;

            protected ProductionLineInput(Parcel in) {
                productionLineInputSeqId = in.readString();
                locatorId = in.readString();
                productId = in.readString();
                attributeSetInstanceId = in.readString();
                quantity = in.readDouble();
                version = in.readLong();
                productionId = in.readString();
                productionLineLineNumber = in.readString();
                createdBy = in.readString();
                attributeSetInstance = in.readHashMap(HashMap.class.getClassLoader());
                product = in.readParcelable(Product.class.getClassLoader());
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(productionLineInputSeqId);
                dest.writeString(locatorId);
                dest.writeString(productId);
                dest.writeString(attributeSetInstanceId);
                dest.writeDouble(quantity);
                dest.writeLong(version);
                dest.writeString(productionId);
                dest.writeString(productionLineLineNumber);
                dest.writeString(createdBy);
                dest.writeMap(attributeSetInstance);
                dest.writeParcelable(product, flags);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<ProductionLineInput> CREATOR = new Creator<ProductionLineInput>() {
                @Override
                public ProductionLineInput createFromParcel(Parcel in) {
                    return new ProductionLineInput(in);
                }

                @Override
                public ProductionLineInput[] newArray(int size) {
                    return new ProductionLineInput[size];
                }
            };

            public String getProductionLineInputSeqId() {
                return productionLineInputSeqId;
            }

            public void setProductionLineInputSeqId(String productionLineInputSeqId) {
                this.productionLineInputSeqId = productionLineInputSeqId;
            }

            public String getLocatorId() {
                return locatorId;
            }

            public void setLocatorId(String locatorId) {
                this.locatorId = locatorId;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getAttributeSetInstanceId() {
                return attributeSetInstanceId;
            }

            public void setAttributeSetInstanceId(String attributeSetInstanceId) {
                this.attributeSetInstanceId = attributeSetInstanceId;
            }

            public double getQuantity() {
                return quantity;
            }

            public void setQuantity(double quantity) {
                this.quantity = quantity;
            }

            public long getVersion() {
                return version;
            }

            public void setVersion(long version) {
                this.version = version;
            }

            public String getProductionId() {
                return productionId;
            }

            public void setProductionId(String productionId) {
                this.productionId = productionId;
            }

            public String getProductionLineLineNumber() {
                return productionLineLineNumber;
            }

            public void setProductionLineLineNumber(String productionLineLineNumber) {
                this.productionLineLineNumber = productionLineLineNumber;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public Date getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(Date createdAt) {
                this.createdAt = createdAt;
            }

            public Map getAttributeSetInstance() {
                return attributeSetInstance;
            }

            public void setAttributeSetInstance(Map attributeSetInstance) {
                this.attributeSetInstance = attributeSetInstance;
            }

            public Product getProduct() {
                return product;
            }

            public void setProduct(Product product) {
                this.product = product;
            }
        }

        public static class ProductionLineOutput implements Parcelable {

            private String productionLineOutputSeqId;
            private String locatorId;
            private String productId;
            private String attributeSetInstanceId;
            private double quantity;
            private long version;
            private String productionId;
            private String productionLineLineNumber;
            private String createdBy;
            private Date createdAt;
            private Map attributeSetInstance;
            private Product product;

            protected ProductionLineOutput(Parcel in) {
                productionLineOutputSeqId = in.readString();
                locatorId = in.readString();
                productId = in.readString();
                attributeSetInstanceId = in.readString();
                quantity = in.readDouble();
                version = in.readLong();
                productionId = in.readString();
                productionLineLineNumber = in.readString();
                createdBy = in.readString();
                attributeSetInstance = in.readHashMap(HashMap.class.getClassLoader());
                product = in.readParcelable(Product.class.getClassLoader());
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(productionLineOutputSeqId);
                dest.writeString(locatorId);
                dest.writeString(productId);
                dest.writeString(attributeSetInstanceId);
                dest.writeDouble(quantity);
                dest.writeLong(version);
                dest.writeString(productionId);
                dest.writeString(productionLineLineNumber);
                dest.writeString(createdBy);
                dest.writeMap(attributeSetInstance);
                dest.writeParcelable(product, flags);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<ProductionLineOutput> CREATOR = new Creator<ProductionLineOutput>() {
                @Override
                public ProductionLineOutput createFromParcel(Parcel in) {
                    return new ProductionLineOutput(in);
                }

                @Override
                public ProductionLineOutput[] newArray(int size) {
                    return new ProductionLineOutput[size];
                }
            };

            public String getProductionLineOutputSeqId() {
                return productionLineOutputSeqId;
            }

            public void setProductionLineOutputSeqId(String productionLineOutputSeqId) {
                this.productionLineOutputSeqId = productionLineOutputSeqId;
            }

            public String getLocatorId() {
                return locatorId;
            }

            public void setLocatorId(String locatorId) {
                this.locatorId = locatorId;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getAttributeSetInstanceId() {
                return attributeSetInstanceId;
            }

            public void setAttributeSetInstanceId(String attributeSetInstanceId) {
                this.attributeSetInstanceId = attributeSetInstanceId;
            }

            public double getQuantity() {
                return quantity;
            }

            public void setQuantity(double quantity) {
                this.quantity = quantity;
            }

            public long getVersion() {
                return version;
            }

            public void setVersion(long version) {
                this.version = version;
            }

            public String getProductionId() {
                return productionId;
            }

            public void setProductionId(String productionId) {
                this.productionId = productionId;
            }

            public String getProductionLineLineNumber() {
                return productionLineLineNumber;
            }

            public void setProductionLineLineNumber(String productionLineLineNumber) {
                this.productionLineLineNumber = productionLineLineNumber;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public Date getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(Date createdAt) {
                this.createdAt = createdAt;
            }

            public Map getAttributeSetInstance() {
                return attributeSetInstance;
            }

            public void setAttributeSetInstance(Map attributeSetInstance) {
                this.attributeSetInstance = attributeSetInstance;
            }

            public Product getProduct() {
                return product;
            }

            public void setProduct(Product product) {
                this.product = product;
            }
        }
    }
}
