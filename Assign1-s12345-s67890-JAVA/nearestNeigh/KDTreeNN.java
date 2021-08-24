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
        if (root == null)
            return false;
    	if (isPointIn(point) == false) {
    		return false;
    	}
        root = deleteNode(point, root, true);
        return true;

    }

    public KDNode deleteNode(Point point, KDNode node, boolean checkLat) {
    	
    	if (isPointIn(point) == false) {
    		return null;
    	}
    	KDNode currentNode = root;
    	if (currentNode.point.equals(point)) {
    		
    		if(currentNode.rightChild != null)
    		{
    		//find min of roots right subtree
    		KDNode rightMin = findMin(currentNode.rightChild, cd);
    		//copy the min root
    		currentNode.point = rightMin.point;
    		
    		//recurviley to delete min
    		 currentNode.rightChild = deleteNode(currentNode.rightChild, rightMin, depth+1);
    		}
    		else if(currentNode.leftChild != null) {
        		//find min of roots right subtree
        		KDNode leftMin = findMin(currentNode.leftChild, cd);
        		//copy the min root
        		currentNode.point = leftMin.point;
        		
        		//recurviley to delete min
        		 currentNode.rightChild = deleteNode(currentNode.leftChild, leftMin, depth+1);
    		}
    		else { //leaf node
    			return null;
    		}
    	}
    	return root;
    	if (checkLat == true) {
    		if (point.lat < currentNode.point.lat)
    		{
    			currentNode.leftChild = deleteNode(point, currentNode.leftChild, false);
    		}
    		else {
    			currentNode.rightChild = deleteNode(point, currentNode.rightChild, false);
    		}
    	}else {
    		if (point.lon < currentNode.point.lon)
        	{
        		currentNode.leftChild = deleteNode(point, currentNode.leftChild, true);
        	}
        	else {
        		currentNode.rightChild = deleteNode(point, currentNode.rightChild, true);
        	}
    	}
    }
    

    private KDNode findMinimum(KDNode root, int targetDim, int currDim) {
        if (root == null)
            return null;
        if (targetDim == currDim) {
            if (root.left == null)
                return root;
            // else
            return findMinimum(root.left, targetDim, (currDim + 1) % dim);
        }
        Node rightMin = findMinimum(root.right, targetDim, (currDim + 1) % dim);
        Node leftMin = findMinimum(root.left, targetDim, (currDim + 1) % dim);
        Node res = root;
        if (rightMin != null && rightMin.point[targetDim] < res.point[targetDim])
            res = rightMin;
        if (leftMin != null && leftMin.point[targetDim] < res.point[targetDim])
            res = leftMin;
        return res;
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
