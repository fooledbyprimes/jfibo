
public class FiboNode {
    FiboNode parent;
    FiboNode child;
    FiboNode left;
    FiboNode right;
    private aLstVertex alVertex;
    int degree;
    boolean marked;


    public FiboNode(aLstVertex v) {
        this.alVertex = new aLstVertex();
        this.alVertex = v;
        parent = child = left = right = null;
        marked = false;
        degree = 0;
    }

    public FiboNode() {
        this.alVertex = new aLstVertex();
        parent = child = left = right = null;
        marked = false;
        degree = 0;
    }

    public void setMark(boolean m) { marked = m;}
    public boolean marked() {return marked;}




}
