import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * This class contains static methods for parsing TSPLIB data files.
 * 
 * TSPLIB is a library of sample instances for the TSP (and related problems)
 * from various sources and of various types. Each library data file consists of
 * two parts; the specification part and the data part.
 * 
 * The specification part contains keyword-value pairs in the form
 * 
 * <keyword>:<value> i.e. NAME: st70 DIMENSION: 70 TYPE: TSP
 * 
 * The data part contains keyword-value pairs in the form
 * 
 * <keyword> <data> i.e. NODE_CORD_SECTION 1 42 35 2 8 54 3 198 26 ...
 * 
 * All valid keywords are mentioned explicitly in the TSPLIB formatting guide.
 * 
 * TSPLIB can be found here:
 * http://elib.zib.de/pub/mp-testdata/tsp/tsplib/tsplib.html
 * 
 * File formatting guide of the TSPLIB instances can be found here:
 * https://www.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/tsp95.pdf
 * 
 * This class's methods help retrieve the value of each keyword of a TSPLIB
 * instance file, either from the specification part or from the data part. To
 * achieve this, two regular expression patterns are used, one for the
 * specification keyword-value pairs and one for the data keyword-value pairs.
 * 
 *  @author AccelSprinter
 */

public class TSPLIBParser {

	// SPECIFICATION PART KEYWORDS
	private static final String NAME = "NAME";
	private static final String TYPE = "TYPE";
	private static final String COMMENT = "COMMENT";
	private static final String DIMENSION = "DIMENSION";
	private static final String EDGE_WEIGHT_TYPE = "EDGE_WEIGHT_TYPE";
	private static final String EDGE_WEIGHT_FORMAT = "EDGE_WEIGHT_FORMAT";
	private static final String NODE_COORD_TYPE = "NODE_COORD_TYPE";
	private static final String DISPLAY_DATA_TYPE = "DISPLAY_DATA_TYPE";

	// DATA PART KEYWORDS
	private static final String NODE_COORD_SECTION = "NODE_COORD_SECTION";
	private static final String DEPOT_SECTION = "DEPOT_SECTION";
	private static final String DEMAND_SECTION = "DEMAND_SECTION";
	private static final String EDGE_DATA_SECTION = "EDGE_DATA_SECTION";
	private static final String FIXED_EDGES_SECTION = "FIXED_EDGES_SECTION";
	private static final String DISPLAY_DATA_SECTION = "DISPLAY_DATA_SECTION";
	private static final String TOUR_SECTION = "TOUR_SECTION";
	private static final String EDGE_WEIGHT_SECTION = "EDGE_WEIGHT_SECTION";

	// PLACEHOLDERS
	private static final String SPECIFICATION_KEYWORD = "SPECIFICATION_KEYWORD";
	private static final String DATA_KEYWORD = "DATA_KEYWORD";

	// FLAGS FOR KEYWORDS
	private static final boolean IS_SPEC = true;
	private static final boolean IS_DATA = false;

	// GENERIC SPECIFICATION KEYWORD_VALUE PAIR STRING PATTERN
	private static final String GENERIC_SPEC_STRING_PATTERN 
	= "(?m)^\\s*" + SPECIFICATION_KEYWORD + "\\s*:\\s*(\\S+)\\s*$";

	// GENERIC DATA KEYWORD_VALUE PAIR STRING PATTERN
	private static final String GENERIC_DATA_STRING_PATTERN 
	= "(?m)^\\s*" + DATA_KEYWORD + "\\s*$(?s)([.&[^\\p{Upper}]]+)(?m)(?=[^\\p{Upper}+$|\\-1|$])";
	
	// Should not be instantiated!
	public TSPLIBParser() {}

	/**
	 * Returns the value of a keyword from a text according to the TSPLIB formatting
	 * rules, if it exists; null otherwise. A boolean flag is used to denote the
	 * keyword as a specification one or a data one (in order to use the appropriate
	 * regular expression).
	 * 
	 * @param text
	 *            the given target text
	 * @param keyword
	 *            the keyword
	 * @param spec
	 *            the boolean flag; true for specification keyword, false for data
	 * @return the value of a keyword from a text according to the TSPLIB formatting
	 *         rules, if it exists; null otherwise
	 */
	private static String getValue(String text, String keyword, boolean spec) {
		if (text == null || keyword == null)
			throw new IllegalArgumentException("at least one input is null\n text = " + text + "\n spec = " + spec);

		String stringPattern;
		if (spec) {
			stringPattern = createStringPattern(keyword.toUpperCase(), true);
		} else {
			stringPattern = createStringPattern(keyword.toUpperCase(), false);
		}

		Pattern pattern = Pattern.compile(stringPattern);
		Matcher matcher = pattern.matcher(text);

		String value = null;
		if (matcher.find())
			value = matcher.group(1);
		return value;
	}

