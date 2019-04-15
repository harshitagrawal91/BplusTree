/*Main class*/
package bplus;

import java.util.ArrayList;

/**
 *
 * @author harsh
 */
public class BPlusTree {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         if(args.length>0){
             Tree tree=new Tree();
             ProcessInputFile processInput=new ProcessInputFile(args[0],tree);
             processInput.readFileAndHandleCommands();
             Utils.printTree(tree);
        }else{
            System.out.print("please enter input file name");
        }
    }
    
}
