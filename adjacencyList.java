package project1;
import java.util.*;


//THIS CLASS ENCAPSULATES THE ADJACENCY LIST STRUCTURE FOR
//MATHEMATICAL GRAPH SPECIFICATIONS


public class adjacencyList {
    private int numVerticies;
    private int numEdges;
    public ArrayList vertexArray;


    //CONSTRUCTORS

    public adjacencyList() {
        this.numVerticies = 0;
        this.numEdges = 0;
        vertexArray = new ArrayList();
    }




    //ADD A VERTEX TO THE ADJACENCY LIST ONLY IF
    //IT HAS NOT BEEN ADDED ALREADY.

    public void addVertex(String n) {
        boolean found = false;
        aLstVertex v = null;

        for (Iterator i = vertexArray.iterator(); i.hasNext() && !found; ) {
            v = (aLstVertex) i.next();
            if (v != null) {
                if (v.vertex.name.equals(n)) { found = true; }
            }

        }

        if (!found) {
            graphVertex vertex = new graphVertex(n);
            aLstVertex alVertex = new aLstVertex(vertex);
            vertexArray.add(alVertex);
        }
    }


    //ADD AN EDGE.  TRY TO ADD THE SINK VERTEX TO THE VERTEX LIST
    //BECAUSE IT MIGHT NOT HAVE BEEN SPECIFIED IN THE INPUT JUST YET.
    //CHECK FOR EXISTANCE OF BOTH SOURCE AND SINK BEFORE WE
    //ADD THE OUT EDGE TO THE SOURCE VERTEX'S EDGE OUT LIST.


    public void addEdge(String t, String f, int weight) {

        addVertex(t);        //CHECK TO SEE IF t SHOULD BE ADDED TO VERTICIES

        aLstVertex av = new aLstVertex();
        aLstVertex fromVertex = null; 
        aLstVertex toVertex = null;

        boolean ffound = false;
        boolean tfound = false;

        
        for (Iterator i = vertexArray.iterator(); 
             (i.hasNext() && !(ffound && tfound)); ) {

            av = (aLstVertex) i.next();
            if (av.vertex.name.equals(f)) {
                ffound = true; fromVertex = av;
            }
            else if (av.vertex.name.equals(t)) {
                tfound = true; toVertex = av;
            }

        }
        
        if (ffound && tfound) {
            fromVertex.addOutEdge(toVertex,weight);
            numEdges++;
        }

    }



    //RETURN THE NUMBER OF VERTICIES IN THE ADJACENCY LIST

    public int getNumVerticies() {
        return numVerticies;
    }


    //STRING REPRESENTATION OF THE ADJACENCY LIST 
    //USED FOR DEBUGGING PURPOSES

    public String textof() {
        String S = new String("adjList contents:\n");
        aLstVertex vtxEntry = new aLstVertex();
        aLstEdge alEdge = new aLstEdge();

        for (Iterator i = vertexArray.iterator(); i.hasNext(); ) {
            vtxEntry = (aLstVertex) i.next();
            S = S.concat( "[" + vtxEntry.vertex.name + "] ");
            for (Iterator j = vtxEntry.edgeList.iterator(); j.hasNext(); ) {
                alEdge = (aLstEdge) j.next(); 
                S = S.concat("."+alEdge.sink.vertex.name + "(" + alEdge.weight + ")");
            }
            S = S.concat("\n");
        }
        
        return S; 
    }


    //MORE DEBUGGING

    public String showVerticies() {
        String S = new String("ADJACENCY LIST VERTEX DUMP:\n");
        for (Iterator i = vertexArray.iterator(); i.hasNext(); ) {
            aLstVertex alv = (aLstVertex) i.next();
            S = S.concat("\t"+alv.vertex.name + ".d["+alv.vertex.dGet()
                         + "].pi[");
            if (alv.vertex.piGet() == null) {
                S = S.concat("null]");
            }
            else {
                S = S.concat(alv.vertex.piGet().name +"]");
            }
                
        }
        return S.concat("\n");
    }
}
