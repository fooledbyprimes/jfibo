package project1;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;




//THIS CLASS RUNS THE GUI.
//DIJKSTRA'S ALGORITHM IS
//USED WHEN YOU SELECT THE
//process GUI BUTTON.
//THE ONLY THINGS INTERESTING
//ARE THE USE OF THE JAVA
//STREAM TOKENIZER OBJECT 
//INSIDE THE loadAdjacencyList 
//METHOD AND THE USE OF THE
//NOTORIOUSLY COMPLEX FIBONACCI
//HEAP DATA STRUCTURE.
//  CHECK IT OUT.


public class FileViewer extends Frame implements ActionListener, ItemListener {
    String directory; 
    Button process; 
    TextArea textarea;
    private List vertexList;
    adjacencyList adjList;
    FiboHeap prioriQ;
    aLstVertex sourceVertex;
    private String graphDescrip;
    private String inputSourceVertex;
    private int sourceIndex = 0;


    //GUI CONSTRUCTORS... NOTHING SPECIAL
    //DIRTY WORK

    public FileViewer() { this(null, null); }

    public FileViewer(String filename) { this(null, filename); }

    public FileViewer(String directory, String filename) {
        super();  
        addWindowListener(new WindowAdapter() {
     		public void windowClosing(WindowEvent e) { dispose(); }
	    });

       
        adjList = null;
        prioriQ = new FiboHeap();
        sourceVertex = null;
        graphDescrip = new String();
        inputSourceVertex = new String();

        textarea = new TextArea("", 24, 80);
        textarea.setFont(new Font("MonoSpaced", Font.PLAIN, 12));

        vertexList = new List(10,false);
        vertexList.setFont(new Font("MonoSpaced",Font.PLAIN,14));
        vertexList.addActionListener(this);
        vertexList.addItemListener(this);
        vertexList.setEnabled(false);

        Font font = new Font("SansSerif", Font.BOLD, 14);
        Button openfile = new Button("Open File");
        Button close = new Button("Close");
        process = new Button("Process");
        
        openfile.addActionListener(this);
        openfile.setActionCommand("open");
        openfile.setFont(font);

        close.addActionListener(this);
        close.setActionCommand("close");
        close.setFont(font);
        close.setSize(new Dimension(30,10));

        process.addActionListener(this);
        process.setActionCommand("process");
        process.setFont(font);
        process.setEnabled(false);


        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);
        
        c.gridx = 2; c.gridy = 0; c.gridwidth = 3; c.gridheight = 3;
        c.weightx = c.weighty = 1.0;
        this.add(textarea,c);

        c.fill = GridBagConstraints.NONE;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.gridheight = 1;
        c.weightx = c.weighty = 0;
        this.add(new JLabel("Selected Source Vertex:"),c);

        c.gridx = 0; c.gridy = 1; c.gridwidth = 2; c.gridheight = 2;
        c.weightx = c.weighty = 0;
        this.add(vertexList,c);
        

        c.gridx = 6; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1;
        c.weightx = c.weighty = 0;
        this.add(openfile,c);
        
        c.gridy = GridBagConstraints.RELATIVE; c.weightx = c.weighty = 0;
        this.add(close,c);

        c.gridwidth = 1; c.gridheight = 1;
        c.weightx = c.weighty = 0;
        this.add(process,c);

        
        this.pack();
	
        if (directory == null) {
            File f;
            if ((filename != null)&& (f = new File(filename)).isAbsolute()) {
                directory = f.getParent();
                filename = f.getName();
            }
            else directory = System.getProperty("user.dir");
        }
	
