package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * 
 */
public class KDTreeNN implements NearestNeigh{
	
	private List<Point> pointList;
    
	private KDNode root; 
	
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
    	insertNode(point, root, true);
    	return true;
    }
    
    public boolean insertNode(Point point, KDNode node, boolean checkLat) {
    	if (node == null) {
    		node = new KDNode(point, null, null, null);
    		return true;
    	} else if (root.point == point) {
    		return false;
    	} else if (checkLat) {
    		if (point.lat < node.point.lat) {
    			insertNode(point, node.leftChild, false);
    		} else {
    			insertNode(point, node.rightChild, false);
    		}
    	} else {
    		if (point.lon < node.point.lon) {
    			insertNode(point, node.leftChild, true);
    		} else {
    			insertNode(point, node.rightChild, true);
    		}
    	}
    	return false;
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
        			currentNode = currentNode.leftChild;
        			isLatitude = false;
        		}else
        		{
        				currentNode = currentNode.rightChild;
        				isLatitude = false;
        		}
        	}else 
        		{ 
        			if (newNode.point.lon < currentNode.point.lon)
        			{
        				currentNode = currentNode.leftChild;
        				isLatitude = true;
        			}else 
        			{
        			currentNode = currentNode.rightChild;
        			isLatitude = false;
        			}
        		}
        }
        return false;
    }
}

//KD NODE CLASS
class KDNode {
    public Point point;
    public KDNode parent;
    public KDNode leftChild;
    public KDNode rightChild;
    
    public KDNode (Point point, KDNode parent, KDNode leftChild, KDNode rightChild) {
    	this.point = point;
    	this.parent = parent;
    	this.leftChild = leftChild;
    	this.rightChild = rightChild;
    }

//  Removed encapsulation and getter/setter methods for now. Can change this back if we have time l8r
//
//	public KDNode getLeftChild() {
//    	return leftChild;
//    }    
//	
//	public KDNode getRightChild() {
//    	return rightChild;
//		
//	}
//
//	public Point getPoint() {
//		return point;
//	}
}