	/**
	 * Returns true if the value of a keyword from a text according to the TSPLIB
	 * formatting rules exists; false otherwise. A boolean flag is used to denote
	 * the keyword as a specification one or a data one (in order to use the
	 * appropriate regular expression).
	 * 
	 * @param text
	 *            the given target text
	 * @param keyword
	 *            the keyword
	 * @param spec
	 *            the boolean flag; true for specification keyword, false for data
	 * @return true if a value of a keyword from a text according to the TSPLIB
	 *         formatting rules exists; false otherwise
	 */
	private static boolean hasValue(String text, String keyword, boolean spec) {
		if (text == null || keyword == null)
			throw new IllegalArgumentException("at least one input is null\n text = " + text + "\n spec = " + spec);

		String stringPattern;
		if (spec) {
			stringPattern = createStringPattern(keyword.toUpperCase(), true);
		} else {
			stringPattern = createStringPattern(keyword.toUpperCase(), false);
		}

		Pattern pattern = Pattern.compile(stringPattern);
		Matcher matcher = pattern.matcher(text);

		if (matcher.find())
			return true;
		return false;
	}

	/**
	 * Injects a generic string pattern with the given keyword and returns the
	 * result. A boolean flag is used to determine which generic string pattern will
	 * be used (specification or data).
	 * 
	 * @param keyword
	 *            the keyword
	 * @param spec
	 *            the boolean flag; true for specification keyword, false for data
	 * @return the injected string pattern
	 */
	private static String createStringPattern(String keyword, boolean spec) {
		if (keyword == null)
			throw new IllegalArgumentException("keyword is null");

		if (spec)
			return GENERIC_SPEC_STRING_PATTERN.replace(SPECIFICATION_KEYWORD, keyword);
		else
			return GENERIC_DATA_STRING_PATTERN.replace(DATA_KEYWORD, keyword);
	}

	////////////////////////////////////////////////////////////////////////////////
	// GETTERS: SPECIFICATION PART
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the value corresponding to the "NAME" specification keyword in a
	 * text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "NAME" specification keyword in a
	 *         text, if it exists; null otherwise
	 */
	public static String getName(String text) {
		return getValue(text, NAME, IS_SPEC);
	}

	/**
	 * Returns the value corresponding to the "TYPE" specification keyword in a
	 * text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "TYPE" specification keyword in a
	 *         text, if it exists; null otherwise
	 */
	public static String getType(String text) {
		return getValue(text, TYPE, IS_SPEC);
	}

	/**
	 * Returns the value corresponding to the "COMMENT" specification keyword in a
	 * text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "COMMENT" specification keyword in a
	 *         text, if it exists; null otherwise
	 */
	public static String getComment(String text) {
		return getValue(text, COMMENT, IS_SPEC);
	}

	/**
	 * Returns the value corresponding to the "DIMENSION" specification keyword in a
	 * text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DIMENSION" specification keyword in a
	 *         text, if it exists; null otherwise
	 */
	public static String getDimension(String text) {
		return getValue(text, DIMENSION, IS_SPEC);
	}

	/**
	 * Returns the value corresponding to the "EDGE_WEIGHT_TYPE" specification
	 * keyword in a text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "EDGE_WEIGHT_TYPE" specification
	 *         keyword in a text, if it exists; null otherwise
	 */
	public static String getEdgeWeightType(String text) {
		return getValue(text, EDGE_WEIGHT_TYPE, IS_SPEC);
	}

	/**
	 * Returns the value corresponding to the "EDGE_WEIGHT_FORMAT" specification
	 * keyword in a text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "EDGE_WEIGHT_FORMAT" specification
	 *         keyword in a text, if it exists; null otherwise
	 */
	public static String getEdgeWeightFormat(String text) {
		return getValue(text, EDGE_WEIGHT_FORMAT, IS_SPEC);
	}

	/**
	 * Returns the value corresponding to the "NODE_COORD_TYPE" specification
	 * keyword in a text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "NODE_COORD_TYPE" specification
	 *         keyword in a text, if it exists; null otherwise
	 */
	public static String getNodeCoordType(String text) {
		return getValue(text, NODE_COORD_TYPE, IS_SPEC);
	}

