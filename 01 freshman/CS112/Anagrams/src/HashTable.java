/**
 * Matthew Huynh
 * CS112A1 - hw5
 * April 15, 2008
 *
 * HashTable.java
 * Mark Allen Weiss's implementation of a separate chaining hash table.
 * original code: http://www.cs.fiu.edu/~weiss/dsaajava2/code/SeparateChainingHashTable.java
 */

public class HashTable {

	public static final int DEFAULT_TABLE_SIZE = 100003;
	
	private WordList[] theLists;
	
    /**
     * Construct the hash table.
     */
	public HashTable( )
	{
		this ( DEFAULT_TABLE_SIZE );
	}
	
    /**
     * Construct the hash table.
     * @param size approximate table size.
     */
	public HashTable( int size )
	{
		theLists = new WordList[ size ];
		for (int i = 0; i < theLists.length; i++)
			theLists[i] = new WordList();
	}
	
    /**
     * Insert into the hash table.
     * @param x the item to insert.
     */
    public void insert( Word x )
    {
        WordList whichList = theLists[ myhash( x.key ) ];
        //System.out.println("WordList found");
       
	    whichList.add( x );
	    //System.out.println("Word " + x.value + " successfully added");
    }
    
    /**
     * Find an item in the hash table.
     * @param key the alphagram to search for
     * @return Word if an item with a matching key is found
     * @return null if no item with a matching key is found
     */
    public WordList getItem( String key )
    {
        WordList whichList = theLists[ myhash( key ) ];
        
        if (whichList.isEmpty()) {
        	return null;
        }

        return whichList;
    }
    
    private int myhash( String key )
    {
        int hashVal = key.hashCode( );
        //System.out.println("key " + key + "'s hashCode(): " + hashVal);

        hashVal %= theLists.length;
        //System.out.println("key " + key + "'s modded hashCode(): " + hashVal);
        if( hashVal < 0 )
            hashVal += theLists.length;
        
        //System.out.println("key " + key + "'s hash value: " + hashVal);

        return hashVal;
    }

}
