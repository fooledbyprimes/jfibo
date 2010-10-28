package project1;


// THIS IS A GRAPH VERTEX REPRESENTATION.
// THERE ARE FIELDS FOR A NAME, A DISTANCE FROM
// SOME SOURCE, AND A POINTER TO ANOTHER VERTEX 
// FOR APPLICATIONS WHICH REQUIRE HISTORICAL
// DATA.


public class graphVertex {      //implements fibofied {

    public int color;
    public String name;
    private int d;
    private graphVertex pi;


    // CONSTRUCTORS

    public graphVertex(String name) {
        this.name = new String(name);
        this.color = color;
        //        pi = new graphVertex();
        //        key = d = -1;
        //        marked = false;
    }

    public graphVertex(graphVertex v) {
        color = v.color;
        name = new String(v.name);
        d = v.dGet();
        
        //        pi = new graphVertex(v.pi);

    }
    public graphVertex() {this("");}



    // METHODS TO GET&SET FIELDS

    public void dSet(int in) { d = in; }
    public int dGet() { return d; }

    public void piSet(graphVertex v) { pi = v; }
    public graphVertex piGet() { return pi; }


    // STRING REPRESENTATION METHOD

    public String toString() {
        return name + ": d["+ d + "] pi[" + pi.name +"]";
    }

}
