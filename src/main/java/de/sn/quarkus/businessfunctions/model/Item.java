package de.sn.quarkus.businessfunctions.model;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Sort;

@Entity
public class Item extends PanacheEntity{
	
	@Column(name= "name", length = 20, nullable = false)//Database
	public String name;
	
	@Column(name= "imageurl", length = 255, nullable = true)//Database
	public String imageURL;
	
	@Column(name= "level", nullable = false)//Database
	public Integer level;

	//Projects containing items
	@ManyToOne
	//@JsonbTransient - relevant for JSON Marshalling later
	public Project project; 
	//Lower items in hierarchy
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	public List<Item> subItems;
	
	//Higher item in hierarchy
	@ManyToOne
    //@JsonbTransient - relevant for JSON Marshalling later
    public Item item; 
	
	public static List<Item> findByLevel(Integer level){
        return list("level", Sort.by("name"), level);
    }
}
