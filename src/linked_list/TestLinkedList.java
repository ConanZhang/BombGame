/**
 * 
 */
package linked_list;
/**
 * @author conanz
 * 
 * Class to test LinkedList class along with its Iterator and their methods
 */
public class TestLinkedList 
{
    /**MAIN METHOD FOR TESTING**/
    public static void main(String args[])
    {
        /**Test Linked List**/
        //Create linked list that holds integers
        LinkedList<Integer> testList = new LinkedList<Integer>();
        
        System.out.println( "Filled List with Numbers 1-4" );

        //Fill linked list
        testList.insert( new Integer(1) );
        testList.insert( new Integer(2) );
        testList.insert( new Integer(3) );
        testList.insert( new Integer(4) );

           
        //Print values of list and its size
        testList.print();
        System.out.println( "List Size: " + testList.size() );
        
        testList.remove(2);
        System.out.println( "Removed 2 from List" );

        //Print values of list and its size
        testList.print();
        System.out.println( "List Size: " + testList.size() );
        
        /**Test Iterator**/
        //Create iterator that starts at the first actual node of the linked list (not header)
        LinkedList<Integer>.Iterator testIterator = testList.first();
        
        testIterator.insert(2);
        System.out.println( "Inserted 2 to List" );

        //Print values of list and its size
        testList.print();
        System.out.println( "List Size: " + testList.size() );
        
        testList.find(3).remove();
        System.out.println( "Removed 3 from List" );

        //Print values of list and its size
        testList.print();
        System.out.println( "List Size: " + testList.size() );
        
        //Scroll through list as long as the node exists AND NOT AT THE END OF THE LIST (not footer)
        while ( testIterator.valid() ) 
        {
            System.out.println( testIterator.getData() );//print data of node
            testIterator.next();//move to next node
        }
    }
}
