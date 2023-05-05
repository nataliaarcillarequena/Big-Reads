package io.javabrains.bigreads.user;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookByUserRepository extends CassandraRepository<BooksByUser, String>{
	
	/*
	 * instead of getting all the books for a user (may be hundreds of books that the user has read,
	 * can leverage the pageable feature that cassandra has- slices the data so it fits on a page-
	 * starts from the beginning and can get more records as the user moves across pages -
	 * uses slice, starts from page 0, gets the records
	 */
	Slice<BooksByUser> findAllById(String id, Pageable pageable);
	
	

}
