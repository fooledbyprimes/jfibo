package project1;

//  DELCARE FIBONACCI NODE FUNCTIONALITY


public interface fibofied {  //"FIBONACCIFIED"

    public fibonaccified getParent();
    public void setParent(fibonaccified vertex);

    public fibonaccified getChild();
    public void setChild(fibonaccified vertex);

    public fibonaccified getLeft();
    public void setLeft(fibonaccified vertex);

    public fibonaccified getRight();
    public void setRight(fibonaccified vertex);

    public boolean getMark();
    public void setMark(boolean m);
    public int getDegree();
    public void setDegree(int dIn);

    public void setKey(int k);
    public int getKey();

}
