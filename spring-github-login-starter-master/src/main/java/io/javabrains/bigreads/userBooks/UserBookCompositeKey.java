package io.javabrains.bigreads.userBooks;


import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//there are 2 keys in the UserBook entity, for the repository we need the key of the 
//UserBook hence create this class as we have a composite key- this is a primary key class- so no longer have the keys in
//the UserBooks class- instead have reference to this class - spring data knows its the primary key class via the @Primary key 
//class annotation
//the @Id annotation doesnt apply for a primary key class
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyClass
public class UserBookCompositeKey {
	
	//ordinal 0- the position of the key 
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String userId;
		
	//ordinal 1- 2 diff keys so diff ordinals 
	@PrimaryKeyColumn(name = "book_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String bookId;
	
	
}
