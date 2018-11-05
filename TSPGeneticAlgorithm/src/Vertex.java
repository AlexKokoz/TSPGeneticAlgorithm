
/**
 * The {@code Vertex} class represents a vertex.
 * 
 * The vertex can be 2-dimensional or 3-dimensional.
 * 
 * This class supports get operations for retrieving the x coordinate, the y
 * coordinate and the z coordinate(if the vertex is 3-dimensional), as well as a
 * {@code is2D} method for determining if a vertex is 2-dimensional.
 * 
 * All methods including the constructor take constant time.
 * 
 * @author AccelSprinter
 *
 */
public class Vertex {

	private final Double x; // x coordinate
	private final Double y; // y coordinate
	private final Double z; // z coordinate

	/**
	 * Creates a 2-dimensional vertex.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public Vertex(Double x, Double y) {
		this.x = x;
		this.y = y;
		this.z = null;
	}

	/**
	 * Creates a 3-dimensional vertex.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 */
	public Vertex(Double x, Double y, Double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns the x coordinate.
	 * 
	 * @return the x coordinate
	 */
	public double x() {
		return this.x;
	}

	/**
	 * Returns the y coordinate.
	 * 
	 * @return the y coordinate
	 */
	public double y() {
		return this.y;
	}

	/**
	 * Returns the z coordinate.
	 * 
	 * @return the z coordinate
	 * @throws IllegalArgumentException
	 *             if the vertex is 2-dimensional
	 */
	public double z() {
		if (is2D())
			throw new IllegalAccessError("vertex is two dimensional");
		return this.z;
	}

	/**
	 * Returns true if the vertex is two dimensional; false if it is 3-dimensional.
	 * 
	 * @return true if the vertex is two dimensional; false if it is 3-dimensional
	 */
	public boolean is2D() {
		return this.z == null;
	}

	// debugging
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
