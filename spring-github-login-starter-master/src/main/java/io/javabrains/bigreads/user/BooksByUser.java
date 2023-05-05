package io.javabrains.bigreads.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "books_by_userid")
public class BooksByUser {

	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String id;
	
	@PrimaryKeyColumn(name = "book_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	@CassandraType(type = Name.TEXT)
	private String bookId;
	
	@PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	@CassandraType(type = Name.TIMEUUID)
	private UUID timeUuid;
	
	@PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING) //use 0-, 1- in front of the values- define it in html thymeleaf
	@CassandraType(type = Name.TEXT) //dont need to specify the size of the text for NoSQL
	private String readingStatus;
	 
	@Column("book_name")
    @CassandraType(type = Name.TEXT)
    private String bookName;

    @Column("author_names")
    @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
    private List<String> authorNames;
    
    @Column("cover_ids")
    @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
    private List<String> coverIds;

    @Column("rating")
    @CassandraType(type = Name.INT)
    private int rating;

	@Transient  //spring framework- to not be persisted to the database 
	private String coverUrl;
	
	
}
