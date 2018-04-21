/**
 * 
 */
package src.application;

/**
 * @author rkhandelwal
 * @author phwang4
 * @author vmanoharan
 */
public class Challenger {
	private int seed;
	private int rank;
	private String name;
	
	public Challenger() {
		this.seed = 0;
		this.rank = 0;
		this.name = "";
	}

	/**
	 * @return the seed
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * @param seed the seed to set
	 */
	public void setSeed(int seed) {
		this.seed = seed;
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
}
