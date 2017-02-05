package tm.shker.mohamed.chickengrill.Objects;

/**
 * Created by mohamed on 18/11/2016.
 * pojo
 */

public class DeliveryArea {
    String areaName;
    String deliveryCost;
    String deliveryTime;
    String minDelivery;

    public DeliveryArea() {
    }

    public DeliveryArea(String areaName, String deliveryCost, String deliveryTime, String minDelivery) {
        this.areaName = areaName;
        this.deliveryCost = deliveryCost;
        this.deliveryTime = deliveryTime;
        this.minDelivery = minDelivery;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getMinDelivery() {
        return minDelivery;
    }

    public void setMinDelivery(String minDelivery) {
        this.minDelivery = minDelivery;
    }

    @Override
    public String toString() {
        return "DeliveryArea{" +
                "areaName='" + areaName + '\'' +
                ", deliveryCost='" + deliveryCost + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", minDelivery='" + minDelivery + '\'' +
                '}';
    }
}