        this.directory = directory;   
        setFile(directory, filename); 
    }
    


    //GET DESIRED FILE.  READ IT INTO BUFFER

    public void setFile(String directory, String filename) {
        if ((filename == null) || (filename.length() == 0)) return;
        File f;
        FileReader in = null;

        try {
            f = new File(directory, filename); 
            in = new FileReader(f);            
            char[] buffer = new char[4096];    
            int len;                           
            graphDescrip = "";
            while((len = in.read(buffer)) != -1) { 
                String s = new String(buffer, 0, len); 
                graphDescrip = graphDescrip.concat(s);                    
            }
            this.setTitle("FileViewer: " + filename);  
        }
        catch (IOException e) { 
            textarea.setText(e.getClass().getName() + ": " + e.getMessage());
            this.setTitle("FileViewer: " + filename + ": I/O Exception");
        }
        finally { try { if (in!=null) in.close(); } catch (IOException e) {} }
    }



    //CLEAN THE ADJACENCY LIST DATA STRUCTURE 
    //BEFORE A NEW GRAPH INPUT FILE IS 
    //SELECTED

    private void cleanAdjacencyList() {

        aLstVertex v = null;
        for (Iterator i = adjList.vertexArray.iterator(); i.hasNext(); ) {
            v = (aLstVertex) i.next();
            v.setKey(9999999);
            v.vertex.piSet(null);
        }

    }


    //TAKE AN INPUT STREAM AND LOAD THE ADJACENCY LIST 
    //DATA STRUCTURE.  LOOK FOR ERRORS IN THE INPUT.
    //USE JAVA'S BUILT-IN STREAM TOKENIZER TO READ INPUT FILE
    //NOTE THE CONVERSION TO int FROM THE PARSER'S double
    //ELEMENT VALUE

    private boolean loadAdjacencyList(String S) {
        StringReader reader;
        StreamTokenizer parser;

        boolean readingVertices = true;
        boolean readingEdges = false;
        int edgeParts = 0;

        String from = new String();
        String to   = new String();
        

        try {

          reader = new StringReader(S);
          parser = new StreamTokenizer(reader);
          parser.eolIsSignificant(true);
          
          boolean gotSource = false;
          boolean expectVtx = false;
          boolean expectSink = false;
          boolean expectWeight = false;

          int inNum,weight;

          while ( parser.nextToken() != StreamTokenizer.TT_EOF ) {
            if (parser.ttype == StreamTokenizer.TT_WORD) {
                echo("ERROR: input has non numeric elements...\n");
                return false;
            }
            else if (parser.ttype == StreamTokenizer.TT_NUMBER ) {

                inNum = (int) parser.nval;
                if (inNum < 0) {
                    inNum = -inNum;
                    echo("WARNING: converted input element to abs()...\n");
                }

                if (!gotSource) {
                    gotSource = true;
                    inputSourceVertex = String.valueOf((int) parser.nval);
                    adjList.addVertex(inputSourceVertex);
                    expectVtx = true;
                }
                else if (expectVtx) {
                    from = String.valueOf((int) parser.nval);
                    adjList.addVertex(from);
                    expectVtx = false;
                    expectSink = true;
                }
                else if (expectSink) {
                    to = String.valueOf((int) parser.nval);
                    expectSink = false;
                    expectWeight = true;
                }
                else if (expectWeight) {
                    weight = (int) parser.nval;
                    adjList.addEdge(to,from,weight);
                    expectWeight = false;
                    expectSink = true;
                }
            }
            else if (parser.ttype == StreamTokenizer.TT_EOL) {
                expectVtx = true;
                //expectSink = expectWeight = false;
            }
          }

          if (!(expectVtx || expectSink)) { 
              echo("ERROR: INCOMPLETE EDGE DESCRIPTION...\n"); 
              return false;
          }

        }
        catch (IOException e) {
            textarea.setText(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        
        return true;
    }




    //CONVENIENCE 
    public void echo(String s) {
        textarea.append(s);
    }


    
    public void printHeapInfo() {
 
    }






    //RUN DIJKSTRA'S SINGLE SOURCE SHORTEST PATH ALGORITHM.
    //USE THE FIBONACCI HEAP prioriQ.
    //USE THE ADJACENCY LIST STRUCTURE adjList.
    //NOTICE THE USE OF THE fibofied interface setKey()
    //FUNCTION.
    //EXTENSIVE USE OF java.util CONTAINERS & ITERATORS

    private void runDijkstraSSSP() {
        aLstVertex entry = new aLstVertex();
        FiboHeap prioriQ = new FiboHeap();
            
        
        // INIT SINGLE SOURCE

        for ( Iterator i = adjList.vertexArray.iterator(); i.hasNext(); ) {
            entry = (aLstVertex) i.next();
            entry.setKey(9999999);
        }
        entry = (aLstVertex) adjList.vertexArray.get(sourceIndex);
        //debug// echo("SELECTED INDEX:"+sourceIndex);

        entry.setKey(0);
        sourceVertex = entry;
  
        //debug// echo(adjList.showVerticies());

        echo("INIT-SINGLE-SOURCE: okay...\n");
        

        // DIJKSTRA SSSP ALGORITHM:  

        echo("DIJKSTRA SSSP ALGORITHM: progressing...\n");

        // LOAD PRIORITY QUEUE

        for (Iterator i = adjList.vertexArray.iterator(); i.hasNext(); ) {
            entry = (aLstVertex) i.next();
            prioriQ.insert(entry);
            //debug// echo(prioriQ.toString());
        }
        echo("\tpriorty queue loaded...\n");

        // SELECT VERTICIES,
        // RELAX EDGES.

        aLstEdge edge = new aLstEdge();
        echo("\tentering main iteration...\n");
        while (prioriQ.getSize() > 0) {
            //debug// echo("("+prioriQ.getSize()+")\t\n");
            aLstVertex v = (aLstVertex) prioriQ.extractMin();
            //debug// echo(v.vertex.name+": ");
            if (v != null) {
                for (Iterator i = v.edgeList.iterator(); i.hasNext(); ) {
                    edge = (aLstEdge) i.next();
                    //debug// echo (edge.sink.vertex.name+ "\t");
                    if (edge.sink.getKey() > v.vertex.dGet() + edge.weight ) {
                        prioriQ.decreaseKey(edge.sink, v.vertex.dGet() + edge.weight);
                        edge.sink.vertex.piSet(v.vertex);
                    }
                }
            }
            //debug// echo("\n");
        }
    } 





    //CONVENIENCE...
    //DUMP THE SHORTEST PATHS INFORMATION WHICH
    //WAS CALCULATED BY THE runDijkstra METHOD
    //(see above)
    //NOTHING INTERESTING...

    public void showPaths() {
        echo ("RESULTS FOR SOURCE "+sourceVertex.vertex.name+"\n");
        aLstVertex v;
        for (Iterator i = adjList.vertexArray.iterator(); i.hasNext(); ) {
            v = (aLstVertex) i.next();
            if (v != sourceVertex) {
                echo("\tFROM ["+v.vertex.name+"]: COST["+v.vertex.dGet()+"]  PATH[");
                graphVertex v2;
                v2 = v.vertex.piGet();
                while (v2 != null) {
                    echo(v2.name);
                    if (!(v2.name.equals(sourceVertex.vertex.name))) {
                        echo(".");
                    }
                    v2 = v2.piGet();
                }
                echo("]\n");
            }
        }
        echo("END RESULTS\n\n");
    }



    //GUI CONTROL.  CHANGE BUTTON STATES AND 
    //CLEAN DATA STRUCTURES FOR REUSE
    //WHEN A DIFFERENT SOURCE VERTEX
    //IS SELECTED.

    public void itemStateChanged(ItemEvent e) {
        sourceIndex = vertexList.getSelectedIndex();
        if (!process.isEnabled()) {
            cleanAdjacencyList();
            process.setEnabled(true);
        }
        echo("NEW SOURCE VERTEX: " + 
                 vertexList.getItem(vertexList.getSelectedIndex()) +
                 "\n");
    }

    
    //MAIN GUI CONTROL.  ALSO MANAGE,CLEAN,
    //AND DESTROY THE MAJOR DATA STRUCTURES
    //USED BY THE PROGRAM.
    //NOTHING SPECIAL

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("open")) {          
            graphDescrip = "";
            vertexList.removeAll();
            textarea.setText("");
            

            FileDialog f = new FileDialog(this, "Open File", FileDialog.LOAD);
            f.setDirectory(directory);     
            f.show();                        

            directory = f.getDirectory();  
            echo("BEGIN GRAPH INPUT FILE...(starting next line)\n");
            setFile(directory, f.getFile());
            echo(graphDescrip+"\n");
            echo("END GRAPH FILE CONTENTS\n\n\n");

            f.dispose();

            //FORCE GARBAGE COLLECTOR TO DELETE OLD adjList
            adjList = new adjacencyList();

            if ( loadAdjacencyList(graphDescrip) ) {
                echo("ADJACENCY LIST LOAD: okay...\n");
                //debug// echo(adjList.textof());
                
                aLstVertex v = new aLstVertex();
                for (Iterator i = adjList.vertexArray.iterator(); i.hasNext(); ) {
                    v = (aLstVertex) i.next();
                    vertexList.add(v.vertex.name);
                }
                vertexList.setEnabled(true);
                vertexList.select(0);
                process.setEnabled(true);
                echo("NEW SOURCE VERTEX: "+
                     vertexList.getItem(vertexList.getSelectedIndex()) +
                     "\n");
            }
            else {
                //FLAG ERROR  
                textarea.append("Bad Graph Descrip...\n");
            };
        }
        else if (cmd.equals("process")) {
            runDijkstraSSSP();
            showPaths();
            process.setEnabled(false);
            
        }
        else if (cmd.equals("close"))      
            this.dispose();                
    }
    
 
}





