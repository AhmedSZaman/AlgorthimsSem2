package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * 
 */
public class KDTreeNN implements NearestNeigh{
	
    
	private boolean checkLat = true;
	private KDNode root; 
	
	@Override
    public void buildIndex(List<Point> points) {
        		
        for (Point p: points)
        { addPoint(p);}
        //build a tree
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
       //get search term and find k amount of nearest neighbours
    	//implemnt KD tree
    	List<PointDist> nearestNeighbours = new ArrayList<PointDist>();
    	List<Point>		nearestPoints = new ArrayList<Point>();
    	nearestNeighbours = nearestNeighbour(root, searchTerm, true , null, k);
    	
    	for (PointDist p: nearestNeighbours)
    		nearestPoints.add(p.pointInList);
        
    	return nearestPoints;
    }

    @Override
    public boolean addPoint(Point point) {
        //REVISE THIS
    	List<KDNode> visitedPoints = new ArrayList<KDNode>();
    	insertNode(point, root, true, visitedPoints);
    	return true;
    }
    
    public boolean insertNode(Point point, KDNode node, boolean checkLat, List<KDNode> visitedPoints) {
    	if (node == null && root == null) {
    		node = new KDNode(point, null, null, null);
    		
    		return true;
    	} else if (node == null) {
    		int index = visitedPoints.size() - 1;
    		node = new KDNode(point, visitedPoints.get(index), null, null);
    	}
    	else if (!isPointIn(node.point)) {
    		return false;
    	} else if (checkLat) {
    		visitedPoints.add(node);
    		if (point.lat < node.point.lat) {
    			insertNode(point, node.leftChild, false, visitedPoints);
    		} else {
    			insertNode(point, node.rightChild, false, visitedPoints);
    		}
    	} else {
    		visitedPoints.add(node);
    		if (point.lon < node.point.lon) {
    			insertNode(point, node.leftChild, true, visitedPoints);
    		} else {
    			insertNode(point, node.rightChild, true, visitedPoints);
    		}
    	}
    	return false;
    }
    
    

//    public boolean insertNode(Point point, KDNode rootNode, boolean checkLat) {
//        KDNode newNode = new KDNode(point, null,null,null);
//        
//    	
//    	if (rootNode == null) {
//    		newNode = new KDNode(point, null, null, null);
//    		return true;
//    	} 
//    	else if (isPointIn(point))
//    		return false;
//    	else if (checkLat == true) {
//    		if (point.lat < rootNode.point.lat) {
//    			if (rootNode.leftChild == null) {
//    				new KDNode(point, rootNode, null, null);
//    				rootNode.setLeftChild(leftChild);
//    			}
//    			else
//    				insertNode(point, rootNode.leftChild, !checkLat);
//    		} else {
//    			if (rootNode.rightChild == null)
//    				new KDNode(point, rootNode, null, null);
//    			else
//    				insertNode(point, rootNode.rightChild, !checkLat);
//    		}
//    	} else {
//    		if (point.lon < node.point.lon) {
//    			insertNode(point, node.leftChild, true);
//    		} else {
//    			insertNode(point, node.rightChild, true);
//    		}
//    	}
//    	return false;
//    }
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
    			KDNode leftMin = findMinimum(currentNode.leftChild, currentDim, true);
        		
        		//copy the min root
        		currentNode.point = leftMin.point;
        		
        		//Recursively to delete min
        		 currentNode.rightChild = deleteNode(currentNode.leftChild.point, leftMin, !checkLat);
    		}
    		else { //leaf node
    			return null;
    		}
    		return root;
    	}
    
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
    	return root;
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

	private List<PointDist> nearestNeighbour(KDNode root, Point searchTerm, boolean checkLat, List<PointDist> nearestList, int k)
//	private List<KDNode> nearestNeighbour(KDNode root, Point searchTerm, boolean checkLat, int k)
	{

		double axisDist;
		KDNode nextBranch, otherBranch;
		//returns nothing if root node is empty
		if (root == null && root.parent == null) 
			return nearestList;
		if (checkLat == true)
			if (searchTerm.lat < root.point.lat)
			{
				nextBranch = root.leftChild;
				otherBranch = root.rightChild;
			}
			else
			{
				nextBranch = root.rightChild;
				otherBranch = root.leftChild;
			}
		else {
			if (searchTerm.lon < root.point.lon)
			{
				nextBranch = root.leftChild;
				otherBranch = root.rightChild;
			}
			else
			{
				nextBranch = root.rightChild;
				otherBranch = root.leftChild;
			}
		}
		
		double currentDist = root.point.distTo(searchTerm);
		List<PointDist> newNearestList = nearestList;
		
		PointDist comparedPoint = nearestList.get(k-1);
		double comparedDist = root.point.distTo(comparedPoint.pointInList);
		
		if(nearestList.size() < k || currentDist < comparedDist )
		{
			PointDist addPointDist = new PointDist(root.point, currentDist) ;
			nearestList.add(addPointDist);
			nearestList = insertionSort(nearestList);
		}
		
		newNearestList = nearestNeighbour(nextBranch, searchTerm, !checkLat,  nearestList,  k );
		
		//calcualtes the distance from the last axis
		if (checkLat == true)
			 axisDist = searchTerm.lat - root.point.lat;
		else
			axisDist = searchTerm.lon - root.point.lon;
		
		if (comparedDist >= axisDist)
			newNearestList = nearestNeighbour(otherBranch, searchTerm, !checkLat,  nearestList,  k );
		
		return newNearestList;	
		
	}
	
	 List<PointDist> insertionSort(List<PointDist> nearestList) {
		//Insertion sort because array will always be sorted
		
		int n = nearestList.size();
        for (int i = 1; i < n; ++i) {
        	PointDist key = new PointDist(null, 0);
        	key.pointInList = nearestList.get(i).pointInList;
        	key.dist = nearestList.get(i).dist;
            int j = i ;
 
            while (j >= 0 && (nearestList.get(j-1).dist > key.dist)) {
                nearestList.set(j, nearestList.get(j-1));
            	j--;
            }
            nearestList.set(j, key);
        }
		return nearestList;
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

	 // Removed encapsulation and getter/setter methods for now. Can change this back if we have time l8r
	public KDNode getLeftChild() {
    	return leftChild;
    }    
	public void setLeftChild(KDNode leftChild) {
		this.leftChild = leftChild;
	}

	public KDNode getRightChild() {
    	return rightChild;
		
	}
	public void setRightChild(KDNode rightChild) {
		this.rightChild = rightChild;
	}
	public Point getPoint() {
		return point;
	}
	

	public void setParent(KDNode parent) {
		this.parent = parent;
	}
}
class PointDist{
	public Point pointInList;
	public double dist;
	
	public PointDist(Point pointInList, double dist) {
		this.pointInList = pointInList;
		this.dist = dist; 
	}




}