	/**
	 * Returns the value corresponding to the "DISPLAY_DATA_TYPE" specification
	 * keyword in a text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DISPLAY_DATA_TYPE" specification
	 *         keyword in a text, if it exists; null otherwise
	 */
	public static String getDisplayDataType(String text) {
		return getValue(text, DISPLAY_DATA_TYPE, IS_SPEC);
	}

	////////////////////////////////////////////////////////////////////////////////
	// GETTERS: DATA PART
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the value corresponding to the "NODE_COORD_SECTION" data keyword in a
	 * text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "NODE_COORD_SECTION" data keyword in a
	 *         text, if it exists; null otherwise
	 */
	public static String getNodeCoordSection(String text) {
		return getValue(text, NODE_COORD_SECTION, IS_DATA);
	}

	/**
	 * Returns the value corresponding to the "DEPOT_SECTION" data keyword in a
	 * text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DEPOT_SECTION" data keyword in a
	 *         text, if it exists; null otherwise
	 */
	public static String getDepotSection(String text) {
		return getValue(text, DEPOT_SECTION, IS_DATA);
	}

	/**
	 * Returns the value corresponding to the "DEMAND_SECTION" data keyword in a
	 * text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DEMAND_SECTION" data keyword in a
	 *         text, if it exists; null otherwise
	 */
	public static String getDemandSection(String text) {
		return getValue(text, DEMAND_SECTION, IS_DATA);
	}

	/**
	 * Returns the value corresponding to the "EDGE_DATA_SECTION" data keyword in a
	 * text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "EDGE_DATA_SECTION" data keyword in a
	 *         text, if it exists; null otherwise
	 */
	public static String getEdgeDataSection(String text) {
		return getValue(text, EDGE_DATA_SECTION, IS_DATA);
	}

	/**
	 * Returns the value corresponding to the "FIXED_EDGES_SECTION" data keyword in
	 * a text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "FIXED_EDGES_SECTION" data keyword in
	 *         a text, if it exists; null otherwise
	 */
	public static String getFixedEdgesSection(String text) {
		return getValue(text, FIXED_EDGES_SECTION, IS_DATA);
	}

	/**
	 * Returns the value corresponding to the "DISPLAY_DATA_SECTION" data keyword in
	 * a text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DISPLAY_DATA_SECTION" data keyword in
	 *         a text, if it exists; null otherwise
	 */
	public static String getDisplayDataSection(String text) {
		return getValue(text, DISPLAY_DATA_SECTION, IS_DATA);
	}

	/**
	 * Returns the value corresponding to the "TOUR_SECTION" data keyword in a text,
	 * if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "TOUR_SECTION" data keyword in a text,
	 *         if it exists; null otherwise
	 */
	public static String getTourSection(String text) {
		return getValue(text, TOUR_SECTION, IS_DATA);
	}

	/**
	 * Returns the value corresponding to the "EDGE_WEIGHT_SECTION" data keyword in
	 * a text, if it exists; null otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "EDGE_WEIGHT_SECTION" data keyword in
	 *         a text, if it exists; null otherwise
	 */
	public static String getEdgeWeightSection(String text) {
		return getValue(text, EDGE_WEIGHT_SECTION, IS_DATA);
	}

	////////////////////////////////////////////////////////////////////////////////
	// BOOLEAN QUERIES: SPECIFICATION PART
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns true if the value corresponding to the "NAME" specification keyword
	 * in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "NAME" specification keyword in a text
	 *         exists; false otherwise
	 */
	public static boolean hasName(String text) {
		return hasValue(text, NAME, IS_SPEC);
	}

	/**
	 * Returns true if the value corresponding to the "TYPE" specification keyword
	 * in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "TYPE" specification keyword in a text
	 *         exists; false otherwise
	 */
	public static boolean hasType(String text) {
		return hasValue(text, TYPE, IS_SPEC);
	}

	/**
	 * Returns true if the value corresponding to the "COMMENT" specification
	 * keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "COMMENT" specification keyword in a
	 *         text exists; false otherwise
	 */
	public static boolean hasComment(String text) {
		return hasValue(text, COMMENT, IS_SPEC);
	}

	/**
	 * Returns true if the value corresponding to the "DIMENSION" specification
	 * keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DIMENSION" specification keyword in a
	 *         text exists; false otherwise
	 */
	public static boolean hasDimension(String text) {
		return hasValue(text, DIMENSION, IS_SPEC);
	}

