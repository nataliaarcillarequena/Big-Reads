package io.javabrains.bigreads.userBooks;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import io.javabrains.bigreads.book.Book;
import io.javabrains.bigreads.book.BookRepository;
import io.javabrains.bigreads.user.BookByUserRepository;
import io.javabrains.bigreads.user.BooksByUser;


@Controller
public class UserBookController {

	@Autowired
	private UserBookRepository userBookRepo;
	
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private BookByUserRepository bookByUserRepo;	
	/*
	 * a user (logged in) fills in form and submitts with their details of the book,
	 * this is handled by the controller by taking the post mapping 
	 * !!!!!!!!!!!!!post vs put?
	 * getting the user name via the authentication principle 
	 * get the form inputs from html via multi valued map - the request body - map of key to value
	 */
	@PostMapping("/addUserBookDetails")
	public String addBookDetailsForUser(
			@RequestBody MultiValueMap<String, String> formInputs,
			@AuthenticationPrincipal OAuth2User principal) {
			
		UserBook userBook = new UserBook();
		
		//setting the primary key; book Id and user Id
		//to get bookId, need to create it as a hidden input in html, so submits with the post request-
		//without the user seeing that 
		UserBookCompositeKey key = new UserBookCompositeKey(principal.getAttribute("login"), formInputs.getFirst("bookId"));
		userBook.setPrimaryKey(key);
		
		//setting object attributes from the form fields
		//using getFirst from the map as there will only be 1 value submitted for each field anyway
		userBook.setStartedDate(LocalDate.parse(formInputs.getFirst("startDate")));
		userBook.setCompletedDate(LocalDate.parse(formInputs.getFirst("completedDate")));
		userBook.setRating(Integer.parseInt(formInputs.getFirst("rating")));
		userBook.setReadingStatus(formInputs.getFirst("readStat"));
		
		userBookRepo.save(userBook);
	
		//need to also save this book with the user details to the BooksByUser repository- need book info- get that from the book repo
		//via the bookId
		Optional<Book> optionalBook= bookRepo.findById(formInputs.getFirst("bookId"));
		Book book = optionalBook.get();
		BooksByUser usersBooks = new BooksByUser();
		usersBooks.setId(principal.getAttribute("login"));
		usersBooks.setBookId(formInputs.getFirst("bookId"));
		//usersBook.setTimeUuid();- apparently dont need? Cassandra does it for us?
		usersBooks.setReadingStatus(formInputs.getFirst("readStat"));
		usersBooks.setBookName(book.getName());
		usersBooks.setAuthorNames(book.getAuthorNames());
		usersBooks.setCoverIds(book.getCoverIds());
		usersBooks.setRating(Integer.parseInt(formInputs.getFirst("rating")));
		usersBooks.setTimeUuid(Uuids.timeBased()); 
		
		
		bookByUserRepo.save(usersBooks);
		
		
		//takes us to the index page hopefully
		return "index";
	}
	
	
	
	
}
