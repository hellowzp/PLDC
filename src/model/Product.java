package model;

public class Product {
	private String ID;

	public Product(String iD) {
		ID = iD;
	}
	
	@Override
	public boolean equals(Object object) {
		if(this == object) 
            return true;
        if(object instanceof Product) 
			return ((Product)object).ID.equalsIgnoreCase(ID);
		return 	false;
	}
}
