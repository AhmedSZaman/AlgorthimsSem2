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
	boolean checkLat = true;
	String lol = "lat";
	
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

    public KDNode deleteNode(Point point, KDNode node, boolean currentDim) {
    	
    	//checks if point exists
    	if (isPointIn(point) == false) 
    		return null;
    	
    	KDNode currentNode = root;
    	
    	//checks if point is root
    	if (currentNode.point.equals(point)) {
    		
    		if(currentNode.rightChild != null)
    		{
    		//find min of roots right subtree
    		KDNode rightMin = findMinimum(currentNode.rightChild, currentDim, true);
    		
    		//copy the min root
    		currentNode.point = rightMin.point;
    		
    		//Recursively to delete min
    		 currentNode.rightChild = deleteNode(currentNode.rightChild.point, rightMin, !checkLat);
    		}
    		
    		else if(currentNode.leftChild != null) {	
    			//find min of roots right subtree
        		KDNode leftMin = findMinimium(currentNode.leftChild, cd);
        		
        		//copy the min root
        		currentNode.point = leftMin.point;
        		
        		//Recursively to delete min
        		 currentNode.rightChild = deleteNode(currentNode.leftChild.point, leftMin, !checkLat);
    		}
    		else { //leaf node
    			return null;
    		}
    	}
    	return root;
    	if (checkLat == true)
    	{
    		if (point.lat < currentNode.point.lat)
    			currentNode.leftChild = deleteNode(point, currentNode.leftChild, false);
    		else 
    			currentNode.rightChild = deleteNode(point, currentNode.rightChild, false);
    	}
    	else 
    	{
    		if (point.lon < currentNode.point.lon)
    			currentNode.leftChild = deleteNode(point, currentNode.leftChild, true);
        	else 
        		currentNode.rightChild = deleteNode(point, currentNode.rightChild, true);
    	}
    }
    

    private KDNode findMinimum(KDNode root, boolean currentDim, boolean targetDim) {
        if (root == null)
            return null;
        
        if (currentDim == targetDim) 
        {
            if (root.leftChild == null) 
                return root;
            else 
            	return findMinimum(root.leftChild, currentDim, !currentDim);
            
        }
        
        KDNode rightMin = findMinimum(root.rightChild, currentDim, !currentDim);
        KDNode leftMin = findMinimum(root.leftChild, currentDim, !currentDim);
        KDNode currentNode = root;
        
        if (targetDim == true)
        {
        	if (rightMin != null && rightMin.point.lat < currentNode.point.lat)
        		currentNode = rightMin;
        	if (leftMin != null && leftMin.point.lat < currentNode.point.lat)
        		currentNode = leftMin;
        	}
        	else 
        	{
        		if (rightMin != null && rightMin.point.lon < currentNode.point.lon)
        			currentNode = rightMin;
        		if (leftMin != null && leftMin.point.lon < currentNode.point.lon)
        			currentNode = leftMin;
        	}
        return currentNode;
    }

	@Override
    public boolean isPointIn(Point point) {

        boolean isLatitude = true;
        KDNode newNode = new KDNode(point, null,null,null);
        KDNode currentNode = root;
       // if current node is null return false
        while (currentNode != null) {
        	
        	if (currentNode.equals(newNode)) 
        		return true;
        
        	if (isLatitude == true)
        	{ 
        		if (newNode.point.lat < currentNode.point.lat) 
        		{ currentNode = currentNode.leftChild;}
        		else
        		{currentNode = currentNode.rightChild;}
        		isLatitude = false;
        	}
        	else 
        	{ 
        		if (newNode.point.lon < currentNode.point.lon)
        		{ currentNode = currentNode.leftChild;}
        		else
        		{currentNode = currentNode.rightChild;}
        		isLatitude = true;
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


