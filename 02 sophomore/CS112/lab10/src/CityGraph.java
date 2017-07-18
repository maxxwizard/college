package lab10;

import java.util.Arrays;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class CityGraph {
	int num_cities;
	boolean [][] adjacency_matrix;  //this matrix is size num_cities x num_cities
	
	// Fields to keep track of the source to destination path information.
	int source_node, destination_node;
	int [] source_destination_path;
	
	void getSourceDestinationPath(){
		
		/* The Breadth First Search (BFS) traversal for graphs is very similar to 
		 * level order traversal in trees and is briefly outlined below:
		 *  
		 * (a) Start BFS traversal from the source_node.
		 *
		 * (b) Instead of children for a node in a tree, we have "neighbors" for a
		 *     node in a graph. 
		 *     ==> The neighbors for node V are obtained from the row of 
		 *         adjacency_matrix A corresponding to V. 
		 *         To get V's neighbors:
		 *         find the cells A[V][0], A[V][1],..., A[V][num_cities - 1]
		 *         that contain a "true" value.
		 * 
		 * (d) Use a queue like we did for level order traversal. In each iteration 
		 *     remove a node V from the head of the queue and add V's not-visited neighbors 
		 *     (lets call the neighbor node N_v) to the tail of the queue. Mark each 
		 *     of N_v as visited and set the previous node of N_v as V, this step is
		 *     described in (e) below. If one of N_v is the the destination node, 
		 *     we are done.  
		 * 
		 *     Repeat this process until either we reach the destination node or 
		 *     the queue gets empty. 
		 * 
		 *     If the queue empties before we reach destination, this implies that the 
		 *     source and destination are disconnected in the graph.
		 * 
		 * (c) Keep track of nodes that are visited during the BFS traversal so that
		 *     we do not revisit the same node (to avoid getting stuck in a cycle).
		 *     ==> Use visited_nodes array below to do this.
		 *     
		 * (e) For each visited node V_n: store the previous node P_n via which we 
		 *     visited V_n during BFS traversal. 
		 *     This is similar to the parent node in trees, since there are no parent nodes in a 
		 *     graph the previous node are defined based on the traversal.
		 *     ==> Use previous_node array below for this, this array is needed 
		 *         to retrace our path to the source once we reach the destination node in 
		 *         our BFS traversal.               
		 */
		
		// Use BFS_queue to queue nodes just like we did for level order traversal in trees.
		LinkedList<Integer> BFS_queue = new LinkedList<Integer>();
		// The source node is the first node to insert into the queue.
		BFS_queue.add(source_node);
		
		// Visited_nodes keeps track of nodes visited during BFS traversal, all nodes 
		// are initialized as not-visited.
		boolean [] visited_nodes = new boolean[num_cities];
		// Source node is set as visited since it has been added to the BFS queue. 
		visited_nodes[source_node] = true;
		
		// We use previous_node array to store the previous_node from which we visited a node
		// in the BFS traversal. This is only set for the nodes that we actually visit, for 
		// other nodes their previous values don't matter. 
		// For instance, if we visit node 17 from node 5, we set previous_node[17] = 5. 
		int [] previous_node = new int[num_cities];
		// Since source_node is the first node, we initialize its previous node to -1.
		previous_node[source_node] = -1;
		
		while( !BFS_queue.isEmpty() ){
			
			/* TASK:
			 * Your task today is to complete the statements inside this while loop.
			 * The BFS algorithm to find source-destination path is described above.
			 */
			break; // Remove this break statement and replace with your code.
		}
		
		// No changes to code are needed after this.
		
		if( !visited_nodes[destination_node] )
		// Unable to reach destination implies there is no path from source to destination.
			return;
		
		// Now we will do the back tracking to find the path from destination to source.
		int cur_node = destination_node;
		source_destination_path = new int[num_cities+1];
		int i = 0;
		while( cur_node != source_node ){
			source_destination_path[i++] = cur_node;
			cur_node = previous_node[cur_node];
		}
		source_destination_path[i++] = source_node;
		source_destination_path[i++] = -1;
	}
	
	// Fields and methods for UI and city coordinates.
	Cartographer cartographer;
	int [][] city_coords;
	Rectangle2D [] button_bboxes;
	
	public CityGraph(Cartographer cartographer){
		this.cartographer = cartographer;
		num_cities = cartographer.num_cities;
		setupAdjacencyMatrix();
		city_coords = new int[num_cities][2];
		source_node = destination_node = -1;
		source_destination_path = null;
		zoom = US_zoom;
	}
	void setupAdjacencyMatrix(){
		adjacency_matrix = new boolean[num_cities][num_cities];
		int node_degree = 3;
		for( int i = 0; i < num_cities; i++ ){
			float [] dist = cartographer.cities_distance[i];
			int [] idx = new int[num_cities];
			for( int j = 0; j < num_cities; j++ ) idx[j] = j;
			quicksort( dist, idx, 0, num_cities-1 );
			for( int j = 0; j < node_degree; j++ ){
				adjacency_matrix[i][idx[j+1]] = true;
				adjacency_matrix[idx[j+1]][i] = true;
			}
		}
	}
	void paint(Graphics g, int framew, int frameh){
		int [] graph_bbox = {40, 40, framew - 40, frameh - 30};
		g.setFont(new Font("Verdana",Font.BOLD,9));
		
		for( int i = 0; i < num_cities; i++ )
			city_coords[i] = getCityCoordinates(cartographer.cities_info[i], graph_bbox);
		
		for( int i = 0; i < num_cities; i++ )
			for( int j = i+1; j < num_cities; j++ ){
				if( adjacency_matrix[i][j] )
					drawLine(g, city_coords[i], city_coords[j], false);
			}
		
		for( int i = 0; i < num_cities; i++ )
			displayCityNode(g, i, graph_bbox);
		
		if( source_destination_path != null ){
			int i = 0;
			while( source_destination_path[i+1] != -1 ){
				drawLine(g, city_coords[source_destination_path[i]],
						    city_coords[source_destination_path[i+1]], true);
				i++;
			}
		}
		
		displayButtons(g, graph_bbox);
	}
	
	boolean mouseClicked(int mouseX, int mouseY){
		int clicked_node = getClickedNode(mouseX, mouseY);
		if( clicked_node < 0 )
			return getClickedButton(mouseX, mouseY);
		if( clicked_node == destination_node )	
			destination_node = -1;
		else if( source_node == -1 )
			source_node = clicked_node; 
		else if( clicked_node == source_node ){
			source_node = destination_node;
			destination_node = -1;
		} else 
			destination_node = clicked_node;
			
		if( source_node != -1 && destination_node != -1 )
			getSourceDestinationPath();
		else
			source_destination_path = null;
		
		return true;
	}
	
	int getClickedNode(int mouseX, int mouseY){
		int min_dist = 999999;
		int mini = -1;
		for( int i = 0; i < num_cities; i++ ){
			int nx = city_coords[i][0] + node_diameter/2;
			int ny = city_coords[i][1] + node_diameter/2;
			int cur_dist = (mouseX - nx)*(mouseX - nx) + (mouseY - ny)*(mouseY - ny);
			if( cur_dist < min_dist ){
				min_dist = cur_dist;
				mini = i;
			}
		}
		if( Math.sqrt(min_dist) < node_diameter/2 )
			return mini;
		else
			return -1;
	}
	
	static int node_diameter = 12;
	void displayCityNode(Graphics g, int cityNodeId, int [] graph_bbox){
		
		CityInfo city_info = cartographer.cities_info[cityNodeId];
		int [] city_coords = this.city_coords[cityNodeId];
		if( cityNodeId == source_node )
			g.setColor(Color.GREEN);
		else if( cityNodeId == destination_node )
			g.setColor(Color.RED);
		else
			g.setColor(Color.ORANGE);
        g.fillOval( city_coords[0], city_coords[1], node_diameter, node_diameter );
        Rectangle2D tbbox = g.getFontMetrics().getStringBounds(city_info.city_name, g);
        g.setColor(Color.WHITE);
        g.drawString(city_info.city_name, 
        			 city_coords[0] - (int)(tbbox.getWidth()/2) + node_diameter/2,  
        			 city_coords[1] - 3);
	}
	
	void drawLine(Graphics g, int [] coords1, int [] coords2, boolean on_source_destination_path){
        int radius = node_diameter / 2;
        int x1 = coords1[0] + radius;
        int y1 = coords1[1] + radius;
        int x2 = coords2[0] + radius;
        int y2 = coords2[1] + radius;
        double dist = Math.sqrt(Math.pow((x1 - x2), 2)+Math.pow((y1 - y2), 2));
        double alpha1 = radius / dist;
        double alpha2 = (dist - radius) / dist;
        int xn1 = (int)(alpha1 * x1 + (1 - alpha1) * x2);
        int yn1 = (int)(alpha1 * y1 + (1 - alpha1) * y2);
        int xn2 = (int)(alpha2 * x1 + (1 - alpha2) * x2);
        int yn2 = (int)(alpha2 * y1 + (1 - alpha2) * y2);
        
        g.setColor(Color.LIGHT_GRAY);
        float d = 2;
        if( on_source_destination_path ){
        	d = 4;
        	g.setColor(Color.BLUE);
        }
        	
        float x_delta = xn2 - xn1; float y_delta = yn2 - yn1;
        float x_d, y_d;
        if( yn2 != yn1 ){
        	x_d = (float)Math.sqrt(d / ( 1 + x_delta*x_delta/(y_delta*y_delta)));
        	y_d = (-x_d * x_delta/y_delta);
        } else {
        	x_d = 0;
        	y_d = (float)Math.sqrt(d);
        }
        int xd = (int)Math.round(x_d);
        int yd = (int)Math.round(y_d);
        int [] polyx = {xn1 + xd, xn1 - xd, xn2 - xd, xn2 + xd};
        int [] polyy = {yn1 + yd, yn1 - yd, yn2 - yd, yn2 + yd};
        g.fillPolygon(polyx, polyy, 4);
    }
	int [] getCityCoordinates(CityInfo info, int [] graph_bbox){
		int [] coords = new int[2];
		double [] cart_bounds = {
			cartographer.min_x + zoom[0]*(cartographer.max_x - cartographer.min_x),
			cartographer.min_y + zoom[1]*(cartographer.max_y - cartographer.min_y),
			cartographer.min_x + zoom[2]*(cartographer.max_x - cartographer.min_x),
			cartographer.min_y + zoom[3]*(cartographer.max_y - cartographer.min_y)};
		
		coords[0] = (int)(graph_bbox[0] + (graph_bbox[2]-graph_bbox[0])*
					(info.x - cart_bounds[0])/(cart_bounds[2] - cart_bounds[0]));
		
		coords[1] = (int)(graph_bbox[1] + (graph_bbox[3]-graph_bbox[1])*
					(cart_bounds[3] - info.y)/(cart_bounds[3] - cart_bounds[1]));
		
		return coords;
	}
	
	double [] zoom;
	Rectangle2D NE_button_bbox, US_button_bbox; 
	double [] NE_zoom = {0.65, 0.5, 1, 0.85};
	double [] US_zoom = {0, 0, 1, 1};
	
	boolean getClickedButton(int mouseX, int mouseY){
		if( US_button_bbox.contains(mouseX, mouseY) )
			zoom = US_zoom;
		else if( NE_button_bbox.contains(mouseX, mouseY) )
			zoom = NE_zoom;
		else
			return false;
		return true;
	}
	void displayButtons(Graphics g, int [] graph_bbox){
		
		int [] US_zoom_coords = {graph_bbox[2] - 220, graph_bbox[3] - 10};
		int [] NE_zoom_coords = {graph_bbox[2] - 220, graph_bbox[3] - 35};
		String US_zoom_str = "Zoom out - continental US";
		String NE_zoom_str = "Zoom in  - North East ";
		
		g.setFont(new Font("Verdana",Font.BOLD,16));
		US_button_bbox = g.getFontMetrics().getStringBounds(US_zoom_str, g);
		US_button_bbox.setRect(US_zoom_coords[0], US_zoom_coords[1] + US_button_bbox.getY(), 
					US_button_bbox.getWidth(), US_button_bbox.getHeight());
		
		NE_button_bbox = g.getFontMetrics().getStringBounds(NE_zoom_str, g);
		NE_button_bbox.setRect(NE_zoom_coords[0], NE_zoom_coords[1] + NE_button_bbox.getY(), 
					NE_button_bbox.getWidth(), NE_button_bbox.getHeight());
		
		g.setColor(Color.WHITE);
		g.fillRect((int)US_button_bbox.getX(), (int)US_button_bbox.getY(),
					(int)US_button_bbox.getWidth(), (int)US_button_bbox.getHeight());
		g.fillRect((int)NE_button_bbox.getX(), (int)NE_button_bbox.getY(),
				   (int)NE_button_bbox.getWidth(), (int)NE_button_bbox.getHeight());
		g.setColor(Color.BLACK);
		g.drawString(US_zoom_str, US_zoom_coords[0], US_zoom_coords[1]);
		g.drawString(NE_zoom_str, NE_zoom_coords[0], NE_zoom_coords[1]);
	}
	
	// Quicksort elements of the array A between a and b inclusive
    void quicksort (float [] A, int [] B, int a, int b) {
 
        if (a < b)  
        {   
           float pivot = A[b];        // or choose one element at random, swap into index b
           int l = a; 
           int r = b-1;
 
           // Keep pivoting until the l and r indices cross over.
           while (l <= r) {
 
               while (l <= r && A[l] <= pivot)    // slide l right until it points to an
               l++;                           //  element larger than pivot
 
               while (l <= r && A[r] >= pivot)    // slide r left until it points to an
               r--;                           //  element smaller than pivot
 
               if (l < r){                         // swap out-of-order pairs 
                   float temp = A[l]; A[l] = A[r]; A[r] = temp;
                   int temp2 = B[l]; B[l] = B[r]; B[r] = temp2;
               }
           }
           //  Re-position the pivot into its correct slot
           float temp = A[l]; A[l] = A[b]; A[b] = temp;
           int temp2 = B[l]; B[l] = B[b]; B[b] = temp2;
 
           quicksort (A, B, a, l-1);
           quicksort (A, B, l + 1, b);
        }
    }
    boolean mouseOver(int mouseX, int mouseY){
		if( source_node >= 0 && destination_node == -1 ){
			int clicked_node = getClickedNode(mouseX, mouseY);
			if( clicked_node < 0 || clicked_node == source_node )	
				return false;
			destination_node = clicked_node;
			getSourceDestinationPath();
			destination_node = -1;
			return true;
		} else 
			return false;
	}
}
