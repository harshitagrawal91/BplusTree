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
             Double s=tree.search(23);
                System.out.println("search------------"+s+"\n");
        Utils.printTree(tree);
        }else{
            System.out.print("please enter input file name");
        }
//                tree.insert(21,"0.3534");
//                tree.insert(108,"31.907");
//                tree.insert(56089, "3.26");
//                tree.insert(234, "121.56");
//                tree.insert(4325, "109.23");
//                
//                tree.insert(102, "39.56");
//                tree.insert(65, "-3.95");
//                
//                tree.insert(106, "-3.91");
//                tree.insert(23, "3.55");
//                 tree.insert(32, "0.02");
//                tree.insert(220, "3.55");
//		myTree.insert(2, "10");
//		myTree.insert(4, "20");
//		myTree.insert(5, "30");
//                myTree.insert(5, "35");
//		myTree.insert(7, "40");
////		myTree.insert(8, "50");
////		myTree.insert(9, "80");
////		myTree.insert(6, "100");
////		myTree.insert(11, "5");
////		myTree.insert(12, "8");
////		myTree.insert(13, "6");
//                myTree.delete(7);
////                myTree.delete(12);
////                myTree.delete(13);
////                myTree.delete(10);
////                myTree.delete(8);
////               StringBuilder b=myTree.range_search(5, 9);
//		String s= myTree.search(4);
//                System.out.println("search------------"+s+"\n");
//                 System.out.println("search range------------"+b+"\n");
//		Utils.printTree(tree);
    }
    
}
