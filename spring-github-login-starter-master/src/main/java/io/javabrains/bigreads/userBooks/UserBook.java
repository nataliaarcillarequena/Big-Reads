package io.javabrains.bigreads.userBooks;

import java.time.LocalDate;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

@Data
@AllArgsConstructor
@NoArgsConstructor
//this will be as a Cassandra table - the keys are user id's and books ids- signle node partition 
@Table(value = "book_by_user_and_bookid")
public class UserBook {

	@PrimaryKey  //so spring data knows that this is a primary key
	private UserBookCompositeKey primaryKey;
	
	@Column("reading_status")
	@CassandraType(type = Name.TEXT) //dont need to specify the size of the text for NoSQL
	private String readingStatus;
	
	@Column("started_date")
	@CassandraType(type = Name.DATE)
	private LocalDate startedDate;
	
	@Column("completed_date")
	@CassandraType(type = Name.DATE)
	private LocalDate completedDate;
	
	@Column("rating")
	@CassandraType(type = Name.INT)
	private int rating;
	
	
}
