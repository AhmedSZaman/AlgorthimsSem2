package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented.  Kd-tree implementation.
 *
 * 
 */
public class KDTreeNN implements NearestNeigh{
	
    
	//private boolean checkLat = true;
	private KDNode rootHospital;
	private KDNode rootEducation;
	private KDNode rootResturant;
	
	@Override
    public void buildIndex(List<Point> points) {
        		
        for (Point p: points)
        {  addPoint(p);
        }
       // System.out.println(rootResturant.leftChild.point );
       // System.out.println(rootResturant.rightChild.point.toString()  );
        //build a tree
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
       //get search term and find k amount of nearest neighbours
    	//implemnt KD tree
    	List<PointDist> nearestNeighbours = new ArrayList<PointDist>();
    	List<Point>		nearestPoints = new ArrayList<Point>();
    	
    	nearestNeighbours = nearestNeighbour(getRootNodeCat(searchTerm), searchTerm, true , nearestNeighbours, k);
    	
    	//fix this
    	if (nearestNeighbours != null)
    	{  for (PointDist p: nearestNeighbours)
    		{nearestPoints.add(p.pointInList);}
    		
    	}else {
    		
    	}
    
    	return nearestPoints;
        
    	
    }

    @Override
    public boolean addPoint(Point point) {
    	List<KDNode> visitedPoints = new ArrayList<KDNode>();
    	if (isPointIn(point)) {
    		return false;}
    	else {
    	KDNode rootNode = getRootNodeCat(point);
    	insertNode(point, rootNode, rootNode, true, visitedPoints);
    	
    	return true;
    	}	
    }
    
    public boolean insertNode(Point point, KDNode currentNode, KDNode rootNode, boolean checkLat, List<KDNode> visitedPoints) {
    	
    	//System.out.println(rootNode);
    	if (rootNode == null) {
    		if (point.cat == Category.RESTAURANT)
    			rootResturant = new KDNode(point, null, null, null);
    		else if(point.cat == Category.EDUCATION)
    			rootEducation = new KDNode(point, null, null, null);
    		else
    			rootHospital = new KDNode(point, null, null, null);
    		return true;
    	} else if (currentNode == null) {
    		int index = visitedPoints.size() - 1;
    		currentNode = new KDNode(point, visitedPoints.get(index), null, null);
    	}
    	else if (checkLat) {
    		visitedPoints.add(currentNode);
    		if (point.lat < currentNode.point.lat) {
    			insertNode(point, currentNode.leftChild, rootNode, false, visitedPoints);
    		} else {
    			insertNode(point, currentNode.rightChild, rootNode, false, visitedPoints);
    		}
    	} else {
    		visitedPoints.add(currentNode);
    		if (point.lon < currentNode.point.lon) {
    			insertNode(point, currentNode.leftChild,rootNode, true, visitedPoints);
    		} else {
    			insertNode(point, currentNode.rightChild, rootNode, true, visitedPoints);
    		}
    	}
    	return false;
    }
    
    


    @Override
    public boolean deletePoint(Point point) {
    	boolean isTrue;
    	if (point.cat == Category.RESTAURANT) 
    	{
    		isTrue = deletePointHelper(point, rootResturant);
    		if (isTrue)
    			rootResturant = deleteNode(point, rootResturant, true);
    	    return isTrue;
    	}
    	
    	else if(point.cat == Category.EDUCATION)
    	{
    		isTrue = deletePointHelper(point, rootEducation);
    		if (isTrue)
    			rootEducation = deleteNode(point, rootEducation, true);
    	    return isTrue;
    	}	
    	else if(point.cat == Category.HOSPITAL)
    	{
    		isTrue = deletePointHelper(point, rootHospital);
    		if (isTrue)
    	    	rootHospital = deleteNode(point, rootHospital, true);
    	    return isTrue;
    	}
    		
    	return false;

    }

    public boolean deletePointHelper(Point point, KDNode rootNode)
    {
	    if (rootNode == null)
            return false;
    	if (isPointIn(point) == false) 
    		return false;
    	    
    	 return true;
    }
    public KDNode deleteNode(Point point, KDNode currentNode, boolean checkLat) {
    	if(currentNode == null)
    		return null;
    	//checks if point exists

    	//checks if point is root
    	if (currentNode.point.equals(point)) {
    		
    		if(currentNode.rightChild != null)
    		{
    		//find min of roots right subtree
    		KDNode rightMin = findMinimum(currentNode.rightChild, checkLat, true);
    		//copy the min root
    		currentNode.point = rightMin.point;
    		
    		//Recursively to delete min
    		 currentNode.rightChild = deleteNode(currentNode.rightChild.point, rightMin, !checkLat);
    		}
    		
    		else if(currentNode.leftChild != null) {	
    			//find min of roots right subtree
    			KDNode leftMin = findMinimum(currentNode.leftChild, checkLat, true);
        		
        		//copy the min root
        		currentNode.point = leftMin.point;
        		
        		//Recursively to delete min
        		 currentNode.rightChild = deleteNode(currentNode.leftChild.point, leftMin, !checkLat);
    		}
    		else { //leaf node
    			return null;
    		}
    		return currentNode;
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
    	return currentNode;
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
        KDNode currentNode = null;
       
    	if (point.cat == Category.RESTAURANT) 
    		currentNode = rootResturant;
    	
    	else if(point.cat == Category.EDUCATION)
    		currentNode = rootEducation;
    	
    	else if(point.cat == Category.HOSPITAL)
    		currentNode = rootHospital;
        
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
		double comparedDist = -1;
		KDNode nextBranch, otherBranch;
		//returns nothing if root node is empty
		if (root == null) 
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
		
		

		
//		if(nearestList.size() < k || currentDist < comparedDist )
//		{
//			PointDist addPointDist = new PointDist(root.point, currentDist) ;
//			nearestList.add(addPointDist);
//			nearestList = insertionSort(nearestList);
//		}
		
		if(nearestList.size() < k) 
		{
			PointDist addPointDist = new PointDist(root.point, currentDist) ;
			nearestList.add(addPointDist);
			nearestList = insertionSort(nearestList);
		}else {
			PointDist comparedPoint = nearestList.get(k-1);
		
			comparedDist = root.point.distTo(comparedPoint.pointInList);
			if(currentDist < comparedDist)
			{
				PointDist addPointDist = new PointDist(root.point, currentDist) ;
				nearestList.add(addPointDist);
				nearestList = insertionSort(nearestList);
			}	
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
	 
	 private KDNode getRootNodeCat(Point point) {
	    	if (point.cat == Category.RESTAURANT) 
	    		return rootResturant;	
	    	
	    	else if(point.cat == Category.EDUCATION)
	    		return rootEducation;
	    	
	    	else 
	    		return rootHospital;
	    	
	  
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

