package oast.project.model;


//P(demand,path)
public class P {
    private int demandId;
    private int pathId;

    public P(int demandId, int pathId) {
        this.demandId = demandId;
        this.pathId = pathId;
    }

    public int getDemandId() {
        return demandId;
    }

    public void setDemandId(int demandId) {
        this.demandId = demandId;
    }

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        P p = (P) o;

        if (demandId != p.demandId) return false;
        return pathId == p.pathId;
    }

    @Override
    public int hashCode() {
        int result = demandId;
        result = 31 * result + pathId;
        return result;
    }
}
