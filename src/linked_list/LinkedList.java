/**
 * 
 */
package linked_list;
/**
 * @author conanz
 *
 * Class LinkedList to create a DOUBLY linked list that holds a generic data type that must be specified when created
 * Specify by: LinkedList<Type> testList = new LinkedList<Type>();
 * 
 * Contains inner classes Node and Iterator where Iterator has its own methods that are 0(1) operations
 */
public class LinkedList<Generic>
{
    /**INNER CLASS NODE**/
    /*
     * Is an inner class to avoid unnecessary parameters, have clean code, 
     * and completely hide the linked list nodes from the end user
     */
    private class Node
    {
        /**Node Class Member Variables**/
        /*Value*/
        Generic data;//data nodes hold **TYPE SPECIFIED WHEN LINKED LIST IS CREATED**
        
        /*Pointers*/
        Node next;//pointer to next node in linked list
        Node prev;//pointer to previous node in linked list
        
        /**NODE CONSTRUCTOR**/
        public Node (Generic data, Node next, Node prev)
        {
            //Assign parameters to class member variables
            this.data = data;
            
            this.next = next;
            this.prev = prev;
        }
    }
    
    /**LinkedList Class Member Variables**/
    //All doubly linked lists will have a header and footer reference point to simplify logic and code
     Node header, footer;
     
     /**LINKEDLIST CONSTRUCTOR**/
     public LinkedList() 
     {
         //Create reference object nodes for class member variables
         header = new Node(null, footer, null);//default header is a node with no data and no previous, but next points to footer
         footer = new Node(null, null, header);//default header is a node with no data and no next, but prev points to header
     }
     
     /**FUNCTION TO INSERT NEW NODE BEFORE FOOTER**/
     public void insert(Generic data)
     {
         //INSERT NEW NODE
         //Node to be inserted has footer as next and the footer's previous as its own previous
         Node n = new Node(data, footer, footer.prev);
         
         //ALTER POINTERS TO REFERENCE NEW POSITIONS
         //Node before n will now point to n as its next (instead of footer)
         footer.prev.next = n;
         //Footer's previous now points to n instead of node before n
         footer.prev = n;
     }
     
     /**FUNCTION TO REMOVE NODE FROM LINKED LIST BY SPECIFYING THE DATA IT HOLDS**/
     public boolean remove(Generic data)
     {
         //Create current node to scroll through list with that starts on the reference node header
         Node current = header;
         
         /*
          * Loop to scroll through linked list to find a node that has the specified data
          * Checks:
          *  1. See if current node actually exists (still in linked list)
          *  2. See if not about to scroll off list (current would be footer so there is no more to scroll through)
          *  3. Next's data doesn't equal data specified in function parameters (keep scrolling through)
          */
         while (current != null && current.next != null && !current.next.data.equals(data))
         { 
             //Move to next node in list because specified data has not been found yet
             current = current.next;
         }
         
         /*
          * Found data matching in linked list: current.next.data.equals(data) == true
          * Checks:
          *  1. See if current node actually exists (still in linked list)
          *  2. See if not footer (next would be null)
          */
         if (current != null && current.next != null) 
         {
             //SKIP OVER NODE WITH SPECIFIED DATA
             //Have node after the one we want remove previous point to the node before the one we want to remove       
             current.next.next.prev = current.next.prev;
             //Have node before the one we want remove next point to the node after the one we want to remove
             current.next = current.next.next;
             
             //Data has been found and removed so function was a success
             return true;
         }   
         //Data wasn't in linked list so removal was a failure
         return false;
     }
     
     /**FUNCTION TO RETURN SIZE OF LINKED LIST**/
     public int size()
     {
         //Create node to scroll through linked list with (starts on next actual node that could be footer)
         Node current = header.next;
         //Create counter to keep track of number of nodes (excludes header and footer)
         int s = 0;
         
         //Until you go off the list ONTO footer increment values
         while(current != footer)
         {
             //Increment counter and current node
             s++;
             current = current.next;
         }
         return s;     
     }
     
     /**FUNCTION TO RETURN ITERATOR WITH THE FIRST NODE OF THE LINKED LIST**/
     public Iterator first()
     {
         return new Iterator(header.next);
     }
     
     /**FUNCTION TO RETURN ITERATOR WITH THE LAST NODE OF THE LINKED LIST**/
     public Iterator last()
     {
         return new Iterator(footer.prev);
     }
     
     /**FUNCTION TO FIND NODE USING ITERATOR**/
     public Iterator find(Generic data)
     {
         //Start iterator search at first node of list (NOT header)
         Iterator search = this.first();
         
         //Keep advancing if that data hasn't been found
         while(!search.getData().equals(data))
         {
             search.next();
         }
         
         //When data is found, return iterator with its "current" at the position it was found
         return search;
         
     }
     
     /**FUNCTION TO PRINT VALUES OF LINKED LIST FOR DEBUGGING**/
     void print()
     {
         //Create test node to scroll through list with that starts on the next node after reference node header
         Node test = header.next;
         
         //Counter for specifying node in print
         int current = 1;
         
         //Print data until you reach the footer (whose data is null)
         while(test.data != null) 
         {
             System.out.println("Node " + current + ": " + test.data);//print
             
             //Scroll to next node (since data != null, you aren't at the footer) and increment counter
             test = test.next;
             current++;
         }
     }
     
     /**INNER CLASS ITERATOR**/
     public class Iterator
     {
         /**Iterator Class Member Variables**/
         //Create current node 
         LinkedList<Generic>.Node current;
         
         /**ITERATOR CONSTRUCTOR**/
         Iterator(LinkedList<Generic>.Node n) 
         {
             //Assign parameters to class member variables 
             this.current = n;
         }
         
         /**FUNCTION TO CHECK IF CURRENT NODE ACTUALLY EXISTS AND NOT AT END OF LIST**/
         public boolean valid() 
         {
             return current != null && current != header && current != footer;
         }
         
         /**FUNCTION TO MOVE TO NEXT NODE**/
         public void next() 
         {
             current = current.next;
         }
         
         /**FUNCTION TO MOVE TO PREVIOUS NODE**/
         public void prev()
         {
             //move to previous node
             current = current.prev;                 
         }
         
         /**FUNCTION TO RETURN DATA OF NODE**/
         public Generic getData() 
         {
             //if node exists, return its data
             if (current != null) return current.data;
             //else return null to indicate node DNE
             else return null;
         }
         
         /**FUNCTION TO REMOVE "current" NODE FROM LIST**/
         public void remove()
         {
             //next pointer of the node before current now points to the node after current
             current.prev.next = current.next;  
             //prev pointer of the node after current now points to the node before current
             current.next.prev = current.prev;
             //current is now the next node so that the iterator has a current that is still in the list
             current = current.next;
                
         }
         
         /**FUNCTION TO INSERT NEW NODE AFTER "current" NODE**/
         public void insert(Generic data)
         {         
            //Create node with data, next pointing to current's next, and prev point to current
            Node n = new Node(data, current.next, current);
            //Node after new node prev points to new node
            current.next.prev = n;
            //Current's next now points to inserted node
            current.next = n;
            
         }
     }
     
     
}
