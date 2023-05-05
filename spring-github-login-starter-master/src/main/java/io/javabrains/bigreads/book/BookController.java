package io.javabrains.bigreads.book;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.javabrains.bigreads.userBooks.UserBook;
import io.javabrains.bigreads.userBooks.UserBookCompositeKey;
import io.javabrains.bigreads.userBooks.UserBookRepository;



@Controller
public class BookController {

	//final keyword so that variable cannot be initialised twice- we need this URL pattern to not be overriden
	private final String COVER_IMAGE_PATH = "https://covers.openlibrary.org/b/id/";
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	UserBookRepository userBookRepo;
	
	//authentication principal tells pring to give us the currently logged in user
	@GetMapping(value = "/books/{bookId}")
	public String getBook(@PathVariable("bookId") String bookId, Model model, @AuthenticationPrincipal OAuth2User principal) {
		
		//gets book if exists - with optional type
		java.util.Optional<Book> optionalBook = bookRepo.findById(bookId);
		if(optionalBook.isPresent()) {
			Book book = optionalBook.get(); //gets the optional book from cassandra if its existing
			String coverImageUrl = "/images/no-image.png";
			
			//getting the cover id's for the book
			if(book.getCoverIds()!= null && book.getCoverIds().size()>0) {
				//taking the 1st cover id and constructing the url to use on the webpage as the book cover image 
				coverImageUrl = COVER_IMAGE_PATH + book.getCoverIds().get(0) + "-M.jpg";
				model.addAttribute("coverImageUrl", coverImageUrl);
			}else {
				//for when theres no cover images 
				model.addAttribute("coverImageUrl", coverImageUrl);
			}
			
			model.addAttribute("book", book);  //adds the book object to "book" model- then that can be put in the html file
			 
			//getAttribute("login") gives the login attribute of the OAuth2User object
			if(principal != null && principal.getAttribute("login") != null) {
				model.addAttribute("loginId", principal.getAttribute("login"));  //can now use this in book.html 
				
				//if user has already saved info about this book, need to show that on the books page
				UserBookCompositeKey key = new UserBookCompositeKey(principal.getAttribute("login"), bookId);
				//optional user book instance when we find by the key 
				Optional<UserBook> userBook = userBookRepo.findById(key);
				if(userBook.isPresent()) {
					model.addAttribute("userBook", userBook.get());
				}else {
					model.addAttribute("userBook", new UserBook()); //giving an empty user object to not have to do th:if in html- 
					//just shows empty feature on ui 
				}
				
			}
			
			return "book"; //returns the book html
		}
		
		//returns this page if book is not found
		return "book-not-found";
	}	
	
	
}
