package io.javabrains.bigreads.userBooks;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UserBookRepository extends CassandraRepository<UserBook, UserBookCompositeKey> {

	
	
	
}
