package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented. Kd-tree implementation.
 *
 * 
 */
public class KDTreeNN implements NearestNeigh {

	// private boolean checkLat = true;
	private KDNode rootHospital;
	private KDNode rootEducation;
	private KDNode rootResturant;

	@Override
	public void buildIndex(List<Point> points) {
		// builds tree
		// initialises the list for 3 different trees
		List<Point> restList = new ArrayList<Point>();
		List<Point> eduList = new ArrayList<Point>();
		List<Point> hospList = new ArrayList<Point>();

		for (Point p : points) {
			if (p.cat == Category.RESTAURANT) {
				restList.add(p);
			} else if (p.cat == Category.EDUCATION) {
				eduList.add(p);
			} else {
				hospList.add(p);
			}
		}

		// finds median for each list and adds it to the tree
		addPoint(findMedian(restList));
		addPoint(findMedian(eduList));
		addPoint(findMedian(hospList));

		// removes the median from the main list
		restList.remove(findMedian(restList));
		eduList.remove(findMedian(eduList));
		hospList.remove(findMedian(hospList));

		// adds the other points to build tree tree
		for (Point p : restList)
			addPoint(p);

		for (Point p : eduList)
			addPoint(p);

		for (Point p : hospList)
			addPoint(p);

	}

	@Override
	public List<Point> search(Point searchTerm, int k) {
		// get search term and find k amount of nearest neighbours

		List<PointDist> nearestNeighbours = new ArrayList<PointDist>();
		List<Point> nearestPoints = new ArrayList<Point>();

		// intilises empty list with Point and distance to find k amount of neighbours
		nearestNeighbours = nearestNeighbour(getRootNode(searchTerm), searchTerm, true, nearestNeighbours, k);

		// Adds only points to nearestPoints and returns it
		if (nearestNeighbours != null) {
			for (PointDist p : nearestNeighbours) {
				nearestPoints.add(p.pointInList);
			}

		} else {

		}

		return nearestPoints;

	}

	@Override
	public boolean addPoint(Point point) {
	
		if (isPointIn(point)) {
			return false;
		} else {
			KDNode rootNode = getRootNode(point);
			insertNode(point, rootNode, rootNode, true);
	       
	       
			return true;
		}
	}

	public boolean insertNode(Point point, KDNode currentNode, KDNode rootNode, boolean checkLat) {
		// Recursively uses method to find placement

		// Adds root node
		if (rootNode == null) {
			setRootNode(new KDNode(point, null, null, null));
		}else {

			// checklat determines whether to compare either x or y axis
			if (checkLat) {
				// Checks if the point belongs in the right child subtree
				if (point.lat >= currentNode.point.lat) {
					// If right child subtree is null/empty, creates a new node for the current
					// point in the right
					// child reference.
					if (currentNode.rightChild == null) {
						currentNode.rightChild = new KDNode(point, currentNode, null, null);

						return true;
					}
					// Calls the insert node method on the right child node.
					else {
						insertNode(point, currentNode.rightChild, rootNode, !checkLat);
					}
				} else {
					if (currentNode.leftChild == null) {

						currentNode.leftChild = new KDNode(point, currentNode, null, null);

						return true;
					} else {
						insertNode(point, currentNode.leftChild, rootNode, !checkLat);
					}
				}
			}
			// Checks latitude if current node is ordered by longitude
			else {
				if (point.lon >= currentNode.point.lon) {
					if (currentNode.rightChild == null) {
						currentNode.rightChild = new KDNode(point, currentNode, null, null);

						return true;
					}
					// Calls the insert node method on the right child node.
					else {
						insertNode(point, currentNode.rightChild, rootNode, !checkLat);
					}
				} else {
					if (currentNode.leftChild == null) {
						currentNode.leftChild = new KDNode(point, currentNode, null, null);

						return true;
					} else {
						insertNode(point, currentNode.leftChild, rootNode, !checkLat);
					}
				}
			}

		}
		return false;
	}

	@Override
	public boolean deletePoint(Point point) {
		boolean isTrue;
		KDNode rootNode = getRootNode(point);
		isTrue = deletePointHelper(point, rootNode);
		if (isTrue) {
			setRootNode(deleteNode(point, rootNode, true));
			return isTrue;
		}
		return false;
	}

