package project1;
import java.util.*;


// AN ADJACENCY LIST VERTEX.
// THIS TYPE OF VERTEX OBJECT HAS AN EDGE LIST.
// THIS OBJECT IMPLEMENTS 'fibofied' INTERFACE
// BECAUSE THEY WILL GET INSERTED INTO
// THE FIBONACCI HEAP STRUCTURE WHICH REQUIRES
// OBJECTS TO BE 'fibofied.'



public class aLstVertex implements fibofied {
    public graphVertex vertex;
    public LinkedList edgeList;

    fibofied parent,child,left,right;
    boolean marked;
    int key;
    int degree;

    //CONSTRUCTORS

    public aLstVertex(graphVertex v) {
        vertex = new graphVertex(v);
        edgeList = new LinkedList();
        key = 9999999;
        vertex.dSet(key);
        marked = false;
    }

    public aLstVertex() {
        vertex = new graphVertex();
        edgeList = new LinkedList();
        key = 9999999;
        vertex.dSet(key);
        marked = false;

    }

    public aLstVertex(aLstVertex v) {
        vertex = new graphVertex(v.vertex);
        edgeList = new LinkedList(v.edgeList);
    }


    //ADD AND OUT EDGE TO THIS VERTEX
    //CALLED DURING CONSTRUCTION OF THE 
    //ADJACENCY LIST

    public void addOutEdge(aLstVertex v, int weight) {
        edgeList.add(new aLstEdge(v,weight));
    }



    // FIBOFIED INTERFACE DEFINITION
    // REQUIRED BY 'fibofied' INTERFACE
    // CONSTRUCT.

    public fibofied getParent() { return parent; }
    public void setParent( fibofied v ) {
        parent = v;
    }

    public fibofied getChild() { return child; }
    public void setChild( fibofied v ) {
        child = v;
    }

    public fibofied getLeft(){ return left; }
    public void setLeft( fibofied v ) {
        left = v;
    }

    public fibofied getRight() { return right; }
    public void setRight( fibofied v ) {
        right = v;
    }

    public boolean getMark() { return marked; }
    public void setMark(boolean m) {
        marked = m;
    }

    public int  getKey()           { return key; }
    public void setKey(int k)      { key = k; vertex.dSet(k);   }
    public int  getDegree()        { return degree;   }
    public void setDegree(int dIn) { degree = dIn;    }


}
