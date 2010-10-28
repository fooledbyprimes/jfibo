package project1;

//  declare fibonacci node attributes


public interface fibofied {  //"FIBONACCIFIED"

    public fibofied getParent();
    public void setParent(fibofied vertex);

    public fibofied getChild();
    public void setChild(fibofied vertex);

    public fibofied getLeft();
    public void setLeft(fibofied vertex);

    public fibofied getRight();
    public void setRight(fibofied vertex);

    public boolean getMark();
    public void setMark(boolean m);

    public int getDegree();
    public void setDegree(int dIn);

    public void setKey(int k);
    public int getKey();

}