	public boolean deletePointHelper(Point point, KDNode rootNode) {
		// determines if root node is null or point is not present
		if (rootNode == null)
			return false;
		if (isPointIn(point) == false)
			return false;

		return true;
	}

	public KDNode deleteNode(Point point, KDNode currentNode, boolean checkLat) {
		// Recursively calls method to find the node to delete

		if (currentNode == null)
			return null;
		// checks if point exists

		// checks if point is root
		if (currentNode.point.equals(point)) {

			if (currentNode.rightChild != null) {
				// find min of roots right subtree
				KDNode rightMin = findMinimum(currentNode.rightChild, checkLat, true);
				// copy the min root
				currentNode.point = rightMin.point;

				// Recursively to delete min
				currentNode.rightChild = deleteNode(currentNode.rightChild.point, rightMin, !checkLat);
			}

			else if (currentNode.leftChild != null) {
				// find min of roots right subtree
				KDNode leftMin = findMinimum(currentNode.leftChild, checkLat, true);

				// copy the min root
				currentNode.point = leftMin.point;

				// Recursively to delete min
				currentNode.rightChild = deleteNode(currentNode.leftChild.point, leftMin, !checkLat);
			} else { // leaf node
				return null;
			}
			return currentNode;
		}

		if (checkLat == true) {
			if (point.lat < currentNode.point.lat)
				currentNode.leftChild = deleteNode(point, currentNode.leftChild, false);
			else
				currentNode.rightChild = deleteNode(point, currentNode.rightChild, false);
		} else {
			if (point.lon < currentNode.point.lon)
				currentNode.leftChild = deleteNode(point, currentNode.leftChild, true);
			else
				currentNode.rightChild = deleteNode(point, currentNode.rightChild, true);
		}
		return currentNode;
	}

