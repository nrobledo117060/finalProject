package mongoDB;
import org.bson.types.ObjectId;

public class Item {
	private ObjectId id;
	private String name, description, winner;
	private double finalPrice;
	
	public Item() {}
	
	public Item(String name, String description, String winner, double finalPrice) {
		this.name = name;
		this.description = description;
		this.winner = winner;
		this.finalPrice = finalPrice;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", description=" + description + ", winner=" + winner + ", finalPrice="
				+ finalPrice + "]";
	}
}
