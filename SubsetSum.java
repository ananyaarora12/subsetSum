
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SubsetSum {
  
  public ArrayList<Integer> input = new ArrayList<Integer>();
  public ArrayList<Integer> scaledInput = new ArrayList<Integer>();
  public TreeNode<List<Integer>> tree;
  
  public SubsetSum() {
    input.add( -7 );
    input.add( -3 );
    input.add( -2 );
    input.add( 0 );
    input.add( 1);
    input.add( 5 );
    input.add( 8 );
    input.add( 17 );
    input.add( 21 ); 
    
    // The sum for which we want to find an appropriate subset
    int target = 50;
    // 1. Sort the set
    Collections.sort( input );
    
    // Scale the set so that there are no negative elements, with the offset
    // being stored in a variable
    int offset = 0;
    if ( input.get( 0 ) <= 0 ) {
      offset = Math.abs( input.get( 0 ) ) + 1;
      for ( int i = 0; i < input.size( ); ++i ) {
        scaledInput.add( input.get( i ) + offset );
      }
    }
    
    // Search all n-sized min-heaps from set S for the target sum
    int scaledTarget;
    boolean targetFound = false;
    List<Integer> result = new ArrayList<Integer>( );
    int resultSum = 0;
    for ( int n = 1; n <= input.size( ); ++n ) {
      // Scale the target to be searchable in the n-sized Subset Tree
      scaledTarget = target + ( offset * n );
      
      // Now generate the k'th smallest subset
      tree = buildTree( scaledInput, n );
      result = binarySearch( tree, 0, choose( input.size( ), n ), scaledTarget );
      if ( result == null ) {
        continue;
      }
      if ( sum( result ) == scaledTarget ) {
        targetFound = true;
        break;
      }
    }
    
    // Output the result
    if ( targetFound ) {
      for ( int i = 0; i < result.size( ); ++i ) {
        result.set( i, result.get( i ) - offset );
      }
      System.out.println( "Subset that sums to the target sum has been found!" );
      printList( result );
    }
    else {
      System.out.println( "No subset sums to the target sum " + target );
    }
  }
  
  public List<Integer> binarySearch( TreeNode<List<Integer>> tree, int lowerBound,  int upperBound, int target ) {
    if ( lowerBound > upperBound ) {
      return null;
    }
    int position = Math.round( ( lowerBound + upperBound ) / 2.0f );
    if ( position == 0 ) {
      return null;
    }
    List<Integer> kthMin = findKthMin( tree, position );
    int sum = sum( kthMin );
    if ( lowerBound == upperBound ) {
      if ( sum == target ) {
        return kthMin;
      }
      else {
        return null;
      }
    }
    else {
      if ( sum == target ) {
        return kthMin;
      }
      else if ( sum < target ) {
        if ( lowerBound == position ) {
          position += 1;
        }
        return binarySearch( tree, position, upperBound, target );
      }
      else {
        if ( upperBound == position ) {
          position -= 1;
        }
        return binarySearch( tree, lowerBound, position, target );
      }
    }
  }
  
  public TreeNode<List<Integer>> buildTree( ArrayList<Integer> list, int n ) {
    TreeNode<List<Integer>> tree = new TreeNode<List<Integer>>();
    
    ArrayList<Integer> idcs = new ArrayList<Integer>();
    for ( int i = 0; i < n; ++i ) {
      idcs.add( i );
    }
    
    // 2. Set the root node to the n smallest elements of S
    tree.setData( list.subList( 0, n ) );
    tree.setIndices( idcs );
    tree.setLimit( 0 );
    
    return tree;
  }
  
  /*
   * In this case, the algorithm terminates after the first layer of chilren are created;
   * the recursive case is not run
   */
  public List<TreeNode<List<Integer>>> buildChildren( List<Integer> list, List<Integer> indices, int limit ) {
    ArrayList<TreeNode<List<Integer>>> children = new ArrayList<TreeNode<List<Integer>>>();
    
   
    for ( int i = list.size( ) - 1; i >= limit; --i ) {
      if ( indices.get( indices.size( ) - 1 ) == input.size( ) - 1 ) {
        continue;
      }
      TreeNode<List<Integer>> child = new TreeNode<List<Integer>>();
      ArrayList<ArrayList<Integer>> tmpArr = incrementList( list, indices, i );
      child.setData( tmpArr.get( 0 ) );
      child.setIndices( tmpArr.get( 1 ) );
      child.setLimit( i );
      children.add( child );
    }
    
    return children;
  }
  
  public ArrayList<ArrayList<Integer>> incrementList( List<Integer> list, List<Integer> indices, int idx ) {
    if ( list.size( ) < 1 ) {
      return null;
    }
    ArrayList<Integer> newList = deepCopy( list );
    ArrayList<Integer> newIndices = deepCopy( indices );
    if ( list.size( ) == 1 ) {
      if ( idx < list.size( ) ) {
        newList.set( idx, scaledInput.get( indices.get( idx ) + 1 ) );
        newIndices.set( idx, indices.get( idx ) + 1 );
      }
      else {
        return null;
      }
    }
    else {
      do {
        newList.set( idx, scaledInput.get( indices.get( idx ) + 1 ) );
        newIndices.set( idx, indices.get( idx ) + 1 );
        ++idx;
      } while ( idx < list.size( ) && newIndices.get( idx - 1 ) == indices.get( idx ) );
      if ( newIndices.get( newIndices.size( ) - 1 ) == newIndices.get( newIndices.size( ) - 2 ) ) {
        return null;
      }
    }
    ArrayList<ArrayList<Integer>> retVal = new ArrayList<ArrayList<Integer>>();
    retVal.add( newList );
    retVal.add( newIndices );
    return retVal;
  }
  
  public ArrayList<Integer> deepCopy( List<Integer> list ) {
    ArrayList<Integer> newList = new ArrayList<Integer>();
    for ( int i = 0; i < list.size(); ++i ) {
      newList.add( list.get( i ) );
    }
    return newList;
  }
  
  @SuppressWarnings("unchecked")
  public List<Integer> findKthMin( TreeNode<List<Integer>> tree, int k ) {
    Comparator<TreeNode<List<Integer>>> comparator = new NodeComparator();
    PriorityQueue<TreeNode<List<Integer>>> toVisit = new PriorityQueue<TreeNode<List<Integer>>>( 11, comparator );
    TreeNode<List<Integer>> root = new TreeNode<List<Integer>>( tree.getData( ) );
    root.setIndices( tree.getIndices( ) );
    root.setLimit( tree.getLimit( ) );
    root.setChildren( tree.getChildren( ) );
    toVisit.add( root );
    ArrayList<TreeNode<List<Integer>>> smallestNodes = new ArrayList<TreeNode<List<Integer>>>( );
    while ( smallestNodes.size( ) < k ) {
      TreeNode<List<Integer>> node = toVisit.poll( );
      List<TreeNode<List<Integer>>> children = buildChildren( node.getData( ), node.getIndices( ),
                                                             node.getLimit( ) );
      for ( TreeNode<List<Integer>> child : children ) {
        TreeNode<List<Integer>> newChild = new TreeNode<List<Integer>>( child.getData( ) );
        newChild.setIndices( child.getIndices( ) );
        newChild.setLimit( child.getLimit( ) );
        newChild.setChildren( child.getChildren( ) );
        toVisit.add( newChild );
      }
      smallestNodes.add( node );
    }
    return smallestNodes.get( k - 1 ).getData( );
  }
  
  public int sum( List<Integer> node ) {
    int sum = 0;
    for ( Integer n : node ) {
      sum += n;
    }
    return sum;
  }
  
  public void printList( List<Integer> list ) {
    for ( int i = 0; i < list.size( ); ++i ) {
      System.out.print( list.get( i ) + "\t" );
    }
    System.out.println( );
  }
  
  public static int choose( int n, int k ) {
    if (k >  n) {
      return 0;
    }
    if (k == 0) {
      return 1;
    }
    if (k > n / 2) {
      return choose(n, n - k);
    }
    return n * choose(n - 1, k - 1) / k;
  }
  
  public static void main(String[] args) {
    new SubsetSum( );
  }
  
}
