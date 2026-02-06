package api.models.plants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantSummary {
    private int totalPlants;
    private int lowStockPlants;

    public int getTotalPlants() {
        return totalPlants;
    }

    public void setTotalPlants(int totalPlants) {
        this.totalPlants = totalPlants;
    }

    public int getLowStockPlants() {
        return lowStockPlants;
    }

    public void setLowStockPlants(int lowStockPlants) {
        this.lowStockPlants = lowStockPlants;
    }
}
