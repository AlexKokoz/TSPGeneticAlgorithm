
public class DistanceFunctions {
	private final static double EARTH_RADIUS = 6378.388;

	/**
	 * Returns the nearest integer of Euclidean distance between two 2-dimensional
	 * points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @return the nearest integer of Euclidean distance between two 2-dimensional
	 *         points
	 */
	public static double nintEuc2D(double x1, double y1, double x2, double y2) {
		return nint(euc2D(x1, y1, x2, y2));
	}

	/**
	 * Returns the nearest integer of Euclidean distance between two 3-dimensional
	 * points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param z1
	 *            the z coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @param z2
	 *            the z coordinate of other point
	 * @return the nearest integer of Euclidean distance between two 3-dimensional
	 *         points
	 */
	public static double nintEuc3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		return nint(euc3D(x1, y1, z1, x2, y2, z2));
	}

	/**
	 * Returns the Euclidean distance between two 2-dimensional points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @return the Euclidean distance between two 2-dimensional points
	 */
	public static double euc2D(double x1, double y1, double x2, double y2) {
		double xd = x1 - x2;
		double yd = y1 - y2;
		return Math.sqrt(xd * xd + yd * yd);
	}

	/**
	 * Returns the Euclidean distance between two 3-dimensional points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param z1
	 *            the z coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @param z2
	 *            the z coordinate of other point
	 * @return the Euclidean distance between two 3-dimensional points
	 */
	public static double euc3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		double xd = x1 - x2;
		double yd = y1 - y2;
		double zd = z1 - z2;
		return Math.sqrt(xd * xd + yd * yd + zd * zd);
	}

	/**
	 * Returns the nearest integer of the Manhattan distance of two 2-dimensional
	 * points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @return the nearest integer of the Manhattan distance of two 2-dimensional
	 *         points
	 */
	public static double nintMan2D(double x1, double y1, double x2, double y2) {
		return nint(man2D(x1, y1, x2, y2));
	}

	/**
	 * Returns the Manhattan distance of two 2-dimensional points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @return the Manhattan distance of two 2-dimensional points
	 */
	public static double man2D(double x1, double y1, double x2, double y2) {
		double xd = Math.abs(x1 - x2);
		double yd = Math.abs(y1 - y2);
		return xd + yd;
	}

	/**
	 * Returns the nearest integer of the Manhattan distance between two
	 * 3-dimensional points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param z1
	 *            the z coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @param z2
	 *            the z coordinate of other point
	 * @return the nearest integer of the Manhattan distance between two
	 *         3-dimensional points
	 */
	public static double nintMan3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		return nint(man3D(x1, y1, z1, x2, y2, z2));
	}

	/**
	 * Returns the Manhattan distance between two 3-dimensional points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param z1
	 *            the z coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @param z2
	 *            the z coordinate of other point
	 * @return the Manhattan distance between two 3-dimensional points
	 */
	public static double man3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		double xd = Math.abs(x1 - x2);
		double yd = Math.abs(y1 - y2);
		double zd = Math.abs(z1 - z2);
		return xd + yd + zd;
	}

	/**
	 * Returns the Maximum distance between two 2-dimensional points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @return the Maximum distance between two 2-dimensional points
	 */
	public static double max2D(double x1, double y1, double x2, double y2) {
		double xd = Math.abs(x1 - x2);
		double yd = Math.abs(y1 - y2);
		return Math.max(nint(xd), nint(yd));
	}

	/**
	 * Returns the Maximum distance between two 3-dimensional points.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param z1
	 *            the z coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @param z2
	 *            the z coordinate of other point
	 * @return the Maximum distance between two 3-dimensional points
	 */
	public static double max3D(double x1, double y1, double z1, double x2, double y2, double z2) {
		double xd = Math.abs(x1 - x2);
		double yd = Math.abs(y1 - y2);
		double zd = Math.abs(z1 - z2);
		return Math.max(Math.max(nint(xd), nint(yd)), nint(zd));
	}

	/**
	 * Returns the Geographical distance between two points. The coordinates of the
	 * points are given in DDD.MM form, where DDD stands for degrees and MM stands
	 * for minutes.
	 * 
	 * @param x1
	 *            the latitude coordinate of one point
	 * @param y1
	 *            the longitude coordinate of one point
	 * @param x2
	 *            the latitude coordinate of other point
	 * @param y2
	 *            the longitude coordinate of other point
	 * @return the Geographical distance between two points. The coordinates of the
	 *         points are given in DDD.MM form, where DDD stands for degrees and MM
	 *         stands for minutes
	 */
	public static double geo(double x1, double y1, double x2, double y2) {
		double lat1 = latitude(x1);
		double long1 = longitude(y1);
		double lat2 = latitude(x2);
		double long2 = longitude(y2);

		double q1 = Math.cos(long1 - long2);
		double q2 = Math.cos(lat1 - lat2);
		double q3 = Math.cos(lat1 + lat2);

		return Math.floor(EARTH_RADIUS * Math.acos(0.5 * ((1.0 + q1) * q2 - (1.0 - q1) * q3)) + 1.0);
	}

	/**
	 * Returns the pseudo-Euclidean distance between two 2-dimensional points.
	 * * @param x1 the x coordinate of one point
	 * 
	 * @param y1
	 *            the y coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @return the pseudo-Euclidean distance between two 2-dimensional points
	 */
	public static double att2D(double x1, double y1, double x2, double y2) {
		double xd = x1 - x2;
		double yd = y1 - y2;
		return Math.ceil(Math.sqrt((xd * xd + yd * yd) / 10.0));
	}

	/**
	 * Returns the Euclidean distance between two 2-dimensional points rounded up to
	 * the next integer.
	 * 
	 * @param x1
	 *            the x coordinate of one point
	 * @param y1
	 *            the y coordinate of one point
	 * @param x2
	 *            the x coordinate of other point
	 * @param y2
	 *            the y coordinate of other point
	 * @return the Euclidean distance between two 2-dimensional points rounded up to
	 *         the next integer
	 */
	public static double ceil2D(double x1, double y1, double x2, double y2) {
		return Math.ceil(euc2D(x1, y1, x2, y2));
	}

	/**
	 * Returns the nearest integer to double {@code x}.
	 * 
	 * @param x
	 *            given double
	 * @return the nearest integer to double {@code x}
	 */
	public static double nint(double x) {
		return Math.round(x);
	}

	/**
	 * Converts given value from degrees to radians and returns the result.
	 * 
	 * @param deg
	 *            the given value in degrees
	 * @return given value converted from degrees to radians
	 */
	public static double degToRad(double deg) {
		return deg * Math.PI / 180;
	}

	/**
	 * Converts a given coordinate value of the DDD.MM format, where DDD stands for
	 * degrees and MM stands for minutes, in radians.
	 * 
	 * @param dddmm
	 *            the given coordinate value in DDD.MM format
	 * @return the coordinate value in radians
	 */
	public static double coordinate(double dddmm) {
		double deg = Math.floor(Math.abs(dddmm));
		double min = Math.abs(dddmm) - deg;

		if (deg < 0 || deg > 180 || min < 0 || min > 60)
			throw new IllegalArgumentException("invalid value for dddmm = " + dddmm);

		return Math.signum(dddmm) * degToRad(deg + 5.0 * min / 3.0);
	}

	/**
	 * Returns the latitude from a given coordinate value in DDD.MM format, where
	 * DDD stands for degrees and MM stands for minutes, in radians.
	 * 
	 * @param dddmm
	 *            the given coordinate value in DDD.MM format
	 * @return the latitude value in radians
	 */
	public static double latitude(double dddmm) {
		return coordinate(dddmm);
	}

	/**
	 * Returns the longitude from a given coordinate value in DDD.MM format, where
	 * DDD stands for degrees and MM stands for minutes, in radians.
	 * 
	 * @param dddmm
	 *            the given coordinate value in DDD.MM format
	 * @return the longitude value in radians
	 */
	public static double longitude(double dddmm) {
		return coordinate(dddmm);
	}

	public static void main(String[] args) {
		System.out.println(latitude(-118.24));
		System.out.println(geo(14.55, -23.31, 28.06, -15.24));

	}

}