	private KDNode findMinimum(KDNode currentNode, boolean currentDim, boolean targetDim) {
		// finds the minimum node
		if (currentNode == null)
			return null;

		if (currentDim == targetDim) {
			if (currentNode.leftChild == null)
				return currentNode;
			else
				return findMinimum(currentNode.leftChild, currentDim, !currentDim);

		}

		KDNode rightMin = findMinimum(currentNode.rightChild, currentDim, !currentDim);
		KDNode leftMin = findMinimum(currentNode.leftChild, currentDim, !currentDim);

		if (targetDim == true) {
			if (rightMin != null && rightMin.point.lat < currentNode.point.lat)
				currentNode = rightMin;
			if (leftMin != null && leftMin.point.lat < currentNode.point.lat)
				currentNode = leftMin;
		} else {
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
       
      
       
       KDNode currentNode = getRootNode(point);

        
        // if current node is null return false
        while (currentNode != null) {
        	
        	if (currentNode.point.equals(point)) 
        		return true;
        
        	if (isLatitude == true)
        	{ 
        		if (point.lat < currentNode.point.lat) 
        		{ currentNode = currentNode.leftChild;}
        		else
        		{currentNode = currentNode.rightChild;}
        		isLatitude = false;
        	}
        	else 
        	{ 
        		if (point.lon < currentNode.point.lon)
        		{ currentNode = currentNode.leftChild;}
        		else
        		{currentNode = currentNode.rightChild;}
        		isLatitude = true;
        	}
        	
        }

        return false;
    }


	private List<PointDist> nearestNeighbour(KDNode currentNode, Point searchTerm, boolean checkLat,
			List<PointDist> nearestList, int k) {

		double axisDist;

		KDNode nextBranch, otherBranch;

		// returns nothing if root node is empty
		if (currentNode == null)
			return nearestList;
		if (checkLat == true)
			if (searchTerm.lat < currentNode.point.lat) {
				nextBranch = currentNode.leftChild;
				otherBranch = currentNode.rightChild;
			} else {
				nextBranch = currentNode.rightChild;
				otherBranch = currentNode.leftChild;
			}
		else {
			if (searchTerm.lon < currentNode.point.lon) {
				nextBranch = currentNode.leftChild;
				otherBranch = currentNode.rightChild;
			} else {
				nextBranch = currentNode.rightChild;
				otherBranch = currentNode.leftChild;
			}
		}

		double currentDist = currentNode.point.distTo(searchTerm);
		List<PointDist> tempNearestList = nearestList;

		tempNearestList = nearestNeighbour(nextBranch, searchTerm, !checkLat, nearestList, k);
		List<PointDist> bestList = findBestNode(tempNearestList, currentNode, searchTerm, currentDist, k);

		if (currentNode.parent != null) {
			// Calculates the distance from the last axis
			if (checkLat == true)
				axisDist = searchTerm.lat - currentNode.parent.point.lat;
			else
				axisDist = searchTerm.lon - currentNode.parent.point.lon;

			if (currentDist >= axisDist) {
				tempNearestList = nearestNeighbour(otherBranch, searchTerm, !checkLat, nearestList, k);
			}
		}
		return bestList;

	}

	private List<PointDist> findBestNode(List<PointDist> tempNearestList, KDNode currentNode, Point searchTerm,
			double currentDist, int k) {
		//finds the closest node by comparing a node to the end of of the templist
		//returns the list with the new closest node
		double comparedDist = -1;

		if (tempNearestList.size() < k) {
			PointDist addPointDist = new PointDist(currentNode.point, currentDist);

			tempNearestList.add(addPointDist);
			tempNearestList = insertionSort(tempNearestList);
		} else {
			PointDist comparedPoint = tempNearestList.get(k - 1);

			comparedDist = searchTerm.distTo(comparedPoint.pointInList);
			if (currentDist < comparedDist) {
				tempNearestList.remove(comparedPoint);
				PointDist addPointDist = new PointDist(currentNode.point, currentDist);
				tempNearestList.add(addPointDist);
				tempNearestList = insertionSort(tempNearestList);
			}
		}
		return tempNearestList;

	}

	List<PointDist> insertionSort(List<PointDist> nearestList) {
		// Insertion sort from lowest to highest

		int n = nearestList.size();
		for (int i = 1; i < n; ++i) {
			PointDist key = new PointDist(null, 0);
			key.pointInList = nearestList.get(i).pointInList;
			key.dist = nearestList.get(i).dist;
			int j = i;

			while (j > 0 && (nearestList.get(j - 1).dist > key.dist)) {
				nearestList.set(j, nearestList.get(j - 1));
				j--;
			}
			nearestList.set(j, key);
		}
		return nearestList;
	}

	private Point findMedian(List<Point> listToSort) {
		// finds median node
		int n = listToSort.size();
		for (int i = 1; i < n; ++i) {
			Point key = listToSort.get(i);

			int j = i;

			while (j > 0 && (listToSort.get(j - 1).lat > key.lat)) {
				listToSort.set(j, listToSort.get(j - 1));
				j--;
			}
			listToSort.set(j, key);
		}
		int index = listToSort.size() % 2 == 0 ? listToSort.size() / 2 - 1 : listToSort.size() / 2;

	
		return listToSort.get(index);
	}

	private KDNode getRootNode(Point point) {
		if (point.cat == Category.RESTAURANT)
			return rootResturant;

		else if (point.cat == Category.EDUCATION)
			return rootEducation;

		else
			return rootHospital;
	}

	private void setRootNode(KDNode KDNode) {
		if (KDNode.point.cat == Category.RESTAURANT)
			rootResturant = KDNode;

		else if (KDNode.point.cat == Category.EDUCATION)
			rootEducation = KDNode;

		else if (KDNode.point.cat == Category.HOSPITAL)
			rootHospital = KDNode;
	}
}

//KD NODE CLASS
class KDNode {
	public Point point;
	public KDNode parent;
	public KDNode leftChild;
	public KDNode rightChild;

	public KDNode(Point point, KDNode parent, KDNode leftChild, KDNode rightChild) {
		this.point = point;
		this.parent = parent;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

}

//Point and distance class for nearest neighbour
class PointDist {
	public Point pointInList;
	public double dist;

	public PointDist(Point pointInList, double dist) {
		this.pointInList = pointInList;
		this.dist = dist;
	}

}
