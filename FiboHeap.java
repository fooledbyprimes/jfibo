package project1;
import java.util.*;
import java.math.*;


//THE MOTHER OF ALL HEAPS:
//A FIBONACCI HEAP IS IMPLEMENTED
//BY THIS CLASS.
//IT REQUIRES HEAPed OBJECTS
//TO IMPLEMENT THE 'fibofied'
//INTERFACE METHODS.


public class FiboHeap {
    
    private fibofied minNode;
    private int numNodes;


    //CONSTRUCTOR

    public FiboHeap() {
        minNode = null;
        numNodes = 0;
    }
    

    //INSERT METHOD
    //JUST ADD NODE TO THE ROOT LIST AND
    //ADJUST THE KEY
    
    public void insert(fibofied n) {
        n.setParent(null);
        n.setChild(null);
        n.setMark(false);
        n.setDegree(0);


        if (numNodes == 0) {
            minNode = n;
            minNode.setLeft(n);
            minNode.setRight(n);
        }
        else {

            n.setLeft(minNode.getLeft());
            n.setRight(minNode);
            minNode.getLeft().setRight(n);
            minNode.setLeft(n);

            if ( n.getKey() < minNode.getKey() ) {
                minNode = n;
            }

        }

        numNodes++;


    }

    
    //CALLED BY THE EXTRACT-MIN METHOD.
    //consolidate CAN BE THOUGHT TO 
    //"BALANCE" THE DATA STRUCTURE.


    private void consolidate() {
        ArrayList auxArray = new ArrayList();
        boolean oneNode;
        fibofied fnode1,fnode2;
        fibofied auxEntry; 
        BigDecimal j = new BigDecimal(
              Math.floor(Math.log(numNodes) / Math.log(2)) );
        BigInteger s = j.toBigInteger();

        
        auxArray.ensureCapacity(s.intValue() + 1);
        auxEntry = null;
        for (int i = 0; i <= s.intValue(); i++) {
            auxArray.add(auxEntry);
        }

        //PROCESS EACH NODE IN ROOT LIST

        fnode1 = minNode;
        fnode2 = minNode.getRight();
        oneNode = (fnode1 == fnode2);
        while (oneNode || (fnode2 != fnode1)) {
            int d = fnode2.getDegree();        
            int debug = auxArray.size();
            while ((fibofied) auxArray.get(d) != null) {
                fibofied y = (fibofied) auxArray.get(d);

                //EXCHANGE Y AND X (fnode2)
                if (fnode2.getKey() > y.getKey()) {
                    fibofied tmp = fnode2;
                    fnode2 = y;
                    y = tmp;
                }

                //HEAP-LINK
                y.getRight().setLeft(y.getLeft());
                y.getLeft().setRight(y.getRight());
                fnode2.setDegree(fnode2.getDegree()+1);
                y.setParent(fnode2);
                if (fnode2.getChild() == null) {
                    fnode2.setChild(y);
                    y.setRight(y);
                    y.setLeft(y);
                } 
                else {
                    y.setRight(fnode2.getChild());
                    y.setLeft(fnode2.getChild().getLeft());
                    fnode2.getChild().getLeft().setRight(y);
                    fnode2.getChild().setLeft(y);
                }
                y.setMark(false);
                //END HEAP-LINK
                
                auxArray.set(d,null);
                d++;
            }
            auxArray.set(d,fnode2);
            oneNode = false;
            fnode2 = fnode2.getRight();
        }
        
        //FINAL FOR  (NO PUN INTENTED - ITS MARCH - GO ILLINOIS)
        minNode = null;
        for (Iterator i = auxArray.iterator(); i.hasNext(); ) {
            fnode1 = (fibofied) i.next();
            if (fnode1 != null) {
                if ((minNode == null)) {
                    minNode = fnode1;
                } 
                else if (fnode1.getKey() < minNode.getKey()) {
                    minNode = fnode1;
                }
            }
        }
    }




    //EXTRACT MIN RETURNS THE MIN NODE, AND
    //THEN CONSOLIDATES THE TREES IN THE 
    //ROOT LIST.


    public fibofied extractMin() {
        fibofied fnode1;
        fibofied fnode2;
        fibofied z;

        z = minNode;
        if (minNode != null) {

            //MOVE MIN's CHILDREN TO ROOT LIST

            if (minNode.getChild() != null) {
                fnode1 = minNode.getChild();
                fnode2 = fnode1.getRight();
                while (fnode2 != fnode1) {
                    fnode2.setParent(null);
                    fnode2.setLeft(minNode.getLeft());
                    fnode2.setRight(minNode);
                    minNode.getLeft().setRight(fnode2);
                    minNode.setLeft(fnode2);
                    fnode2 = fnode2.getRight();
                }
                fnode1.setParent(null);
                fnode1.setLeft(minNode.getLeft());
                fnode1.setRight(minNode);
                minNode.getLeft().setRight(fnode1);
                minNode.setLeft(fnode1);
            }


            //REMOVE z FROM ROOT LIST

            fnode1 = minNode.getLeft();
            fnode2 = minNode.getRight();
            fnode1.setRight(fnode2);
            fnode2.setLeft(fnode1);


                
            if (minNode == minNode.getRight()) {
                minNode = null;
            }
            else {
                minNode = minNode.getRight();
                consolidate();
            }

            numNodes--;
        }
        
        return z;
    }

    

    //USED BY THE DECREASE-KEY PROCEDURE.


    private void cut(fibofied x, fibofied y) {
        x.getLeft().setRight(x.getRight());
        x.getRight().setLeft(x.getLeft());
        if ((y.getDegree() > 1) &&
            (y.getChild() == x)) {
            y.setChild(x.getRight());
        } 
        else if (y.getDegree() == 1) {
            y.setChild(null);
        }
        y.setDegree(y.getDegree() - 1);
        x.setParent(null);
        minNode.getLeft().setRight(x);
        x.setLeft(minNode.getLeft());
        minNode.setLeft(x);
        x.setRight(minNode);
        x.setMark(false);
    }

    //USED BY THE DECREASE-KEY METHOD

    private void cascadeCut(fibofied y) {
        fibofied z = y.getParent();
        if (z != null) {
            if (!y.getMark()) {
                y.setMark(true);
            }
            else {
                cut(y,z);
                cascadeCut(z);
            }
        }
    }


    //DECREASE THE KEY OF A NODE IN THE HEAP.
    //THIS CAN POSSIBLY UPSET THE HEAP STRUCTURE
    //IN ONE OF THE TREES AND WILL THEREFORE
    //"BALANCE" THE UPSET TREE IF NECESSARY.

    public void decreaseKey(fibofied fnode, int newKey) {
        if (newKey < fnode.getKey()) {
            fnode.setKey(newKey);
            fibofied y = fnode.getParent();
            if ((y != null) && (fnode.getKey() < y.getKey())) {
                cut(fnode,y);
                cascadeCut(y);
            }
            if (fnode.getKey() < minNode.getKey()) {
                minNode = fnode;
            }
        }
    }



    //CONVENIENCE METHODS

    public int getSize() { return numNodes; }
    public int minPeek() { return minNode.getKey(); }
    public String toString() {
        String s = "";
        if (numNodes > 0) {
            s = s.concat( "\t minNode.KEY: " +minNode.getKey());
            s = s.concat("\t numNodes: " + numNodes + "\n");
        }
        return s;
    }
}
