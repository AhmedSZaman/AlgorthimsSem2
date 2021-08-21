package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * 
 */
public class KDTreeNN implements NearestNeigh{
	
	List<Point> pointList;
    
	KDNode root; 
	
	@Override
    public void buildIndex(List<Point> points) {
        for (Point p: points)
        { pointList.add(p);}
        
        //build a tree
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
       //get search term and find k amount of nearest neighbours
    	//implemnt KD tree

        return new ArrayList<Point>();
    }

    @Override
    public boolean addPoint(Point point) {
        //REVISE THIS
    	if (pointList.contains(point)){
    		return false;
    	}
    	
    	KDNode currentNode = root;
    	if (root == null) {
            root = new KDNode(point, null, null, null);
        //2nd depth
    	} else if (point.lat < root.point.lon) {
            root.left = new KDNode(point, root, null,null);
        } else {
            root.right = new KDNode(point, root, null,null);;
        }

    
    	return true;
    }

    @Override
    public boolean deletePoint(Point point) {
    	if (pointList.contains(point)){
    		pointList.remove(point);
    		return true;
    	}
    	return false;
    }

    @Override
    public boolean isPointIn(Point point) {

        boolean isLatitude = true;
        KDNode newNode = new KDNode(point, null,null,null);
        KDNode currentNode = root;
       // if current node is null return false
        while (currentNode != null) {
        	if (currentNode.equals(newNode)) {return true;}
        	
        	if (isLatitude == true)
        	{ 
        		if (newNode.point.lat < currentNode.point.lat) {
        			currentNode = currentNode.getLeftChild(currentNode);
        			isLatitude = false;
        		}else
        		{
        				currentNode = currentNode.getRightChild(currentNode);
        				isLatitude = false;
        		}
        	}else 
        		{ 
        			if (newNode.point.lon < currentNode.point.lon)
        			{
        				currentNode = currentNode.getLeftChild(currentNode);
        				isLatitude = true;
        			}else 
        			{
        			currentNode = currentNode.getRightChild(currentNode);
        			isLatitude = false;
        			}
        		}
        }
        return false;
    }
}

//KD NODE CLASS
class KDNode {
    Point point;
    KDNode parent;
    KDNode leftChild;
    KDNode rightChild;
    
    public KDNode (Point point, KDNode parent, KDNode leftChild, KDNode rightChild) {
    	this.point = point;
    	this.parent = parent;
    	this.leftChild = leftChild;
    	this.rightChild = rightChild;
    }
    

	public KDNode getLeftChild(KDNode parent)
    {
    	this.parent = parent;
    	return parent.leftChild;
    }    
	
	public KDNode getRightChild(KDNode parent) {
    	this.parent = parent;
    	return parent.rightChild;
		
	}

}
