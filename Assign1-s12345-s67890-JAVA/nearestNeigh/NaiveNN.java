package nearestNeigh;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class is required to be implemented.  Naive approach implementation.
 *
 * 
 */
public class NaiveNN implements NearestNeigh{
	
	private List<Point> index = new ArrayList<Point>();
	
    @Override
    public void buildIndex(List<Point> points) {
        index.addAll(points);
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        List<Point> searchResults = new ArrayList<Point>();
        List<Point> indexCopy = new ArrayList<Point>(index);
        
        while (searchResults.size() < k && indexCopy.size() > 0) {
        	Point nextNN = null;
        	for (ListIterator<Point> iter = indexCopy.listIterator(); iter.hasNext(); ) {
        		Point point = iter.next();
        		double distTo = point.distTo(searchTerm);
        		if (nextNN == null || distTo < searchTerm.distTo(nextNN)) {
        			nextNN = point;
        		}
        	}
        	searchResults.add(nextNN);
        	indexCopy.remove(nextNN);
        }
        return searchResults;
    }

    @Override
    public boolean addPoint(Point point) {
        if (index.contains(point)) {
        	return false;
        } else {
        	index.add(point);
        	return true;
        }
    }

    @Override
    public boolean deletePoint(Point point) {
        if (index.contains(point)) {
        	index.remove(point);
        	return true;
        } else {
        	return false;
        }
    }

    @Override
    public boolean isPointIn(Point point) {
        return index.contains(point);
    }

}