	/**
	 * Returns true if the value corresponding to the "EDGE_WEIGHT_TYPE"
	 * specification keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "EDGE_WEIGHT_TYPE" specification
	 *         keyword in a text exists; false otherwise
	 */
	public static boolean hasEdgeWeightType(String text) {
		return hasValue(text, EDGE_WEIGHT_TYPE, IS_SPEC);
	}

	/**
	 * Returns true if the value corresponding to the "EDGE_WEIGHT_FORMAT"
	 * specification keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "EDGE_WEIGHT_FORMAT" specification
	 *         keyword in a text exists; false otherwise
	 */
	public static boolean hasEdgeWeightFormat(String text) {
		return hasValue(text, EDGE_WEIGHT_FORMAT, IS_SPEC);
	}

	/**
	 * Returns true if the value corresponding to the "NODE_COORD_TYPE"
	 * specification keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "NODE_COORD_TYPE" specification
	 *         keyword in a text exists; false otherwise
	 */
	public static boolean hasNodeCoordType(String text) {
		return hasValue(text, NODE_COORD_TYPE, IS_SPEC);
	}

	/**
	 * Returns true if the value corresponding to the "DISPLAY_DATA_TYPE"
	 * specification keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DISPLAY_DATA_TYPE" specification
	 *         keyword in a text exists; false otherwise
	 */
	public static boolean hasDisplayDataType(String text) {
		return hasValue(text, DISPLAY_DATA_TYPE, IS_SPEC);
	}

	////////////////////////////////////////////////////////////////////////////////
	// BOOLEAN QUERIES: DATA PART
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns true if the value corresponding to the "NODE_COORD_SECTION" data
	 * keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "NODE_COORD_SECTION" data keyword in a
	 *         text exists; false otherwise
	 */
	public static boolean hasNodeCoordSection(String text) {
		return hasValue(text, NODE_COORD_SECTION, IS_DATA);
	}

	/**
	 * Returns true if the value corresponding to the "DEPOT_SECTION" data keyword
	 * in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DEPOT_SECTION" data keyword in a text
	 *         exists; false otherwise
	 */
	public static boolean hasDepotSection(String text) {
		return hasValue(text, DEPOT_SECTION, IS_DATA);
	}

	/**
	 * Returns true if the value corresponding to the "DEMAND_SECTION" data keyword
	 * in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DEMAND_SECTION" data keyword in a
	 *         text exists; false otherwise
	 */
	public static boolean hasDemandSection(String text) {
		return hasValue(text, DEMAND_SECTION, IS_DATA);
	}

	/**
	 * Returns true if the value corresponding to the "EDGE_DATA_SECTION" data
	 * keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "EDGE_DATA_SECTION" data keyword in a
	 *         text exists; false otherwise
	 */
	public static boolean hasEdgeDataSection(String text) {
		return hasValue(text, EDGE_DATA_SECTION, IS_DATA);
	}

	/**
	 * Returns true if the value corresponding to the "FIXED_EDGES_SECTION" data
	 * keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "FIXED_EDGES_SECTION" data keyword in
	 *         a text exists; false otherwise
	 */
	public static boolean hasFixedEdgesSection(String text) {
		return hasValue(text, FIXED_EDGES_SECTION, IS_DATA);
	}

	/**
	 * Returns true if the value corresponding to the "DISPLAY_DATA_SECTION" data
	 * keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return the value corresponding to the "DISPLAY_DATA_SECTION" data keyword in
	 *         a text exists; false otherwise
	 */
	public static boolean hasDisplayDataSection(String text) {
		return hasValue(text, DISPLAY_DATA_SECTION, IS_DATA);
	}

	/**
	 * Returns true if the value corresponding to the "TOUR_SECTION" data keyword in
	 * a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return true if the value corresponding to the "TOUR_SECTION" data keyword in
	 *         a text exists; false otherwise
	 */
	public static boolean hasTourSection(String text) {
		return hasValue(text, TOUR_SECTION, IS_DATA);
	}

	/**
	 * Returns true if the value corresponding to the "EDGE_WEIGHT_SECTION" data
	 * keyword in a text exists; false otherwise.
	 * 
	 * @param text
	 *            the text
	 * @return true if the value corresponding to the "EDGE_WEIGHT_SECTION" data
	 *         keyword in a text exists; false otherwise
	 */
	public static boolean hasEdgeWeightSection(String text) {
		return hasValue(text, EDGE_WEIGHT_SECTION, IS_DATA);
	}

	// debugging client
	public static void main(String[] args) {
		String filename = "IO/bays29.tsp";
		In in = new In(filename);

		String allInput = in.readAll();
		System.out.println(getEdgeWeightSection(allInput));
	}

}
