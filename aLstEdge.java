package project1;

//THIS CLASS IS USED TO STORE OUT EDGE INFORMATION
//IN THE EDGE LIST OF AN ADJACENCY LIST VERTEX OBJECT.
//STORED DATA INCLUDES THE SINK VERTEX AND 
//THE WEIGHT OF THIS OUT EDGE.

public class aLstEdge {
    public aLstVertex sink;
    public int weight;
    public aLstEdge(aLstVertex vin, int weight) {
        sink = new aLstVertex();
        sink = vin;
        this.weight = weight;
    }
    public aLstEdge() {
        sink = null;
        this.weight = 0;
    }

}
