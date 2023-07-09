package stepper.dataStorage;

public class StatisticData {
    private String name;
    private Integer timeUsed;
    private Double AverageRunTime;
    public StatisticData(String name, Integer timeUsed, Double AverageRunTime){
        this.AverageRunTime = AverageRunTime;
        this.timeUsed = timeUsed;
        this.name = name;
    }
    public Double getAverageRunTime() {
        return AverageRunTime;
    }

    public Integer getTimeUsed() {
        return timeUsed;
    }

    public String getName() {
        return name;
    }
    public StatisticData AddAdditionalVariables(StatisticData data){
        timeUsed += data.timeUsed;
        AverageRunTime = (AverageRunTime + data.AverageRunTime) / 2;
        return this;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return name.equals(((StatisticData)obj).getName());
    }
}
